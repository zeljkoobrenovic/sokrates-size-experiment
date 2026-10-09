# Brief for a split batch (variant B)

You are refactoring Java code in a git worktree of an experiment repository: `<WT>` (given below). Work ONLY on the
files listed at the end, under `<WT>/variants/b/`, and commit on the worktree's current branch. Do not touch any other
file in the repository, and never edit `variants/a` (it is the untouched original, useful for comparison).

**Context.** `variants/b` must become a copy of `variants/a` where every main file is ≤ 500 LOC and every unit (method)
is ≤ 50 LOC with McCabe ≤ 25, as measured by Sokrates (LOC = lines without comments and blank lines), with **no other
change**. This is for an experiment on the effect of code LENGTH on AI coding agents, so the split must be mechanical
and behaviour-preserving, not a redesign. Read `<WT>/scripts/make_variant_b.md` first.

**Rules.**
- Allowed moves: *extract method* (private helpers in the same class, explicit parameters, no new fields unless
  needed) and *extract class* (package-private helper classes in the same package, one responsibility each, e.g. one
  helper per report section; the original class keeps its name, its public and package-private API and delegates;
  fields move to a helper when they belong there or are passed in its constructor). Static helpers stay static.
- Not allowed: behaviour changes, output changes (the generated HTML/JSON is compared byte for byte), renaming or
  removing public or package-private members, changing signatures other files use, new frameworks/interfaces/
  abstractions, reformatting or "improving" code the thresholds do not require, touching files outside your list.
- Aim with margin so measurement rounding never puts you over: files ≤ 450 LOC, units ≤ 45 LOC, McCabe ≤ 22.
  A new helper class must itself be under the thresholds.
- Tests in `src/test` may use package-private members: keep those too. Never edit or delete a test.

**Verification after each original file** (commands run from `<WT>/variants/b` unless said otherwise):
1. `mvn -q -o -pl <module> -am test` must be green. If a test fails, check whether it also fails in
   `<WT>/variants/a` (same command there) before assuming you broke it; report pre-existing failures.
2. `cd <WT> && scripts/golden.sh b` must print `golden output identical`. It builds variants/b's jar (offline) and
   compares the reports it generates with `golden/`; on a difference read `<WT>/work/golden-b/diff.txt` and fix the
   behaviour change. Run it before every commit (you may skip it between small unit extractions).
3. Measure: `cd <WT> && SOKRATES_JAR=/Users/zeljkoobrenovic/IdeaProjects/sokrates/cli/target/cli-1.0-jar-with-dependencies.jar scripts/analyze_variant.sh b && python3 -I scripts/targets.py --variant b --date 2025-09-20 > analysis/targets-b.md`
   and check that none of YOUR files or their units remain in `analysis/targets-b.md` (files of other batches will
   still be there; that is expected). The analysis takes about a minute.
4. `git add -A variants/b && git commit -m "Split <File>: <what was extracted>"` on your branch, one commit per
   original file.

At the end, run the whole build once from `<WT>/variants/b`: `mvn -q -o test` (all modules, including codeexplorer,
must compile and pass), and the golden check once more.

**Report back, concisely:** per file LOC before → after, the new classes with their LOC and what each holds, the units
extracted; anything left above the threshold and why; test and golden status; pre-existing test failures; any file
outside your list you had to touch (should be none; if unavoidable, say exactly what and why).
