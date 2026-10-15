You are in a folder with:

- `code/`: a Java/Maven codebase (Sokrates, a source-code analysis tool), a git repository; one commit per finished
  refactoring.
- `proposal.md`: a list of refactorings (R1 … R10) proposed for this codebase, with their evidence.
- `check.sh`: the behaviour check. It builds `code/`, runs every test, then runs the built tool and the baseline
  (`reference/cli-a.jar`, the codebase before any refactoring) over `fixture/` and compares the generated reports.
  It prints `CHECK PASSED` or `CHECK FAILED` with the differences. `./check.sh --no-tests` skips the tests for a
  quicker output check. A full check takes under a minute.
- `IMPLEMENTATION.md`: the log of the refactorings done so far (create it if it does not exist).

Work only with what is in this folder; do not look anywhere else on the disk and do not use the network. Do not
change `check.sh`, `fixture/`, `reference/`, `tools/` or `proposal.md`.

## Your job: implement exactly one refactoring, the one named at the end of this message

1. Read its section in `proposal.md` (and the sections it depends on, which are already implemented if the
   proposal orders them first; see `IMPLEMENTATION.md` and `git -C code log`).
2. Implement it as described. The rules of the proposal hold:
   - behaviour-preserving: the generated reports must stay identical (`check.sh`), all existing tests pass;
   - keep every existing public and package-private class, constructor, method and field signature; old members
     may delegate to new code; new classes and methods are fine; private members may move or be renamed;
   - do not fix the defects listed in the proposal's appendix, and do not fix other bugs you find: preserve them;
   - no new dependencies, no build changes;
   - tests the proposal asks for may be added; existing tests may change only where the refactoring moves what
     they test.
3. If keeping the output identical makes part of the refactoring impossible, implement the rest and record the
   part left out and why. Do not widen the refactoring beyond its section.
4. Run `./check.sh` (the full check, with tests) until it prints `CHECK PASSED`. Then commit:
   `git -C code add -A && git -C code commit -m "<id>: <title>"`.
5. Append a section to `IMPLEMENTATION.md`: the id and title, what you did (files, new classes), what you left out
   or did differently from the proposal and why, and the `git -C code show --stat HEAD` summary.
6. If you cannot get `CHECK PASSED`, restore the last commit (`git -C code reset --hard && git -C code clean -fd`),
   and record the refactoring in `IMPLEMENTATION.md` as not done, with the reason.

Use `git -C code …` and `mvn -f code/pom.xml …` (or `cd code && …`) for commands in the codebase.
