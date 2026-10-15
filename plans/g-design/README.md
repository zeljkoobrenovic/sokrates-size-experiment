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
