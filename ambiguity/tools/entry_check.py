#!/usr/bin/env python3
"""Entry criteria for the grown baseline A (plans/ambiguity-experiment.md), checked before any task is written:
  1. DataStore.java >= 10,000 lines
  2. Sokrates duplication in DataStore.java >= 15% of its (cleaned) lines, and >= 20 clone clusters of >= 3 members
     (the clusters from scripts/ambiguity_measure.py: near-twin methods, Jaccard >= 0.6 on 5-token shingles)
  3. ambiguity: median over ambiguity/entry-terms.txt of the methods a search for the term hits >= 8
  4. tests green, demo deterministic (two runs, identical output)
Runs Sokrates (init + generateReports) on a copy of the codebase for 2.

Usage: entry_check.py <codebase root> [--sokrates-jar path] [--json out.json]
"""
import argparse
import json
import os
import shutil
import subprocess
import sys
import tempfile
import zipfile

HERE = os.path.dirname(os.path.abspath(__file__))
EXP = os.path.dirname(os.path.dirname(HERE))
DS = "src/main/java/com/acme/backoffice/DataStore.java"


def run(cmd, cwd):
    return subprocess.run(cmd, cwd=cwd, capture_output=True, text=True, shell=isinstance(cmd, str))


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("root")
    ap.add_argument("--sokrates-jar", default=os.path.join(os.path.dirname(EXP), "sokrates/cli/target/cli-1.0-jar-with-dependencies.jar"))
    ap.add_argument("--json")
    args = ap.parse_args()
    root = os.path.abspath(args.root)
    res = {}
    res["datastore_lines"] = sum(1 for _ in open(os.path.join(root, DS), encoding="utf-8"))
    # 2a. Sokrates duplication of DataStore.java
    tmp = tempfile.mkdtemp(prefix="entry-")
    try:
        shutil.copytree(root, os.path.join(tmp, "code"), ignore=shutil.ignore_patterns("target", ".git", "_sokrates"))
        code = os.path.join(tmp, "code")
        env = "SOKRATES_OFFLINE=1"
        run(f"{env} java -jar {args.sokrates_jar} init -srcRoot . -confFile _sokrates/config.json -name backoffice", code)
        r = run(f"{env} java -jar {args.sokrates_jar} generateReports -confFile _sokrates/config.json -outputFolder _sokrates/reports -timeout 1200", code)
        with zipfile.ZipFile(os.path.join(code, "_sokrates/reports/data/data.zip")) as z:
            dups = json.loads(z.read("duplicates.json"))
            metrics = z.read("text/metrics.txt").decode()
        lines, total = set(), None
        for block in dups["duplicates"]:
            for fb in block["duplicatedFileBlocks"]:
                if fb["file"]["relativePath"] == DS:
                    total = fb["sourceFileCleanedLinesOfCode"]
                    lines.update(range(fb["cleanedStartLine"], fb["cleanedEndLine"] + 1))
        res["sokrates_duplication_codebase_pct"] = float(next(l.split(":")[1] for l in metrics.splitlines() if l.startswith("DUPLICATION_PERCENTAGE:")))
        res["sokrates_duplication_datastore_pct"] = round(100 * len(lines) / total, 1) if total else 0.0
    finally:
        shutil.rmtree(tmp, ignore_errors=True)
    # 2b and 3. near-twin clusters and ambiguity of the entry terms
    out = os.path.join(tempfile.mkdtemp(prefix="amb-"), "a.json")
    run([sys.executable, os.path.join(EXP, "scripts/ambiguity_measure.py"), root, "--terms-file",
         os.path.join(EXP, "ambiguity/entry-terms.txt"), "--json", out], EXP)
    amb = json.load(open(out))
    res["clusters_ge3"] = amb["summary"]["clusters_ge3"]
    res["datastore_methods"] = amb["summary"]["largest_file_methods"]
    res["datastore_methods_with_twin"] = amb["summary"]["largest_file_methods_with_twin"]
    hits = sorted(t["methods"] for t in amb["terms"])
    res["terms"] = {t["term"]: t["methods"] for t in amb["terms"]}
    res["ambiguity_median_methods_hit"] = hits[len(hits) // 2] if len(hits) % 2 else (hits[len(hits) // 2 - 1] + hits[len(hits) // 2]) / 2
    # 4. tests and a deterministic demo
    r = run("mvn -q -o test", root)
    res["tests_green"] = r.returncode == 0
    h2 = os.path.expanduser("~/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar")
    demo = [run(f"java -cp target/classes:{h2} com.acme.backoffice.DemoScenario", root).stdout for _ in range(2)]
    res["demo_lines"] = demo[0].count("\n")
    res["demo_deterministic"] = demo[0] == demo[1] and res["demo_lines"] > 1
    crit = {
        "1 DataStore >= 10,000 lines": res["datastore_lines"] >= 10000,
        "2a Sokrates duplication in DataStore >= 15%": res["sokrates_duplication_datastore_pct"] >= 15,
        "2b >= 20 clusters of >= 3 near-twins": res["clusters_ge3"] >= 20,
        "3 median methods hit >= 8": res["ambiguity_median_methods_hit"] >= 8,
        "4 tests green, demo deterministic": res["tests_green"] and res["demo_deterministic"],
    }
    res["criteria"] = crit
    for k, v in res.items():
        if k not in ("criteria", "terms"):
            print(f"{k:40s} {v}")
    print("terms:", ", ".join(f"{k} {v}" for k, v in res["terms"].items()))
    for k, ok in crit.items():
        print(f"{'PASS' if ok else 'FAIL'}  {k}")
    if args.json:
        json.dump(res, open(args.json, "w"), indent=1)
    sys.exit(0 if all(crit.values()) else 1)


if __name__ == "__main__":
    main()
