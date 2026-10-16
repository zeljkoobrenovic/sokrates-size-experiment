# Length or ambiguity? A 2×2 experiment on a giant agent-written file

*Design, 2026-10-16. Follows `next-experiments.md` and the pilots of arms g and p.*

## Why

Four results so far point the same way: on Sokrates, neither splitting files (B/A 1.03), nor merging them to
3k lines (smoke run), nor generic refactorings (g/A 1.00 Sonnet, 1.03 Haiku), nor preparation tailored to the
change (p/A 0.94 Sonnet, 1.08 Haiku) moves the bill. The agent greps, lands on one or two hits, reads a window,
and the fixed per-call context dominates. *The Economic Benefit of Refactoring* (martinfowler.com, 2026) reports
−83% after refactoring a 17,155-line agent-written data-access file. The working explanation:

1. **Ambiguity, not length.** In a file of many near-identical, generically named functions, a search for a
   task's words hits dozens of look-alike places; the agent opens window after window to tell them apart.
   Sokrates never had that, so nothing there could show it.
2. **Measurement.** Characters ÷ 4 of what the agent read counts the reading only; the billed run is mostly
   fixed context, cache writes and output (46% fixed context for Sonnet). The same change shrinks much less
   in the bill.
3. **One run per step**, against 20–50% (Sonnet) and 1.5× (Haiku) run-to-run spread.

The experiment separates 1 into its two parts (length and ambiguity), measures 2 directly, and removes 3.

## Hypotheses (registered before any run)

- **H1, ambiguity:** removing ambiguity in place lowers billed input tokens on the target tasks: D/A ≤ 0.8.
- **H2, length:** cutting the file into pieces without touching names or clones changes little: S/A within
  0.9–1.1.
- **H3, no interaction:** SD ≈ D (SD/D within 0.9–1.1); the split adds nothing once ambiguity is gone.
- **H4, measurement:** the drop in *reading-only* tokens (characters ÷ 4 of Read and shell output, the
  article's measure) is at least twice the drop in billed input tokens, for the same runs.
- **Mechanism check:** on D and SD, search noise (files and places a grep hits that the agent never opens or
  edits) and read calls fall; on S they do not.

Decision rule as before: an effect counts if its 95% interval excludes 1 and lies outside the range of the control
tasks. The run-to-run spread is measured on A itself (10 repeats), so "within noise" is a number, not a phrase.

## The codebase: grown by an agent, under constraints that make the giant file plausible

No public codebase is known to have a 10k+ line agent-written file with a history to mine tasks from, so one is
grown, the way the article's was: by an agent, feature by feature, never reviewed.

- **Language and build:** Java 17 / Maven, single module, JUnit 5, so the harness, `check.sh`-style checks and
  acceptance tests carry over unchanged. (The article used Rust; a Rust replication is a follow-up, see Threats.)
- **Domain:** an order-and-inventory back office (customers, products, orders, invoices, shipments, suppliers,
  warehouses, returns, promotions, … ≈ 25–30 entities) on an embedded database (H2), with a REST-less service
  layer and a CLI that runs fixed scenarios. Plain enough that any model knows the domain; large enough for
  ≈ 300–400 data-access methods.
- **How it is grown:** a scripted sequence of ≈ 60–80 small feature requests (`plans/ambiguity/grow/NN.md`), one
  `claude -p` session each (Sonnet 5.5, so the code is in the style of the model being measured), each ending
  with tests green. The standing instruction is what an unreviewed project gives an agent in practice: "all data
  access lives in `DataStore.java`; follow the existing patterns; do not refactor existing code; add a test
  per feature". No instruction to duplicate or to name badly: the ambiguity has to arise from the agent's own
  habits, as in the article.
- **Entry criteria for the grown baseline A** (checked before any task is written; if a criterion fails, grow
  further or adjust the standing instruction once, and record that):
  - `DataStore.java` ≥ 10,000 lines (target 12–15k, ≥ 5× the 25k-token Read window);
  - Sokrates duplication in `DataStore.java` ≥ 15% of its lines, and ≥ 20 clone clusters of ≥ 3 members;
  - **ambiguity measure** (defined below) ≥ 8 hit sites, median over the task terms;
  - all tests green, the scenario output deterministic.
  If the agent does *not* produce such a file under these instructions, that is a finding too, and the second
  attempt makes the constraint explicit ("add each query as a new method next to similar ones").

**Ambiguity measure (new, also the Sokrates candidate).** For each task, the words a maintainer would use (from
the task text: entity, field and behaviour nouns), grep over main code: the number of distinct methods hit, and
how alike they are (the share of hit methods that belong to one clone cluster). Computed by a script before the
runs, for every variant, so the manipulation is verified, not assumed.

## Variants: a 2×2 of length and ambiguity, plus the article's full refactoring

| variant | file layout | ambiguity | made how |
|---|---|---|---|
| **A** | one `DataStore.java`, 12–15k lines | high | grown (above) |
| **S** | split into ≈ 12 classes of ≈ 1,000 lines, cut by position (method order kept, neutral names `DataStorePart01…`), `DataStore` delegates | unchanged: same names, same clones | script, mechanical (like B) |
| **D** | one file | removed in place: distinct domain names for look-alike methods, clone clusters merged into shared helpers with parameters, the entity named in every method | blind agent, brief below |
| **SD** | D, then cut by position as S | removed | script on D |
| **R** (optional) | the article's kind of result: split by entity (`CustomerStore`, `OrderStore`, …) and disambiguated | removed | blind agent |

- **S isolates length:** the cut is by position, so a file name says nothing about the entities in it and
  every clone and name survives. It brings every file under one Read window.
- **D isolates ambiguity:** the file stays one file. Merging clones shortens it (likely to 6–9k lines), so D's
  length is recorded and must stay ≥ 2.5× the Read window. A rename-only variant **D-names** (no clone merging,
  length unchanged) is built as well, so a D effect can be attributed to names versus to clone removal.
- **SD** answers whether splitting adds anything once ambiguity is gone; **R** shows what a natural "good
  refactoring" yields, for comparison with the article's number.
- **Behaviour preserved, checked like g:** all tests green, the scenario CLI's output identical to A's
  (normalized), every acceptance test compiling against every variant (public API kept; S and SD delegate).
- **Blind construction:** D and R are made by agents that see the code and the brief, never the tasks (isolated
  folder, transcript audit, as for g). The brief for D gives the measured mechanism (calls spent searching and
  telling look-alike hits apart) and forbids moving code between files.

## Tasks: written before any variant exists

- **12 target tasks** in the data-access layer, three of each kind, ranging from where ambiguity should hurt
  most to where it should not matter:
  1. *fix one of many look-alike queries* (a bug in the open-orders-by-region query, not in its ten siblings);
  2. *change a rule all siblings share* (soft-deleted rows excluded everywhere; one place in D, many in A);
  3. *add a query next to similar ones* (a new report for one entity);
  4. *thread a new field through one entity's queries*.
- **3 control tasks** outside the data-access layer (CLI parsing, a formatting utility), identical code in every
  variant.
- Written as behaviour (what should happen, in the domain's words) plus one JUnit acceptance test that fails on A
  and passes with a reference change; reference patches ported to every variant and verified with
  `check_tasks.py` (fail before, pass after), as for g and p.
- Task authors see A only and do not see the variants' briefs.

## Measurement

Per run, as now: billed tokens from the CLI (fresh input, cache read, cache write, output, cost), turns, time,
diff, acceptance; the transcript (per-call usage) and the tool log. Added:

- **reading-only tokens**: characters of Read results and shell output ÷ 4, summed over the run (the article's
  measure), next to the billed figure, for H4;
- **search noise**: per grep, hit sites; per run, methods hit but never opened or edited;
- **windows per file and paging**: reads of the big file, offsets, truncation notices (25k-token cap);
- **calls by class** (`turn_analysis.py`): locate, read, edit, build, fix.

## Runs and cost

- Pilot first: 15 tasks × {A, S, D} × 3 repeats × Sonnet = 135 runs. Go on only if it runs cleanly; it also
  shows whether the A runs reach the ambiguity regime at all (search noise per run ≫ Sokrates' median of 4).
- Series: 15 tasks × {A, S, D, D-names, SD} × 10 repeats × {Sonnet 5.5, Haiku 5.5} = 1,500 runs; R optional
  (+300). Repeats interleaved, variant order alternated, one Maven repository per worker.
- Cost estimate: runs on this codebase are likely heavier than on Sokrates (more search, bigger windows):
  Sonnet ≈ $0.15–0.40 per run, Haiku ≈ $0.02–0.08, so ≈ $130–330 for the series plus ≈ $20–60 to grow the
  codebase and build D, D-names and R. One codebase growth, recorded and committed, serves all variants.

## Analysis

- Pooled ratios per variant against A (geometric mean of per-task medians, bootstrap intervals), per task kind and
  per model; the 2×2 contrasts: length effect (S/A and SD/D), ambiguity effect (D/A and SD/S), interaction.
- H4: per run, billed input versus reading-only tokens; the ratio of their drops (A → SD, A → R).
- Mechanism: search noise, read calls and files opened per variant, and their correlation with cost within task.
- Break-even: the cost of making D (or R) against the measured saving per change.
- Sokrates: whether the ambiguity measure predicts the per-task effect better than file size does (it should, by
  H1–H2); if so it replaces "files beyond read budget" in the AI ease-of-change score.

## Threats and limits

- **Grown, not found.** The codebase is made for the experiment, under an instruction ("all data access in one
  file") that makes the giant file likely. It is realistic for unreviewed agent work, but it is still one
  codebase, one domain and one generation run.
- **Java, not Rust.** The article's file was Rust; grep and read behaviour should not depend much on the language,
  but a Rust replication (the same growth script, `cargo test`) is the obvious follow-up if H1 holds.
- **D bundles two operations** (names and clone merging); D-names separates them, but other disambiguations exist.
- **Model familiarity:** the code is grown by the model under test, so its naming is "familiar" to it; the
  article's agent and ours may differ.
- **Self-consistency:** every manipulation (length, ambiguity) is measured on every variant before the runs, so a
  null result can be told apart from a failed manipulation.

## Order

1. Write the growth script (feature list, standing instruction) and grow A; check the entry criteria.
2. Write the 15 tasks and acceptance tests on A; reference patches; register the hypotheses (this file, committed).
3. Build S and SD (script), D, D-names and R (blind agents); behaviour checks; ambiguity and length measured.
4. Pilot (135 Sonnet runs); then the series.

## Log

- **2026-10-16 00:08**: first growth (`grow.sh`, `STANDING.md`) started, features 1–72, Sonnet 5.5.
- **2026-10-16 ~01:10, the registered adjustment, decided early.** After 40 features `DataStore.java` had 2,403
  lines (≈ 55 per feature, projecting ≈ 4.5–5k at 72) and almost no near-copies: after 19 features, 7 twin pairs
  (Jaccard ≥ 0.6) and 1 cluster of ≥ 3 in the whole codebase; the methods "open orders" hits had a mean similarity
  of 0.01. Left to the plain standing instruction, Sonnet writes distinct, if verbose, methods: the length and
  duplication entry criteria would fail. So the second growth (`grow-v2.sh`, `STANDING-v2.md`: "add each new query
  next to the most similar existing one: copy that method and adapt it; no shared helpers") was started in
  parallel, with features 1–100 (73–100 added for length), rather than after the first finished. The first growth
  runs to its end and is kept as the record of unguided growth; A is taken from whichever growth meets the entry
  criteria (the second, if the projection holds), and the choice is recorded here.
- **2026-10-16 ~08:00, A frozen.** The second growth needed 180 features (1–100, then 101–150, then 151–180 from the
  template-generated fourth batch) to pass 10,000 lines; $62.02, one fix-up session. All entry criteria pass
  (`ambiguity/grow/v2/README.md`): DataStore 10,387 lines, 190 of 401 methods with a near-twin, Sokrates
  duplication 32% of DataStore, 34 clusters, median 11 methods hit per entry term. Found on the way: Sokrates'
  default `maxLines` of 10,000 silently drops longer files from the whole analysis.
