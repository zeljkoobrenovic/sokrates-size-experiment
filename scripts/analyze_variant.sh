#!/usr/bin/env bash
# Regenerate the Sokrates analysis of a variant from its kept config: analysis/<variant>/config.json
# (srcRoot points at variants/<variant>; fileHistoryAnalysis.importPath points at the git-history.txt kept
# next to the config, so the variant folder itself stays clean).
#
# Usage: scripts/analyze_variant.sh <variant> [reference date, default 2025-09-20] [path to the Sokrates CLI jar]
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
V="${1:?variant (a|b)}"
DATE="${2:-2025-09-20}"
JAR="${3:-${SOKRATES_JAR:-$ROOT/../sokrates/cli/target/cli-1.0-jar-with-dependencies.jar}}"
AN="$ROOT/analysis/$V"
SRC="$ROOT/variants/$V"
[ -f "$AN/config.json" ] || { echo "no $AN/config.json — create it with: java -jar $JAR init -srcRoot $SRC -confFile $AN/config.json"; exit 1; }
SOKRATES_OFFLINE=1 java -jar "$JAR" generateReports -confFile "$AN/config.json" -outputFolder "$AN/reports" -date "$DATE" -timeout 1200
echo "reports: file://$AN/reports/index.html"
