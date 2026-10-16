#!/usr/bin/env python3
"""Analysis of the ambiguity pilot (plans/ambiguity-experiment.md): per variant against A, pooled over tasks (geometric
mean of per-task median ratios, 95% bootstrap interval, runs resampled within task and variant), for billed input
tokens, cost, turns, and the article's measure, reading-only tokens (characters of tool results / 4); per task kind;
and the mechanism from the transcripts (calls by class, files opened, search noise).
Runs listed in results/pilot-ambiguity-excluded.tsv (infrastructure errors) are left out.
Usage: analyze_pilot.py [--csv results/pilot-ambiguity.csv]"""
import argparse, csv, json, math, os, random, statistics as st, sys
HERE = os.path.dirname(os.path.abspath(__file__)); EXP = os.path.dirname(os.path.dirname(HERE))
sys.path.insert(0, os.path.join(EXP, "scripts"))


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("--csv", default=os.path.join(EXP, "results", "pilot-ambiguity.csv"))
    args = ap.parse_args()
    excluded = set()
    ex = os.path.join(EXP, "results", "pilot-ambiguity-excluded.tsv")
    if os.path.exists(ex):
        excluded = {l.split("\t")[0] for l in open(ex) if l.strip()}
    rows = [r for r in csv.DictReader(open(args.csv)) if r["run_id"] not in excluded]
    kinds = {}
    for t in {r["task"] for r in rows}:
        m = json.load(open(os.path.join(EXP, "ambiguity", "tasks", t, "meta.json")))
        kinds[t] = str(m.get("task_kind", m.get("kind")))
    for r in rows:
        r["reading_tokens"] = float(r["tool_response_chars"] or 0) / 4
    measures = [("total_input_tokens", "billed input tokens"), ("cost_usd", "cost"), ("turns", "turns"),
                ("output_tokens", "output tokens"), ("reading_tokens", "reading-only tokens (chars/4)")]
    variants = [v for v in ("s", "d", "dnames", "r") if any(r["variant"] == v for r in rows)]
    fails = [(r["task"], r["variant"]) for r in rows if r["success"] != "True"]
    print(f"{len(rows)} runs ({len(excluded)} excluded as infrastructure errors), {len(fails)} failed acceptance: {fails}")
    random.seed(1)

    def pooled(data, k):
        return math.exp(st.mean(math.log(st.median(float(x[k]) for x in b) / st.median(float(x[k]) for x in a)) for a, b in data.values()))

    for v in variants:
        for label, sel in (("all target tasks", lambda t: kinds[t] != "control"), ("controls", lambda t: kinds[t] == "control")) + tuple(
                (f"kind {k}", (lambda k: lambda t: kinds[t] == k)(k)) for k in ("1", "2", "3", "4")):
            data = {}
            for t in sorted(kinds):
                if not sel(t):
                    continue
                a = [r for r in rows if r["task"] == t and r["variant"] == "a"]
                b = [r for r in rows if r["task"] == t and r["variant"] == v]
                if a and b:
                    data[t] = (a, b)
            if not data:
                continue
            print(f"\n{v.upper()}/A, {label} ({len(data)} tasks, {sum(len(a) + len(b) for a, b in data.values())} runs)")
            for k, name in measures:
                est = pooled(data, k)
                boots = sorted(pooled({t: ([random.choice(a) for _ in a], [random.choice(b) for _ in b]) for t, (a, b) in data.items()}, k) for _ in range(2000))
                print(f"  {name:32s} {est:.2f}  ({boots[50]:.2f}–{boots[1949]:.2f})")
            if label == "all target tasks":
                print("  per task, billed input:", ", ".join(f"{t[:3]} {st.median(float(x['total_input_tokens']) for x in b) / st.median(float(x['total_input_tokens']) for x in a):.2f}" for t, (a, b) in data.items()))
    # mechanism, from the archived transcripts
    import turn_analysis
    print("\nmechanism (target tasks, mean per run): calls, locate, read, edit, files opened, search noise")
    for v in ["a"] + variants:
        rs = []
        for r in rows:
            if r["variant"] != v or kinds[r["task"]] == "control":
                continue
            tr = os.path.join(EXP, "results", "transcripts", r["run_id"] + ".jsonl.gz")
            if os.path.exists(tr):
                rs.append(turn_analysis.classify_run(tr))
        if rs:
            m = lambda f: st.mean(f(x) for x in rs)
            print(f"  {v:7s} n={len(rs):3d}  {m(lambda x: x['calls']):5.1f} {m(lambda x: x['classes'].count('locate')):5.1f} "
                  f"{m(lambda x: x['classes'].count('read')):5.1f} {m(lambda x: x['classes'].count('edit')):5.1f} "
                  f"{m(lambda x: x['files_touched']):5.1f} {m(lambda x: x['wasted_hits']):6.1f}")


if __name__ == "__main__":
    main()
