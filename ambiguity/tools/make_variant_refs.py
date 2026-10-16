#!/usr/bin/env python3
"""Derive a task's acceptance tests and reference patch for a mechanically made variant from its A versions:
  s       acceptance = A's (S keeps every signature); patch = split(a.patch applied to A) diffed against S
  dnames  acceptance = A's with renames.tsv applied; patch = renamed(a.patch applied to A) diffed against renamed(A)
Writes ambiguity/tasks/<task>/acceptance/<variant>/ (dnames only) and reference/<variant>.patch.
Usage: make_variant_refs.py <s|dnames> [--renames path] [--tasks id ...]"""
import argparse, os, shutil, subprocess, sys
HERE = os.path.dirname(os.path.abspath(__file__)); AMB = os.path.dirname(HERE); EXP = os.path.dirname(AMB)
WORK = os.path.join(EXP, "work", "variant-refs")


def tree_copy(src, dst):
    shutil.rmtree(dst, ignore_errors=True)
    shutil.copytree(src, dst, ignore=shutil.ignore_patterns("target", ".git"))


def sh(cmd, cwd=None, env=None):
    r = subprocess.run(cmd, cwd=cwd, capture_output=True, text=True, env=env)
    if r.returncode not in (0, 1):
        sys.exit(f"{cmd}: {r.stderr}")
    return r


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("variant", choices=["s", "dnames"])
    ap.add_argument("--renames")
    ap.add_argument("--tasks", nargs="+")
    args = ap.parse_args()
    A = os.path.join(AMB, "variants", "a")
    tasks = args.tasks or sorted(t for t in os.listdir(os.path.join(AMB, "tasks")) if os.path.exists(os.path.join(AMB, "tasks", t, "reference", "a.patch")))
    env = dict(os.environ, RENAME_ROOT=A)
    if args.variant == "dnames":
        base = os.path.join(WORK, "renamed-a"); tree_copy(A, base)
        sh([sys.executable, os.path.join(HERE, "apply_renames.py"), args.renames, base], env=env)
    else:
        base = os.path.join(AMB, "variants", "s")
    for t in tasks:
        td = os.path.join(AMB, "tasks", t)
        tree = os.path.join(WORK, t); tree_copy(A, tree)
        sh(["patch", "-p1", "-s", "--no-backup-if-mismatch", "-i", os.path.join(td, "reference", "a.patch")], cwd=tree)
        if args.variant == "s":
            sh([sys.executable, os.path.join(HERE, "split_by_position.py"), tree])
        else:
            sh([sys.executable, os.path.join(HERE, "apply_renames.py"), args.renames, tree], env=env)
            acc_src, acc_dst = os.path.join(td, "acceptance", "a"), os.path.join(td, "acceptance", "dnames")
            shutil.rmtree(acc_dst, ignore_errors=True); shutil.copytree(acc_src, acc_dst)
            sh([sys.executable, os.path.join(HERE, "apply_renames.py"), args.renames, acc_dst], env=env)
        cmp = os.path.join(WORK, "_cmp"); os.makedirs(cmp, exist_ok=True)
        for side, src in (("a", base), ("b", tree)):
            link = os.path.join(cmp, side)
            if os.path.lexists(link):
                os.remove(link)
            os.symlink(src, link)
        d = sh(["diff", "-ruN", "-x", "target", "-x", ".git", "a/", "b/"], cwd=cmp)
        open(os.path.join(td, "reference", f"{args.variant}.patch"), "w").write(d.stdout)
        files = [l.split()[1] for l in d.stdout.splitlines() if l.startswith("+++ ")]
        print(f"{t}: {len(files)} files: {', '.join(os.path.basename(f) for f in files)}")
        shutil.rmtree(tree)


if __name__ == "__main__":
    main()
