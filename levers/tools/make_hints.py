#!/usr/bin/env python3
"""The two hint levels of the levers experiment (plans/levers-experiment.md), derived mechanically from each task's
reference change on variant A (reference/a.patch):
  file    the files the reference change touches ("Where to look: the change belongs in X and Y.")
  symbol  the same files with, per Java file, the methods (or, outside a method, the class) the changed lines are in,
          and for other files the hunk context (e.g. a table of schema.sql)
Methods are those of the original code (the changed lines' enclosing declarations in A), so a hint never names a method the
change adds.
Writes levers/hints/<task>.json. Usage: make_hints.py --set ambiguity|sokrates <task> ..."""
import argparse, json, os, re, shutil, subprocess, tempfile
HERE = os.path.dirname(os.path.abspath(__file__)); EXP = os.path.dirname(os.path.dirname(HERE))
DECL = re.compile(r"^\s*(?:@\w+(?:\([^)]*\))?\s+)*(?:(?:public|protected|private|static|final|synchronized|abstract|default)\s+)*(?:<[^>]+>\s+)?(?:[\w<>\[\],.?]+(?:\s+|\s*(?=\w)))?(\w+)\s*\([^;=]*$")
KEYWORDS = {"if", "for", "while", "switch", "catch", "return", "new", "else", "try", "synchronized", "throw", "super", "this"}
CLASS = re.compile(r"^\s*(?:(?:public|protected|private|static|final|abstract)\s+)*(?:class|record|enum|interface)\s+(\w+)")


def strip(l):
    return re.sub(r'"(?:[^"\\]|\\.)*"', '""', l).split("//")[0]


def spans(lines):
    """(start, end, name) of every method/constructor declaration (by brace count from its first '{')."""
    out = []
    for i, l in enumerate(lines):
        m = DECL.match(l)
        if not m or m.group(1) in KEYWORDS or l.strip().startswith(("return", "new ", "else", "}")) or "." in l.split("(")[0].split()[-1]:
            continue
        if not re.match(r"^\s*(?:@\w+\s+)*(?:(?:public|protected|private|static|final|synchronized|abstract|default)\s+|[\w<>\[\],.?]+\s+\w+\s*\()", l):
            continue
        depth, opened, j = 0, False, i
        while j < len(lines):
            c = strip(lines[j])
            if not opened and ";" in c and "{" not in c:
                break  # an abstract or interface method, or a statement
            depth += c.count("{") - c.count("}")
            opened |= "{" in c
            if opened and depth <= 0:
                out.append((i, j, m.group(1)))
                break
            j += 1
    return out


def enclosing(lines, sp, i):
    inside = [s for s in sp if s[0] <= i <= s[1]]
    if inside:
        return max(inside)[2]
    return None


def changed_old_lines(patch):
    """Per file, the 0-based line numbers in the ORIGINAL file that a hunk removes, or before which it inserts."""
    out, f, n = {}, None, 0
    for l in patch.splitlines():
        if l.startswith("--- "):
            continue
        if l.startswith("+++ "):
            f = l[6:]; out.setdefault(f, {"lines": [], "ctx": []})
        elif l.startswith("@@"):
            n = int(re.search(r"-(\d+)", l).group(1)) - 1
            ctx = l.split("@@")[2].strip()
            if ctx:
                out[f]["ctx"].append(ctx)
        elif f and l.startswith("-"):
            out[f]["lines"].append(n); n += 1
        elif f and l.startswith("+"):
            out[f]["lines"].append((max(n - 1, 0), n))  # an insertion: between these two original lines
        elif f and not l.startswith("\\"):
            n += 1
    return out


def join(xs):
    return xs[0] if len(xs) == 1 else ", ".join(xs[:-1]) + " and " + xs[-1]


def main():
    ap = argparse.ArgumentParser(); ap.add_argument("--set", required=True); ap.add_argument("tasks", nargs="+")
    a = ap.parse_args()
    tasks_dir = os.path.join(EXP, "ambiguity", "tasks") if a.set == "ambiguity" else os.path.join(EXP, "tasks")
    variant = os.path.join(EXP, "ambiguity", "variants", "a") if a.set == "ambiguity" else os.path.join(EXP, "variants", "a")
    for t in a.tasks:
        td = next(os.path.join(tasks_dir, d) for d in sorted(os.listdir(tasks_dir)) if d.startswith(t))
        # re-diff the reference change against A itself, so hunk line numbers are exact (git apply tolerates offsets)
        tmp = tempfile.mkdtemp()
        r = os.path.join(tmp, "r")
        shutil.copytree(variant, r, ignore=shutil.ignore_patterns("target", ".git", "_sokrates*"))
        for c in (["git", "init", "-q"], ["git", "add", "-A"], ["git", "-c", "user.name=x", "-c", "user.email=x@x", "commit", "-qm", "a"],
                  ["git", "apply", os.path.join(td, "reference", "a.patch")]):
            subprocess.run(c, cwd=r, check=True)
        patch = subprocess.run(["git", "diff"], cwd=r, capture_output=True, text=True, check=True).stdout
        shutil.rmtree(tmp, ignore_errors=True)
        files, parts = [], []
        for f, info in changed_old_lines(patch).items():
            files.append(f"`{f}`")
            if f.endswith(".java"):
                lines = open(os.path.join(variant, f), encoding="utf-8").read().split("\n")
                sp, names = spans(lines), []
                for i in info["lines"]:
                    if isinstance(i, tuple):  # an insertion belongs to a method only if both neighbours are inside it
                        e1, e2 = (enclosing(lines, sp, min(k, len(lines) - 1)) for k in i)
                        e = e1 if e1 == e2 else None
                    else:
                        e = enclosing(lines, sp, min(i, len(lines) - 1))
                    if e and e not in names:
                        names.append(e)
                cls = os.path.basename(f)[:-5]
                syms = [n for n in names if n != cls]
                parts.append(f"in `{f}`, " + (("the method" if len(syms) == 1 else "the methods") + " " + join([f"`{s}`" for s in syms]) if syms else f"the class `{cls}`"))
            else:
                ctx = list(dict.fromkeys(info["ctx"]))
                parts.append(f"in `{f}`" + (", at " + join([f"`{c}`" for c in ctx]) if ctx else ""))
        hints = {"file": f"Where to look: the change belongs in {join(files)}.",
                 "symbol": f"Where to look: {'; '.join(parts)}."}
        json.dump(hints, open(os.path.join(EXP, "levers", "hints", os.path.basename(td) + ".json"), "w"), indent=1)
        print(os.path.basename(td)); print("  " + hints["file"]); print("  " + hints["symbol"])


if __name__ == "__main__":
    main()
