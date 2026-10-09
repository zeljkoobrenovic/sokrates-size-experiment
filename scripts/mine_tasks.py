#!/usr/bin/env python3
"""List candidate tasks: real commits made to the snapshot's code after the snapshot.

Reads the git history of the source repository (the current Sokrates checkout), keeps the
non-merge commits after the base commit that touched at least one of the "target" files
(the very-high-risk files/units listed in analysis/<variant>/targets.md, or any list of
paths given with --files), and prints them as a markdown table sorted by size, with the
number of test files each commit touched. Small commits with tests are the best tasks:
the task text comes from the commit message, the acceptance test from the commit.

Usage:
  scripts/mine_tasks.py --repo ../sokrates --base 5e6aa97a [--max-lines 250] [--max-files 8]
                        [--files path1 path2 ...] > tasks/candidates.md
"""
import argparse
import re
import subprocess
import sys

DEFAULT_TARGET_FILES = [
    "reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportGenerator.java",
    "codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/landscape/analysis/LandscapeAnalysisResults.java",
    "reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportContributorsTab.java",
    "reports/src/main/java/nl/obren/sokrates/reports/core/ReportFileExporter.java",
    "reports/src/main/java/nl/obren/sokrates/reports/dataexporters/DataExporter.java",
    "reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/repositories/LandscapeRepositoriesReport.java",
    "cli/src/main/java/nl/obren/sokrates/cli/CommandLineInterface.java",
    "reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml/LogicalComponentsReportGenerator.java",
    "reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml/LandscapeReportPeopleTopologyTab.java",
    "reports/src/main/java/nl/obren/sokrates/reports/core/SummaryUtils.java",
    "codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/lang/LanguageAnalyzerFactory.java",
    "codeanalyzer/src/main/java/nl/obren/sokrates/sourcecode/scoping/ScopingConventions.java",
]


def git(repo, *args):
    return subprocess.run(["git", "-C", repo, *args], check=True, capture_output=True, text=True).stdout


def commits_touching(repo, base, files):
    out = git(repo, "log", "--no-merges", "--format=%H|%h|%ad|%s", "--date=short", f"{base}..HEAD", "--", *files)
    rows = []
    for line in out.splitlines():
        full, short, date, subject = line.split("|", 3)
        rows.append((full, short, date, subject))
    return rows


def commit_stats(repo, sha):
    numstat = git(repo, "show", "--numstat", "--format=", sha)
    files, ins, dels, tests = [], 0, 0, 0
    for line in numstat.splitlines():
        parts = line.split("\t")
        if len(parts) != 3:
            continue
        a, d, path = parts
        files.append(path)
        ins += int(a) if a.isdigit() else 0
        dels += int(d) if d.isdigit() else 0
        if "/src/test/" in path:
            tests += 1
    return files, ins, dels, tests


def applies_to_base(repo, base, sha, files):
    """True when the files the commit touched are identical in the base and in the commit's parent,
    i.e. the commit's diff applies to the snapshot as is. Otherwise the task needs re-deriving."""
    r = subprocess.run(["git", "-C", repo, "diff", "--quiet", base, f"{sha}^", "--", *files], capture_output=True)
    return r.returncode == 0


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--repo", required=True, help="path to the Sokrates git checkout (the source of the history)")
    ap.add_argument("--base", required=True, help="the snapshot commit (variant A)")
    ap.add_argument("--max-lines", type=int, default=250, help="max insertions + deletions")
    ap.add_argument("--max-files", type=int, default=8)
    ap.add_argument("--files", nargs="*", default=DEFAULT_TARGET_FILES)
    args = ap.parse_args()

    target_names = {f.rsplit("/", 1)[-1] for f in args.files}
    rows = []
    for full, short, date, subject in commits_touching(args.repo, args.base, args.files):
        files, ins, dels, tests = commit_stats(args.repo, full)
        if ins + dels > args.max_lines or len(files) > args.max_files:
            continue
        hit = sorted({f.rsplit("/", 1)[-1].replace(".java", "") for f in files if f.rsplit("/", 1)[-1] in target_names})
        applies = "yes" if applies_to_base(args.repo, args.base, full, files) else "no"
        rows.append((ins + dels, short, date, len(files), ins, dels, tests, applies, ", ".join(hit), subject.replace("|", "\\|")))

    rows.sort(key=lambda r: (r[6] == 0, r[0]))
    print(f"# Candidate tasks mined from {args.repo} after {args.base}")
    print()
    print(f"{len(rows)} non-merge commits touching a target file, at most {args.max_lines} changed lines and "
          f"{args.max_files} files. Commits with tests first, smallest first. Pick tasks that are self-contained, "
          "whose outcome a test can check, and whose message does not give the solution away. \"applies to snapshot\" = the "
          "touched files are unchanged between the snapshot and the commit's parent, so the real diff applies as is; "
          "otherwise re-derive the task against the snapshot (later commits may build on intermediate changes).")
    print()
    print("| commit | date | files | +/- | tests | applies to snapshot | target files touched | subject |")
    print("|---|---|---|---|---|---|---|---|")
    for size, short, date, nfiles, ins, dels, tests, applies, hit, subject in rows:
        print(f"| `{short}` | {date} | {nfiles} | +{ins}/-{dels} | {tests} | {applies} | {hit} | {subject} |")


if __name__ == "__main__":
    main()
