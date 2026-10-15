# Arm g: the blind list of generic refactorings

`proposal.md` is the list, unedited, drawn up on 2026-10-15 by Claude Opus 5.5 (`claude -p`, $6.87, 3 Explore
subagents) from `scripts/g_brief.md` (copied in as BRIEF.md) in an isolated folder that held only a copy of
variant A (`code/`) and its Sokrates reports (`sokrates-reports/`, experiment paths rewritten to `code/`), nothing
of `tasks/`, `results/` or this repository.

Audit of the four transcripts (`*.jsonl.gz`, the main session and its subagents): every file path read is inside
the folder, apart from Claude Code's own saved tool outputs of the session (`~/.claude/projects/…/tool-results/`);
one write went one level up (`../g-design-tools/churn.sh`, a helper script). No path under `tasks/`, `results/`
or the experiment repository appears.

The held-out task selection was committed (e20431f) before this proposal was read.

## Implementation (2026-10-15, 18:59–21:02, $22.84)

One `claude -p` session per refactoring (`IMPLEMENT.md`, `run_all.sh`, `driver.log`), all ten committed and
re-checked by the driver (`check.sh`: all tests, report output identical to A over the fixture, 906 files).
`IMPLEMENTATION.md` is the agents' own log (deviations: R6 left `SummaryUtils` alone, as it has no bucket list;
R5 kept `ContributorRepositories`' different blank-date semantics; R3 needed two extra parameters; R8 left the
CLI's file-size colouring in place). `impl/` holds each session's result, the commits as patches, and the
transcripts. Audit: no tool call names a path under the experiment repository; the out-of-folder paths are the
agents' own scratch files (`r1scripts`, `r2work`, `/tmp/r5`, file lists in `/tmp`) and the session's own task
output. Extra check, since the fixture does not run the CLI's help: `scripts/compare_cli.sh` runs A's and g's
jar for every command with and without `-help`, plus no command, an unknown and a mixed-case one (82
invocations): identical apart from timings, timestamps and stack-trace line numbers. `scripts/golden.sh g`:
identical (906 files). Result: `variants/g`, 66 files changed against A, +2,865 / −2,167.
