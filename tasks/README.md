# Tasks

One folder per task. The harness reads:

- `task.md` — the prompt the agent gets (after `harness/prompt-preamble.md`). Describes the wanted behaviour, not the solution; names a class or method only when a user would (the agent must still find and read the code). Never names a variant or a file layout.
- `meta.json` — `kind` (`target` = the change lands in a file/unit above the size threshold; `control` = untouched area, expected to show no A/B difference), `reference_commit` (the real commit in zeljkoobrenovic/sokrates the task was derived from), `source_files` (what the real change touched, variant-A paths; not shown to the agent), `module`, `test_command` (run in the repo after the agent finishes; exit code 0 = success), optional `test_command_b` when variant B needs another command.
- `acceptance/a/…` — files copied into the repository **after** the agent's run (mirroring repository paths), normally one JUnit test. `acceptance/b/…` when variant B needs a different version (other imports after the split); the harness falls back to `acceptance/a/`.

Every acceptance test must be checked twice before the task is used: it **fails** on the untouched snapshot and **passes** with the reference change applied (`scripts/mine_tasks.py` tells whether the real diff applies to the snapshot as is; when not, re-derive the change by hand). Record both checks in `meta.json`'s `notes`.

Candidates: `candidates.md` (regenerate with `scripts/mine_tasks.py --repo ../sokrates --base 5e6aa97a > tasks/candidates.md`). Good tasks are small (one to a few dozen changed lines), self-contained, located in the files listed in `analysis/targets-a.md`, and have a crisp testable outcome. Aim for 6–10 target tasks of mixed kinds (bug fix, small feature, behaviour change) plus 2 control tasks.
