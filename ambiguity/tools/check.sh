#!/usr/bin/env bash
# Behaviour check of a BackOffice variant: build and run every test (mvn -o verify), then run DemoScenario of the
# built code and of the baseline (reference/baseline.jar) and require identical output. Prints CHECK PASSED or
# CHECK FAILED.
set -uo pipefail
HERE="$(cd "$(dirname "$0")" && pwd)"; OUT="$HERE/.check"; rm -rf "$OUT"; mkdir -p "$OUT"
H2=$(ls ~/.m2/repository/com/h2database/h2/2.2.224/h2-2.2.224.jar)
( cd "$HERE/code" && mvn -o verify ) > "$OUT/build.log" 2>&1 || {
  grep -E "Tests run:.*(Failures: [1-9]|Errors: [1-9])|ERROR\]" "$OUT/build.log" | head -30; echo "CHECK FAILED: build or tests (.check/build.log)"; exit 1; }
echo "build and tests: $(grep -E '^\[(INFO|WARNING)\] Tests run:' "$OUT/build.log" | grep -v ' - in ' | awk -F'[ ,]+' '{s+=$4} END{print s}') tests passed"
java -cp "$HERE/code/target/classes:$H2" com.acme.backoffice.DemoScenario > "$OUT/new.txt" 2> "$OUT/new.err" || { echo "CHECK FAILED: demo did not run"; tail -5 "$OUT/new.err"; exit 1; }
java -cp "$HERE/reference/baseline.jar:$H2" com.acme.backoffice.DemoScenario > "$OUT/baseline.txt" 2> "$OUT/baseline.err"
if diff "$OUT/baseline.txt" "$OUT/new.txt" > "$OUT/diff.txt"; then
  echo "demo output identical to the baseline ($(wc -l < "$OUT/new.txt" | tr -d ' ') lines)"; echo "CHECK PASSED"
else
  echo "demo output differs from the baseline:"; head -20 "$OUT/diff.txt"; echo "CHECK FAILED: output"; exit 1
fi
