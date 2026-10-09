#!/usr/bin/env python3
"""List the files and units of a variant that are above the size thresholds, from its Sokrates analysis.

Reads analysis/<variant>/reports/data/data.zip (files.json, units.json) and analysis/<variant>/git-history.txt,
and prints a markdown report with:
  * every main file above --file-loc lines of code (very high risk = 1000, high = 500 in Sokrates' default bands),
    with its physical lines, its units, its biggest unit and the number of commits in the 365 days before --date;
  * every unit above --unit-loc lines of code (very high = 100, high = 50) or above --unit-mccabe (very high = 50, high = 25).

Usage:
  scripts/targets.py --variant a --date 2025-09-20 [--file-loc 500] [--unit-loc 50] [--unit-mccabe 25] > analysis/targets-a.md

The thresholds given are the ones variant B must satisfy; the report marks the very-high tier separately
so the two tiers can be compared.
"""
import argparse
import datetime as dt
import json
import os
import zipfile
from collections import defaultdict

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)

VERY_HIGH_FILE_LOC = 1000
VERY_HIGH_UNIT_LOC = 100
VERY_HIGH_UNIT_MCCABE = 50


def read_zip_json(zip_path, entry):
    with zipfile.ZipFile(zip_path) as z:
        return json.loads(z.read(entry))


def commits_per_file(history_file, since, until):
    """sha set per path for commits dated in [since, until]."""
    per_file = defaultdict(set)
    if not os.path.exists(history_file):
        return per_file
    with open(history_file, encoding="utf-8", errors="replace") as f:
        for line in f:
            parts = line.split(" ")
            if len(parts) < 4:
                continue
            date, sha, path = parts[0], parts[2], parts[3]
            if since <= date <= until:
                per_file[path].add(sha)
    return per_file


def physical_lines(path):
    try:
        with open(path, encoding="utf-8", errors="replace") as f:
            return sum(1 for _ in f)
    except OSError:
        return -1


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--variant", default="a")
    ap.add_argument("--date", required=True, help="reference date of the analysis (YYYY-MM-DD)")
    ap.add_argument("--file-loc", type=int, default=500)
    ap.add_argument("--unit-loc", type=int, default=50)
    ap.add_argument("--unit-mccabe", type=int, default=25)
    args = ap.parse_args()

    analysis = os.path.join(ROOT, "analysis", args.variant)
    src = os.path.join(ROOT, "variants", args.variant)
    data_zip = os.path.join(analysis, "reports", "data", "data.zip")
    files = read_zip_json(data_zip, "files.json")
    units = read_zip_json(data_zip, "units.json")

    until = args.date
    since = (dt.date.fromisoformat(args.date) - dt.timedelta(days=365)).isoformat()
    commits = commits_per_file(os.path.join(analysis, "git-history.txt"), since, until)

    units_by_file = defaultdict(list)
    for u in units:
        units_by_file[u["relativeFileName"]].append(u)

    big_files = [f for f in files if f["linesOfCode"] > args.file_loc]
    big_files.sort(key=lambda f: -f["linesOfCode"])
    big_units = [u for u in units if u["linesOfCode"] > args.unit_loc or u["mcCabeIndex"] > args.unit_mccabe]
    big_units.sort(key=lambda u: (-u["linesOfCode"], -u["mcCabeIndex"]))

    main_loc = sum(f["linesOfCode"] for f in files)
    big_files_loc = sum(f["linesOfCode"] for f in big_files)
    big_units_loc = sum(u["linesOfCode"] for u in big_units)

    print(f"# Size targets of variant `{args.variant}` (reference date {args.date})")
    print()
    print(f"Thresholds variant B must satisfy: file ≤ {args.file_loc} LOC, unit ≤ {args.unit_loc} LOC and McCabe ≤ {args.unit_mccabe}.")
    print(f"Sokrates' very-high-risk tier: file > {VERY_HIGH_FILE_LOC} LOC, unit > {VERY_HIGH_UNIT_LOC} LOC or McCabe > {VERY_HIGH_UNIT_MCCABE}.")
    print()
    print(f"- main files: {len(files)}, {main_loc:,} LOC")
    print(f"- files above the threshold: {len(big_files)} ({big_files_loc:,} LOC, {100 * big_files_loc / max(main_loc, 1):.0f}% of main code), "
          f"of which very high risk: {sum(1 for f in big_files if f['linesOfCode'] > VERY_HIGH_FILE_LOC)}")
    print(f"- units above the threshold: {len(big_units)} ({big_units_loc:,} LOC), "
          f"of which very high risk: {sum(1 for u in big_units if u['linesOfCode'] > VERY_HIGH_UNIT_LOC or u['mcCabeIndex'] > VERY_HIGH_UNIT_MCCABE)}")
    print(f"- commits counted per file: {since} … {until}")
    print()
    print("## Files")
    print()
    print("| tier | file | LOC | lines | units | units over threshold | biggest unit (LOC) | commits 365d |")
    print("|---|---|---|---|---|---|---|---|")
    for f in big_files:
        path = f["relativePath"]
        us = units_by_file.get(path, [])
        over = sum(1 for u in us if u["linesOfCode"] > args.unit_loc or u["mcCabeIndex"] > args.unit_mccabe)
        biggest = max((u["linesOfCode"] for u in us), default=0)
        tier = "very high" if f["linesOfCode"] > VERY_HIGH_FILE_LOC else "high"
        print(f"| {tier} | `{path}` | {f['linesOfCode']:,} | {physical_lines(os.path.join(src, path)):,} | {len(us)} | {over} | {biggest} | {len(commits.get(path, ()))} |")
    print()
    print("## Units")
    print()
    print("| tier | unit | file | lines | LOC | McCabe | commits 365d (file) |")
    print("|---|---|---|---|---|---|---|")
    for u in big_units:
        tier = "very high" if u["linesOfCode"] > VERY_HIGH_UNIT_LOC or u["mcCabeIndex"] > VERY_HIGH_UNIT_MCCABE else "high"
        name = u["shortName"].replace("|", "\\|")
        print(f"| {tier} | `{name}` | `{u['relativeFileName']}` | {u['startLine']}–{u['endLine']} | {u['linesOfCode']} | {u['mcCabeIndex']} | {len(commits.get(u['relativeFileName'], ()))} |")


if __name__ == "__main__":
    main()
