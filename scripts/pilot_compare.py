#!/usr/bin/env python3
"""Pooled ratio of an arm against A for pilot CSVs: per task the ratio of medians, pooled as a geometric mean,
with a 95% bootstrap interval (runs resampled within task and variant).
Usage: scripts/pilot_compare.py <arm prefix: p|g> <csv>... [--tasks-regex ^t]"""
import csv, math, random, re, statistics as s, sys
args = sys.argv[1:]
regex = args[args.index("--tasks-regex") + 1] if "--tasks-regex" in args else "."
if "--tasks-regex" in args:
    i = args.index("--tasks-regex"); args = args[:i] + args[i + 2:]
arm, files = args[0], args[1:]
rows = [r for f in files for r in csv.DictReader(open(f))]
is_arm = lambda v: v == arm or v.startswith(arm + "-")
tasks = sorted({r["task"] for r in rows if is_arm(r["variant"]) and re.search(regex, r["task"])})
tasks = [t for t in tasks if any(r["task"] == t and r["variant"] == "a" for r in rows)]
measures = ["total_input_tokens", "output_tokens", "cost_usd", "turns"]
def cell(t, arm_side): return [r for r in rows if r["task"] == t and (is_arm(r["variant"]) if arm_side else r["variant"] == "a")]
def pooled(sample, k):
    return math.exp(s.mean(math.log(s.median(float(x[k]) for x in sample[t][1]) / s.median(float(x[k]) for x in sample[t][0])) for t in tasks))
data = {t: (cell(t, False), cell(t, True)) for t in tasks}
fails = sum(r["success"] != "True" for t in tasks for side in data[t] for r in side)
print(f"arm {arm}: {len(tasks)} tasks, {sum(len(a)+len(b) for a,b in data.values())} runs, {fails} failed acceptance")
random.seed(1)
for k in measures:
    est = pooled(data, k)
    boots = sorted(pooled({t: ([random.choice(a) for _ in a], [random.choice(b) for _ in b]) for t, (a, b) in data.items()}, k) for _ in range(2000))
    print(f"  {k:20s} {est:.2f}  ({boots[50]:.2f}–{boots[1949]:.2f})")
print("  per task (input tokens, median arm/A):", ", ".join(f"{t[:3]} {s.median(float(x['total_input_tokens']) for x in b)/s.median(float(x['total_input_tokens']) for x in a):.2f}" for t, (a, b) in data.items()))
