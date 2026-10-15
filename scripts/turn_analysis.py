#!/usr/bin/env python3
"""Where the turns and the input tokens of a series go (plan step 2.0), from the archived transcripts.

Every API call (turn) of every run gets one class, from its tool calls (scripts/turn_trace.py's kinds):
  locate  searches only (grep, find, ls, Glob/Grep)        read   reads a file or a window of one (and no edit)
  edit    any edit                                         build  build or test command (mvn …), no edit
  fix     an edit or build after a failed build/test, until a build passes
  other   text only, version control, other shell
Input tokens are split the way they arise: a call's context is the shared prefix, then everything earlier calls
added. What call t adds (its cache write: the previous call's tool results and output, plus reminders) is re-sent
by every later call, so its cost over the run is added_t × (calls from t to the end). That cost is booked to the
class of the call whose tools produced it (call t-1); the first call's additions are "setup". Per run:
  input ≈ shared_prefix × calls + Σ_class booked tokens  (the check column shows how close)

Wasted locate: files a search hit that the agent never read nor edited in the run.

Usage: scripts/turn_analysis.py [--csv results/runs.csv results/runs-haiku.csv] [--out results/turn-analysis.json]
"""
import argparse
import csv
import json
import os
import re
import statistics
import sys
from collections import defaultdict

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
import turn_trace  # noqa: E402

ROOT = turn_trace.ROOT
CLASSES = ["setup", "locate", "read", "edit", "build", "fix", "other"]
FAILED = ("BUILD FAILURE", "COMPILATION ERROR", "Tests run:.*Failures: [1-9]", "FAILED", "ERROR]")


def build_failed(tool, text):
    return bool(tool.get("error")) or any(re.search(p, text or "") for p in FAILED)  # a piped tail hides the exit code


def classify_run(transcript):
    turns, results, cwd = turn_trace.load(transcript)
    root = (cwd or "").rstrip("/") + "/"
    outputs = [[results.get(u.get("id"), {}).get("text", "") for u in t["_uses"]] for t in turns]  # build() drops them
    turn_trace.build(turns, results, root, None)
    for t, outs in zip(turns, outputs):
        for x, out in zip(t["tools"], outs):
            x["output"] = out
    classes, broken = [], False
    for t in turns:
        kinds = {x["kind"] for x in t["tools"]}
        builds = [x for x in t["tools"] if x["kind"] == "build"]
        if "edit" in kinds:
            c = "fix" if broken else "edit"
        elif builds:
            c = "fix" if broken else "build"
        elif "read" in kinds:
            c = "read"
        elif "search" in kinds:
            c = "locate"
        else:
            c = "other"
        for b in builds:  # the state after this call's builds decides what the next calls are
            broken = build_failed(b, b.get("output", ""))
        classes.append(c)
    calls = len(turns)
    booked = defaultdict(float)
    for i, t in enumerate(turns):
        added = t["cache_write"] + t["input"]
        booked["setup" if i == 0 else classes[i - 1]] += added * (calls - i)
    prefix = turns[0]["cache_read"] if turns else 0
    # the same in price units (fresh input 1, cache write 1.25, cache read 0.1, output 5): what call t adds is
    # written once and read by every later call; a call's output is booked to its own class
    dollars = defaultdict(float)
    dollars["prefix"] = 0.1 * prefix * calls
    for i, t in enumerate(turns):
        origin = "setup" if i == 0 else classes[i - 1]
        dollars[origin] += 1.25 * t["cache_write"] + t["input"] + 0.1 * (t["cache_write"] + t["input"]) * (calls - i - 1)
        dollars[classes[i]] += 5 * t["output"]
    total = sum(t["input"] + t["cache_read"] + t["cache_write"] for t in turns)
    hit, touched = set(), set()
    for t in turns:
        for x in t["tools"]:
            for f in x["files"]:
                (hit if f["kind"] == "hit" else touched).add(f["path"])
    return {
        "calls": calls, "classes": classes, "prefix_tokens": prefix * calls, "booked": dict(booked), "dollars": dict(dollars),
        "total_input": total, "check": round((prefix * calls + sum(booked.values())) / total, 3) if total else None,
        "result_chars": {c: sum(t["result_chars"] for t, k in zip(turns, classes) if k == c) for c in CLASSES},
        "wasted_hits": len(hit - touched), "files_hit": len(hit), "files_touched": len(touched),
    }


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--csv", nargs="+", default=[os.path.join(ROOT, "results", "runs.csv"),
                                                 os.path.join(ROOT, "results", "runs-haiku.csv")])
    ap.add_argument("--out", default=os.path.join(ROOT, "results", "turn-analysis.json"))
    args = ap.parse_args()
    runs = []
    for path in args.csv:
        series = "haiku" if "haiku" in os.path.basename(path) else "sonnet"
        for row in csv.DictReader(open(path)):
            tr = os.path.join(ROOT, "results", "transcripts", row["run_id"] + ".jsonl.gz")
            if not os.path.exists(tr):
                print(f"no transcript: {row['run_id']}", file=sys.stderr)
                continue
            r = classify_run(tr)
            r.update(run_id=row["run_id"], series=series, task=row["task"], variant=row["variant"])
            runs.append(r)
    with open(args.out, "w") as f:
        json.dump(runs, f, indent=0)
    print(f"{len(runs)} runs -> {args.out}; token check (model / measured) median "
          f"{statistics.median(r['check'] for r in runs if r['check']):.3f}")
    for series in ("sonnet", "haiku"):
        rs = [r for r in runs if r["series"] == series]
        if not rs:
            continue
        print(f"\n## {series}: share of calls and of input tokens per class (median run)")
        print("| task | calls | " + " | ".join(CLASSES[1:]) + " | prefix % | " + " | ".join(f"{c} tok %" for c in CLASSES) + " | wasted hits |")
        print("|---" * (2 + len(CLASSES) - 1 + 1 + len(CLASSES) + 1) + "|")
        for task in sorted({r["task"] for r in rs}) + ["ALL"]:
            tr = [r for r in rs if task == "ALL" or r["task"] == task]
            med = lambda f: statistics.median(f(r) for r in tr)
            calls = med(lambda r: r["calls"])
            counts = [med(lambda r, c=c: r["classes"].count(c)) for c in CLASSES[1:]]
            pre = med(lambda r: 100 * r["prefix_tokens"] / r["total_input"])
            toks = [med(lambda r, c=c: 100 * r["booked"].get(c, 0) / r["total_input"]) for c in CLASSES]
            print(f"| {task[:28]} | {calls:g} | " + " | ".join(f"{x:g}" for x in counts) + f" | {pre:.0f} | "
                  + " | ".join(f"{x:.0f}" for x in toks) + f" | {med(lambda r: r['wasted_hits']):g} |")
        print(f"\n## {series}: share of cost (price-weighted) per class, mean over runs")
        keys = ["prefix"] + CLASSES
        print("| task | " + " | ".join(keys) + " |")
        print("|---" * (len(keys) + 1) + "|")
        for task in sorted({r["task"] for r in rs}) + ["ALL"]:
            tr = [r for r in rs if task == "ALL" or r["task"] == task]
            shares = [statistics.mean(100 * r["dollars"].get(k, 0) / sum(r["dollars"].values()) for r in tr) for k in keys]
            print(f"| {task[:28]} | " + " | ".join(f"{x:.0f}" for x in shares) + " |")


if __name__ == "__main__":
    main()
