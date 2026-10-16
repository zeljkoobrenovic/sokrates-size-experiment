#!/usr/bin/env python3
"""Apply a renames.tsv (old name(param types) <TAB> new call) as identifier substitutions to Java sources: every
`oldName(` that is a method call or declaration becomes `newName(`. Only for one-to-one renames (no overloaded old
name mapped to two new names, no merged methods); the script refuses otherwise.
Usage: apply_renames.py <renames.tsv> <file or folder>..."""
import os, re, sys
pairs = {}
for line in open(sys.argv[1], encoding="utf-8"):
    if not line.strip():
        continue
    old, new = line.rstrip("\n").split("\t")
    o = old.split("(")[0].strip(); n = re.match(r"\s*(\w+)\s*\(", new).group(1)
    if pairs.get(o, n) != n:
        sys.exit(f"{o} maps to two names ({pairs[o]}, {n}): not a one-to-one rename")
    pairs[o] = n
# names that another class also declares (a service method with the same name) are not renamed blindly
declared_elsewhere = set()
root_hint = os.environ.get("RENAME_ROOT")
if root_hint:
    for d, _, fs in os.walk(root_hint):
        for f in fs:
            if f.endswith(".java") and f != "DataStore.java" and "/src/main/" in os.path.join(d, f):
                for m in re.finditer(r"^\s*(?:public|protected|private)?\s*(?:static\s+)?[\w<>\[\],.? ]+\s+(\w+)\s*\([^;]*\)\s*\{", open(os.path.join(d, f), encoding="utf-8").read(), re.M):
                    declared_elsewhere.add(m.group(1))
skipped = sorted(set(pairs) & declared_elsewhere)
for o in skipped:
    pairs.pop(o)
if skipped:
    print("not renamed (also declared outside DataStore, check by hand):", ", ".join(skipped), file=sys.stderr)
rx = re.compile(r"\b(" + "|".join(sorted(map(re.escape, pairs), key=len, reverse=True)) + r")(\s*\(|\b)")
def fix(path):
    s = open(path, encoding="utf-8").read(); t = rx.sub(lambda m: pairs[m.group(1)] + m.group(2), s)
    if t != s:
        open(path, "w", encoding="utf-8").write(t)
for target in sys.argv[2:]:
    if os.path.isfile(target):
        fix(target)
    for d, _, fs in os.walk(target):
        for f in fs:
            if f.endswith(".java") and "/target/" not in d:
                fix(os.path.join(d, f))
