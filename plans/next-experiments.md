# Next experiments: files beyond the read budget, and refactoring before the change

*Draft plan, 2026-10-15. Builds on the A/B series of 2026-10-09 (`docs/posts/analysis.md`).*

The A/B series found no cost of file and unit length below 2,000 lines. In the Sonnet runs input tokens correlate
0.93 with turns, 0.81 with tool response characters, 0.72 with lines seen and 0.36 with Maven runs; 93% of input is
cache reads. Two follow-ups:

1. **Merge series:** is there a step above the read budget, and where? (Part 1)
2. **Preparation series:** which behaviour-preserving refactorings, made before a change, cut its token cost? (Part 2)

## Step 0: archive the transcripts of the A/B series (done 2026-10-15)

The per-call tool logs survived (`results/runs/<id>.tools.jsonl`, 480). The Claude Code transcripts, which add the
per-API-call usage, are deleted 30 days after the session, so they are now archived gzipped as
`results/transcripts/<run id>.jsonl.gz` (485: the 480 runs and 5 smoke runs, 23 MB). `harness/run.py` archives
each new run's transcript, and `scripts/turn_trace.py --run <id>` falls back to the archive.

---

## Part 1: Merge series

### Hypothesis and prediction, registered before the runs

- **H1:** the cost of length is a step near the agent's read budget (2,000 lines per Read, plus a token cap on one
  Read's output), not a slope. Below it the ratio is ≈ 1 (the A/B result); above it, tasks in the big file cost
  more than in A, and the controls stay at ≈ 1.
- **Sokrates' new scoring** ("files beyond read budget", > 2,000 physical lines) predicts a higher cost for the
  merged variants. Write its predicted scores and context lines per change for every variant into this file before
  the first run. The series tests that rule.
- **Decision rule:** an effect counts if the pooled target ratio's 95% interval excludes 1 *and* lies outside the
  range of the control ratios from the same series.

**Registered predictions (2026-10-15, before any m-series run;** Sokrates jar of `../sokrates` with the
2026-10-09 scoring, reference date 2025-09-20, `analysis/<variant>/reports`):

| | a | m3 |
|---|---|---|
| AI ease of change (`MAINTAINABILITY_SCORE_AI`) | 7.2 | **6.8** |
| Human ease of change | 6.5 | 6.4 |
| Files beyond read budget (sub-score) | 10.0 (0 files) | 6.3 (2 files > 2,000 lines, 11% of main code) |
| Context lines per change (`AI_CONTEXT_LINES_PER_CHANGE`) | 571 | 541 |
| main LOC | 48,137 | 48,034 (headers and imports of the moved files) |

The score predicts m3 is harder for an agent, entirely through "files beyond read budget"; its context-per-change
measure predicts slightly *less* reading (fewer touched files per change, each capped at 200 lines). The AI Cost
Estimator only uses LOC and predicts no difference. If the m3 tasks cost the same as on A, the read-budget
sub-score's weight is wrong too; if they cost more, its size says by how much it should count.

### Variants: the inverse of B

Each variant is A with classes of one package merged into one file and nothing else changed. Doses (physical
lines of the merged file):

| variant | landscape report group (t05, t06, t07) | generators group (t10) | how |
|---|---|---|---|
| `a` | `LandscapeReportGenerator.java` 1,836 | `LogicalComponentsReportGenerator.java` 736 | as is |
| `m3` (built) | + `LandscapeReportContributorsTab` → **3,092** | + Contributors, Concerns, Overview, FileAge, FileChurn, Trend, FileSize generators → **2,846** | auxiliary top-level classes |
| `m4` | + `LandscapeReportPeopleTopologyTab`, `LandscapeContributorsReport` → **≈ 4,090** | + the other package-local generators except those importing math3's `Pair` (Duplication, FileTemporalDependencies, ForceGraphExporter) → **≈ 3,900** | auxiliary top-level classes |
| `m8` (conditional) | all of `reports/…/landscape/**` → **≈ 8,160** | as in `m4` | nested static classes, imports rewritten |

**Merge mechanics (m3, m4).** Java allows one public top-level class per file plus any number of package-private
top-level classes. A class that nothing outside its package references can move into another file of that package
as a package-private top-level class. Its fully qualified name stays the same, so imports, the acceptance tests
(all four sit in the merged classes' packages) and the reference patches keep working. The rules:
- Only classes that nothing outside the package references. Measured on A: in `landscape/statichtml` that is
  ContributorsTab, PeopleTopologyTab and ContributorsReport (2,252 lines). `LandscapeIndividualContributorsReports`
  and `LandscapeAnalysisCommands` are used elsewhere and stay where they are. In `generators/statichtml` it is all
  but HistoryPerLanguageGenerator, BasicSourceCodeReportGenerator and ContributorsReportUtils (≈ 4,640 lines).
  Re-check each class with a compiler-backed search before moving it, not with grep alone.
- `public` comes off the moved classes. Nothing else changes: bodies byte-identical, the host class first, then the
  moved classes in a fixed order. Imports become the union of the files' imports; a name clash (two imports with
  the same simple name) stops the merge of that class.
- After each merge: all modules' tests green; `scripts/compare_bytecode.sh a <variant>` identical (javap of every
  main class, ignoring source file names and top-level `public`); `scripts/golden.sh` output identical to A's
  *generated the same day*. The stored `golden/` drifts with the calendar: 11 files (e.g.
  `FILE_CHANGE_HISTORY_TOTAL_AGE_DAYS`) are computed from today, not from `-date`, so A itself no longer matches it.
- Built by `scripts/make_variant_m.py <variant>` (also writes `analysis/<variant>`: A's config, and A's git history
  with each moved file's commits re-pointed to its host file). Reference patches: `scripts/make_reference_m.py
  <variant>` applies `a.patch` to A, merges the result the same way and diffs it against the variant.
- Known side effect: `find -name LandscapeReportContributorsTab.java` now finds nothing; only a grep for
  `class LandscapeReportContributorsTab` does. Big files really do cost this, so keep it, but log it (below).

**m8 only if needed.** If m3/m4 show nothing, the step lies higher (Edwards-Alexander's file was 17k lines), and m8
goes to about 8k lines. That needs nested static classes, which changes fully qualified names (`Outer.Inner`) and
rewrites imports in the referencing files and tests, a confound to state. A real codebase with a big file that grew
naturally is the alternative for that size; decide after m3/m4.

**Untouched files are controls.** In m3/m4 the files of t01–t04, t08, t09, c01 and c02 are byte-identical to A. All
eight are within-series controls, not two as before.

### Tasks

- Keep the 12 tasks. Regenerate the reference patches for m3/m4 (same hunks, new paths) and verify all tasks with
  `scripts/check_tasks.py` on every variant.
- **Add 4–6 target tasks that land in the merged files.** Four tasks give a wide pooled interval.
  `tasks/candidates.md` has 54 candidate commits touching `LandscapeReportGenerator`, the contributors/topology
  tabs or `generators/statichtml`. Choose and write them before merging, as for the first series.
- Prefer at least two tasks that need the agent to understand much of a class (e.g. a section whose
  order or data flow depends on other sections). Grep-and-window tasks are exactly where length showed no cost.

### Measurement additions

The hook already records Read offset/limit and line counts. Add:
- per Read: was the output truncated or refused (the response says so), and was the file read in pages;
- per grep: number of hit lines that fall inside the merged file;
- `find`/Glob calls for a file name that no longer exists (the cost of auxiliary classes);
- turns until the first edit (the locating cost);
- the transcript of each run, copied into `results/` (Step 0).

**Read-tool limits:** check them in the smoke run rather than assuming. A 3,000-line Java file is about 30–40k
tokens, probably over the single-Read token cap. Whether the agent gets an error, a truncation or pages is the
mechanism H1 depends on.

### Runs

- **Re-run A in the same series**, interleaved with m3/m4. The Claude Code version and the model may have changed
  since 2026-10-09 (2.1.295); old A rows are not a valid baseline.
- Sonnet 5.5 and Haiku 5.5, 10 repeats per cell, 20 for the target tasks in the merged files.
  ≈ 16–18 tasks × 3 variants: ≈ 600 runs per model. ≈ $100 Sonnet, ≈ $12 Haiku at the earlier per-run costs.
- First a smoke run: one target task on m4 with `--keep`, read the tool log and the trace page.
- Analysis: `harness/compare.py` per variant pair (m3/a, m4/a), plus the dose curve (ratio against physical lines
  of the touched file, merged-file tasks only, with the B/A points of the first series below 2k added).

---

## Part 2: Preparation series

### Question

Which behaviour-preserving changes, made before a task, cut the agent's cost of the task? Cost = turns ×
re-sent context + tool output. A refactoring can only help by (a) cutting turns (fewer places to find, change and
fix, a first grep that lands) or (b) shrinking what each turn adds (smaller test and build output).

### Step 2.0: where the turns go (from the archived transcripts, no new runs)

Classify each turn of the 480 A/B runs: *locate* (grep/find/ls), *read*, *edit*, *build/test*,
*fix after failed build/test*, *verify/other*. Per task: the share of turns and of tool-output characters per
class, and the "wasted" locate turns (greps whose hits the agent never opens). This shows which lever is worth
pulling for which task, before anything is refactored. A first guess from the data: t02 (654k, the most expensive)
is shotgun surgery (the `"  *.ext"` label is built and stripped in several places); t10's extra cost in B came
from finding a second place.

### Arms

| arm | what | chosen from | role |
|---|---|---|---|
| `a` | baseline | n/a | re-run in the same series |
| `g` (generic) | one variant with a fixed set of refactorings | only Sokrates signals and the code, **without the tasks** | the realistic, actionable answer |
| `p-<task>` (tailored) | one variant per task, "make the change easy" | the task's reference patch | upper bound of what preparation can save |
| `ctx` (optional) | A plus a short agent-facing code map (CLAUDE.md) | the code | does documenting beat refactoring? breaks the "no agent docs" rule on purpose |

**Generic arm `g`: candidate refactorings and the signal behind each**

| refactoring | Sokrates signal | expected effect |
|---|---|---|
| Consolidate duplicated logic and repeated literals/keys into one place | duplication report; repeated string literals across files (a new concern regex) | fewer places to edit, fewer missed spots, fewer fix turns |
| Co-locate or unify code that changes together | temporal (change) coupling between files from the history | fewer files to find per change |
| Rename to the domain's words (classes, methods, report section ids) | a new findability measure: identifiers from past commit messages vs. names in code | the first grep lands; fewer locate turns |
| Focused, fast tests per component | test-to-main ratio per component; test runtime per module | the agent runs one test class instead of a module build |
| Quieter build and test output (surefire `trimStackTrace`, test log level, `redirectTestOutputToFile`) | n/a (repository-level) | smaller tool output per test turn |

Leakage control for `g`: the refactoring list is drawn up by an agent given only the Sokrates reports and the
code. It never sees `tasks/` (run it in a copy without that folder). The list is committed before any `g` run
(record the commit hash here). Every refactoring passes the module tests and `scripts/golden.sh g`.

**Tailored arm `p-<task>`:** a separate agent gets the task's reference patch and makes a preparatory refactoring
that makes the patch small and local, *without making the change itself*. The acceptance test must still fail
afterwards, tests stay green and golden output identical. Record the tokens the preparation costs, since a saving
only counts net of it (break-even, as in the Fowler comparison).

**Held-out tasks.** To check that `g` was not tuned to the known tasks, mine and write 4–6 new tasks from
`tasks/candidates.md` *after* the `g` list is committed. Their B/A-style ratio is the honest number.

### Runs

- 12 tasks + held-out tasks × {a, g}, 10 repeats: ≈ 340 runs; tailored: 10 tasks × p-<task>, 10 repeats: 100 runs
  (against the same a rows). Sonnet first, Haiku if Sonnet shows an effect. ≈ $75 Sonnet.
- Report per arm: ratio of input tokens, turns, tool output, by turn class (from 2.0), plus success rate. A cheaper
  failed run is no win.

### What would change in Sokrates

If `g` saves tokens, the signals that chose the winning refactorings belong in the AI ease-of-change score,
next to "files beyond read budget": duplication across files and change coupling (places per change),
findability, and test feedback cost. The size of the effect sets their weights.

---

## Order and checkpoints

1. Step 0: archive transcripts, make `run.py` keep them. *(before 2026-11-08)*
2. Part 1: write the new merge-group tasks → build m3, m4 (tests + golden) → Sokrates predictions registered →
   smoke run → series → decide on m8.
3. Step 2.0 turn analysis (can run in parallel with step 2; needs only the archive).
4. Part 2: `g` list drawn up blind and committed → held-out tasks written → `g` and `p-*` built → series.
