# Refactoring for coding agents is a trade, not a saving

*The targeted series of the ambiguity experiment (`plans/ambiguity-experiment.md`), 2026-10-16: 600 runs, the kind-1
and kind-4 tasks × five variants × 10 repeats × Claude Sonnet 5.5 and Claude Haiku 5.5. It follows the pilot
(`docs/posts/ambiguity-pilot.md`), which it confirms in part and corrects in part.*

## The answer first

On a 10,387-line data-access file grown by an agent and full of near-identical methods, five versions of the same
code, the same six change requests, ten runs each, two models:

- **Consolidating the look-alikes makes some changes much cheaper and others dearer.** The article-style
  refactoring (one store per entity, near-copies merged, shared row mappers) cuts the tokens of a change that must
  touch every copy by a third: **0.67** (0.55–0.78) with Sonnet, **0.68** (0.61–0.78) with Haiku, cost 0.79 and
  0.74. It raises the tokens of a fix to one specific method among many by 13–21%: **1.21** (1.02–1.35) with Sonnet,
  cost **1.18** (1.11–1.29), and cost 1.16 (1.01–1.30) with Haiku.
- **Over this task mix it comes out ahead in tokens, not in money.** Pooled over the six tasks, the refactored
  code took **0.90** (0.79–0.98) of the tokens with Sonnet and **0.87** (0.77–0.97) with Haiku; in dollars 0.97
  (0.91–1.03) and 0.93 (0.86–0.99). The saved tokens are mostly context re-read from the prompt cache, the
  cheapest kind, while the extra ones of the harder fixes are paid in full.
- **Length does nothing.** Cutting the file by position into pieces that each fit one read: 0.96 (Sonnet), 1.01
  (Haiku). **Names alone do nothing either**: 1.02 and 0.97.
- **What an agent reads is not what it pays.** On the split code Sonnet read 24% less (reading-only tokens 0.76,
  0.69–0.91) for the same bill (0.96). On the renamed code it read 29% more for the same bill.

The decision a refactoring makes for an agent is how many places a change has to touch against how easily the one
right place is found. It does not make the agent cheaper in general; it makes some changes cheaper and others dearer,
and what a team saves depends on which kind of change it makes most.

## Setup in brief

- **Codebase A**: BackOffice, Java 17 / Maven / H2, grown by Sonnet 5.5 over 180 feature requests with the
  instruction to copy the most similar method and adapt it (left to plain conventions the model writes distinct
  methods: see the pilot report). `DataStore.java` 10,387 lines, 190 of 401 methods with a near-identical sibling,
  Sokrates duplication 32%.
- **Variants**, all passing the same behaviour check (1,214 tests, the 1,106-line demo output identical to A's):
  - **S**: `DataStore` cut by position into 10 parts of ≈ 1,000 lines plus a facade, names and copies untouched
    (script);
  - **D**: one file, one class, domain names, near-copies merged into private helpers (blind Opus 5.5 agent);
  - **D-names**: names only, bodies untouched (blind agent);
  - **R**: 30 stores per entity or group (≤ 615 lines each), shared JDBC helper, shared row mappers, near-copy
    families merged (blind agent).
  SD (S applied to D) was dropped: D's shared helpers stay in its facade, which then is still 2.5× the read window.
- **Tasks**: the pilot's two kinds with the largest effects, three tasks each, written on A before the variants:
  - *kind 1, fix one of many look-alikes*: the purchase-order count per sales rep counts cancelled orders where its
    five siblings do not; one region's ticket list sorts differently from its siblings; one stock listing shows
    empty rows;
  - *kind 4, thread a new field through an entity*: payment terms for suppliers, the assigned agent of a support
    ticket, a description for promotions, each shown in every listing of the entity.
  Reference changes were ported to every variant and verified (fail before, pass after). The kind-4 acceptance tests
  find the attribute the change added whatever it was named; until 10:52 they required fixed names, which cost three
  early Haiku runs of the ticket task a pass (`plans/ambiguity-experiment.md` log; their tokens stand).
- **Runs**: Claude Code 2.1.300, ten parallel workers with their own Maven repositories, 10 repeats per cell, all
  cells complete. One Haiku run that ended on an API outage was excluded and re-run; five runs lost to a harness
  run-id collision were run again. 596 of 600 runs passed their acceptance test: three are the early Haiku ticket runs
  judged by the strict test, and one is a real failure (Sonnet, S, supplier payment terms: it stored the new
  attribute but never read the value back).

## Results

Ratio against A, geometric mean of per-task medians, 95% bootstrap interval. "Tokens" are all tokens of the run:
input, including what is re-read from the prompt cache, plus output.

### Sonnet 5.5

| variant | all 6 tasks: tokens | cost | kind 1: tokens | cost | kind 4: tokens | cost |
|---|---|---|---|---|---|---|
| S | 0.96 (0.87–1.06) | 0.99 | 1.10 (0.98–1.21) | 1.04 | 0.85 (0.72–1.00) | 0.94 |
| D | 1.00 (0.89–1.09) | 0.99 | **1.15 (1.04–1.25)** | **1.07 (1.02–1.16)** | 0.87 (0.71–1.04) | 0.91 |
| D-names | 1.02 (0.89–1.13) | 1.02 | 1.09 (0.99–1.21) | **1.11 (1.01–1.21)** | 0.95 (0.76–1.15) | 0.93 |
| R | **0.90 (0.79–0.98)** | 0.97 | **1.21 (1.02–1.35)** | **1.18 (1.11–1.29)** | **0.67 (0.55–0.78)** | **0.79 (0.71–0.87)** |

### Haiku 5.5

| variant | all 6 tasks: tokens | cost | kind 1: tokens | cost | kind 4: tokens | cost |
|---|---|---|---|---|---|---|
| S | 1.01 (0.90–1.10) | 1.01 | 0.98 (0.84–1.14) | 1.00 | 1.04 (0.89–1.17) | 1.01 |
| D | 1.00 (0.91–1.11) | 0.99 | **1.22 (1.06–1.41)** | **1.13 (1.03–1.26)** | **0.82 (0.71–0.96)** | **0.86 (0.79–0.95)** |
| D-names | 0.97 (0.87–1.09) | 0.99 | 0.95 (0.81–1.11) | 0.95 | 0.99 (0.87–1.18) | 1.03 |
| R | **0.87 (0.77–0.97)** | **0.93 (0.86–0.99)** | 1.13 (0.91–1.32) | **1.16 (1.01–1.30)** | **0.68 (0.61–0.78)** | **0.74 (0.69–0.81)** |

Per task (tokens, R/A): fix-one tasks 1.38 / 1.17 / 1.09 (Sonnet) and 1.25 / 1.05 / 1.09 (Haiku); new-field tasks
0.94 / 0.55 / 0.58 (Sonnet) and 0.67 / 0.70 / 0.65 (Haiku). The supplier field (0.94 with Sonnet) is the one where
R still lists the columns query by query in several reports; the ticket and promotion fields go through one shared
row mapper and one column list.

### What the agent does differently

Mean per run, all six tasks, from the classified API calls of the transcripts:

| | Sonnet: calls | search | edit | files opened | Haiku: calls | search | edit | files opened |
|---|---|---|---|---|---|---|---|---|
| A | 11.2 | 3.8 | 2.0 | 3.0 | 16.6 | 4.8 | 3.5 | 5.3 |
| S | 10.7 | 3.3 | 1.9 | 3.5 | 16.3 | 4.6 | 3.5 | 6.2 |
| D | 10.8 | 3.4 | 1.3 | 3.1 | 16.2 | 4.1 | 3.0 | 5.8 |
| D-names | 11.2 | 3.7 | 1.8 | 3.2 | 16.0 | 4.5 | 3.2 | 5.0 |
| R | **9.7** | **2.8** | **0.9** | 3.7 | **14.1** | **3.3** | 2.8 | 5.8 |

On R the agent makes fewer calls, because the change it has to make exists once: one row mapper and one column list
instead of six or eight copies, so fewer searches to find them all and fewer edits to change them. That is the whole
saving, and it is a saving of calls, each of which re-sends the conversation: hence large in tokens and smaller in
money. On the fix-one tasks the same structure works against it: the method with the bug delegates to shared code
that every sibling also uses, and the agent reads more (reading-only tokens 1.30 Sonnet, 1.40 Haiku) to work out
which path the one wrong case takes and how to change it without changing the others.

## Pilot against series

| | pilot (3 repeats, Sonnet) | series (10 repeats, Sonnet) | series (Haiku) |
|---|---|---|---|
| S/A, all | 1.06 | 0.96 | 1.01 |
| D/A, kind 1 | 1.38 | 1.15 | 1.22 |
| D/A, kind 4 | **0.61** | 0.87 | 0.82 |

The pilot's largest effect, D on the new-field tasks, shrank from 0.61 to 0.87 with more repeats: a small-sample
estimate pulled toward the middle, which is why the pilot report called its effects directions, not results. The
kind-1 penalty and the absence of a length effect held. R, not in the pilot, gives the clearest effects of the
series.

## Against the hypotheses

| registered | result |
|---|---|
| H1 ambiguity: D/A ≤ 0.8 | **not supported** (1.00 both models); R reaches 0.67–0.68 on kind 4, D 0.82–0.87, and both pay on kind 1 |
| H2 length: S/A within 0.9–1.1 | **supported** (0.96 Sonnet, 1.01 Haiku) |
| H3 SD ≈ D | not tested (SD dropped, see setup) |
| H4 reading-only drop ≥ 2× billed drop | **supported for S with Sonnet** (reading 0.76, bill 0.96); not for R, where both fell together on kind 4 and both rose on kind 1 |

## What this says about *The Economic Benefit of Refactoring*

The article's −83% came from one change request, measured once, after refactoring a 17,155-line agent-written file,
counted as characters read ÷ 4. In this series the best a refactoring did for one kind of change was −33% in tokens
(−21 to −26% in money), and the same refactoring made another kind of change 13–21% dearer. Three things together
account for the distance: the article's change was of the kind consolidation favours (one change across many
look-alike places); its measure counts reading, which can fall where the bill does not; and a single run cannot
separate a 30% effect from the 20–50% run-to-run spread. Nothing here says the article's refactoring was not worth
doing; it says the number does not carry over to "refactoring halves what agents cost".

## What it means

- **For a team deciding whether to refactor for agents:** refactor to consolidate where changes repeatedly have to
  touch every copy of something (a field added to an entity, a rule that every listing must follow). Expect to pay a
  little more on fixes to single cases inside the consolidated code. Do not split files for agents' sake, and do not
  expect renaming alone to change the bill. And count the refactoring itself: R cost $17.86 of Opus. With Sonnet a new-field change cost $0.29 on A
  and $0.23 on R, $0.06 saved, while a fix to one look-alike cost $0.016 more; R pays back after about 320
  new-field changes, and with Haiku (saving under a cent) after more than 2,000.
- **For Sokrates' AI ease-of-change score:** file length (even beyond the read window) and naming should not carry
  cost weight. Duplication is not uniformly bad for agents: it predicts cost for changes that must touch every copy
  and the opposite for changes to one copy. The measure that would predict the effect is per area of the code: how
  often changes there touch all members of a clone family (from the history's change coupling) against how often
  they touch one. That is computable from what Sokrates already collects, and it is the next thing worth building.

## Limits

- One grown codebase, one domain, Java; an A that became ambiguous only under an explicit copy instruction.
- Three tasks per kind: the per-kind effects rest on three changes each, and the supplier task shows how much a
  single task's details matter (R 0.94 there, 0.55–0.58 on the other two with Sonnet).
- No controls in the targeted series (by choice of tasks); the pilot's controls showed D cheaper on unrelated tasks
  too (0.90), so D's kind-4 effect is partly general, R's is not tested for that.
- The model under test grew the code; the refactorings were made by a stronger model.
- Costs are at the CLI's reported prices; the dollar effects would differ with other cache pricing.

## Method and cost

- Runs: `scripts/pilots/series_ambiguity.sh`, `harness/run.py --set ambiguity`; data
  `results/series-ambiguity-{sonnet,haiku}.csv`, per-run records, tool logs, diffs and transcripts in
  `results/runs/` and `results/transcripts/`, excluded runs in `results/series-ambiguity-*-excluded.tsv`;
  analysis `ambiguity/tools/analyze_pilot.py` → `results/series-ambiguity-{sonnet,haiku}-analysis.txt`.
- Series cost: $54.66 Sonnet, $5.80 Haiku. With the growth ($84.72), the variants ($64.16) and the pilot ($21.33)
  the ambiguity experiment cost about $225 in agent runs; the task-writing and porting agents ran inside the
  coordinating session and are not counted.
