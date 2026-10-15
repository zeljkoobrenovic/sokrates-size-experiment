#!/usr/bin/env bash
# The behaviour check for a refactoring step. Prints CHECK PASSED or CHECK FAILED.
#  1. builds code/ and runs every test of every module (mvn -o verify);
#  2. runs the built CLI and the baseline CLI (reference/cli-a.jar) over fixture/ (two small repositories, then a
#     landscape over them), normalizes both outputs (dates, timings) and compares them: they must be identical.
#   ./check.sh            both steps
#   ./check.sh --no-tests skip the tests (quick output check after a build)
set -uo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"
OUT="$HERE/.check"; DATE=2025-09-20
rm -rf "$OUT"; mkdir -p "$OUT"
if [ "${1:-}" = "--no-tests" ]; then
  ( cd "$HERE/code" && mvn -q -o package -DskipTests ) > "$OUT/build.log" 2>&1 || { tail -40 "$OUT/build.log"; echo "CHECK FAILED: build"; exit 1; }
else
  ( cd "$HERE/code" && mvn -o verify ) > "$OUT/build.log" 2>&1 || {
    grep -E "Tests run:.*(Failures: [1-9]|Errors: [1-9])|FAIL|ERROR\]" "$OUT/build.log" | head -30; echo "CHECK FAILED: build or tests (full log: .check/build.log)"; exit 1; }
  echo "build and tests: $(grep -E '^\[(INFO|WARNING)\] Tests run:' "$OUT/build.log" | grep -v ' - in ' | awk -F'[ ,]+' '{s+=$4} END{print s}') tests passed"
fi
run() { # <jar> <name>
  local jar="$1" name="$2" root="$OUT/$2/root"
  mkdir -p "$OUT/$name"; cp -R "$HERE/fixture" "$root"
  for repo in repo1 repo2; do
    ( cd "$root/$repo" \
      && SOKRATES_OFFLINE=1 java -jar "$jar" init -srcRoot . -confFile _sokrates/config.json -name "$repo" \
      && python3 -I "$HERE/tools/fixture_config.py" _sokrates/config.json \
      && SOKRATES_OFFLINE=1 java -jar "$jar" generateReports -confFile _sokrates/config.json -outputFolder _sokrates/reports -date $DATE -timeout 1200 ) >"$OUT/$name/$repo.log" 2>&1
  done
  ( cd "$root" && SOKRATES_OFFLINE=1 java -jar "$jar" updateLandscape -analysisRoot . -date $DATE -timeout 1200 ) >"$OUT/$name/landscape.log" 2>&1
  local norm="$OUT/$name/norm"; mkdir -p "$norm"
  for part in repo1/_sokrates:repo1 repo2/_sokrates:repo2 _sokrates_landscape:landscape; do
    mkdir -p "$norm/${part#*:}"; cp -R "$root/${part%%:*}/." "$norm/${part#*:}/"
  done
  find "$norm" -name '*.zip' | while read -r z; do unzip -q -o "$z" -d "${z%.zip}_unzipped" && rm -f "$z"; done
  python3 -I "$HERE/tools/normalize.py" "$norm"
  find "$norm" -type d -empty -delete
}
run "$HERE/code/cli/target/cli-1.0-jar-with-dependencies.jar" new &
run "$HERE/reference/cli-a.jar" baseline &
wait
if diff -r -q "$OUT/baseline/norm" "$OUT/new/norm" > "$OUT/diff.txt" 2>&1; then
  echo "report output identical to the baseline ($(find "$OUT/new/norm" -type f | wc -l | tr -d ' ') files)"
  echo "CHECK PASSED"
else
  echo "report output differs from the baseline in $(wc -l < "$OUT/diff.txt" | tr -d ' ') files (list: .check/diff.txt; compare .check/baseline/norm with .check/new/norm):"
  head -20 "$OUT/diff.txt" | sed "s|$OUT/||g"; echo "CHECK FAILED: output"; exit 1
fi
