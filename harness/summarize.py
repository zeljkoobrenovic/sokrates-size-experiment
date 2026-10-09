#!/usr/bin/env python3
"""Summarize results/runs.csv: per task and variant the median (and quartiles) of what a run consumed,
the success rate, and the A-vs-B ratio of the medians per task.

Usage: harness/summarize.py [--csv results/runs.csv] [--successful-only] [--tasks t01 ...]
"""
import argparse
import csv
import os
import statistics
from collections import defaultdict

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)

MEASURES = [
    ("total_input_tokens", "input tokens (incl. cache)"),
    ("output_tokens", "output tokens"),
    ("cost_usd", "cost USD"),
    ("turns", "turns"),
    ("lines_read", "lines read"),
    ("reads", "file reads"),
    ("files_read", "distinct files read"),
    ("tool_response_chars", "tool output chars"),
    ("duration_s", "duration s"),
]


def num(v):
    try:
        return float(v)
    except (TypeError, ValueError):
        return None


def quartiles(values):
    values = sorted(v for v in values if v is not None)
    if not values:
        return None
    if len(values) < 4:
        return (values[0], statistics.median(values), values[-1])
    q = statistics.quantiles(values, n=4)
    return (q[0], statistics.median(values), q[2])


def fmt(v, key):
    if v is None:
        return "-"
    if key == "cost_usd":
        return f"{v:.2f}"
    return f"{v:,.0f}"


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--csv", default=os.path.join(ROOT, "results", "runs.csv"))
    ap.add_argument("--successful-only", action="store_true", help="only runs whose acceptance test passed")
    ap.add_argument("--tasks", nargs="*")
    args = ap.parse_args()

    rows = list(csv.DictReader(open(args.csv)))
    if args.tasks:
        rows = [r for r in rows if r["task"] in args.tasks]
    groups = defaultdict(list)
    for r in rows:
        groups[(r["task"], r["variant"])].append(r)

    tasks = sorted({t for t, _ in groups})
    variants = sorted({v for _, v in groups})

    print(f"# Results ({len(rows)} runs, {args.csv})")
    print()
    print("## Success rate")
    print()
    print("| task | " + " | ".join(variants) + " |")
    print("|---|" + "---|" * len(variants))
    for t in tasks:
        cells = []
        for v in variants:
            g = groups.get((t, v), [])
            ok = sum(1 for r in g if r.get("success") == "True")
            cells.append(f"{ok}/{len(g)}" if g else "-")
        print(f"| {t} | " + " | ".join(cells) + " |")
    print()

    if args.successful_only:
        groups = {k: [r for r in g if r.get("success") == "True"] for k, g in groups.items()}

    for key, label in MEASURES:
        print(f"## {label} — median (p25–p75)")
        print()
        header = "| task | " + " | ".join(variants) + " |"
        if len(variants) == 2:
            header += f" {variants[1]}/{variants[0]} |"
        print(header)
        print("|---|" + "---|" * (len(variants) + (1 if len(variants) == 2 else 0)))
        for t in tasks:
            cells, medians = [], []
            for v in variants:
                q = quartiles([num(r.get(key)) for r in groups.get((t, v), [])])
                medians.append(q[1] if q else None)
                cells.append(f"{fmt(q[1], key)} ({fmt(q[0], key)}–{fmt(q[2], key)})" if q else "-")
            line = f"| {t} | " + " | ".join(cells) + " |"
            if len(variants) == 2:
                a, b = medians
                line += f" {b / a:.2f} |" if a and b is not None else " - |"
            print(line)
        print()


if __name__ == "__main__":
    main()
