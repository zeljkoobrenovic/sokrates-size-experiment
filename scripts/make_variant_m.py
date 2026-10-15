#!/usr/bin/env python3
"""Make a merged variant (m3, m4): variant A with classes of one package merged into one file, nothing else changed.

Java allows one public top-level class per file and any number of package-private ones. A class that nothing
outside its package references can therefore move into another file of the package as a package-private top-level
class: its fully qualified name stays the same, so imports, tests and the reference patches keep working.
Per moved class: its file is deleted, its imports join the host file's (deduplicated, host order first), `public`
comes off its declaration, and its body from the declaration to the end of the file is appended unchanged.

Checks after a merge (see plans/next-experiments.md): `mvn install` with tests, scripts/golden.sh <variant>, and
scripts/compare_bytecode.sh a <variant> (javap of every class identical apart from the dropped public flags).

Usage: scripts/make_variant_m.py m3 [--force]
"""
import os
import re
import shutil
import subprocess
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
LANDSCAPE = "reports/src/main/java/nl/obren/sokrates/reports/landscape/statichtml"
GENERATORS = "reports/src/main/java/nl/obren/sokrates/reports/generators/statichtml"

# variant -> [(package dir, host class, classes moved into the host file, in this order)]
GROUPS = {
    "m3": [
        (LANDSCAPE, "LandscapeReportGenerator", ["LandscapeReportContributorsTab"]),
        (GENERATORS, "LogicalComponentsReportGenerator",
         # not DuplicationReportGenerator: it imports math3's Pair, ContributorsReportGenerator lang3's
         ["ContributorsReportGenerator", "ConcernsReportGenerator", "OverviewReportGenerator",
          "FileAgeReportGenerator", "FileChurnReportGenerator", "TrendReportGenerator", "FileSizeReportGenerator"]),
    ],
}

DECL = re.compile(r"^public ((?:final |abstract )*(?:class|interface|enum) \w+)", re.M)
IMPORT = re.compile(r"^import [^;]+;$", re.M)


def split(text, name):
    """(imports, body from the top-level declaration to the end)."""
    m = DECL.search(text)
    if not m or not m.group(1).endswith(" " + name):
        sys.exit(f"{name}: no 'public class {name}' at the start of a line")
    if len(re.findall(r"^(?:public )?(?:final |abstract )*(?:class|interface|enum) ", text, re.M)) != 1:
        sys.exit(f"{name}: more than one top-level type")
    return IMPORT.findall(text[:m.start()]), text[m.start():]


def merge(repo, pkg, host, moved):
    host_path = os.path.join(repo, pkg, host + ".java")
    text = open(host_path, encoding="utf-8").read()
    imports, host_body = split(text, host)
    head = text[:text.index(host_body)]
    extra, bodies = [], []
    for name in moved:
        path = os.path.join(repo, pkg, name + ".java")
        imp, body = split(open(path, encoding="utf-8").read(), name)
        extra += [i for i in imp if i not in imports and i not in extra]
        bodies.append(DECL.sub(r"\1", body, count=1).rstrip("\n") + "\n")
        os.remove(path)
    simple = {}
    for i in imports + extra:  # two single-type imports of one simple name would not compile, or worse, rebind
        if not i.startswith("import static") and not i.endswith(".*;"):
            n = i[:-1].rsplit(".", 1)[1]
            if simple.setdefault(n, i) != i:
                sys.exit(f"{host}: import clash {simple[n]} / {i}")
    if extra:
        last = head.rindex(imports[-1]) + len(imports[-1])
        head = head[:last] + "\n" + "\n".join(extra) + head[last:]
    out = head + host_body.rstrip("\n") + "\n" + "".join("\n" + b for b in bodies)
    open(host_path, "w", encoding="utf-8").write(out)
    return out.count("\n")


def make_analysis(variant):
    """analysis/<variant>: A's config pointed at the variant, and A's git history with every moved file's commits
    re-pointed to its host file (one line per commit and file, line counts added), as if the classes had always
    lived there."""
    src, dest = os.path.join(ROOT, "analysis", "a"), os.path.join(ROOT, "analysis", variant)
    os.makedirs(dest, exist_ok=True)
    config = open(os.path.join(src, "config.json"), encoding="utf-8").read()
    open(os.path.join(dest, "config.json"), "w", encoding="utf-8").write(config.replace('"../../variants/a"', f'"../../variants/{variant}"'))
    for name in ("git-commits.txt", "git-commit-trailers.txt"):
        shutil.copy(os.path.join(src, name), dest)
    moved_to = {f"{pkg}/{m}.java": f"{pkg}/{host}.java" for pkg, host, moved in GROUPS[variant] for m in moved}
    rows, order = {}, []
    for line in open(os.path.join(src, "git-history.txt"), encoding="utf-8"):
        parts = line.rstrip("\n").split(" ")
        if len(parts) < 7:
            order.append(line)
            continue
        parts[3] = moved_to.get(parts[3], parts[3])
        key = tuple(parts[:5])
        if key in rows:
            rows[key][5] += int(parts[5])
            rows[key][6] += int(parts[6])
        else:
            parts[5], parts[6] = int(parts[5]), int(parts[6])
            rows[key] = parts
            order.append(key)
    with open(os.path.join(dest, "git-history.txt"), "w", encoding="utf-8") as f:
        for k in order:
            f.write(k if isinstance(k, str) else " ".join(map(str, rows[k])) + "\n")
    print(f"analysis/{variant}: config and git history ({len(moved_to)} files re-pointed); run scripts/analyze_variant.sh {variant}")


def main():
    if len(sys.argv) < 2 or sys.argv[1] not in GROUPS:
        sys.exit(f"usage: make_variant_m.py {{{','.join(GROUPS)}}} [--force]")
    variant = sys.argv[1]
    repo = os.path.join(ROOT, "variants", variant)
    if os.path.exists(repo):
        if "--force" not in sys.argv:
            sys.exit(f"variants/{variant} exists (--force to remake it)")
        shutil.rmtree(repo)
    shutil.copytree(os.path.join(ROOT, "variants", "a"), repo, symlinks=True,
                    ignore=shutil.ignore_patterns("target", "_sokrates"))
    for pkg, host, moved in GROUPS[variant]:
        lines = merge(repo, pkg, host, moved)
        print(f"{pkg}/{host}.java: {lines} lines ({len(moved)} classes merged in)")
    make_analysis(variant)


if __name__ == "__main__":
    main()
