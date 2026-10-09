#!/usr/bin/env python3
"""A versus B: per-task medians and B/A ratios, pooled over the target and the control tasks (geometric mean of the
per-task ratios of means) with a bootstrap interval, for the measures that matter. Markdown output.

Usage: harness/compare.py [--csv results/runs.csv] [--successful-only] [--boot 2000]
"""
import argparse
import csv
import math
import os
import random
import statistics as st
from collections import defaultdict

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
MEASURES = [("total_input_tokens", "input tokens (incl. cache reads)"), ("output_tokens", "output tokens"),
            ("cost_usd", "cost (USD)"), ("turns", "turns"), ("lines_seen", "lines seen"), ("duration_s", "duration (s)")]


def gmean(xs):
    return math.exp(sum(map(math.log, xs)) / len(xs))


def pooled(groups, tasks, key, boot, seed=1):
    def est(sample):
        logs = []
        for t in tasks:
            a = [float(r[key]) for r in sample[(t, "a")]]
            b = [float(r[key]) for r in sample[(t, "b")]]
            if a and b and st.mean(a) > 0:
                logs.append(math.log(st.mean(b) / st.mean(a)))
        return math.exp(sum(logs) / len(logs)) if logs else float("nan")
    point = est(groups)
    rnd = random.Random(seed)
    boots = sorted(est({k: [rnd.choice(v) for _ in v] for k, v in groups.items()}) for _ in range(boot))
    return point, boots[int(0.025 * boot)], boots[int(0.975 * boot) - 1]


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--csv", default=os.path.join(ROOT, "results", "runs.csv"))
    ap.add_argument("--successful-only", action="store_true")
    ap.add_argument("--boot", type=int, default=2000)
    args = ap.parse_args()
    rows = list(csv.DictReader(open(args.csv)))
    if args.successful_only:
        rows = [r for r in rows if r["success"] == "True"]
    groups = defaultdict(list)
    for r in rows:
        groups[(r["task"], r["variant"])].append(r)
    tasks = sorted({r["task"] for r in rows})
    kinds = {"target": [t for t in tasks if not t.startswith("c")], "control": [t for t in tasks if t.startswith("c")]}
    failures = sum(1 for r in rows if r["success"] != "True")
    print(f"{len(rows)} runs, {failures} failed acceptance tests; "
          f"{min(len(v) for v in groups.values())}–{max(len(v) for v in groups.values())} runs per task and variant.\n")
    print("## Pooled B/A (geometric mean over tasks of the ratio of means; 95% bootstrap interval)\n")
    print("| measure | target tasks | control tasks |")
    print("|---|---|---|")
    for key, label in MEASURES:
        cells = []
        for kind in ("target", "control"):
            p, lo, hi = pooled(groups, kinds[kind], key, args.boot)
            cells.append(f"{p:.2f} ({lo:.2f}–{hi:.2f})")
        print(f"| {label} | {cells[0]} | {cells[1]} |")
    print()
    for key, label in MEASURES[:5]:
        print(f"## {label}: per task (median; B/A of medians)\n")
        print("| task | kind | runs A, B | A | B | B/A |")
        print("|---|---|---|---|---|---|")
        for t in tasks:
            a = [float(r[key]) for r in groups[(t, "a")]]
            b = [float(r[key]) for r in groups[(t, "b")]]
            if not a or not b:
                continue
            fmt = (lambda v: f"{v:.2f}") if key == "cost_usd" else (lambda v: f"{v:,.0f}")
            print(f"| {t} | {'control' if t.startswith('c') else 'target'} | {len(a)}, {len(b)} | {fmt(st.median(a))} | {fmt(st.median(b))} | {st.median(b) / st.median(a):.2f} |")
        print()
    cv = [st.pstdev(v) / st.mean(v) for v in ([float(r["total_input_tokens"]) for r in g] for g in groups.values()) if len(v) > 1]
    print(f"Within a cell (same task, same variant) the input tokens vary with a coefficient of variation of "
          f"{st.median(cv):.2f} (median over cells), up to {max(cv):.2f}.")


if __name__ == "__main__":
    main()
