You are in a folder with:

- `code/`: a Java/Maven codebase (Sokrates, a source-code analysis tool), a git repository.
- `task.md`: a change that a coding agent will be asked to make to this codebase *later*, in exactly these words.
- `reference.patch`: how the change was actually made (for your understanding; do not apply it).
- `acceptance/`: the test that will decide whether the later change is right, and `accept.sh`, which runs it
  against `code/`. Today it must FAIL (the change is not made); it must still fail after your work.
- `check.sh`: the behaviour check. It builds `code/`, runs every test, then runs the built tool and the baseline
  (`reference/cli-a.jar`, the codebase as given) over `fixture/` and compares the generated reports. It prints
  `CHECK PASSED` or `CHECK FAILED`. `./check.sh --no-tests` skips the tests.

Work only with what is in this folder; do not look anywhere else on the disk and do not use the network. Do not
change `check.sh`, `accept.sh`, `acceptance/`, `fixture/`, `reference/`, `tools/`, `task.md` or `reference.patch`.

## Your job: a preparatory refactoring that makes the later change easy

"Make the change easy, then make the easy change." Make a behaviour-preserving refactoring now, such that the
agent who later gets `task.md` finds the place to change quickly and can make the change small and in one place.

What the later agent costs is measured: it is set by **how many calls it makes**. It searches (grep) for words
from the task description, opens windows of 40–70 lines around the hits, edits, compiles or runs one test, and
finishes. Calls are lost on searches that hit many files it does not need or that miss because the code uses other
words than the task does, on reading several files to understand one behaviour, and on edits in several places
(and fix-ups when one is missed). Length of files and methods did not matter, and neither did build output.

So: bring together what the change needs, make the logic it changes exist in one place, and make the words of the
task findable where the change goes. Keep it to what this change needs.

Rules:
- Do NOT make the change itself, or any part of it: `./accept.sh` must still report the acceptance test FAILING.
- Behaviour-preserving: `./check.sh` (full, with tests) must print `CHECK PASSED`.
- Keep every existing public and package-private class, constructor, method and field signature (the acceptance
  test and other code use them); old members may delegate; new classes and methods are fine.
- Do not fix bugs (other than by not touching them); no new dependencies, no build changes.

When `./check.sh` passes and `./accept.sh` reports FAILING, commit:
`git -C code add -A && git -C code commit -m "Preparation for the change in task.md"`, and write `PREPARATION.md`:
what you changed and why it makes the later change cheaper, and `git -C code show --stat HEAD`.
If no preparation would help, commit nothing and say why in `PREPARATION.md`.

Use `git -C code …` and `mvn -f code/pom.xml …` (or `cd code && …`) for commands in the codebase.
