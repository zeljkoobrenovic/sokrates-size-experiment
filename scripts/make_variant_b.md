# Making variant B (the size-limited twin of variant A)

Variant B is variant A with every main file and unit brought under the thresholds in `analysis/targets-a.md`
(default: file ≤ 500 LOC, unit ≤ 50 LOC and McCabe ≤ 25 — Sokrates' "high risk" boundaries, so B has no
high- or very-high-risk code by size), **and nothing else changed**. Behaviour, public API used by tests, build
layout, dependencies and test suite stay as they are. The point is to measure length, not design quality, so the
split is mechanical, not a redesign:

1. `cp -R variants/a variants/b`, then `java -jar <sokrates cli> init -srcRoot variants/b -confFile analysis/b/config.json`
   and copy the `maxLineLength`, `fileHistoryAnalysis.importPath` and threshold settings from `analysis/a/config.json`
   (or copy `analysis/a/config.json` and change `srcRoot`). Copy `analysis/a/git-history.txt` (and the two sidecars)
   into `analysis/b/`: B has no history of its own, and the history is only used for the predictions, which should
   see the same commits (paths that moved in the split are simply not found; note this when reading B's hotspots).
2. Work from `analysis/targets-a.md`, biggest first. Allowed moves, in order of preference:
   - **extract method**: a unit over the threshold becomes a short orchestrator calling private helpers in the same
     class (each helper under the threshold, parameters passed explicitly, no new fields unless needed);
   - **extract class**: a file over the threshold is split into package-private helper classes in the same package
     (one responsibility each, named after what they render/compute), the original class keeping its public API and
     delegating. Keep the original file name for the public entry point so links, reflection and tests keep working.
   - **not allowed**: changing behaviour, renaming public methods, changing output, introducing interfaces/frameworks,
     reformatting untouched code, "improving" anything the thresholds do not require.
3. After each file: the module's tests (`mvn -q -o -pl <module> test`) stay green, and the **golden output** stays
   identical (`scripts/golden.sh b` compares the reports both variants generate for the fixture; see the script).
4. Re-measure with `scripts/analyze_variant.sh b` and `scripts/targets.py --variant b --date 2025-09-20` until both
   target lists are empty. Commit after each file so the history of the split is reviewable.
5. Record B's predicted scores next to A's in the README (Human/AI ease of change, context lines per change).

The `sokrates-improve` skill (from sokrates-skills) does exactly this loop — one target, one behaviour-preserving
change, re-measure — and can be pointed at `analysis/b/`. Whoever or whatever does the split, the rules above are
what keeps the comparison about size: the split must not smuggle in a better design than a mechanical one.

Keep the two variants' **git view identical**: the harness creates a one-commit repository for each run, so B's
split history is not visible to the agent. Neither variant has a CLAUDE.md or any agent-facing documentation.
