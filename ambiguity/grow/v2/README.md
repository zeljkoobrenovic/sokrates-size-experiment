# Second growth: `STANDING-v2.md` (copy the most similar method and adapt it), features 1–180, Sonnet 5.5

2026-10-16 01:23–~08:00. `history.bundle` (git, one commit per feature), `grow-log.tsv` (per feature: DataStore
lines, main lines, turns, cost), `chain.log` (the continuation decisions), `entry-check.json`.

180 features (1–100 as planned, then 101–150, then 151–180 in chunks of 10 until DataStore passed 10,000 lines),
$62.02, 3,640 turns, one fix-up session. Features 151–180 are the template-generated fourth batch (five similar
reports per entity).

Entry check (`ambiguity/tools/entry_check.py`), all passed: DataStore 10,387 lines, 401 methods, 190 of them with a
near-twin; Sokrates duplication 32.0% of DataStore (19.0% of the codebase), 34 clusters of ≥ 3 near-twins; median
11 methods hit over the 15 entry terms; 453 Java files; tests green; the demo (1,106 lines) deterministic.

Measurement note: Sokrates' default `analysis.maxLines` (10,000) drops longer files from the analysis altogether, so
at 10,387 lines DataStore vanished (codebase duplication 1.1%); the entry check raises the limit.

The frozen baseline is `ambiguity/variants/a` (the final commit, `git archive`).
