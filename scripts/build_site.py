#!/usr/bin/env python3
"""Build the GitHub Pages site (docs/index.html) from the results: one self-contained page with the data embedded
and the charts drawn as inline SVG in the browser. Re-run after every series: scripts/build_site.py

Series are read from results/<file>.csv as listed in SERIES; a series that has no file yet is skipped.
"""
import csv
import glob
import html
import json
import math
import os
import random
import statistics as st
import sys
from collections import defaultdict

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
sys.path.insert(0, os.path.join(ROOT, "harness"))
from compare import pooled  # noqa: E402

SERIES = [
    ("sonnet", "Claude Sonnet 5.5", "runs.csv", "The main series."),
    ("haiku", "Claude Haiku 5.5", "runs-haiku.csv", "The weaker-model series, same tasks and harness."),
]
MEASURES = [("total_input_tokens", "input tokens"), ("output_tokens", "output tokens"), ("cost_usd", "cost"),
            ("turns", "turns"), ("lines_seen", "lines seen"), ("duration_s", "wall time")]
BOOT = 2000


def load_series(path):
    rows = list(csv.DictReader(open(path)))
    groups = defaultdict(list)
    for r in rows:
        groups[(r["task"], r["variant"])].append(r)
    return rows, groups


def quart(values):
    values = sorted(values)
    if len(values) < 4:
        return [values[0], st.median(values), values[-1]]
    q = st.quantiles(values, n=4)
    return [q[0], st.median(values), q[2]]


def series_stats(key, rows, groups):
    tasks = sorted({r["task"] for r in rows})
    kinds = {"target": [t for t in tasks if not t.startswith("c")], "control": [t for t in tasks if t.startswith("c")]}
    out = {"key": key, "runs": len(rows), "failures": sum(1 for r in rows if r["success"] != "True"),
           "cost": round(sum(float(r["cost_usd"]) for r in rows), 2),
           "runs_per_cell": [min(len(v) for v in groups.values()), max(len(v) for v in groups.values())],
           "model": sorted({r["model"] for r in rows}), "started": min(r["started"] for r in rows)[:16].replace("T", " "),
           "pooled": {}, "tasks": {}, "behaviour": {}}
    for m, _ in MEASURES:
        out["pooled"][m] = {}
        for kind in ("target", "control"):
            p, lo, hi = pooled(groups, kinds[kind], m, BOOT)
            out["pooled"][m][kind] = [round(p, 3), round(lo, 3), round(hi, 3)]
    for t in tasks:
        a = groups[(t, "a")]
        b = groups[(t, "b")]
        if not a or not b:
            continue
        entry = {"kind": "control" if t.startswith("c") else "target", "n": [len(a), len(b)], "success": [
            sum(1 for r in a if r["success"] == "True"), sum(1 for r in b if r["success"] == "True")]}
        for m, _ in MEASURES:
            va = [float(r[m]) for r in a]
            vb = [float(r[m]) for r in b]
            entry[m] = {"a": quart(va), "b": quart(vb), "ratio": round(st.median(vb) / st.median(va), 3),
                        "runs_a": sorted(va), "runs_b": sorted(vb)}
        out["tasks"][t] = entry
    out["behaviour"] = {
        "median_input": st.median(float(r["total_input_tokens"]) for r in rows),
        "median_turns": st.median(float(r["turns"]) for r in rows),
        "median_lines_seen": st.median(float(r["lines_seen"]) for r in rows),
        "read_lines_per_run": round(sum(int(r["lines_read"]) for r in rows) / len(rows)),
        "read_calls_per_run": round(sum(int(r["reads"]) for r in rows) / len(rows), 2),
        "bash_calls_per_run": round(sum(int(r["bashes"]) for r in rows) / len(rows), 1),
        "median_cost": round(st.median(float(r["cost_usd"]) for r in rows), 3),
        "median_duration": st.median(float(r["duration_s"]) for r in rows),
    }
    cv = [st.pstdev(v) / st.mean(v) for v in ([float(r["total_input_tokens"]) for r in g] for g in groups.values()) if len(v) > 1]
    out["cv_median"] = round(st.median(cv), 2)
    out["cv_max"] = round(max(cv), 2)
    return out


def load_tasks():
    tasks = []
    for d in sorted(glob.glob(os.path.join(ROOT, "tasks", "*", "meta.json"))):
        m = json.load(open(d))
        tasks.append({"id": m["id"], "kind": m["kind"], "title": m["title"], "commit": m["reference_commit"],
                      "module": m["module"], "lands": m.get("target_unit", ""),
                      "prompt": open(os.path.join(os.path.dirname(d), "task.md")).read().strip()})
    return tasks


# Sokrates' predictions as they were when the experiment started (scoring of 2026-10-07); the scoring was changed on
# 2026-10-09 in response to the results, and load_variant_facts() reads the re-scored values from the analyses.
ORIGINAL_PREDICTIONS = {"a": {"human": [6.4, "C"], "ai": [6.6, "B"], "context": 1690}, "b": {"human": [7.2, "B"], "ai": [7.7, "B"], "context": 774}}


def load_variant_facts():
    facts = {}
    for v in ("a", "b"):
        import zipfile
        z = os.path.join(ROOT, "analysis", v, "reports", "data", "data.zip")
        if not os.path.exists(z):
            continue
        with zipfile.ZipFile(z) as zf:
            r = json.loads(zf.read("analysisResults.json"))
            files = json.loads(zf.read("files.json"))
            units = json.loads(zf.read("units.json"))
        ms = r["maintainabilityScores"]
        facts[v] = {"files": len(files), "loc": sum(f["linesOfCode"] for f in files),
                    "big_files": sum(1 for f in files if f["linesOfCode"] > 500),
                    "big_units": sum(1 for u in units if u["linesOfCode"] > 50 or u["mcCabeIndex"] > 25),
                    "human": [ms["human"]["value"], ms["human"]["grade"]], "ai": [ms["ai"]["value"], ms["ai"]["grade"]],
                    "context": ms.get("contextLinesPerChange"), "original": ORIGINAL_PREDICTIONS[v]}
    return facts


TEMPLATE = r"""<!doctype html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Below the read budget, file and unit length is not a cost driver</title>
<meta name="description" content="A controlled experiment: splitting long files and units did not lower what an AI coding agent spends. Sokrates, two variants, twelve tasks, two models, 480 runs.">
<style>
:root {
  color-scheme: light;
  --bg: #f9f9f7; --surface: #fcfcfb; --surface-2: #f0efec; --text: #0b0b0b; --text-2: #52514e; --muted: #898781;
  --grid: #e1e0d9; --axis: #c3c2b7; --border: rgba(11,11,11,0.10); --link: #1c5cab;
  --s1: #2a78d6; --s2: #eb6834; --s3: #1baf7a; --good: #006300;
  --font: system-ui, -apple-system, "Segoe UI", sans-serif; --mono: ui-monospace, SFMono-Regular, Menlo, monospace;
}
@media (prefers-color-scheme: dark) { :root:not([data-theme="light"]) {
  color-scheme: dark; --bg: #0d0d0d; --surface: #1a1a19; --surface-2: #262625; --text: #ffffff; --text-2: #c3c2b7; --muted: #898781;
  --grid: #2c2c2a; --axis: #383835; --border: rgba(255,255,255,0.10); --link: #86b6ef; --s1: #3987e5; --s2: #d95926; --s3: #199e70; --good: #0ca30c; } }
:root[data-theme="dark"] {
  color-scheme: dark; --bg: #0d0d0d; --surface: #1a1a19; --surface-2: #262625; --text: #ffffff; --text-2: #c3c2b7; --muted: #898781;
  --grid: #2c2c2a; --axis: #383835; --border: rgba(255,255,255,0.10); --link: #86b6ef; --s1: #3987e5; --s2: #d95926; --s3: #199e70; --good: #0ca30c; }
* { box-sizing: border-box; }
body { margin: 0; background: var(--bg); color: var(--text); font-family: var(--font); line-height: 1.5; font-size: 16px; }
main { max-width: 980px; margin: 0 auto; padding: 24px 16px 64px; }
header.hero { padding: 40px 0 8px; }
h1 { font-size: 2rem; line-height: 1.15; margin: 0 0 12px; letter-spacing: -0.01em; }
h2 { font-size: 1.4rem; margin: 48px 0 12px; letter-spacing: -0.01em; }
h3 { font-size: 1.05rem; margin: 24px 0 8px; }
p { max-width: 72ch; }
.lede { font-size: 1.15rem; color: var(--text-2); max-width: 64ch; }
.meta { color: var(--muted); font-size: 0.9rem; }
a { color: var(--link); }
.tiles { display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr)); gap: 12px; margin: 20px 0; }
.tile { background: var(--surface); border: 1px solid var(--border); border-radius: 10px; padding: 14px 16px; }
.tile .v { font-size: 1.9rem; font-weight: 600; line-height: 1.1; }
.tile .l { color: var(--text-2); font-size: 0.9rem; margin-top: 4px; }
.tile .s { color: var(--muted); font-size: 0.8rem; margin-top: 2px; }
.card { background: var(--surface); border: 1px solid var(--border); border-radius: 12px; padding: 16px; margin: 16px 0; }
.card h3 { margin-top: 0; }
.row { display: grid; grid-template-columns: 1fr; gap: 16px; }
@media (min-width: 760px) { .row.two { grid-template-columns: 1fr 1fr; } }
table { border-collapse: collapse; width: 100%; font-size: 0.92rem; }
th, td { text-align: left; padding: 6px 8px; border-bottom: 1px solid var(--grid); vertical-align: top; }
th { color: var(--text-2); font-weight: 600; }
td.num, th.num { text-align: right; font-variant-numeric: tabular-nums; white-space: nowrap; }
.scroll { overflow-x: auto; }
.legend { display: flex; gap: 16px; flex-wrap: wrap; font-size: 0.85rem; color: var(--text-2); margin: 4px 0 8px; }
.legend span::before { content: ""; display: inline-block; width: 10px; height: 10px; border-radius: 50%; margin-right: 6px; vertical-align: middle; }
.legend .k1::before { background: var(--s1); } .legend .k2::before { background: var(--s2); } .legend .k3::before { background: var(--s3); }
svg text { font-family: var(--font); fill: var(--text-2); font-size: 12px; }
svg .grid { stroke: var(--grid); stroke-width: 1; }
svg .axis { stroke: var(--axis); stroke-width: 1; }
svg .ref { stroke: var(--text-2); stroke-width: 1.5; }
svg .ci { stroke-width: 2; stroke-linecap: round; }
svg .hit { fill: transparent; }
.tip { position: fixed; pointer-events: none; background: var(--surface); color: var(--text); border: 1px solid var(--border); border-radius: 8px; padding: 8px 10px; font-size: 0.85rem; box-shadow: 0 4px 16px rgba(0,0,0,0.12); display: none; max-width: 320px; z-index: 10; }
details { margin: 8px 0; } summary { cursor: pointer; color: var(--text-2); }
code { font-family: var(--mono); font-size: 0.9em; background: var(--surface-2); padding: 1px 4px; border-radius: 4px; }
pre { background: var(--surface-2); padding: 12px; border-radius: 8px; overflow-x: auto; font-size: 0.85rem; }
.toggle { position: fixed; top: 12px; right: 12px; border: 1px solid var(--border); background: var(--surface); color: var(--text); border-radius: 999px; width: 36px; height: 36px; cursor: pointer; }
.pill { display: inline-block; border-radius: 999px; padding: 1px 8px; font-size: 0.8rem; background: var(--surface-2); color: var(--text-2); }
.kind-target { color: var(--s1); } .kind-control { color: var(--s2); }
ul { padding-left: 20px; } li { margin: 4px 0; max-width: 72ch; }
.note { border-left: 3px solid var(--s1); padding: 4px 12px; color: var(--text-2); }
footer { margin-top: 48px; color: var(--muted); font-size: 0.85rem; }
</style>
</head>
<body>
<button class="toggle" id="theme" title="Toggle light/dark" aria-label="Toggle light or dark theme">◐</button>
<div class="tip" id="tip"></div>
<main>
<header class="hero">
  <div class="meta">An experiment · Sokrates · ${generated}</div>
  <h1>Below the agent’s read budget, file and unit length is not a cost driver</h1>
  <p class="lede">Splitting long files and units did not save an AI coding agent tokens. The same twelve changes, made by the same agent in two versions of one codebase that differ only in the length of their files and units, cost the same tokens, turns and time. Measured with two models, ten repeats per task and variant, control tasks and acceptance tests. What the agent pays for is the number of places a change touches, and files longer than one read.</p>
</header>

<div class="tiles" id="hero-tiles"></div>

<h2>What was tested</h2>
<p>Sokrates' AI ease-of-change score and its AI Cost Estimator assumed that long files and long units make every change more expensive for an AI agent, because the agent has to read more to make the same change. The same claim was made with numbers in July 2026 in <a href="https://martinfowler.com/articles/exploring-gen-ai/refactoring-economic-benefit.html">The Economic Benefit of Refactoring</a> on martinfowler.com: after splitting a 17,155-line file, a change cost 83% fewer input tokens. This experiment measured the claim with repeats, controls and acceptance tests on a mature human-written codebase: Sokrates itself, as it was a year before the experiment.</p>

<h2>Two variants of one codebase</h2>
<p>Variant A is Sokrates at commit <code>5e6aa97a</code> of 2025-09-20. Variant B is A with every main file brought under 500 lines of code and every unit under 50 lines and McCabe 25, by <em>extract method</em> and <em>extract class</em> only: 52 package-private helper classes in the original packages, every original class keeping its name and public API. The split was done in five parallel batches by coding agents under a strict brief, with the module tests and a golden-output check after every file: the reports both variants generate for a fixture are byte-identical across 850 normalized files.</p>
<div class="card scroll" id="variants"></div>

<h2>The result: no difference</h2>
<p>For every measure, the ratio of what variant B cost to what variant A cost, pooled over the ten target tasks (whose change lands in a split file) and separately over the two control tasks (whose files are byte-identical in A and B, so they should sit at 1.0). The pooled ratio is the geometric mean over tasks of the ratio of means, with a 95% bootstrap interval over runs. A value of 1.0 means the split changed nothing.</p>
<div class="legend"><span class="k1">target tasks (split files)</span><span class="k2">control tasks (identical files)</span></div>
<div class="row two" id="pooled"></div>
<div class="note"><p id="result-text"></p></div>

<h2>Per task</h2>
<p>Input tokens per run, variant A and variant B, every run shown; the bar is the median. Tasks are ordered as in the task list; the two control tasks are last.</p>
<div class="legend"><span class="k1">variant A runs</span><span class="k2">variant B runs</span></div>
<div id="pertask"></div>

<h2>How the two agents read</h2>
<p>The tool log of every run records each file read, with its line count, and each shell command with its output. The two models behave differently, and that is what decides whether length can matter.</p>
<div class="tiles" id="behaviour"></div>
<p id="behaviour-text"></p>

<h2>The tasks</h2>
<p>Real changes made to this code in the year after the snapshot, mined from the git history and re-derived against the snapshot. Each task is a behaviour description (what should happen, not where) and a JUnit acceptance test verified to fail on each untouched variant and to pass with a reference patch, on A and on B.</p>
<div class="card scroll" id="tasks"></div>

<h2>Compared with the martinfowler.com experiment</h2>
<div class="card scroll" id="fowler"></div>
<p>The two experiments agree on the mechanism and disagree on the result. His explanation for the 83% drop is that the agent read less code, not that there was less code; our logs say the same from the other side: what the agent reads is decided by how it searches, not by how long the file is. The difference in starting points is a factor of ten: his largest file was 17,155 lines, beyond what one read returns, so the agent had to page through it or read blind; ours all fit in one read, and a grep landed on one or two hits. If the cost of length is a step around the agent's read budget rather than a slope from zero, both results are consistent. That is the next experiment.</p>

<h2>Conclusions</h2>
<ul>
  <li><strong>Below the read budget, length is not a cost driver.</strong> Splitting files and units that already fit in one read did not change input tokens, output tokens, cost, turns or time, for either model. The target tasks sit at 1.0 with intervals of a few percent; the controls scatter more.</li>
  <li><strong>The bill is the re-sent context.</strong> A run is 13 to 16 turns, and each turn re-reads the whole conversation as cache reads. The number of turns is set by how many greps, edits and test runs the task needs, and the split neither adds nor removes those on average.</li>
  <li><strong>A split can cost reading.</strong> The weaker model, which opens files whole, looked at about a quarter more code in B, because the same feature spans more files there. The extra reading did not reach the token bill.</li>
  <li><strong>Unit length did not matter either, for local edits.</strong> Seven target tasks land in units of 55 to 128 lines in A, split to under 45 in B, with no separation from the three tasks in short units. The agent greps for the line and reads twenty lines either side; it never needs the rest of the unit.</li>
  <li><strong>For Sokrates:</strong> before the experiment its AI ease-of-change score rose from 6.6 to 7.7 for the split and the predicted context per change halved, while the measured cost did not move. The scoring was changed on 2026-10-09 in response: unit size and the 500-LOC file size no longer count for agents, a new measure counts only files beyond one read (2,000 lines), and context per change is capped at a 200-line window per touched file. Re-scored, the two variants sit at 7.2 and 7.3.</li>
</ul>
<h3>Limits</h3>
<ul>
  <li>One agent harness (Claude Code), two models, one Java codebase, tasks phrased with greppable terms, a mechanical split.</li>
  <li>The original layout is public and plausibly in the models' training data, which would favour A; with ratios at 1.0 it is not hiding an effect.</li>
  <li>Every task is a local edit. Changes that depend on state far away in a long unit, or files above the read budget, are where length could show, and this experiment did not create that situation.</li>
</ul>
<h3>Next</h3>
<ul>
  <li>The same harness on a codebase with a file far above the read budget, split to under it, to find the step.</li>
  <li>Tasks whose change is non-local inside a long unit.</li>
  <li>Sokrates' thresholds for agents moved to where the step is, not where the human thresholds are.</li>
</ul>

<h2>Method</h2>
<ul>
  <li><strong>Runs.</strong> Claude Code headless (<code>claude -p … --output-format json</code>), the same prompt, tools, permissions and turn limit for every run, in a fresh one-commit copy of the variant with a neutral folder name. One worker per variant with its own Maven repository. Token counts are the CLI's own, with input split by cache state.</li>
  <li><strong>Per run:</strong> prebuild, agent, a PostToolUse hook logging every tool call (file reads with line counts, shell commands and output size, edits, test runs), the diff, the acceptance test with the upstream modules built from the working tree, one row in the results file and a full record with the tool log.</li>
  <li><strong>Analysis.</strong> Per task: medians and quartiles over runs. Pooled: geometric mean over tasks of the ratio of means, 95% bootstrap interval (2,000 resamples of runs within cells). Control tasks on identical code bound the noise.</li>
  <li><strong>Reproduce.</strong> The repository holds the variants, the analyses, the tasks with acceptance tests and reference patches, the harness and every run's record:</li>
</ul>
<pre>harness/run.py --tasks &lt;tasks&gt; --variants a --repeats 10 --m2 work/m2-a            # one worker per variant
harness/run.py --tasks &lt;tasks&gt; --variants b --repeats 10 --m2 work/m2-b
harness/compare.py --csv results/runs.csv                                           # the pooled tables
scripts/check_tasks.py                                                              # every task fails before, passes after, on both variants
scripts/golden.sh b                                                                 # B generates the same reports as A
scripts/build_site.py                                                               # this page</pre>
<p>Longer write-up: <a href="posts/analysis.md">docs/posts/analysis.md</a>.</p>

<footer>Generated ${generated} from the results in the repository. Charts are inline SVG; no external resources.</footer>
</main>

<script id="data" type="application/json">${data}</script>
<script>
(function () {
  const D = JSON.parse(document.getElementById('data').textContent);
  const fmt = (v, m) => m === 'cost_usd' ? '$' + v.toFixed(2) : m === 'duration_s' ? Math.round(v) + ' s' : Math.round(v).toLocaleString('en-US');
  const r2 = v => v.toFixed(2);
  const esc = s => String(s).replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
  const tip = document.getElementById('tip');
  function showTip(e, html) { tip.innerHTML = html; tip.style.display = 'block'; moveTip(e); }
  function moveTip(e) { const x = e.clientX + 14, y = e.clientY + 14; tip.style.left = Math.min(x, window.innerWidth - tip.offsetWidth - 8) + 'px'; tip.style.top = Math.min(y, window.innerHeight - tip.offsetHeight - 8) + 'px'; }
  function hideTip() { tip.style.display = 'none'; }
  const svgNS = 'http://www.w3.org/2000/svg';
  function el(name, attrs, text) { const n = document.createElementNS(svgNS, name); for (const k in attrs) n.setAttribute(k, attrs[k]); if (text != null) n.textContent = text; return n; }

  // theme toggle
  const root = document.documentElement;
  try { const t = localStorage.getItem('size-exp-theme'); if (t) root.setAttribute('data-theme', t); } catch (e) {}
  document.getElementById('theme').onclick = () => {
    const cur = root.getAttribute('data-theme') || (matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light');
    const next = cur === 'dark' ? 'light' : 'dark'; root.setAttribute('data-theme', next);
    try { localStorage.setItem('size-exp-theme', next); } catch (e) {}
  };

  const series = D.series;
  const main = series[0];

  // hero tiles
  const tiles = document.getElementById('hero-tiles');
  function tile(v, l, s) { const d = document.createElement('div'); d.className = 'tile'; d.innerHTML = '<div class="v">' + esc(v) + '</div><div class="l">' + esc(l) + '</div>' + (s ? '<div class="s">' + esc(s) + '</div>' : ''); return d; }
  for (const s of series) {
    const p = s.pooled.total_input_tokens.target, c = s.pooled.total_input_tokens.control;
    tiles.appendChild(tile(r2(p[0]), 'B/A input tokens, target tasks, ' + s.name, '95% interval ' + r2(p[1]) + '–' + r2(p[2]) + ' · controls ' + r2(c[0])));
  }
  tiles.appendChild(tile(series.reduce((a, s) => a + s.runs, 0).toLocaleString('en-US'), 'runs', series.map(s => s.name.replace('Claude ', '') + ' ' + s.runs).join(' · ')));
  tiles.appendChild(tile(series.reduce((a, s) => a + s.failures, 0), 'failed acceptance tests', 'every run was checked by a JUnit test'));

  // variants table
  const V = D.variants;
  if (V.a && V.b) {
    const rows = [
      ['main code', V.a.files + ' files, ' + V.a.loc.toLocaleString('en-US') + ' LOC', V.b.files + ' files, ' + V.b.loc.toLocaleString('en-US') + ' LOC'],
      ['files over 500 LOC', V.a.big_files, V.b.big_files],
      ['units over 50 LOC or McCabe 25', V.a.big_units, V.b.big_units],
      ['Human ease of change (Sokrates)', V.a.human[0] + ' (' + V.a.human[1] + ')', V.b.human[0] + ' (' + V.b.human[1] + ')'],
      ['AI ease of change, scoring before the experiment', V.a.original.ai[0] + ' (' + V.a.original.ai[1] + ')', V.b.original.ai[0] + ' (' + V.b.original.ai[1] + ')'],
      ['predicted context lines per change, before', V.a.original.context.toLocaleString('en-US'), V.b.original.context.toLocaleString('en-US')],
      ['AI ease of change, scoring changed after the results', V.a.ai[0] + ' (' + V.a.ai[1] + ')', V.b.ai[0] + ' (' + V.b.ai[1] + ')'],
      ['predicted context lines per change, after', V.a.context.toLocaleString('en-US'), V.b.context.toLocaleString('en-US')],
    ];
    document.getElementById('variants').innerHTML = '<table><thead><tr><th></th><th>variant A</th><th>variant B</th></tr></thead><tbody>' +
      rows.map(r => '<tr><td>' + esc(r[0]) + '</td><td>' + esc(r[1]) + '</td><td>' + esc(r[2]) + '</td></tr>').join('') + '</tbody></table>';
  }

  // pooled chart: dot + interval per measure, two series (target, control), one panel per model series
  const measures = D.measures;
  const pooledDiv = document.getElementById('pooled');
  for (const s of series) {
    const card = document.createElement('div'); card.className = 'card';
    card.innerHTML = '<h3>' + esc(s.name) + ' <span class="pill">' + s.runs + ' runs, ' + s.runs_per_cell[0] + '–' + s.runs_per_cell[1] + ' per cell</span></h3>';
    const W = 440, H = 30 * measures.length + 50, L = 110, R = 16, T = 10;
    const xmin = 0.5, xmax = 1.6;
    const x = v => L + (Math.log(v) - Math.log(xmin)) / (Math.log(xmax) - Math.log(xmin)) * (W - L - R);
    const svg = el('svg', {viewBox: '0 0 ' + W + ' ' + H, width: '100%', role: 'img', 'aria-label': 'Pooled B/A ratios with intervals, ' + s.name});
    for (const g of [0.5, 0.7, 1.0, 1.4]) {
      svg.appendChild(el('line', {x1: x(g), x2: x(g), y1: T, y2: H - 28, class: g === 1 ? 'ref' : 'grid'}));
      svg.appendChild(el('text', {x: x(g), y: H - 10, 'text-anchor': 'middle'}, g.toFixed(1) + (g === 1 ? ' = no change' : '')));
    }
    measures.forEach((m, i) => {
      const y = T + 15 + i * 30;
      svg.appendChild(el('text', {x: L - 8, y: y + 4, 'text-anchor': 'end'}, m[1]));
      ['target', 'control'].forEach((k, j) => {
        const v = s.pooled[m[0]][k]; const yy = y + (j ? 7 : -7); const col = j ? 'var(--s2)' : 'var(--s1)';
        const lo = Math.max(v[1], xmin), hi = Math.min(v[2], xmax);
        svg.appendChild(el('line', {x1: x(lo), x2: x(hi), y1: yy, y2: yy, stroke: col, class: 'ci', opacity: 0.55}));
        svg.appendChild(el('circle', {cx: x(Math.min(Math.max(v[0], xmin), xmax)), cy: yy, r: 5, fill: col, stroke: 'var(--surface)', 'stroke-width': 2}));
        const hit = el('rect', {x: L, y: yy - 7, width: W - L - R, height: 14, class: 'hit'});
        hit.addEventListener('mousemove', e => showTip(e, '<b>' + esc(m[1]) + '</b>, ' + k + ' tasks<br>B/A = ' + r2(v[0]) + ' (95% interval ' + r2(v[1]) + '–' + r2(v[2]) + ')'));
        hit.addEventListener('mouseleave', hideTip);
        svg.appendChild(hit);
      });
    });
    card.appendChild(svg);
    const det = document.createElement('details'); det.innerHTML = '<summary>table</summary>';
    det.innerHTML += '<div class="scroll"><table><thead><tr><th>measure</th><th class="num">target tasks</th><th class="num">control tasks</th></tr></thead><tbody>' +
      measures.map(m => { const t = s.pooled[m[0]].target, c = s.pooled[m[0]].control; return '<tr><td>' + esc(m[1]) + '</td><td class="num">' + r2(t[0]) + ' (' + r2(t[1]) + '–' + r2(t[2]) + ')</td><td class="num">' + r2(c[0]) + ' (' + r2(c[1]) + '–' + r2(c[2]) + ')</td></tr>'; }).join('') + '</tbody></table></div>';
    card.appendChild(det);
    pooledDiv.appendChild(card);
  }
  const p0 = main.pooled.total_input_tokens;
  document.getElementById('result-text').textContent = 'With ' + main.name + ', the target tasks cost ' + r2(p0.target[0]) + ' times as many input tokens in B as in A (95% interval ' + r2(p0.target[1]) + ' to ' + r2(p0.target[2]) + '); the controls, on identical code, sit at ' + r2(p0.control[0]) + ' (' + r2(p0.control[1]) + ' to ' + r2(p0.control[2]) + ').' +
    (series[1] ? ' With ' + series[1].name + ': ' + r2(series[1].pooled.total_input_tokens.target[0]) + ' (' + r2(series[1].pooled.total_input_tokens.target[1]) + ' to ' + r2(series[1].pooled.total_input_tokens.target[2]) + ') on the targets and ' + r2(series[1].pooled.total_input_tokens.control[0]) + ' on the controls; its lines seen in B are ' + r2(series[1].pooled.lines_seen.target[0]) + ' times A’s on the targets, the one measure with a lean.' : '') +
    ' Within a cell (same task, same variant) input tokens vary with a coefficient of variation of ' + main.cv_median + ' typically, up to ' + main.cv_max + '.';

  // per-task strip charts, one card per series
  const taskOrder = D.tasks.map(t => t.id).filter(id => !id.startsWith('c')).concat(D.tasks.map(t => t.id).filter(id => id.startsWith('c')));
  const perTask = document.getElementById('pertask');
  for (const s of series) {
    const card = document.createElement('div'); card.className = 'card';
    card.innerHTML = '<h3>' + esc(s.name) + ': input tokens per run</h3>';
    const ids = taskOrder.filter(id => s.tasks[id]);
    const W = 900, rowH = 26, L = 250, R = 70, T = 30, H = T + rowH * ids.length + 36;
    const allv = ids.flatMap(id => s.tasks[id].total_input_tokens.runs_a.concat(s.tasks[id].total_input_tokens.runs_b));
    const xmin = Math.min.apply(null, allv) * 0.9, xmax = Math.max.apply(null, allv) * 1.1;
    const x = v => L + (Math.log(v) - Math.log(xmin)) / (Math.log(xmax) - Math.log(xmin)) * (W - L - R);
    const svg = el('svg', {viewBox: '0 0 ' + W + ' ' + H, width: '100%', role: 'img', 'aria-label': 'Input tokens per run by task and variant, ' + s.name});
    const ticks = [100000, 200000, 500000, 1000000, 2000000, 5000000].filter(v => v >= xmin && v <= xmax);
    for (const v of ticks) { svg.appendChild(el('line', {x1: x(v), x2: x(v), y1: T - 6, y2: H - 30, class: 'grid'})); svg.appendChild(el('text', {x: x(v), y: H - 12, 'text-anchor': 'middle'}, (v / 1000) + 'k')); }
    svg.appendChild(el('text', {x: L, y: 12, fill: 'var(--muted)'}, 'input tokens per run, log scale · bar = median'));
    ids.forEach((id, i) => {
      const t = s.tasks[id], y = T + i * rowH + rowH / 2;
      const meta = D.tasks.find(q => q.id === id);
      svg.appendChild(el('text', {x: L - 10, y: y + 4, 'text-anchor': 'end'}, id.length > 34 ? id.slice(0, 33) + '…' : id));
      if (i % 2 === 0) svg.appendChild(el('rect', {x: L, y: y - rowH / 2, width: W - L - R, height: rowH, fill: 'var(--surface-2)', opacity: 0.5}));
      [['runs_a', 'var(--s1)', -5, 'A'], ['runs_b', 'var(--s2)', 5, 'B']].forEach(([k, col, dy, lab]) => {
        const m = t.total_input_tokens;
        for (const v of m[k]) svg.appendChild(el('circle', {cx: x(v), cy: y + dy, r: 4, fill: col, opacity: 0.55}));
        const med = lab === 'A' ? m.a[1] : m.b[1];
        svg.appendChild(el('line', {x1: x(med), x2: x(med), y1: y + dy - 7, y2: y + dy + 7, stroke: col, 'stroke-width': 3, 'stroke-linecap': 'round'}));
      });
      svg.appendChild(el('text', {x: W - R + 8, y: y + 4}, 'B/A ' + r2(t.total_input_tokens.ratio)));
      const hit = el('rect', {x: L, y: y - rowH / 2, width: W - L, height: rowH, class: 'hit'});
      hit.addEventListener('mousemove', e => showTip(e, '<b>' + esc(id) + '</b> (' + t.kind + ')<br>' + esc(meta ? meta.title : '') + '<br>A median ' + fmt(t.total_input_tokens.a[1]) + ' (' + t.n[0] + ' runs), B median ' + fmt(t.total_input_tokens.b[1]) + ' (' + t.n[1] + ' runs)<br>B/A ' + r2(t.total_input_tokens.ratio) + ' · lines seen B/A ' + r2(t.lines_seen.ratio) + ' · turns B/A ' + r2(t.turns.ratio)));
      hit.addEventListener('mouseleave', hideTip);
      svg.appendChild(hit);
    });
    const wrap = document.createElement('div'); wrap.className = 'scroll'; svg.style.minWidth = '640px'; wrap.appendChild(svg); card.appendChild(wrap);
    const det = document.createElement('details'); det.innerHTML = '<summary>table: medians per task (A | B | B/A) for every measure</summary>';
    det.innerHTML += '<div class="scroll"><table><thead><tr><th>task</th><th>kind</th><th class="num">runs</th>' + measures.map(m => '<th class="num">' + esc(m[1]) + '</th>').join('') + '</tr></thead><tbody>' +
      ids.map(id => { const t = s.tasks[id]; return '<tr><td>' + esc(id) + '</td><td class="kind-' + t.kind + '">' + t.kind + '</td><td class="num">' + t.n[0] + ', ' + t.n[1] + '</td>' + measures.map(m => '<td class="num">' + fmt(t[m[0]].a[1], m[0]) + ' | ' + fmt(t[m[0]].b[1], m[0]) + ' | <b>' + r2(t[m[0]].ratio) + '</b></td>').join('') + '</tr>'; }).join('') + '</tbody></table></div>';
    card.appendChild(det);
    perTask.appendChild(card);
  }

  // behaviour tiles
  const beh = document.getElementById('behaviour');
  const items = [['median_input', 'median input tokens per run', v => Math.round(v / 1000) + 'k'], ['median_turns', 'median turns', v => v], ['median_lines_seen', 'median lines of code seen per run', v => v],
    ['read_lines_per_run', 'lines read through the Read tool per run', v => v], ['bash_calls_per_run', 'shell commands per run', v => v], ['median_cost', 'median cost per run', v => '$' + v.toFixed(3)]];
  for (const [k, l, f] of items) {
    const d = document.createElement('div'); d.className = 'tile';
    d.innerHTML = '<div class="l">' + esc(l) + '</div>' + series.map(s => '<div class="v" style="font-size:1.4rem">' + esc(f(s.behaviour[k])) + ' <span class="s" style="font-size:0.8rem">' + esc(s.name.replace('Claude ', '')) + '</span></div>').join('');
    beh.appendChild(d);
  }
  document.getElementById('behaviour-text').textContent = series.length > 1 ?
    series[0].name + ' locates code by grep and prints a window of 40 to 70 lines around the hit with sed or a bounded Read; it reads a whole file rarely. ' + series[1].name + ' reads ' + Math.round(series[1].behaviour.median_lines_seen / series[0].behaviour.median_lines_seen) + ' times as many lines per run, mostly whole files, takes more turns and costs about a tenth per run. Both pay mostly for the context re-sent on every turn; neither pays for the length of the file the change lands in.' :
    series[0].name + ' locates code by grep and prints a window of 40 to 70 lines around the hit; it reads a whole file rarely.';

  // tasks table
  document.getElementById('tasks').innerHTML = '<table><thead><tr><th>task</th><th>kind</th><th>change</th><th>lands in (variant A)</th><th>reference commit</th></tr></thead><tbody>' +
    D.tasks.map(t => '<tr><td><code>' + esc(t.id) + '</code></td><td class="kind-' + t.kind + '">' + t.kind + '</td><td>' + esc(t.title) + '<details><summary>prompt</summary><p style="white-space:pre-wrap;font-size:0.85rem">' + esc(t.prompt) + '</p></details></td><td style="font-size:0.85rem">' + esc(t.lands) + '</td><td><code>' + esc(t.commit) + '</code></td></tr>').join('') + '</tbody></table>';

  // fowler comparison
  const F = [
    ['codebase', 'Rust app of ~150k lines, written by agents, unreviewed', 'Sokrates, human-written Java, 48k LOC, with a test suite'],
    ['largest file before / after', '17,155 → 3,695 lines', '1,836 → under 500 LOC (14 files split)'],
    ['tasks', 'one fixed change, run once after each of 13 refactoring steps', '12 real changes with acceptance tests, 10 runs each per variant'],
    ['controls', 'none', '2 tasks on byte-identical code'],
    ['token counts', 'characters ÷ 4, reported by the agent', 'the CLI’s own, split by cache state'],
    ['result, input tokens', '−83% after the refactoring', 'B/A ' + r2(main.pooled.total_input_tokens.target[0]) + ' (' + r2(main.pooled.total_input_tokens.target[1]) + '–' + r2(main.pooled.total_input_tokens.target[2]) + ')'],
    ['output tokens / time', '+24% / +33%', 'unchanged'],
    ['explanation', 'the agent reads less code', 'the agent never read the file; it greps and reads a window'],
  ];
  document.getElementById('fowler').innerHTML = '<table><thead><tr><th></th><th>The Economic Benefit of Refactoring (July 2026)</th><th>this experiment</th></tr></thead><tbody>' + F.map(r => '<tr><td>' + esc(r[0]) + '</td><td>' + esc(r[1]) + '</td><td>' + esc(r[2]) + '</td></tr>').join('') + '</tbody></table>';
})();
</script>
</body>
</html>
"""


def main():
    import datetime as dt
    data = {"series": [], "measures": MEASURES, "tasks": load_tasks(), "variants": load_variant_facts()}
    for key, name, csv_name, note in SERIES:
        path = os.path.join(ROOT, "results", csv_name)
        if not os.path.exists(path):
            continue
        rows, groups = load_series(path)
        s = series_stats(key, rows, groups)
        s["name"] = name
        s["note"] = note
        data["series"].append(s)
    generated = dt.date.today().isoformat()
    page = TEMPLATE.replace("${data}", json.dumps(data).replace("</", "<\\/")).replace("${generated}", generated)
    os.makedirs(os.path.join(ROOT, "docs"), exist_ok=True)
    with open(os.path.join(ROOT, "docs", "index.html"), "w") as f:
        f.write(page)
    open(os.path.join(ROOT, "docs", ".nojekyll"), "w").close()
    print(f"docs/index.html written: {len(page):,} bytes, series: {[s['key'] + ' ' + str(s['runs']) for s in data['series']]}")


if __name__ == "__main__":
    main()
