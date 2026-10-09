#!/usr/bin/env bash
# Golden-output check for a variant: build the variant's CLI jar (package, no install — safe to run in parallel
# worktrees), run it over golden-fixture/ (two small repositories with git history, then a landscape over them)
# and diff the generated reports and data against the stored golden output (produced by variant A). Volatile
# parts (dates, timings) are normalized before the diff.
#
#   scripts/golden.sh a --store      # build variant A, generate and store golden/ (done once; committed)
#   scripts/golden.sh b              # build variant B, generate and compare with golden/
#   GOLDEN_SKIP_BUILD=1 scripts/golden.sh b   # reuse the jar already in variants/b/cli/target
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
V="${1:?variant}"; MODE="${2:-}"
SRC="$ROOT/variants/$V"
OUT="$ROOT/work/golden-$V"
GOLDEN="$ROOT/golden"
DATE=2025-09-20
JAR="$SRC/cli/target/cli-1.0-jar-with-dependencies.jar"

if [ "${GOLDEN_SKIP_BUILD:-}" != "1" ]; then
  ( cd "$SRC" && mvn -q -o package -DskipTests )
fi
[ -f "$JAR" ] || { echo "no jar at $JAR"; exit 1; }

rm -rf "$OUT"; mkdir -p "$OUT"
cp -R "$ROOT/golden-fixture" "$OUT/root"
for repo in repo1 repo2; do
  ( cd "$OUT/root/$repo" \
    && SOKRATES_OFFLINE=1 java -jar "$JAR" init -srcRoot . -confFile _sokrates/config.json -name "$repo" >"$OUT/$repo-init.log" 2>&1 \
    && SOKRATES_OFFLINE=1 java -jar "$JAR" generateReports -confFile _sokrates/config.json -outputFolder _sokrates/reports -date $DATE -timeout 1200 >"$OUT/$repo-reports.log" 2>&1 )
done
( cd "$OUT/root" && SOKRATES_OFFLINE=1 java -jar "$JAR" updateLandscape -analysisRoot . -date $DATE -timeout 1200 >"$OUT/landscape.log" 2>&1 )

# collect the comparable output: reports + data (loose or zipped), normalized
NORM="$OUT/norm"; mkdir -p "$NORM"
collect() { # <source folder> <name>
  local src="$1" name="$2"
  mkdir -p "$NORM/$name"
  cp -R "$src/." "$NORM/$name/"
  find "$NORM/$name" -name '*.zip' | while read -r z; do unzip -q -o "$z" -d "${z%.zip}_unzipped" && rm -f "$z"; done
}
collect "$OUT/root/repo1/_sokrates" repo1
collect "$OUT/root/repo2/_sokrates" repo2
collect "$OUT/root/_sokrates_landscape" landscape
python3 -I "$ROOT/scripts/golden_normalize.py" "$NORM"
find "$NORM" -type d -empty -delete   # git stores no empty folders, so golden/ has none either

if [ "$MODE" = "--store" ]; then
  rm -rf "$GOLDEN"; cp -R "$NORM" "$GOLDEN"
  echo "golden output stored in $GOLDEN ($(find "$GOLDEN" -type f | wc -l | tr -d ' ') files)"
else
  [ -d "$GOLDEN" ] || { echo "no golden output yet: run scripts/golden.sh a --store"; exit 1; }
  find "$GOLDEN" -type d -empty -delete   # a freshly stored golden/ may still hold them
  if diff -r -q "$GOLDEN" "$NORM" >"$OUT/diff.txt" 2>&1; then
    echo "golden output identical ($(find "$NORM" -type f | wc -l | tr -d ' ') files)"
  else
    echo "GOLDEN OUTPUT DIFFERS ($(wc -l <"$OUT/diff.txt" | tr -d ' ') entries, see $OUT/diff.txt):"; head -30 "$OUT/diff.txt"; exit 1
  fi
fi
