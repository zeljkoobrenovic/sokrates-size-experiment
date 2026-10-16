#!/usr/bin/env python3
"""Run a chain of tasks one after the other in one repository, either each in a fresh agent session or all in one
continued session (levers experiment, plans/levers-experiment.md).

Per chain (repeat): variants/<variant> is copied, made a one-commit repository and pre-built once, as in run.py.
Then, for each task in the given order:
  1. the agent gets the same prompt as in run.py (preamble + task.md); in --mode session every step after the first
     resumes the session of the previous step (claude -p --resume <session id>), in --mode fresh it starts a new one;
  2. the step's usage is recorded. `usage` in the JSON result is per invocation; `total_cost_usd` is cumulative over a
     resumed session, so a session step's cost is the difference to the previous step's;
  3. the step's diff is saved and committed (fixed author and date), so the next step starts from a clean tree that
     holds every earlier change, in both modes;
  4. the task's acceptance tests run on a scratch copy of the repository at that point, so they never enter the
     repository the later steps see.
One row per step goes to results/<--results>, with the chain id, the step number and the mode (condition column).
Transcripts: fresh mode keeps one per step (<step run id>.jsonl.gz); session mode keeps the one session, archived
after every step under the chain id (the last archive holds all steps).

Usage: harness/chain.py --set ambiguity --tasks a01 a11 ... --variant a --mode fresh|session --repeats N [--model …]
"""
import argparse
import datetime as dt
import json
import os
import shutil
import subprocess
import sys
import time

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import run as R  # noqa: E402

CHAIN_FIELDS = R.CSV_FIELDS + ["chain", "step", "session_id", "cost_cum_usd"]


def commit_step(repo, label):
    env = dict(R.ENV, GIT_AUTHOR_DATE=R.CONFIG["git_date"], GIT_COMMITTER_DATE=R.CONFIG["git_date"])
    R.sh(["git", "add", "-A"], repo, env=env)
    R.sh(["git", "reset", "-q", "--", ".claude"], repo, env=env)
    R.sh(["git", "commit", "-q", "--allow-empty", "-m", label], repo, env=env)


def acceptance_on_copy(task_dir, meta, variant, repo, work):
    copy = os.path.join(work, "acceptance-repo")
    shutil.rmtree(copy, ignore_errors=True)
    shutil.copytree(repo, copy, symlinks=True, ignore=shutil.ignore_patterns(".git"))
    try:
        return R.run_acceptance(task_dir, meta, variant, copy, work)
    finally:
        shutil.rmtree(copy, ignore_errors=True)


def append(path, row):
    import csv, fcntl
    os.makedirs(os.path.join(R.RESULTS, "runs"), exist_ok=True)
    with open(path, "a+", newline="") as f:
        fcntl.flock(f, fcntl.LOCK_EX)
        f.seek(0, os.SEEK_END)
        w = csv.DictWriter(f, fieldnames=CHAIN_FIELDS, extrasaction="ignore")
        if f.tell() == 0:
            w.writeheader()
        w.writerow(row)
        fcntl.flock(f, fcntl.LOCK_UN)
    with open(os.path.join(R.RESULTS, "runs", row["run_id"] + ".json"), "w") as f:
        json.dump(row, f, indent=1)


def one_chain(tasks, variant, repeat, mode, model, csv_path, keep):
    started = dt.datetime.now()
    chain = f"chain-{mode}-{variant}-r{repeat:02d}-{started.strftime('%Y%m%d-%H%M%S')}"
    work = os.path.join(R.ROOT, "work", chain)
    repo = os.path.join(work, "repo")
    os.makedirs(work)
    print(f"=== {chain}", flush=True)
    R.prepare_repo(variant, repo)
    log_path = os.path.join(work, "tools.jsonl")
    R.write_settings(repo, log_path)
    prebuild_s = round(R.prebuild(repo, os.path.join(work, "prebuild.log")), 1)
    session_id, cum = None, 0.0
    preamble = os.path.join(os.path.dirname(R.TASKS_DIR), "prompt-preamble.md")
    if not os.path.exists(preamble) or os.path.dirname(R.TASKS_DIR) == R.ROOT:
        preamble = os.path.join(R.HERE, "prompt-preamble.md")
    for step, task in enumerate(tasks, 1):
        task_dir = os.path.join(R.TASKS_DIR, task)
        meta = json.load(open(os.path.join(task_dir, "meta.json")))
        prompt = open(preamble).read().strip() + "\n\n" + open(os.path.join(task_dir, "task.md")).read().strip()
        run_id = f"{chain}-s{step}-{task[:3]}"
        row = {"run_id": run_id, "started": dt.datetime.now().isoformat(timespec="seconds"), "task": task,
               "kind": meta.get("kind", ""), "variant": variant, "repeat": repeat, "agent": R.CONFIG["agent"], "model": model,
               "claude_version": R.claude_version(), "condition": mode, "chain": chain, "step": step, "prebuild_s": prebuild_s}
        try:
            cmd = R.agent_command(prompt, model)
            if mode == "session" and session_id:
                cmd += ["--resume", session_id]
            t0 = time.time()
            try:
                r = subprocess.run(cmd, cwd=repo, capture_output=True, text=True, timeout=R.CONFIG["timeout_seconds"], env=R.ENV)
                stdout, stderr, rc = r.stdout, r.stderr, r.returncode
            except subprocess.TimeoutExpired as e:
                stdout, stderr, rc = (e.stdout or ""), f"TIMEOUT\n{e.stderr or ''}", -1
            with open(os.path.join(work, f"agent-s{step}.json"), "w") as f:
                f.write(stdout if isinstance(stdout, str) else stdout.decode())
            result = R.parse_claude_json(stdout if isinstance(stdout, str) else stdout.decode())
            usage = R.usage_of(result)
            if rc != 0 and not usage["turns"]:
                usage["agent_error"] = True
            if not usage["duration_s"]:
                usage["duration_s"] = round(time.time() - t0, 1)
            reported = float(result.get("total_cost_usd") or 0)
            if mode == "session":
                usage["cost_usd"] = round(reported - cum, 6)
                cum = reported
            else:
                cum += reported
            row["cost_cum_usd"] = round(cum, 6)
            if mode == "session":
                session_id = result.get("session_id") or session_id
            row["session_id"] = result.get("session_id", "")
            row.update(usage)
            if usage.get("model_used"):
                row["model"] = usage["model_used"]
            step_log = os.path.join(work, f"tools-s{step}.jsonl")
            if os.path.exists(log_path):
                shutil.move(log_path, step_log)
            row.update(R.tool_stats(step_log))
            row["tools_log"] = step_log
            row.update(R.diff_stats(repo, work))
            shutil.copy(os.path.join(work, "diff.patch"), os.path.join(R.RESULTS, "runs", run_id + ".diff"))
            if os.path.exists(step_log):
                shutil.copy(step_log, os.path.join(R.RESULTS, "runs", run_id + ".tools.jsonl"))
            commit_step(repo, f"Step {step}: {task}")
            ok, acc_s = acceptance_on_copy(task_dir, meta, variant, repo, work)
            row["success"] = "" if ok is None else ok
            row["acceptance_s"] = round(acc_s, 1)
            shutil.copy(os.path.join(work, "acceptance.log"), os.path.join(work, f"acceptance-s{step}.log"))
            row["result_text"] = (result.get("result") or "")[:2000]
            row["transcript"] = R.archive_transcript(repo, chain if mode == "session" else run_id)
        except Exception as e:
            row["agent_error"] = True
            row["success"] = False
            row["result_text"] = f"HARNESS ERROR: {e}"
            print(f"  harness error: {e}", file=sys.stderr)
        append(csv_path, row)
        print(f"  s{step} {task[:3]} success={row.get('success')} turns={row.get('turns')} input={row.get('total_input_tokens')} "
              f"cost={row.get('cost_usd')} cum={row.get('cost_cum_usd')}", flush=True)
    if not keep:
        shutil.rmtree(work, ignore_errors=True)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--set", required=True)
    ap.add_argument("--tasks", nargs="+", required=True, help="task ids or prefixes, in chain order")
    ap.add_argument("--variant", default="a")
    ap.add_argument("--mode", choices=["fresh", "session"], required=True)
    ap.add_argument("--repeats", type=int, default=1)
    ap.add_argument("--first-repeat", type=int, default=1)
    ap.add_argument("--model", default=R.CONFIG["model"])
    ap.add_argument("--results", required=True)
    ap.add_argument("--m2")
    ap.add_argument("--keep", action="store_true")
    args = ap.parse_args()
    R.TASKS_DIR = os.path.join(R.ROOT, args.set, "tasks")
    R.VARIANTS_DIR = os.path.join(R.ROOT, args.set, "variants")
    if args.m2:
        R.use_maven_repo(args.m2)
    tasks = [next(d for d in sorted(os.listdir(R.TASKS_DIR)) if d.startswith(t)) for t in args.tasks]
    for i in range(args.repeats):
        one_chain(tasks, args.variant, args.first_repeat + i, args.mode, args.model, os.path.join(R.RESULTS, args.results), args.keep)


if __name__ == "__main__":
    main()
