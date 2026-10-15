# Arm p: tailored preparations, one per target task

One `claude -p` session (Opus 5.5) per task, brief `PREPARE.md`, driver `run_all.sh`, log `driver.log`
(2026-10-15, 20:28–20:51, $4.14 in all). Each session had A as a git repository, `task.md`, the reference patch,
the acceptance test, `check.sh` (all tests + report output identical to A over the golden fixture) and `accept.sh`
(the acceptance test must still FAIL). The driver re-ran both after each session; all ten passed.

A first attempt (`driver-aborted.log`) stopped after t01: the session had prepared the change but could not run
`../check.sh` (not in the tool allowlist), so it did not commit and the driver discarded the work. The allowlist
was widened, a sentence added to the brief (run the scripts from the folder root), every folder reset to A, and all
ten tasks run again from scratch. t01's folder holds both transcripts.

Per task (`tNN/`): `PREPARATION.md` (the agent's account), `preparation.patch` (the commit), `run.json` (cost,
turns), the transcript. Audit: every path named in a tool call lies inside the task's folder (three flags checked
by hand: `../` chains that resolve inside `code/`, and a path inside the text of an edit).

| task | files | lines | turns | cost |
|---|---|---|---|---|
| t01 | 1 | +11 / −1 | 14 | $0.34 |
| t02 | 3 | +37 / −7 | 17 | $0.60 |
| t03 | 1 | +17 / −6 | 16 | $0.37 |
| t04 | 1 | +15 / −4 | 12 | $0.31 |
| t05 | 1 | +24 / −9 | 14 | $0.35 |
| t06 | 3 | +61 / −11 | 14 | $0.36 |
| t07 | 1 | +21 / −12 | 17 | $0.48 |
| t08 | 1 | +24 / −7 | 15 | $0.39 |
| t09 | 2 | +10 / −3 | 21 | $0.50 |
| t10 | 1 | +37 / −15 | 12 | $0.44 |
