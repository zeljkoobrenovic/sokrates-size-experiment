# Length or ambiguity? What a 10,000-line agent-written file costs a coding agent

*A pilot of the 2×2 experiment of `plans/ambiguity-experiment.md`, 2026-10-16. 135 runs of Claude Sonnet 5.5, three
repeats per task and variant: enough to see large effects and the direction of smaller ones, not to settle them.*

## The answer first

On a 10,387-line data-access file that an agent grew itself, full of near-identical methods:

- **Length did not cost anything.** Cutting the file by position into ten pieces that each fit in one read left
  the bill unchanged: billed input tokens 1.06 times A's (95% interval 0.92–1.18), cost 1.01.
- **Removing the look-alikes did not cost less overall.** Disambiguating in place (domain names, near-copies
  merged into shared helpers): 0.96 (0.86–1.09), cost 0.97. The registered hypothesis, at most 0.8, does not hold.
- **Underneath the average the kinds of change pull in opposite directions.** Where a change now lands in one
  shared place (a new field, read by one shared row mapper instead of six copies), the disambiguated code costs
  0.61 (0.50–0.84). Where the agent must fix one specific method among many, it costs 1.38 (1.10–1.47): the
  shared helpers are used everywhere, so a search hits twice as many places the agent never needs.
- **Reading less is not paying less.** On the split variant the agent read 12% fewer characters (0.88,
  0.71–0.98) while its billed tokens went up 6%. The measure *The Economic Benefit of Refactoring* used
  (characters read ÷ 4) and the bill moved in opposite directions on the same runs.

So for this agent the question is not "how long is the file" or "how ambiguous is the code", but **how many places
a change has to touch, against how hard the one right place is to find**. A refactoring trades the two.

## Why this experiment

Four earlier results on Sokrates' own code all said that reshaping code does not change what a coding agent
spends: splitting every file under 500 lines (B/A 1.03, 480 runs), ten generic refactorings chosen blind from
Sokrates' signals (1.00 Sonnet, 1.03 Haiku), and a preparation made for each change by an agent that knew the
change (0.94 Sonnet, 1.08 Haiku). Against that stands the article's −83% after refactoring a 17,155-line
agent-written data-access file. The working explanation was that Sokrates never had the article's condition: a
huge file in which a search lands on dozens of look-alikes. This experiment creates that condition and takes it
apart into length and ambiguity.

## The codebase: grown by an agent

BackOffice, an order-and-inventory back office in Java 17 / Maven with an embedded H2 database, grown from a
minimal skeleton by Sonnet 5.5, one `claude -p` session per feature request, each ending with tests green and a
commit; the standing instruction kept all SQL in `DataStore.java` and forbade refactoring.

**The first growth did not produce the article's file.** With the plain instruction ("follow the existing patterns,
do not refactor"), 72 features gave a `DataStore.java` of 3,881 lines and 240 methods of which only **7** had a
near-identical sibling. The agent wrote long, verbose, but distinct methods.

**The second growth had to be told to copy.** With "add each new query next to the most similar method: copy it and
adapt it; no shared helpers", it took 180 features to pass the entry criteria: `DataStore.java` 10,387 lines, 401
methods, **190** with a near-twin, Sokrates duplication 32% of the file, 34 clusters of ≥ 3 near-identical methods,
a median of 11 methods hit by a search for a typical request's words. All entry criteria passed. That is the A of
this experiment. (Growing it cost $62; the first growth $23.)

That the giant ambiguous file needed an instruction to appear is itself a result: this model, left to plain
conventions, does not write it.

## Variants

| variant | what it is | made by | longest data-access file | hits with a near-twin |
|---|---|---|---|---|
| **A** | as grown | — | `DataStore` 10,387 lines (≈ 8× the 25k-token Read window) | 0.26 |
| **S** | `DataStore` cut by position into 10 parts of ~1,000 lines, names and clones untouched, a facade of delegates | script | ≈ 1,200 lines, each under one read | 0.19 |
| **D** | one file, one class: domain names, near-copies merged into private helpers, JDBC plumbing in helpers, ordered by entity | blind Opus 5.5 agent | `DataStore` 5,991 lines (≈ 4.5× the window) | 0.00 |
| D-names | names only (302 methods renamed), bodies untouched | blind Opus 5.5 agent | 10,387 lines | (0.27: the body-based measure cannot see names) |
| R | split by entity into 30 stores, shared JDBC helper, near-copies merged | blind Opus 5.5 agent | stores ≤ 615 lines | 0.00 |

The pilot ran A, S and D (the plan's pilot set); D-names and R are built and verified for the series. Every
variant passes the same behaviour check: all 1,214 tests green and the 1,106-line demo output identical to A's.
The blind agents saw the code and a brief, never the tasks; their transcripts name no path in the experiment
repository. "Hits with a near-twin" is the median, over 15 typical request terms, of the share of methods a search
hits that have a near-identical sibling (`scripts/ambiguity_measure.py`): the manipulation check, done before any
run. S kept the look-alikes and removed the length; D removed the look-alikes and kept a long file.

## Tasks

15 tasks written on A by three agents that saw neither the variants' briefs nor the plan: 12 in the data-access layer,
three of each kind, and 3 controls outside it.

1. *Fix one of many look-alikes*: a bug in one member of a family of similar queries (the purchase-order count per
   sales rep counts cancelled orders where its five siblings do not).
2. *Change a rule the siblings share*: archived orders must leave the latest-orders lists; payments on cancelled
   invoices must leave the payment lists.
3. *Add a query next to similar ones*: payments per quarter, beside six other per-quarter reports.
4. *Thread a new field through one entity*: suppliers get payment terms, shown in every supplier listing.

Each task is a behaviour description with a JUnit acceptance test; the reference change was ported to every
variant and verified to fail before and pass after (`scripts/check_tasks.py --set ambiguity`). All 135 pilot runs
passed their acceptance test. Three runs that ended on a brief API outage (connection refused, 08:42–08:44) were
excluded and re-run.

## Results

Pooled ratio against A, geometric mean of per-task medians, 95% bootstrap interval:

| | billed input tokens | cost | turns | reading-only tokens |
|---|---|---|---|---|
| S / A, 12 target tasks | 1.06 (0.92–1.18) | 1.01 (0.95–1.13) | 1.09 (1.00–1.21) | **0.88 (0.71–0.98)** |
| D / A, 12 target tasks | 0.96 (0.86–1.09) | 0.97 (0.90–1.07) | 1.03 (0.94–1.14) | 0.93 (0.82–1.10) |
| S / A, 3 controls | 1.11 (0.90–1.21) | | | |
| D / A, 3 controls | 0.90 (0.72–0.97) | | | |

By kind of change (billed input tokens against A):

| kind | S / A | D / A |
|---|---|---|
| 1 fix one of many look-alikes | 1.21 (0.91–1.45) | **1.38 (1.10–1.47)** |
| 2 change a rule the siblings share | 1.05 (0.84–1.31) | 1.04 (0.80–1.28) |
| 3 add a query next to similar ones | 1.04 (0.82–1.36) | 0.98 (0.76–1.36) |
| 4 thread a new field through an entity | 0.94 (0.71–1.24) | **0.61 (0.50–0.84)** |

Per task, D/A ranges from 0.57 (a11, a supplier field) and 0.59 (a12, a ticket field) to 1.85 (a01, the cancelled
purchase orders of one count) and 1.45 (a03, one stock listing).

A median run on A cost 227k input tokens, 11 turns and $0.13: about what a Sokrates task cost (248k, 13 turns),
despite a file eight times the read window.

## What the transcripts show

Mean per run over the target tasks, from the classified API calls:

| | calls | search | read | edit | files opened | search noise |
|---|---|---|---|---|---|---|
| A | 10.3 | 3.0 | 2.6 | 1.5 | 3.4 | 19.9 |
| S | 10.7 | 3.1 | 2.5 | 1.4 | 4.4 | 28.3 |
| D | 9.8 | 3.2 | 2.4 | **1.0** | 3.3 | **39.9** |

Search noise is the number of files a search hit that the agent never opened or edited.

- **The agent never reads the big file whole**, in any variant: it searches, reads a window, edits. Ten pieces or
  one 10,000-line file make no difference to that loop, so length does not reach the bill (H2).
- **D cuts the edits by a third** (1.0 against 1.5 per run): where a rule or a mapping now lives once, one edit
  does what took several. That is the kind-4 saving.
- **D doubles the search noise** (39.9 against 19.9): its shared helpers and query objects are referenced from
  every method, so a search for a request's words hits them all. Finding the one method a kind-1 bug lives in, and
  working out which helper path it takes, costs the calls the edits saved. That is the kind-1 penalty.
- **S's facade adds noise too** (28.3): every delegate matches the same search as the method it delegates to.

## The hypotheses

| hypothesis, as registered | pilot |
|---|---|
| H1 ambiguity: D/A ≤ 0.8 | **not supported** overall (0.96); supported for kind 4 only (0.61), against a penalty for kind 1 (1.38) |
| H2 length: S/A within 0.9–1.1 | **supported** (1.06, 0.92–1.18) |
| H3 SD ≈ D | not tested in the pilot (SD not run) |
| H4 reading-only drop ≥ 2× billed drop | **supported in direction**: on S reading fell 12% while the bill rose 6%; on D both moved alike |

By the registered decision rule (interval excludes 1 *and* lies outside the controls' range), even the kind-4
saving is not yet a clear effect: D's own controls came out cheaper too (0.90, 0.72–0.97). D's controls are not
clean controls: D shrank the codebase from 19.7k to 15.3k lines and renamed calls in the services the controls
touch. Kind 1's penalty lies outside that range.

## Reading the article again

The article's change request went from 159,564 to 27,360 "tokens" (characters read ÷ 4) after a 13-step
refactoring of a 17,155-line file. Three things in this pilot bear on that number.

1. **What it measured moves differently from the bill.** On the same runs, reading-only tokens and billed tokens
   went in opposite directions on S. A large drop in what an agent reads can be a small change, or none, in what
   it costs, because most of the bill is the context re-sent on every call.
2. **The kind of change decides the sign.** A change that touches many look-alike places (add a field everywhere,
   change a rule everywhere) is exactly where consolidation pays: 0.61 here. A change to one place among
   look-alikes can get dearer after consolidation. One change request, measured once, says which of the two it
   was, not what refactoring does in general.
3. **The agent's search loop survives length.** Even at 8× the read window the agent windowed the file and did
   not page through it; the article's file was 17× the window and agent-written in a way our agent would not write
   unprompted.

## What it means for Sokrates

- **File length is not an agent cost driver, up to at least 8× the read window.** "Files beyond read budget"
  should not carry weight in the AI ease-of-change score until a cost is measured.
- **Duplication cuts both ways.** It predicts cost for changes that must touch every copy and the opposite for
  changes to one member. A score that counts duplication as uniformly bad for agents is wrong in half the cases.
- **A useful measure is per change, not per file:** how many places a typical change in an area touches (change
  coupling, clone families) against how many places a search for its words hits (search noise). The
  `ambiguity_measure.py` prototype measures the second; the first is in Sokrates' history analysis already.
- **A configuration finding:** Sokrates' default `analysis.maxLines` of 10,000 drops longer files from the analysis
  altogether, silently; at 10,387 lines `DataStore.java` vanished from the duplication report. It should at least
  be reported, and probably raised.

## Limits

- **A pilot:** three repeats per cell against a run-to-run spread of 20–50%; per-kind results rest on three tasks
  each. Directions are informative; the kind-1 and kind-4 effects need the series to be trusted.
- **One grown codebase, one domain, Java**, and an A that needed an explicit copy instruction to become ambiguous.
- **The model under test grew the code**, so its own naming may be easy for it.
- **D bundles names, merging and plumbing helpers**; D-names (names only) is built to separate them but was not in
  the pilot. D's controls are not identical code.
- **Costs of the refactorings are not set against the savings here:** D cost $42.96 of Opus to build, against at
  most a few cents saved per kind-4 change.

## Next

The full series as planned: 15 tasks × {A, S, D, D-names, SD, optionally R} × 10 repeats × Sonnet and Haiku,
≈ 1,500–1,800 runs, ≈ $200–350 at the pilot's ≈ $0.13 per Sonnet run. What it would settle: whether the kind-4
saving and the kind-1 penalty hold, whether names alone (D-names) give D's saving without its search noise, and
whether a split by entity (R) avoids the penalty. Given the pilot, a cheaper intermediate is the series on the
kind-1 and kind-4 tasks only (6 tasks × 5 variants × 10 × 2 ≈ 600 runs, ≈ $90), which is where the effects are.

## Method and cost

- Growth: `ambiguity/grow/` (skeleton, standing instructions, 180 feature requests, drivers); first growth
  `ambiguity/grow/v1/`, second `ambiguity/grow/v2/` (history bundles, logs, entry check).
- Variants: `ambiguity/variants/{a,s,dnames,d,r}`; records of the blind sessions with transcripts in
  `ambiguity/variant-records/`; briefs in `ambiguity/briefs/`; behaviour check `ambiguity/tools/check.sh`;
  manipulation check `ambiguity/manipulation-check/`.
- Tasks: `ambiguity/tasks/` (task, acceptance tests per variant, reference patch per variant).
- Runs: `harness/run.py --set ambiguity`, Claude Code 2.1.300, Sonnet 5.5, three workers with their own Maven
  repositories; `results/pilot-ambiguity.csv`, excluded runs in `results/pilot-ambiguity-excluded.tsv`; analysis
  `ambiguity/tools/analyze_pilot.py` → `results/pilot-ambiguity-analysis.txt`.
- Spent: growth $84.72 (Sonnet), variants $64.16 (Opus: D-names $3.34, D $42.96, R $17.86), pilot $21.33 (Sonnet);
  the task-writing and patch-porting agents ran inside this session and are not counted.
