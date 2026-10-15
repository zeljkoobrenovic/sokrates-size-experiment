#!/usr/bin/env python3
"""Derive tasks/<id>/reference/<variant>.patch for a merged variant from the task's A patch: apply a.patch to a
copy of A, merge that tree exactly as scripts/make_variant_m.py merges A, and diff it against variants/<variant>.
The merged patch is the A change in the merged layout by construction, not edited by hand.

Usage: scripts/make_reference_m.py m3 [--tasks t05-… …]
"""
import argparse
import glob
import os
import shutil
import subprocess
import sys

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from make_variant_m import GROUPS, ROOT, merge  # noqa: E402

WORK = os.path.join(ROOT, "work", "reference-m")


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("variant", choices=sorted(GROUPS))
    ap.add_argument("--tasks", nargs="+")
    args = ap.parse_args()
    target = os.path.join(ROOT, "variants", args.variant)
    tasks = args.tasks or sorted(os.path.basename(d) for d in glob.glob(os.path.join(ROOT, "tasks", "*"))
                                 if os.path.exists(os.path.join(d, "reference", "a.patch")))
    for task in tasks:
        ref = os.path.join(ROOT, "tasks", task, "reference")
        tree = os.path.join(WORK, task)
        shutil.rmtree(tree, ignore_errors=True)
        shutil.copytree(os.path.join(ROOT, "variants", "a"), tree, symlinks=True,
                        ignore=shutil.ignore_patterns("target", "_sokrates"))
        subprocess.run(["patch", "-p1", "-s", "--no-backup-if-mismatch", "-i", os.path.join(ref, "a.patch")],
                       cwd=tree, check=True)
        for pkg, host, moved in GROUPS[args.variant]:
            merge(tree, pkg, host, moved)
        # diff the two trees under neutral a/ b/ prefixes so the patch applies with -p1
        os.makedirs(os.path.join(WORK, "_cmp"), exist_ok=True)
        for side, src in (("a", target), ("b", tree)):
            link = os.path.join(WORK, "_cmp", side)
            if os.path.lexists(link):
                os.remove(link)
            os.symlink(src, link)
        diff = subprocess.run(["diff", "-ruN", "-x", "target", "-x", "_sokrates", "a/", "b/"],
                              cwd=os.path.join(WORK, "_cmp"), capture_output=True, text=True)
        if diff.returncode not in (0, 1):
            sys.exit(f"{task}: diff failed: {diff.stderr}")
        out = os.path.join(ref, f"{args.variant}.patch")
        with open(out, "w") as f:
            f.write(diff.stdout)
        same = diff.stdout == open(os.path.join(ref, "a.patch")).read()
        files = [l.split()[1] for l in diff.stdout.splitlines() if l.startswith("+++ ")]
        print(f"{task}: {len(files)} files{' (same as a.patch)' if same else ''}: {', '.join(os.path.basename(f) for f in files)}")
        shutil.rmtree(tree)


if __name__ == "__main__":
    main()
