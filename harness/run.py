#!/usr/bin/env python3
"""Run tasks against variants with a headless coding agent and record what each run consumed.

For every (repeat, task, variant) the harness:
  1. copies variants/<variant> into work/<run id>/repo (no target/, no analysis files) and makes it a fresh
     one-commit git repository with a fixed author and date, so the agent sees a neutral repository;
  2. writes .claude/settings.json with a PostToolUse hook that logs every tool call (file, lines read,
     command) to work/<run id>/tools.jsonl;
  3. pre-builds the repository (mvn install -DskipTests) so the agent's own test runs are incremental and
     ~/.m2 holds this variant's modules (runs are serial for that reason);
  4. runs the agent on harness/prompt-preamble.md + tasks/<task>/task.md with --output-format json and
     parses the usage (input, cache read/write and output tokens, cost, turns, duration);
  5. saves the diff the agent produced, copies the task's acceptance tests in (tasks/<task>/acceptance/<variant>/,
     falling back to acceptance/a/) and runs the task's test command — pass/fail is the outcome;
  6. appends one row to results/runs.csv and writes results/runs/<run id>.json with everything.

Usage:
  harness/run.py --tasks t01 t02 --variants a b --repeats 10 [--model …] [--keep] [--dry-run]
"""
import argparse
import csv
import datetime as dt
import json
import os
import shutil
import statistics
import subprocess
import sys
import time

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
CONFIG = json.load(open(os.path.join(HERE, "config.json")))
RESULTS = os.path.join(ROOT, "results")
RUNS_CSV = os.path.join(RESULTS, "runs.csv")

CSV_FIELDS = [
    "run_id", "started", "task", "kind", "variant", "repeat", "agent", "model", "claude_version",
    "success", "agent_error", "turns", "duration_s", "api_duration_s", "cost_usd",
    "input_tokens", "cache_read_tokens", "cache_write_tokens", "total_input_tokens", "output_tokens",
    "tool_calls", "reads", "lines_read", "files_read", "greps", "globs", "bashes", "mvn_runs", "edits",
    "tool_response_chars", "files_changed", "lines_added", "lines_deleted", "prebuild_s", "acceptance_s",
]


def sh(cmd, cwd, timeout=None, env=None, check=False):
    return subprocess.run(cmd, cwd=cwd, capture_output=True, text=True, timeout=timeout, env=env, check=check,
                          shell=isinstance(cmd, str))


def ignore_patterns():
    return shutil.ignore_patterns(*CONFIG["exclude_from_copy"])


def prepare_repo(variant, repo):
    shutil.copytree(os.path.join(ROOT, "variants", variant), repo, ignore=ignore_patterns(), symlinks=True)
    env = dict(os.environ, GIT_AUTHOR_DATE=CONFIG["git_date"], GIT_COMMITTER_DATE=CONFIG["git_date"])
    name, email = CONFIG["git_author"].rsplit(" <", 1)
    email = email.rstrip(">")
    for cmd in (["git", "init", "-q", "-b", "main"], ["git", "config", "user.name", name],
                ["git", "config", "user.email", email], ["git", "add", "-A"],
                ["git", "commit", "-q", "-m", "Initial import"]):
        sh(cmd, repo, env=env, check=True)


def write_settings(repo, log_path):
    template = open(os.path.join(HERE, "settings.template.json")).read()
    hook = os.path.join(HERE, "hooks", "log_tool.py")
    settings = template.replace("{{HOOK}}", hook).replace("{{LOG}}", log_path)
    os.makedirs(os.path.join(repo, ".claude"), exist_ok=True)
    with open(os.path.join(repo, ".claude", "settings.json"), "w") as f:
        f.write(settings)


def prebuild(repo, log_file):
    t0 = time.time()
    r = sh(CONFIG["prebuild"], repo, timeout=CONFIG["prebuild_timeout_seconds"])
    if r.returncode != 0 and CONFIG.get("prebuild_fallback"):
        r = sh(CONFIG["prebuild_fallback"], repo, timeout=CONFIG["prebuild_timeout_seconds"])
    with open(log_file, "w") as f:
        f.write(r.stdout + "\n" + r.stderr)
    if r.returncode != 0:
        raise RuntimeError(f"prebuild failed, see {log_file}")
    return time.time() - t0


def agent_command(prompt, model):
    cmd = []
    for part in CONFIG["command"]:
        cmd.append(part.replace("{prompt}", prompt).replace("{model}", model)
                   .replace("{max_turns}", str(CONFIG["max_turns"]))
                   .replace("{permission_mode}", CONFIG["permission_mode"])
                   .replace("{allowed_tools}", ",".join(CONFIG["allowed_tools"]))
                   .replace("{setting_sources}", CONFIG["setting_sources"]))
    return cmd


def parse_claude_json(stdout):
    """The last JSON object on stdout is the result (claude -p --output-format json prints exactly one)."""
    text = stdout.strip()
    try:
        return json.loads(text)
    except json.JSONDecodeError:
        for line in reversed(text.splitlines()):
            line = line.strip()
            if line.startswith("{"):
                try:
                    return json.loads(line)
                except json.JSONDecodeError:
                    continue
    return {}


def usage_of(result):
    usage = result.get("usage") or {}
    inp = usage.get("input_tokens", 0)
    cr = usage.get("cache_read_input_tokens", 0)
    cw = usage.get("cache_creation_input_tokens", 0)
    out = usage.get("output_tokens", 0)
    model = ",".join(sorted((result.get("modelUsage") or {}).keys())) or ""
    return {
        "input_tokens": inp, "cache_read_tokens": cr, "cache_write_tokens": cw,
        "total_input_tokens": inp + cr + cw, "output_tokens": out,
        "cost_usd": result.get("total_cost_usd", ""), "turns": result.get("num_turns", ""),
        "duration_s": round((result.get("duration_ms") or 0) / 1000, 1),
        "api_duration_s": round((result.get("duration_api_ms") or 0) / 1000, 1),
        "agent_error": result.get("is_error", False), "model_used": model,
    }


def tool_stats(log_path):
    stats = {"tool_calls": 0, "reads": 0, "lines_read": 0, "files_read": 0, "greps": 0, "globs": 0,
             "bashes": 0, "mvn_runs": 0, "edits": 0, "tool_response_chars": 0}
    files = set()
    if not os.path.exists(log_path):
        return stats
    for line in open(log_path):
        try:
            e = json.loads(line)
        except json.JSONDecodeError:
            continue
        tool = e.get("tool", "")
        stats["tool_calls"] += 1
        if tool not in ("Edit", "Write", "MultiEdit", "NotebookEdit"):  # an edit's response echoes the file; the model sees a one-liner
            stats["tool_response_chars"] += e.get("response_chars", 0) or 0
        if tool == "Read":
            stats["reads"] += 1
            stats["lines_read"] += e.get("lines") or 0
            files.add(e.get("file"))
        elif tool == "Grep":
            stats["greps"] += 1
        elif tool == "Glob":
            stats["globs"] += 1
        elif tool == "Bash":
            stats["bashes"] += 1
            if "mvn" in (e.get("command") or ""):
                stats["mvn_runs"] += 1
        elif tool in ("Edit", "Write", "MultiEdit", "NotebookEdit"):
            stats["edits"] += 1
    stats["files_read"] = len(files)
    return stats


def diff_stats(repo, work):
    sh(["git", "add", "-A"], repo)
    sh(["git", "reset", "-q", "--", ".claude"], repo)
    stat = sh(["git", "diff", "--cached", "--numstat"], repo).stdout
    with open(os.path.join(work, "diff.patch"), "w") as f:
        f.write(sh(["git", "diff", "--cached"], repo).stdout)
    files = added = deleted = 0
    for line in stat.splitlines():
        parts = line.split("\t")
        if len(parts) == 3 and ".claude/" not in parts[2]:
            files += 1
            added += int(parts[0]) if parts[0].isdigit() else 0
            deleted += int(parts[1]) if parts[1].isdigit() else 0
    return {"files_changed": files, "lines_added": added, "lines_deleted": deleted}


def run_acceptance(task_dir, meta, variant, repo, work):
    src = os.path.join(task_dir, "acceptance", variant)
    if not os.path.isdir(src):
        src = os.path.join(task_dir, "acceptance", "a")
    if os.path.isdir(src):
        shutil.copytree(src, repo, dirs_exist_ok=True)
    command = meta.get(f"test_command_{variant}") or meta.get("test_command")
    if not command:
        return None, 0.0
    t0 = time.time()
    try:
        r = sh(command, repo, timeout=CONFIG["acceptance_timeout_seconds"])
        output, ok = r.stdout + "\n" + r.stderr, r.returncode == 0
    except subprocess.TimeoutExpired as e:
        output, ok = f"TIMEOUT after {CONFIG['acceptance_timeout_seconds']}s\n{e.stdout or ''}", False
    with open(os.path.join(work, "acceptance.log"), "w") as f:
        f.write(output)
    return ok, time.time() - t0


def claude_version():
    try:
        return sh(["claude", "--version"], ROOT).stdout.strip()
    except Exception:
        return ""


def append_row(row):
    os.makedirs(os.path.join(RESULTS, "runs"), exist_ok=True)
    new = not os.path.exists(RUNS_CSV)
    with open(RUNS_CSV, "a", newline="") as f:
        w = csv.DictWriter(f, fieldnames=CSV_FIELDS, extrasaction="ignore")
        if new:
            w.writeheader()
        w.writerow(row)
    with open(os.path.join(RESULTS, "runs", row["run_id"] + ".json"), "w") as f:
        json.dump(row, f, indent=1)


def one_run(task, variant, repeat, model, keep, dry_run):
    task_dir = os.path.join(ROOT, "tasks", task)
    meta = json.load(open(os.path.join(task_dir, "meta.json")))
    prompt = open(os.path.join(HERE, "prompt-preamble.md")).read().strip() + "\n\n" + open(os.path.join(task_dir, "task.md")).read().strip()
    started = dt.datetime.now()
    run_id = f"{task}-{variant}-r{repeat:02d}-{started.strftime('%Y%m%d-%H%M%S')}"
    work = os.path.join(ROOT, "work", run_id)
    repo = os.path.join(work, "repo")
    log_path = os.path.join(work, "tools.jsonl")
    print(f"=== {run_id}", flush=True)
    if dry_run:
        print(" ".join(agent_command(prompt, model))[:400])
        return
    os.makedirs(work)
    row = {"run_id": run_id, "started": started.isoformat(timespec="seconds"), "task": task, "kind": meta.get("kind", ""),
           "variant": variant, "repeat": repeat, "agent": CONFIG["agent"], "model": model, "claude_version": claude_version()}
    try:
        prepare_repo(variant, repo)
        write_settings(repo, log_path)
        row["prebuild_s"] = round(prebuild(repo, os.path.join(work, "prebuild.log")), 1)
        with open(os.path.join(work, "prompt.md"), "w") as f:
            f.write(prompt)
        env = dict(os.environ)
        env.pop("CLAUDECODE", None)  # allow running from inside another Claude Code session
        t0 = time.time()
        try:
            r = subprocess.run(agent_command(prompt, model), cwd=repo, capture_output=True, text=True,
                               timeout=CONFIG["timeout_seconds"], env=env)
            stdout, stderr, rc = r.stdout, r.stderr, r.returncode
        except subprocess.TimeoutExpired as e:
            stdout, stderr, rc = (e.stdout or ""), f"TIMEOUT after {CONFIG['timeout_seconds']}s\n" + (e.stderr or ""), -1
        wall = time.time() - t0
        with open(os.path.join(work, "agent.json"), "w") as f:
            f.write(stdout)
        with open(os.path.join(work, "agent.stderr"), "w") as f:
            f.write(stderr)
        result = parse_claude_json(stdout)
        usage = usage_of(result)
        if rc != 0 and not usage["turns"]:
            usage["agent_error"] = True
        if not usage["duration_s"]:
            usage["duration_s"] = round(wall, 1)
        row.update(usage)
        if usage.get("model_used"):
            row["model"] = usage["model_used"]
        row.update(tool_stats(log_path))
        row.update(diff_stats(repo, work))
        ok, acc_s = run_acceptance(task_dir, meta, variant, repo, work)
        row["success"] = "" if ok is None else ok
        row["acceptance_s"] = round(acc_s, 1)
        row["result_text"] = (result.get("result") or "")[:2000]
        row["work"] = work if keep else ""
    except Exception as e:  # record the failure and go on with the next run
        row["agent_error"] = True
        row["success"] = False
        row["result_text"] = f"HARNESS ERROR: {e}"
        print(f"  harness error: {e}", file=sys.stderr)
    append_row(row)
    print(f"  success={row.get('success')} turns={row.get('turns')} input={row.get('total_input_tokens')} "
          f"output={row.get('output_tokens')} cost={row.get('cost_usd')} lines_read={row.get('lines_read')}", flush=True)
    if not keep:
        shutil.rmtree(work, ignore_errors=True)


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--tasks", nargs="+", required=True)
    ap.add_argument("--variants", nargs="+", default=["a", "b"])
    ap.add_argument("--repeats", type=int, default=1)
    ap.add_argument("--first-repeat", type=int, default=1, help="number the repeats from here (to continue a series)")
    ap.add_argument("--model", default=CONFIG["model"])
    ap.add_argument("--keep", action="store_true", help="keep work/<run id> (repo, logs, diff) after the run")
    ap.add_argument("--dry-run", action="store_true")
    args = ap.parse_args()
    for v in args.variants:
        if not os.path.isdir(os.path.join(ROOT, "variants", v)):
            sys.exit(f"variants/{v} does not exist")
    for t in args.tasks:
        if not os.path.isfile(os.path.join(ROOT, "tasks", t, "task.md")):
            sys.exit(f"tasks/{t}/task.md does not exist")
    for i in range(args.repeats):
        repeat = args.first_repeat + i
        variants = list(args.variants) if i % 2 == 0 else list(reversed(args.variants))  # alternate the order
        for task in args.tasks:
            for variant in variants:
                one_run(task, variant, repeat, args.model, args.keep, args.dry_run)


if __name__ == "__main__":
    main()
