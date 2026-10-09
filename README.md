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
| source | the snapshot as is (`variants/a`) | A with every main file ≤ 500 LOC and every unit ≤ 50 LOC / McCabe ≤ 25, nothing else changed (`variants/b`; how: `scripts/make_variant_b.md`) |
| main code | 489 files, 48,137 LOC | 541 files, 49,401 LOC (+2.6%: constructors, delegations, imports of the helpers) |
| files > 500 LOC / units > 50 LOC or McCabe > 25 | 14 / 60 | 0 / 0 |
| Human ease of change (Sokrates) | 6.4 (C) | 7.2 (B) |
| AI ease of change (Sokrates) | 6.6 (B) | 7.7 (B) |
| context lines per change (past year, 52 changes) | 1,690 | 774 |
| AI ease of change, re-scored with the 2026-10-09 scoring (changed in response to the results) | 7.2 (B) | 7.3 (B) |
| context lines per change, re-scored (200-line window per touched file) | 571 | 552 |

(Predictions from `analysis/<variant>/reports`, reference date 2025-09-20 so the history windows are the ones the
snapshot had; regenerate with `scripts/analyze_variant.sh <variant>`. B is measured against A's git history: split
files keep their original path, so a change to one of them counts against the now smaller file, while the helper
classes have no history. Both configs ignore `target/`, since both variants are built in place.)

**How B was made (2026-10-09).** Five parallel batches (one per group of files, in git worktrees, branches `split/1`–`5`,
merged into main) applied only *extract method* and *extract class* to the 14 files and 60 units of
`analysis/targets-a.md`: 48 new package-private helper classes in the same packages, every original class keeping
its name, constructors and public/package-private API and delegating; the order of every report call and file write
kept. After each file: module tests green, `scripts/golden.sh b` identical (850 normalized files of two repository
reports and a landscape), re-measured with Sokrates. The split history is in the merge commits. Deviations to know:
in `LandscapeAnalysisResults` the 141 trivial accessors (getters, setters, one-line delegates) are written on one line
each, because in the original three-line style they alone exceed the threshold and moving public members was not
allowed; a few private members became package-private for the helpers; helper classes have their own loggers.

**Tasks.** Real changes made to this code after the snapshot, re-derived against the snapshot and written as a
behaviour description plus a JUnit acceptance test that fails before and passes after (`tasks/`, candidates mined
from the history in `tasks/candidates.md`). Target tasks land in files/units above the thresholds; control tasks land
elsewhere and should show no difference.

**The task set** (every acceptance test re-verified by `scripts/check_tasks.py`: fails on each untouched variant,
passes with `tasks/<id>/reference/<variant>.patch`):

| task | kind | change | reference commit | module | lands in (variant A) |
|---|---|---|---|---|---|
| `c01-template-literal-duplication` | control | Ignore the text of backtick template literals in unit duplication | `b8aeec06` | codeanalyzer | n/a |
| `c02-component-duplication-metrics` | control | Fix per-component duplication metrics and most-frequent-duplicates ordering | `3e0a9b5c` | codeanalyzer | n/a |
| `t01-undefined-team-active` | target | Limit the Undefined Team to active contributors | `ad61da0f` | codeanalyzer | getAllTeams() in LandscapeAnalysisResults (file of 1,133 LOC, very high risk) |
| `t02-extension-metric-names` | target | Name per-extension metrics by the bare extension instead of "  *.ext" | `ac3d6b75` | codeanalyzer | getLinesOfCodePerExtension() and merge() in LandscapeAnalysisResults (file of 1,133 LOC, v |
| `t03-scoping-conventions-never-matched` | target | Fix four scoping conventions that never matched | `13023770` | codeanalyzer | addGeneratedConventions() (62 LOC, high) and addBuildAndDeploymentConventions() in Scoping |
| `t04-mock-folder-is-test-code` | target | Scope a folder named plainly "mock" as test code | `0ec83b3c` | codeanalyzer | addTestConventions() (63 LOC, high) in ScopingConventions (file of 514 LOC, high risk) |
| `t05-missing-sublandscape-config` | target | Don't crash the landscape report when a sub-landscape config is missing | `54369e67` | reports | addSubLandscapeSection() in LandscapeReportGenerator (unit of 128 LOC, McCabe 17; file of  |
| `t06-all-contributors-exported` | target | Export all contributors to the report (drop the contributorsListLimit cap) | `e4ac6240` | reports | addRecentContributorsSection() in LandscapeReportContributorsTab (unit of 73 LOC; file of  |
| `t07-past-90d-block` | target | Fix the 'past 90d' repositories block showing the 180-day LOC | `90fa40b1` | reports | addBigRepositoriesSummary() in LandscapeReportGenerator (file of 1,587 LOC, very high risk |
| `t08-scope-file-lists-named-after-aspects` | target | Name the scope file lists in all_files.zip after the aspects | `a4a30ed0` | reports | exportJson() in DataExporter (unit of 61 LOC, file of 813 LOC, high risk) |
| `t09-analyzelandscape-command` | target | Add analyzeLandscape as the documented name of updateLandscape | `5d61daab` | cli | run(String[]) and updateLandscape(String[]) in CommandLineInterface (file of 744 LOC, high |
| `t10-group-dependency-npe` | target | Fix NPE in group-dependency merge and divide-by-zero in coverage bar | `3e948503` | reports | getGroupDependencies() and getFromDependencyCoverageSvg() in LogicalComponentsReportGenera |

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
scripts/              analyze_variant.sh, targets.py, mine_tasks.py, golden.sh, make_variant_b.md, build_site.py,
                      turn_trace.py + trace_template.html + build_trace_page.py (the turn-by-turn trace page)
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

### Turn-by-turn traces

The tracer also lives in its own repository, usable with any Claude Code session:
[claude-session-tracer](https://github.com/zeljkoobrenovic/claude-session-tracer)
(page: https://zeljkoobrenovic.github.io/claude-session-tracer/). The copy here keeps the `--run` option for the
experiment's runs.

[docs/trace.html](https://zeljkoobrenovic.github.io/sokrates-size-experiment/trace.html) shows one run as a grid:
columns are the API calls (turns), the top rows the tokens each call re-sent from the prompt cache, wrote to it and
produced, the rows below the files the agent read, searched and edited in that turn (with line counts). It needs no
extra logging: Claude Code writes a transcript of every session, interactive or `claude -p`, under
`~/.claude/projects/<cwd slug>/<session id>.jsonl`, and every assistant message there carries the call's usage
(`cache_read_input_tokens`, `cache_creation_input_tokens`, `output_tokens`) next to the tool calls and their results.

```bash
scripts/turn_trace.py --cwd /path/you/ran/claude/in --list       # the sessions started there: id, start, API calls, prompt
scripts/turn_trace.py --cwd /path/you/ran/claude/in --out t.json # the latest of them (--session <id prefix> for another)
scripts/turn_trace.py --run <run id> --out trace.json            # an experiment run
scripts/turn_trace.py --transcript ~/.claude/projects/<slug>/<id>.jsonl --root /path/to/repo --out t.json
scripts/build_trace_page.py                                      # rebuild docs/trace.html with the example runs
```

Any Claude Code session can be viewed this way: list the sessions of the folder you worked in, extract one, open
[trace.html](https://zeljkoobrenovic.github.io/sokrates-size-experiment/trace.html) (or the local `docs/trace.html`) and drop
the JSON on it. Paths are shortened relative to the session's working directory; `--source-root` gives the files' current
location for their total line counts.

Drop the JSON onto the page (or pick it with the file input) to render it. The format is documented at the top of
`scripts/turn_trace.py` (`TRACE_FORMAT`); the example traces are in `docs/traces/`. Claude Code deletes old
transcripts after its `cleanupPeriodDays` (30 by default), so extract a trace soon after the session, or copy the
transcript. File attribution is heuristic: `Read`/`cat`/`sed -n`/`head` count as read lines, `Edit`/`Write` as
edited lines, and a `grep` output line naming a file as a search hit; a piped `head` is a filter, not a read.

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
- [x] 10 target tasks + 2 control tasks, each checked on both variants (`scripts/check_tasks.py`)
- [x] variant B (mechanical split to the thresholds, golden output identical, all tests green), its analysis and predictions
- [x] t01's acceptance test compiles against B unchanged (the target code now sits in `LandscapeContributorsAggregator`)
- [x] the runs: Sonnet 5.5 and Haiku 5.5 series, 240 runs each, no failed acceptance test (`results/runs.csv`, `results/runs-haiku.csv`); `harness/compare.py` for the tables
- [x] the write-up (`docs/posts/analysis.md`) and the results site (`docs/index.html`, `scripts/build_site.py`)
- [x] Sokrates' AI ease-of-change scoring changed in response (2026-10-09): unit size and 500-LOC file size out, files beyond the 2,000-line read budget in, context per change capped at a 200-line window
