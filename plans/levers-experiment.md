# Levers experiment: what the agent is given, not what the code looks like

Status: designed 2026-10-16, before any run. Pre-registered here; changes after the first series run go in the log at
the end.

## Why

The code-shape experiments (480 runs on Sokrates; 135 + 600 on the ambiguity codebase) found no cheap code change that
lowers what a coding agent costs: splitting files, renaming, generic refactorings and tailored preparation stayed
within noise, and the one real effect (consolidating look-alikes, R) is a trade. The turn analysis says where the money
goes instead. With Sonnet, about 46% of the cost is fixed context re-sent on every call, about 18% is locating, and the
number of calls drives the rest (r ≈ 0.9). So the remaining candidates are things that change **the calls and the
context** without touching the code:

1. **Hints.** A change request that says where to look should cut the locate calls.
2. **CLAUDE.md.** A project memory file is re-sent on every call (fixed context), and it may also cut locate calls
   when it holds a map of the code.
3. **Session length.** Running several changes in one continued session re-sends the earlier work on every later
   call. Fresh sessions do not, but each fresh session pays its setup again.

Each of these is free to apply, so even a modest effect would beat every code change we measured.

## Design

Two codebases, each at variant A as it stands (no refactoring):

- **ambiguity**: BackOffice (`ambiguity/variants/a`, DataStore 10,387 lines), the six tasks of the targeted series:
  a01, a02 and a03 (kind 1, one look-alike method) and a11, a12 and a13 (kind 4, a new field through every look-alike).
  These tasks are worded as behaviour, without file or method names.
- **sokrates**: Sokrates (`variants/a`), six tasks of the first series, chosen by a fixed rule. The rule takes the six
  t-tasks with the most median turns on A with Sonnet in `results/runs.csv`, with ties broken by median cost. That
  gives t02 (27 turns), t03 and t09 (17.5), t04 (12) and t08 and t01 (11; t10 and t08 also had 11, and t08 and t01 cost
  more). These tasks often name classes already.

### Single-task conditions (`harness/run.py`, one fresh session per run)

| condition | prompt | CLAUDE.md |
|---|---|---|
| `base` | preamble + task (as in every earlier series) | none |
| `hint-file` | + "Where to look: the change belongs in `<files>`." | none |
| `hint-symbol` | + "Where to look: in `<file>`, the methods `<m1>`, …; …" | none |
| `md-init` | as base | what Claude Code's `/init` wrote for A (37 lines / 2.8k chars; Sokrates 50 lines / 4.5k) |
| `md-index` | as base | init + a code index (every package, its classes and their public methods; 20k chars; Sokrates 27k, one method per class to fit the 30k budget) |
| `md-padding` | as base | init + inert team-handbook text cut to the index's length (no instructions about code, build, test or tools) |

- The hints are derived mechanically from each task's reference change on A (`levers/tools/make_hints.py`). `file`
  lists the files it touches. `symbol` also names, per Java file, the methods of the *original* code that the changed
  lines are in, so a hint never names a method the change adds. `symbol` is an upper bound on what a hint can do: a
  developer who knows this much has nearly located the change. Hints are in `levers/hints/<task>.json`.
- The CLAUDE.md files are in `levers/claude-md/` (`make_claude_md.py`). The `/init` output is kept as generated
  ($0.16 + $0.15, Sonnet; `*-init.agent.json`). The padding text came from one Sonnet call with a recorded prompt
  (`padding-prompt.md`, $0.13). `md-index` and `md-padding` have the same character count, so their difference is
  content (a map against irrelevant text) and their difference to `md-init` is size plus content. Code tokenizes
  denser than prose, so index and padding are equal in characters, not exactly in tokens. The series records the
  first call's cache write, which measures it.
- The CLAUDE.md is committed in the repository's initial commit, as a team's would be. It is loaded because the harness
  runs with `--setting-sources project` (checked: a CLAUDE.md fact is answered correctly).

12 tasks × 6 conditions × 10 repeats = 720 runs per model.

### Chain conditions (`harness/chain.py`, ambiguity set only)

The six ambiguity tasks in one repository, one after the other, in the fixed order a01, a11, a02, a12, a03, a13. The
order is fixed because a11 and a12 change record constructors that the a01 and a02 acceptance tests use; reversing it
would make those tests fail to compile for reasons other than the agent. Between the steps, the harness commits the
change, so both modes see the same code at each step. Each step's acceptance test runs on a scratch copy of the
repository.

- `fresh`: every step is a new session.
- `session`: every step after the first resumes the previous step's session (`claude -p --resume`), with the same
  prompt text as in fresh mode.

10 chains per mode per model = 120 steps per model. A Sokrates chain is left out, because its tasks overlap in the same
report classes and the order effects would need their own check.

### Models

Sonnet (the default, `claude-sonnet-5-5`) and Haiku (`claude-haiku-5-5`), as in the ambiguity series. Same Claude Code
version (2.1.300) and harness as the ambiguity series. The `base` condition is rerun here rather than reused from
earlier series, so that all conditions are measured at the same time.

## Measures and analysis

- Primary measure: **cost** (USD, as reported). Secondary measures: billed input tokens, output tokens, turns, and
  acceptance success. Mechanism, from the transcripts (`scripts/turn_analysis.py`): calls by class (locate, read,
  edit, build) and the fixed-context share.
- Per condition against `base`, per set and pooled: the geometric mean of per-task median ratios, with a 95% bootstrap
  interval (runs resampled within task and condition), as in the earlier series (`levers/tools/analyze_levers.py`).
- Chains: the cost of the whole chain (the sum of six steps), `session`/`fresh` as a ratio of medians with a bootstrap
  interval over chains. The per-step ratio shows where a difference arises, early or late in the chain.

## Hypotheses (directions stated before the runs)

- **H1 (hints).** `hint-symbol` costs less than `base`, by more on the ambiguity set (behavioural wording, 10k-line
  file) than on the Sokrates set (tasks that already name classes). `hint-file` sits in between. On the ambiguity set
  every task's file is DataStore, so `hint-file` there says little that the agent wouldn't find in its first search.
  Expected: `hint-symbol` ambiguity ≤ 0.80.
- **H2 (CLAUDE.md size).** `md-padding` costs more than `md-init`: the fixed context grows by about 5–7k tokens, paid
  on every call, mostly as cache reads. Expected: +3–8%, depending on the number of calls.
- **H3 (CLAUDE.md content).** `md-index` costs less than `md-padding` (same size, but it can replace searches). Whether
  it costs less than `base` is open.
- **H4 (sessions).** A `session` chain costs more than a `fresh` one: later steps re-send the earlier steps' context,
  and caching makes that cheaper but not free. Expected: ratio > 1.2, growing with step.
- **Success.** No condition lowers the acceptance rate by more than one run in ten. If one does, its saving is not
  counted as a saving.

## Budget

At the ambiguity series' per-run cost (Sonnet $0.08–0.32 per task, Haiku about a tenth of that), about $120 for the
Sonnet single-task series, $25 for the Sonnet chains, and $15–20 for Haiku. That makes about $165 in all. The single
runs and chain steps go to `results/levers-{sonnet,haiku}.csv` and `results/levers-chains-{sonnet,haiku}.csv`.

## Log

- 2026-10-16, designed. A smoke run comes before the series (Haiku, one repeat of each condition on a few tasks, and
  one chain per mode). It checks only the mechanics, and its runs are not analysed.
- 2026-10-17 ~00:03, Sonnet series launched (14 workers); Haiku launched in parallel later at the user's request, with
  its own Maven repositories (`run_series.sh` now names them per model). Both complete: 1,440 single runs, 40 chains,
  $175.10 (Sonnet $134.43 + $18.76, Haiku $16.56 + $5.36). No harness errors; 3 single runs failed acceptance.
- Found while analysing: `num_turns` sometimes reports 1 for runs with many calls (cost and tokens unaffected; the
  mechanism counts calls from transcripts). Haiku's session chains cross 100k tokens of context at step 4–5, above
  which the reported price per token is 5×; that, not the re-sent context, makes Haiku's sessions dearer.
- Report: `docs/posts/levers.md`.
