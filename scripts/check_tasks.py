#!/usr/bin/env python3
"""Re-verify the tasks: for every tasks/<id> and every variant, the acceptance test must compile and FAIL on the
untouched variant and PASS after tasks/<id>/reference/<variant>.patch is applied (the module's tests staying green
is checked by the task author; this checker runs only the acceptance test, with -am so no ~/.m2 install is needed).

Usage: scripts/check_tasks.py [--tasks t01-… …] [--variants a b] [--keep]
Prints one line per task and variant: before=FAIL after=PASS is what we want.
"""
import argparse
import json
import os
import shutil
import subprocess
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
WORK = os.path.join(ROOT, "work", "check-tasks")


def sh(cmd, cwd, timeout=1800):
    return subprocess.run(cmd, cwd=cwd, shell=isinstance(cmd, str), capture_output=True, text=True, timeout=timeout)


def with_am(command):
    return command.replace("-pl ", "-pl ", 1).replace(" test ", " -am test ", 1) if " -am " not in command else command


def outcome(result):
    out = result.stdout + result.stderr
    if "COMPILATION ERROR" in out or "Compilation failure" in out:
        return "COMPILE-ERROR"
    if result.returncode == 0:
        return "PASS"
    if "Tests run:" in out and ("Failures: 0" not in out or "Errors: 0" not in out):
        return "FAIL"
    return f"ERROR(rc={result.returncode})"


def check(task, variant, keep):
    task_dir = os.path.join(ROOT, "tasks", task)
    meta = json.load(open(os.path.join(task_dir, "meta.json")))
    command = with_am(meta.get(f"test_command_{variant}") or meta["test_command"])
    repo = os.path.join(WORK, task, variant)
    shutil.rmtree(repo, ignore_errors=True)
    os.makedirs(repo)
    subprocess.run(["rsync", "-a", "--exclude", "target", os.path.join(ROOT, "variants", variant) + "/", repo + "/"], check=True)
    acceptance = os.path.join(task_dir, "acceptance", variant)
    if not os.path.isdir(acceptance):
        acceptance = os.path.join(task_dir, "acceptance", "a")
    shutil.copytree(acceptance, repo, dirs_exist_ok=True)
    before = outcome(sh(command, repo))
    patch = os.path.join(task_dir, "reference", f"{variant}.patch")
    if os.path.exists(patch):
        # patch(1), not git apply: inside the experiment repository git apply silently skips paths of a nested copy
        applied = sh(["patch", "-p1", "-s", "--no-backup-if-mismatch", "-i", patch], repo)
        after = outcome(sh(command, repo)) if applied.returncode == 0 else "PATCH-FAILED: " + applied.stderr.strip()[:200]
    else:
        after = "NO-PATCH"
    if not keep:
        shutil.rmtree(repo, ignore_errors=True)
    return before, after


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--tasks", nargs="*")
    ap.add_argument("--variants", nargs="*", default=["a", "b"])
    ap.add_argument("--keep", action="store_true")
    args = ap.parse_args()
    tasks = args.tasks or sorted(d for d in os.listdir(os.path.join(ROOT, "tasks")) if os.path.isfile(os.path.join(ROOT, "tasks", d, "meta.json")))
    ok = True
    for task in tasks:
        for variant in args.variants:
            before, after = check(task, variant, args.keep)
            good = before == "FAIL" and after == "PASS"
            ok &= good
            print(f"{'OK ' if good else 'BAD'} {task} {variant}: before={before} after={after}", flush=True)
    sys.exit(0 if ok else 1)


if __name__ == "__main__":
    main()
