#!/usr/bin/env python3
"""Build docs/trace.html: the turn-by-turn trace page with a few example runs embedded (and their JSON in docs/traces/).

The examples are the median-turn run of each task/variant/model listed in EXAMPLES, rebuilt from the Claude Code
session transcripts with scripts/turn_trace.py. Usage: scripts/build_trace_page.py
"""
import datetime
import json
import os
import sys

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)
sys.path.insert(0, HERE)
import turn_trace  # noqa: E402

# (run id, title)
EXAMPLES = [
    ("t01-undefined-team-active-a-r06-20261009-130934", "Sonnet 5.5 · t01 undefined team active · A (long files)"),
    ("t01-undefined-team-active-b-r05-20261009-125844", "Sonnet 5.5 · t01 undefined team active · B (split files)"),
    ("t07-past-90d-block-a-r08-20261009-134134", "Sonnet 5.5 · t07 past 90 days block · A (long files)"),
    ("t07-past-90d-block-b-r06-20261009-131711", "Sonnet 5.5 · t07 past 90 days block · B (split files)"),
    ("c02-component-duplication-metrics-a-r08-20261009-133358", "Sonnet 5.5 · c02 component duplication metrics (control) · A"),
    ("t01-undefined-team-active-a-r07-20261009-155034", "Haiku 5.5 · t01 undefined team active · A (long files)"),
    ("t01-undefined-team-active-b-r07-20261009-154840", "Haiku 5.5 · t01 undefined team active · B (split files)"),
    ("t05-missing-sublandscape-config-a-r03-20261009-145902", "Haiku 5.5 · t05 missing sub-landscape config · A (long files)"),
    ("t05-missing-sublandscape-config-b-r09-20261009-162042", "Haiku 5.5 · t05 missing sub-landscape config · B (split files)"),
]


def trace_for(run_id, title):
    class Args:
        run, transcript, cwd, root, source_root = run_id, None, None, None, None
    transcript = turn_trace.find_transcript(Args)
    turns, results, cwd = turn_trace.load(transcript)
    root = (cwd or "").rstrip("/") + "/"
    rec = json.load(open(os.path.join(ROOT, "results", "runs", run_id + ".json")))
    meta = {k: rec.get(k) for k in ("task", "kind", "variant", "model", "turns", "duration_s", "cost_usd", "total_input_tokens",
                                    "cache_read_tokens", "cache_write_tokens", "output_tokens", "success", "files_changed",
                                    "lines_added", "lines_deleted")}
    meta["run_id"] = run_id
    files = turn_trace.build(turns, results, root, os.path.join(ROOT, "variants", rec["variant"]))
    return {"title": title, "model": rec["model"], "meta": meta, "turns": turns, "files": files}


def main():
    out_dir = os.path.join(ROOT, "docs", "traces")
    os.makedirs(out_dir, exist_ok=True)
    traces = []
    for run_id, title in EXAMPLES:
        t = trace_for(run_id, title)
        traces.append(t)
        with open(os.path.join(out_dir, run_id + ".json"), "w") as f:
            json.dump(t, f, indent=1)
        print(f"{run_id}: {len(t['turns'])} turns, {len(t['files'])} files, "
              f"{sum(f['read'] for f in t['files'])} lines read, {sum(f['edits'] for f in t['files'])} edits")
    template = open(os.path.join(HERE, "trace_template.html")).read()
    page = template.replace("${examples}", json.dumps(traces).replace("</", "<\\/")) \
        .replace("${generated}", datetime.date.today().isoformat())
    with open(os.path.join(ROOT, "docs", "trace.html"), "w") as f:
        f.write(page)
    print("docs/trace.html")


if __name__ == "__main__":
    main()
