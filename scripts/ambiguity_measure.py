#!/usr/bin/env python3
"""Search ambiguity of a Java codebase, the measure of plans/ambiguity-experiment.md.

For each search term (a word a maintainer or a task description would use), a case-insensitive grep over the main
code, mapped to the methods the hit lines lie in:
  methods   distinct methods hit
  similar   mean pairwise similarity of the hit methods (Jaccard of 5-token shingles of their bodies, identifiers
            and literals kept), 0..1
  clustered share of hit methods that have a near-twin (similarity >= 0.6) among the other hit methods
And for the codebase: methods, near-twin pairs (>= 0.6) and clusters (connected components of near-twins, >= 3
members), overall and in the largest file.

Usage: scripts/ambiguity_measure.py <source root> [--terms "open orders" region invoice ...] [--terms-file f]
       [--json out.json]
"""
import argparse
import itertools
import json
import os
import re
import statistics

METHOD_START = re.compile(r"^\s*(?:(?:public|protected|private|static|final|synchronized|abstract|default)\s+)*"
                          r"(?:<[^>]+>\s+)?[\w<>\[\],.? ]+\s+(\w+)\s*\([^;{]*\)\s*(?:throws [\w., ]+)?\s*\{\s*$")
TOKEN = re.compile(r'"(?:[^"\\]|\\.)*"|\w+|[^\s\w]')
TWIN = 0.6


def methods_of(path):
    """(name, first line, last line, body text) of each method, by brace matching from a method header."""
    lines = open(path, encoding="utf-8", errors="replace").read().splitlines()
    out, i = [], 0
    while i < len(lines):
        m = METHOD_START.match(lines[i])
        if m and m.group(1) not in ("if", "for", "while", "switch", "catch", "try", "synchronized", "return", "new"):
            depth, j = 0, i
            while j < len(lines):
                depth += lines[j].count("{") - lines[j].count("}")
                if depth == 0:
                    break
                j += 1
            out.append((m.group(1), i + 1, j + 1, "\n".join(lines[i:j + 1])))
            i = j + 1
        else:
            i += 1
    return out, lines


def shingles(text, k=5):
    toks = TOKEN.findall(text)
    return {tuple(toks[i:i + k]) for i in range(max(1, len(toks) - k + 1))}


def jaccard(a, b):
    return len(a & b) / len(a | b) if a and b else 0.0


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("root")
    ap.add_argument("--terms", nargs="*", default=[])
    ap.add_argument("--terms-file")
    ap.add_argument("--json")
    args = ap.parse_args()
    terms = list(args.terms)
    if args.terms_file:
        terms += [t.strip() for t in open(args.terms_file) if t.strip() and not t.startswith("#")]
    files = [os.path.join(d, f) for d, _, fs in os.walk(args.root) for f in fs
             if f.endswith(".java") and "/src/main/" in os.path.join(d, f)]
    methods = []  # (file, name, start, end, shingles)
    file_lines = {}
    for path in sorted(files):
        ms, lines = methods_of(path)
        file_lines[path] = lines
        methods += [(path, n, s, e, shingles(body)) for n, s, e, body in ms]
    # near-twins over all methods (pairs within the same codebase)
    twins = {i: set() for i in range(len(methods))}
    by_size = sorted(range(len(methods)), key=lambda i: len(methods[i][4]))
    for x, y in itertools.combinations(by_size, 2):
        a, b = methods[x][4], methods[y][4]
        if min(len(a), len(b)) / max(len(a), len(b), 1) < TWIN:  # Jaccard cannot reach TWIN
            continue
        if jaccard(a, b) >= TWIN:
            twins[x].add(y); twins[y].add(x)
    seen, clusters = set(), []
    for i in range(len(methods)):
        if i in seen or not twins[i]:
            continue
        stack, comp = [i], set()
        while stack:
            k = stack.pop()
            if k in comp:
                continue
            comp.add(k); stack += list(twins[k] - comp)
        seen |= comp
        if len(comp) >= 3:
            clusters.append(comp)
    largest = max(file_lines, key=lambda p: len(file_lines[p]))
    in_largest = [i for i, m in enumerate(methods) if m[0] == largest]
    summary = {
        "files": len(files), "main_lines": sum(len(v) for v in file_lines.values()),
        "methods": len(methods), "twin_pairs": sum(len(v) for v in twins.values()) // 2,
        "clusters_ge3": len(clusters), "methods_in_clusters": sum(len(c) for c in clusters),
        "largest_file": os.path.relpath(largest, args.root), "largest_file_lines": len(file_lines[largest]),
        "largest_file_methods": len(in_largest),
        "largest_file_methods_with_twin": sum(1 for i in in_largest if twins[i]),
    }
    per_term = []
    for term in terms:
        rx = re.compile(re.escape(term).replace(r"\ ", r"[\s_]*"), re.I)
        hit = set()
        for i, (path, _, s, e, _) in enumerate(methods):
            if any(rx.search(l) for l in file_lines[path][s - 1:e]):
                hit.add(i)
        sims = [jaccard(methods[a][4], methods[b][4]) for a, b in itertools.combinations(sorted(hit), 2)]
        per_term.append({"term": term, "methods": len(hit),
                         "files": len({methods[i][0] for i in hit}),
                         "similar": round(statistics.mean(sims), 3) if sims else 0.0,
                         "clustered": round(sum(1 for i in hit if twins[i] & hit) / len(hit), 3) if hit else 0.0})
    for k, v in summary.items():
        print(f"{k:32s} {v}")
    if per_term:
        print(f"\n{'term':32s} methods files similar clustered")
        for t in per_term:
            print(f"{t['term'][:32]:32s} {t['methods']:7d} {t['files']:5d} {t['similar']:7.2f} {t['clustered']:9.2f}")
        print(f"{'median':32s} {statistics.median(t['methods'] for t in per_term):7.1f}")
    if args.json:
        json.dump({"summary": summary, "terms": per_term}, open(args.json, "w"), indent=1)


if __name__ == "__main__":
    main()
