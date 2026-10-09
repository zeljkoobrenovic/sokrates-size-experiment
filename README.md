# Does code size change what an AI agent spends? An experiment

**Question.** Sokrates' "AI ease of change" score and its AI Cost Estimator assume that long files and long units make
every change more expensive for an AI coding agent, because the agent has to read more to make the same change.
This experiment measures it: the same tasks, run by the same agent, against two versions of one codebase that differ
only in the length of their files and units.

**Codebase.** Sokrates itself, as it was on 2025-09-20 (commit `5e6aa97a` of
[zeljkoobrenovic/sokrates](https://github.com/zeljkoobrenovic/sokrates), the last one before 2025-10-09, a year before
the experiment started). Chosen because the author knows it, it has a test suite, its history since then provides
real tasks with known solutions, and it has the profile wanted: 48,137 main lines of Java with 14 files over 500 LOC
(4 over 1,000) and 60 units over 50 LOC (11 over 100 LOC or McCabe 50) — see `analysis/targets-a.md`.

**Variants.**

| | variant A | variant B |
|---|---|---|
| source | the snapshot as is (`variants/a`) | A with every main file ≤ 500 LOC and every unit ≤ 50 LOC / McCabe ≤ 25, nothing else changed (`variants/b`, to be made: `scripts/make_variant_b.md`) |
| Human ease of change (Sokrates) | 6.4 (C) | |
| AI ease of change (Sokrates) | 6.6 (B) | |
| context lines per change (past year, 52 changes) | 1,690 | |

(Predictions from `analysis/<variant>/reports`, reference date 2025-09-20 so the history windows are the ones the
snapshot had; regenerate with `scripts/analyze_variant.sh <variant>`.)

**Tasks.** Real changes made to this code after the snapshot, re-derived against the snapshot and written as a
behaviour description plus a JUnit acceptance test that fails before and passes after (`tasks/`, candidates mined
from the history in `tasks/candidates.md`). Target tasks land in files/units above the thresholds; control tasks land
elsewhere and should show no difference.

**Measurement.** `harness/run.py` runs each task × variant × repeat in a fresh one-commit copy of the variant with
Claude Code headless (`claude -p … --output-format json`), the same model, prompt and tool permissions, and records
per run: input tokens (split by cache read/write), output tokens, cost, turns, duration, every tool call through a
PostToolUse hook (file reads with line counts, greps, shell commands, edits, tool output size), the diff size, and
whether the acceptance test passed. `harness/summarize.py` gives medians, quartiles and the B/A ratio per task.

## Layout

```
variants/a/           the snapshot (never edited; no .git, no CLAUDE.md — the harness makes a neutral repo per run)
variants/b/           the size-limited twin (to be made)
analysis/<v>/         Sokrates config + reports of each variant (reports/ is generated, not committed), git history of the snapshot
analysis/targets-a.md the files and units above the thresholds, with commit counts
tasks/<id>/           task.md (prompt), meta.json, acceptance/<variant>/ (test files); tasks/candidates.md
harness/              run.py, summarize.py, config.json (model, flags, timeouts), prompt-preamble.md, hooks/log_tool.py
scripts/              analyze_variant.sh, targets.py, mine_tasks.py, golden.sh, make_variant_b.md
results/runs.csv      one row per run (results/runs/<id>.json has the full record incl. the agent's final message)
work/                 per-run working copies and logs (kept with --keep)
```

## How to run

```bash
# prerequisites: Java 17, Maven, claude CLI logged in, the Sokrates CLI jar built in ../sokrates (for the analyses)
harness/run.py --tasks t01-undefined-team-active --variants a b --repeats 10        # the experiment
harness/run.py --tasks t01-undefined-team-active --variants a --repeats 1 --keep   # one run, keep work/ to inspect
harness/summarize.py                                                                # the tables
harness/summarize.py --successful-only                                              # only runs that passed
```

Runs are serial on purpose (the prebuild installs the variant's modules into `~/.m2`; two variants at once would
mix). Budget: one run is a prebuild (1–2 min) + the agent (minutes, typically a few hundred thousand input tokens with
cache reads) + the acceptance test.

## Design rules (what keeps the comparison fair)

- **Same everything but length.** Same model, prompt, tools, permissions, turn limit; a fresh neutral repository per
  run (one commit, fixed author and date, no agent-facing docs, folder names `a`/`b` only outside the repo). B is a
  mechanical split, not a redesign (`scripts/make_variant_b.md`), with the golden-output check (`scripts/golden.sh`)
  proving the reports it generates are identical.
- **Repeats.** Agent runs vary a lot. Ten repeats per cell, medians and quartiles, variant order alternated per repeat.
- **Success first.** A cheaper failed run is not a win; the summary reports success rates and `--successful-only`.
- **Tasks fixed before the split.** Written from the real commits, located by `analysis/targets-a.md`; control tasks
  as a sanity check; prompts describe behaviour, not the solution.
- **Known biases, stated.** Sokrates is public, so the original layout and the real fixes are plausibly in the
  model's training data while B is not (favours A). Java/Maven build output costs tokens; `mvn_runs` and
  `tool_response_chars` are recorded so it can be separated. `--setting-sources project` keeps the user's own
  settings out of the runs; check the first run's `work/<id>/tools.jsonl` for anything unexpected (e.g. skills).

## Status

- [x] variant A extracted, analyzed (`analysis/a`), targets listed
- [x] task candidates mined (106 commits touching the target files; 3 apply as is, the rest need re-deriving)
- [x] task t01 (Undefined Team keeps only active contributors; `LandscapeAnalysisResults`, 1,133 LOC) with an
      acceptance test verified to fail before / pass after the reference change
- [x] harness with tool-call logging, acceptance run and CSV results; one smoke run on A
- [ ] 5–9 more target tasks + 2 control tasks
- [ ] variant B (mechanical split to the thresholds, golden output identical), its analysis and predictions
- [ ] the runs, the summary, the write-up
