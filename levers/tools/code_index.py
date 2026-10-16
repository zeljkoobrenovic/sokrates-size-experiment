#!/usr/bin/env python3
"""A code index for a CLAUDE.md, from the code alone: one line per package of main code, listing its classes, each
with the names of its public and protected methods. With --budget the number of method names per class is capped at
the largest N that keeps the index within that many characters (classes over the cap end with "+k").
Usage: code_index.py <codebase root> [--budget CHARS]"""
import argparse, collections, os, re
SIG = re.compile(r"^\s+(?:@\w+\s+)*(?:public|protected)\s+(?:static\s+)?(?:final\s+)?(?:synchronized\s+)?(?:<[^>]+>\s+)?[\w<>\[\],.? ]+?\s+(\w+)\s*\(")


def collect(root):
    pkgs = collections.OrderedDict()
    for d, _, fs in sorted(os.walk(root)):
        if "/src/main/java/" not in d + "/" or "/target" in d:
            continue
        for f in sorted(fs):
            if f.endswith(".java"):
                names = []
                for l in open(os.path.join(d, f), encoding="utf-8", errors="replace"):
                    m = SIG.match(l)
                    if m and m.group(1) not in names and m.group(1) != f[:-5]:
                        names.append(m.group(1))
                pkgs.setdefault(os.path.relpath(d, root), []).append((f[:-5], names))
    return pkgs


def render(pkgs, n):
    out = []
    for p, classes in pkgs.items():
        items = []
        for c, ms in classes:
            shown = ms[:n]
            items.append(c + (f"({', '.join(shown)}{', +' + str(len(ms) - n) if len(ms) > n else ''})" if ms else ""))
        out.append(f"- `{p}/`: " + "; ".join(items))
    return "\n".join(out)


def main():
    ap = argparse.ArgumentParser(); ap.add_argument("root"); ap.add_argument("--budget", type=int)
    a = ap.parse_args()
    pkgs = collect(a.root)
    n = max(len(ms) for cs in pkgs.values() for _, ms in cs)
    while a.budget and n > 0 and len(render(pkgs, n)) > a.budget:
        n -= 1
    print(render(pkgs, n))


if __name__ == "__main__":
    main()
