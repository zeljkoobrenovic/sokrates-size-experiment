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

**Smoke run findings (2026-10-15, before the series).**

- *The read window is 25,000 tokens, not 2,000 lines.* A Read of m3's `LogicalComponentsReportGenerator.java`
  (2,847 lines, 63,289 tokens, about 22 tokens per line) returned lines 1–955 with "PARTIAL view … cap 25000".
  For this Java the window is therefore about 1,000 lines, so variant A already had four files beyond it
  (`LandscapeReportGenerator` 1,836, `LandscapeAnalysisResults` 1,405, `LandscapeReportContributorsTab` 1,284,
  `ReportFileExporter` 1,204). The A/B series compared files above the real window with files below it, and found
  no difference. Sokrates' "files beyond read budget" (> 2,000 lines) should count tokens (≈ 25k) rather than lines.
- *Agents avoid the cap rather than run into it.* In the 480 A/B runs, no Read without a limit was ever made on a
  file over 1,000 lines, and no transcript contains a truncation notice. Haiku read whole files 88 times on B and
  34 times on A: it reads small files whole and takes windows of big ones.
- *The smoke run (t06 on m3, Sonnet, Claude Code 2.1.300)* did the same: three greps, a 40-line window of
  `LandscapeContributorsReport`, an 8-line window at offset 2,308 of the 3,093-line host file, four edits, one
  compile. It passed in 13 turns, 209k input tokens and $0.11, against a t06/A median of 11.5 turns and 194k. That
  is inside the A range; one run says nothing about the ratio.
- *Consequence for H1.* A step at the read window is unlikely: the window has already been crossed with no effect,
  and the agent's habit does not touch it. A cost of size, if there is one, will come from elsewhere: more grep
  hits to sift in a big file, windows that miss the context they need, or tasks that need a whole class
  understood. m3/m4 still test this, and the per-call logging (grep hits inside the merged file, windows per file,
  turns to first edit) matters more than the read-truncation counter.
- The Claude Code version is now 2.1.300 (the A/B series ran on 2.1.295): re-running A in the same series is
  required, as planned.

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

### Step 2.0: where the turns go (done 2026-10-15, `scripts/turn_analysis.py`, `results/turn-analysis.json`)

All 480 A/B runs, every API call classified (locate, read, edit, build, fix, other) from the archived transcripts.
Input tokens decompose exactly (model/measured 1.000): what a call adds is re-sent by every later call.

| share of cost (price-weighted, mean run) | Sonnet | Haiku |
|---|---|---|
| shared prefix (system prompt, tools) + session setup (task prompt, environment), paid on every call | 46% | 34% |
| locate (search calls) | 18% | 20% |
| read | 14% | 24% |
| edit | 11% | 12% |
| build + fix after a failed build/test | 5% | 7% |
| other | 5% | 3% |

In dollars a Sonnet run is 38% cache reads, 37% cache writes, 25% output.

**Within a task, the number of calls drives the cost:** r = 0.90 (Sonnet), 0.85 (Haiku); one extra call adds 6.6%
(Sonnet) and 5.2% (Haiku). The calls that vary are locate and read (r with cost 0.62 / 0.57 for Sonnet) and the
number of files the agent opens (0.52); for Haiku also edits and fixes (0.49 / 0.42). Search noise (files a grep
hit and the agent never opened) is highest on the most expensive tasks: c01 32 and t02 23.5 per Sonnet run,
against a median of 4.

**Consequences for the arms:**
- The lever is **calls spent finding and reading**: findability (the first search lands on the place) and
  locality (one behaviour in one place, fewer files to open per change).
- **Build and test output is not worth refactoring for** (≤ 3% of cost): quieter Maven output is dropped from `g`.
  Focused tests can only touch the 5–7% of build and fix calls; kept as a low-priority item.
- **Fixed context costs on every call.** A `ctx` code map adds to the setup on every call, so it pays only if it
  saves more than about one call per 2k tokens it adds.
- Refactorings must not change what the acceptance tests use (public and package-private classes and
  signatures), which rules out plain renames of those. Renames of private members, and domain words in comments
  and Javadoc that a grep finds, stay possible.

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
| Consolidate duplicated logic and repeated literals/keys into one place | duplication report; repeated string literals across files (a new concern regex) | fewer places to find, read and edit |
| Co-locate or unify code that changes together | temporal (change) coupling between files from the history | fewer files to open per change |
| Domain words where a grep finds them (private names, comments, Javadoc of the entry points of a feature) | words of past commit messages vs. names in the code | the first search lands; fewer locate calls, less search noise |
| Focused, fast tests per component (low priority) | test-to-main ratio per component | fewer build and fix calls |

The brief for the agent that draws up the list is `scripts/g_brief.md`; it gives the 2.0 findings (what costs
calls) and the constraints, nothing about the tasks.

Leakage control for `g`: the refactoring list is drawn up by an agent given only the Sokrates reports and the
code. It never sees `tasks/` (run it in a copy without that folder). The list is committed before any `g` run
(record the commit hash here). Every refactoring passes the module tests and `scripts/golden.sh g`.

**Tailored arm `p-<task>`:** a separate agent gets the task's reference patch and makes a preparatory refactoring
that makes the patch small and local, *without making the change itself*. The acceptance test must still fail
afterwards, tests stay green and golden output identical. Record the tokens the preparation costs, since a saving
only counts net of it (break-even, as in the Fowler comparison).

**Held-out tasks.** To check that `g` was not tuned to the known tasks, 6 new tasks from `tasks/candidates.md`,
written *after* the `g` list is committed. Their g/A ratio is the honest number. So that the choice cannot follow
the `g` list either, the selection was fixed on 2026-10-15 while the blind agent was still working, before its
proposal was read: the candidates with a test, in `candidates.md` order (tests first, smallest first), skipping the
12 used commits; the first 6 that re-derive into a self-contained behaviour with an acceptance test failing on A
and passing with the reference change are taken, and each skipped one is recorded with its reason. The order:
`6df44dc0`, `7cfd01cb`, `00232dac`, `f8e04e14`, `a27a5686`, `faa3e12d`, `6c9ac2b4`, `9f03561c`, `494ad74a`,
`f81c0ab0`, `89be4f3a`, `47eb87d9`, `0ef770d8`, `8937f539`, `f9fd704e`, `4f5d869b`.

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
