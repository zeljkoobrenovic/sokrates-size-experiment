#!/usr/bin/env python3
"""Analysis of the levers experiment (plans/levers-experiment.md).

Single-task conditions (results/levers-<model>.csv): per condition against base, per set and pooled over both sets,
the geometric mean of per-task median ratios with a 95% bootstrap interval (runs resampled within task and
condition), for cost, billed input, output, turns and calls; success counts; the first call's cache write (what the
CLAUDE.md adds to the fixed context); and the mechanism from the transcripts (calls by class).
Chains (results/levers-chains-<model>.csv): chain cost (sum of steps) session/fresh, ratio of medians with a bootstrap
over chains, and per step.
Usage: analyze_levers.py sonnet|haiku"""
import csv, gzip, json, math, os, random, statistics as st, sys
HERE = os.path.dirname(os.path.abspath(__file__)); EXP = os.path.dirname(os.path.dirname(HERE))
sys.path.insert(0, os.path.join(EXP, "scripts"))
CONDS = ["hint-file", "hint-symbol", "md-init", "md-index", "md-padding"]
MEASURES = [("cost_usd", "cost"), ("total_input_tokens", "billed input"), ("output_tokens", "output"), ("turns", "turns")]


def load(path):
    if not os.path.exists(path):
        return []
    return [r for r in csv.DictReader(open(path)) if r.get("cost_usd") not in ("", None) and r.get("turns") not in ("", None)]


def gm_ratio(data, k):
    return math.exp(st.mean(math.log(st.median(float(x[k]) for x in b) / st.median(float(x[k]) for x in a)) for a, b in data.values()))


def boot(data, k, n=2000):
    random.seed(1)
    bs = sorted(gm_ratio({t: ([random.choice(a) for _ in a], [random.choice(b) for _ in b]) for t, (a, b) in data.items()}, k) for _ in range(n))
    return bs[int(0.025 * n)], bs[int(0.975 * n) - 1]


def first_call_cache_write(run_id):
    p = os.path.join(EXP, "results", "transcripts", run_id + ".jsonl.gz")
    if not os.path.exists(p):
        return None
    for l in gzip.open(p, "rt"):
        d = json.loads(l)
        if d.get("type") == "assistant":
            u = d["message"].get("usage", {})
            return (u.get("cache_creation_input_tokens") or 0) + (u.get("cache_read_input_tokens") or 0) + (u.get("input_tokens") or 0)
    return None


def single(model):
    rows = load(os.path.join(EXP, "results", f"levers-{model}.csv"))
    if not rows:
        return
    for r in rows:
        r["set"] = "ambiguity" if r["task"].startswith("a") else "sokrates"
    print(f"== single-task conditions, {model}: {len(rows)} runs")
    for c in ["base"] + CONDS:
        rs = [r for r in rows if r["condition"] == c]
        fails = [r["task"][:3] for r in rs if r["success"] != "True"]
        fc = [x for x in (first_call_cache_write(r["run_id"]) for r in rs[:40]) if x]
        print(f"  {c:12s} n={len(rs):3d} failed={len(fails)} {fails if fails else ''} first-call context≈{int(st.median(fc)) if fc else '-'} tokens, "
              f"median cost ${st.median(float(r['cost_usd']) for r in rs) if rs else 0:.3f}")
    for scope in ("ambiguity", "sokrates", "both"):
        print(f"\n  {scope}: condition/base (95% interval)")
        for c in CONDS:
            data = {}
            for t in sorted({r["task"] for r in rows}):
                if scope != "both" and not t.startswith("a" if scope == "ambiguity" else "t"):
                    continue
                a = [r for r in rows if r["task"] == t and r["condition"] == "base"]
                b = [r for r in rows if r["task"] == t and r["condition"] == c]
                if a and b:
                    data[t] = (a, b)
            if not data:
                continue
            parts = []
            for k, name in MEASURES:
                lo, hi = boot(data, k)
                parts.append(f"{name} {gm_ratio(data, k):.2f} ({lo:.2f}–{hi:.2f})")
            print(f"    {c:12s} [{len(data)} tasks] " + "; ".join(parts))
            if c == "hint-symbol" or scope != "both":
                print("      per task, cost: " + ", ".join(f"{t[:3]} {st.median(float(x['cost_usd']) for x in b) / st.median(float(x['cost_usd']) for x in a):.2f}" for t, (a, b) in data.items()))
    try:
        import turn_analysis
    except ImportError:
        return
    print("\n  mechanism (mean per run): calls, locate, read, edit, build+fix, fixed-context share of cost")
    for scope in ("ambiguity", "sokrates"):
        for c in ["base"] + CONDS:
            rs = []
            for r in rows:
                if r["condition"] == c and r["set"] == scope:
                    p = os.path.join(EXP, "results", "transcripts", r["run_id"] + ".jsonl.gz")
                    if os.path.exists(p):
                        rs.append(turn_analysis.classify_run(p))
            if rs:
                m = lambda f: st.mean(f(x) for x in rs)
                print(f"    {scope:9s} {c:12s} n={len(rs):3d} {m(lambda x: x['calls']):5.1f} {m(lambda x: x['classes'].count('locate')):5.1f} "
                      f"{m(lambda x: x['classes'].count('read')):5.1f} {m(lambda x: x['classes'].count('edit')):5.1f} {m(lambda x: x['classes'].count('build') + x['classes'].count('fix')):5.1f} "
                      f"fixed {m(lambda x: (x['dollars'].get('prefix', 0) + x['dollars'].get('setup', 0)) / sum(x['dollars'].values())):.0%}")


def chains(model):
    rows = load(os.path.join(EXP, "results", f"levers-chains-{model}.csv"))
    if not rows:
        return
    by = {}
    for r in rows:
        by.setdefault(r["chain"], []).append(r)
    complete = {c: sorted(rs, key=lambda r: int(r["step"])) for c, rs in by.items() if len(rs) == 6}
    print(f"\n== chains, {model}: {len(complete)} complete chains ({len(by)} started)")
    modes = {m: [rs for c, rs in complete.items() if rs[0]["condition"] == m] for m in ("fresh", "session")}
    for m, cs in modes.items():
        fails = sum(1 for rs in cs for r in rs if r["success"] != "True")
        print(f"  {m:8s} chains={len(cs)} failed steps={fails} median chain cost ${st.median(sum(float(r['cost_usd']) for r in rs) for rs in cs) if cs else 0:.3f}")
    f, s = modes["fresh"], modes["session"]
    if not f or not s:
        return
    for k, name in MEASURES:
        val = lambda cs: st.median(sum(float(r[k]) for r in rs) for rs in cs)
        random.seed(1)
        bs = sorted(val([random.choice(s) for _ in s]) / val([random.choice(f) for _ in f]) for _ in range(2000))
        print(f"  session/fresh, whole chain, {name}: {val(s) / val(f):.2f} ({bs[50]:.2f}–{bs[1949]:.2f})")
    print("  per step, cost session/fresh (median): " + ", ".join(
        f"s{i + 1} {rs_s:.2f}" for i, rs_s in enumerate(
            st.median(float(rs[i]["cost_usd"]) for rs in s) / st.median(float(rs[i]["cost_usd"]) for rs in f) for i in range(6))))
    print("  per step, billed input session/fresh: " + ", ".join(
        f"s{i + 1} {st.median(float(rs[i]['total_input_tokens']) for rs in s) / st.median(float(rs[i]['total_input_tokens']) for rs in f):.2f}" for i in range(6)))
    print("  per step, turns session/fresh: " + ", ".join(
        f"s{i + 1} {st.median(float(rs[i]['turns']) for rs in s) / st.median(float(rs[i]['turns']) for rs in f):.2f}" for i in range(6)))


if __name__ == "__main__":
    model = sys.argv[1] if len(sys.argv) > 1 else "sonnet"
    single(model)
    chains(model)
