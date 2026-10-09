# Brief for authoring experiment tasks

The experiment repository is `<EXP>` = /Users/zeljkoobrenovic/IdeaProjects/sokrates-size-experiment. Read `<EXP>/README.md`
and `<EXP>/tasks/README.md` first, and look at the finished example `<EXP>/tasks/t01-undefined-team-active/`
(task.md, meta.json, acceptance/a/...). The reference history is the current Sokrates checkout
`<SOK>` = /Users/zeljkoobrenovic/IdeaProjects/sokrates (read-only: `git -C <SOK> show <sha>` for a reference commit).
`<EXP>/variants/a` is the year-old snapshot, `<EXP>/variants/b` the size-limited twin (same behaviour, files split into
helper classes). Never modify anything under `<EXP>/variants/`; work in scratch copies under `<EXP>/work/<your name>/`.

For each task you are given a reference commit. Produce `<EXP>/tasks/<id>/` with:

1. `task.md` — the prompt for the coding agent. Describe the wanted behaviour from a user's or maintainer's point of
   view, as the commit message would for a reviewer: what should happen, in which feature, with the concrete observable
   outcome. Do NOT give the solution, do NOT name files, classes, methods or line numbers unless a user naturally
   would (a command name, a config key, a report section name, a file the tool writes are fine). Never mention the
   variants, the experiment, helper classes or the split. 3–10 sentences.
2. `meta.json` — like t01's: `id`, `kind` (`target` or `control`, as given), `title`, `reference_commit`,
   `reference_repo`, `source_files` (the files the reference change touched, variant-A paths), `target_unit` (what
   the change lands in, with its size in A, from `<EXP>/analysis/targets-a.md`; for a control: "n/a"), `module`,
   `test_command` (`mvn -q -o -pl <module> test -Dtest=<TestClass> -Dsurefire.failIfNoSpecifiedTests=false`),
   `test_command_b` only if B needs another command, `notes` (what the reference change does; the verification
   results below).
3. `acceptance/a/<repo path of the test>` — ONE JUnit 5 test class (package of the code under test) that fails on
   the untouched snapshot and passes with the change. Test through public (or package-private) API that both
   variants share — variant B keeps every original class name and public API, so a test written against A's public
   API compiles on B. Prefer small synthetic inputs (objects built in the test, a temp folder, a tiny fixture
   string) over big fixtures. If the outcome is generated HTML or files, generate into a `@TempDir` and assert on
   the content. If B really needs a different test (it should not), add `acceptance/b/...`.

Verification protocol (record the results in meta.json `notes`):
- A, before: in a scratch copy of variants/a (`rsync -a --exclude target <EXP>/variants/a/ <EXP>/work/<name>/a/`)
  add the test and run the test command with `-am` added (`mvn -q -o -pl <module> -am test -Dtest=... -Dsurefire.failIfNoSpecifiedTests=false`):
  it must COMPILE and FAIL (an assertion failure, not a compilation error or an unrelated exception).
- A, after: apply the reference change. If `git -C <SOK> show <sha> -- <files>` applies cleanly (`git apply --check`),
  apply it; otherwise re-derive the same change by hand against the snapshot (later commits often build on
  intermediate changes; the task is defined by the behaviour, not by the literal diff). The test must PASS, and
  the module's existing tests must still pass (`mvn -q -o -pl <module> -am test`).
- B, before: same in a scratch copy of variants/b: the test must COMPILE and FAIL the same way.
- B, after: port the same change into B (the code may now live in a helper class next to the original; find it with
  grep) and the test must PASS with the module's tests green. This proves the task is solvable in both variants and
  the test is variant-neutral.
- Keep the diff you applied to A and to B as `<EXP>/tasks/<id>/reference/a.patch` and `b.patch` (git diff of the
  scratch copies, excluding the test file), so the reference solution is documented.

If a candidate turns out not to be feasible (the behaviour depends on code that does not exist in the snapshot, or
no reasonable acceptance test exists), skip it, say why in your report, and take the next candidate from your list.
Do not run `git` commands that change `<EXP>` (no add/commit/checkout); just leave the task folders in place.
Clean up `<EXP>/work/<name>/` at the end.

Report back: per task: id, title, reference commit, kind, module, the four verification results, the size of the
reference change (+/- lines) and where it lands in A and in B (file and LOC), and anything a reviewer should know.
