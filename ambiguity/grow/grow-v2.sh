#!/usr/bin/env bash
# Second growth (STANDING-v2.md: copy the most similar method and adapt it). Otherwise as grow.sh:
# Grow the BackOffice codebase: one claude -p session (Sonnet 5.5) per feature request in features.md, in an isolated
# git repository, with the standing instruction of STANDING.md. After each session the driver checks for itself that
# `mvn -q -o test` is green and that a commit was made; it gives one fix-up session if not, and stops if that fails.
# It logs, per feature, the length of DataStore.java, the main code size, the session's turns and cost.
#
#   ambiguity/grow/grow.sh <work folder> [first feature number] [last feature number]
set -u
HERE="$(cd "$(dirname "$0")" && pwd)"
WORK="${1:?work folder}"; FIRST="${2:-1}"; LAST="${3:-72}"
REPO="$WORK/backoffice"; LOG="$WORK/grow-log.tsv"
MODEL="claude-sonnet-5-5"
TOOLS="Read,Edit,Write,MultiEdit,Grep,Glob,Bash(mvn:*),Bash(git:*),Bash(cd:*),Bash(ls:*),Bash(cat:*),Bash(head:*),Bash(tail:*),Bash(sed -n:*),Bash(grep:*),Bash(rg:*),Bash(find:*),Bash(wc:*),Bash(mkdir:*)"
export GIT_AUTHOR_NAME="BackOffice Dev" GIT_AUTHOR_EMAIL="dev@acme.example" GIT_COMMITTER_NAME="BackOffice Dev" GIT_COMMITTER_EMAIL="dev@acme.example"

if [ ! -d "$REPO/.git" ]; then
  mkdir -p "$REPO"; cp -R "$HERE/skeleton/." "$REPO/"
  ( cd "$REPO" && git init -q -b main && git add -A && git commit -qm "Skeleton" )
  printf "feature\ttitle\tdatastore_lines\tmain_lines\tturns\tcost\tfixup\tstatus\n" > "$LOG"
fi
mkdir -p "$WORK/sessions"

feature_text() { # <number> -> the "## NN Title" section of features.md
  awk -v n="$(printf '%02d' "$1")" '/^## /{p = ($2 == n)} p' "$HERE/features.md"
}
session() { # <prompt> <log name>
  ( cd "$REPO" && env -u CLAUDECODE claude -p "$1" --model "$MODEL" --output-format json --max-turns 150 \
      --permission-mode acceptEdits --setting-sources project --allowedTools "$TOOLS" ) > "$WORK/sessions/$2.json" 2> "$WORK/sessions/$2.stderr"
  python3 -c "import json,sys;r=json.load(open(sys.argv[1]));print(r.get('num_turns',0), round(r.get('total_cost_usd') or 0,3))" "$WORK/sessions/$2.json" 2>/dev/null || echo "0 0"
}
green() { ( cd "$REPO" && mvn -q -o test > "$WORK/sessions/last-test.log" 2>&1 ); }

for n in $(seq "$FIRST" "$LAST"); do
  text="$(feature_text "$n")"; [ -n "$text" ] || { echo "no feature $n"; exit 1; }
  title="$(echo "$text" | head -1 | sed -E 's/^## [0-9]+ //')"
  nn=$(printf '%02d' "$n"); before=$(git -C "$REPO" rev-parse HEAD)
  echo "=== $nn $title ($(date +%H:%M:%S))"
  read -r turns cost <<< "$(session "$(cat "$HERE/STANDING-v2.md")

$text" "$nn")"
  fixup=0
  if ! green || [ "$(git -C "$REPO" rev-parse HEAD)" = "$before" ] || [ -n "$(git -C "$REPO" status --porcelain)" ]; then
    fixup=1
    read -r t2 c2 <<< "$(session "$(cat "$HERE/STANDING-v2.md")

Feature $nn ($title) is not finished: either \`mvn -q -o test\` fails, or the work is not committed. Finish it:
make the tests green, keeping to the conventions, then commit with the message \"Feature $nn: $title\".

$text" "$nn-fixup")"
    turns=$((turns + t2)); cost=$(python3 -c "print(round($cost + $c2, 3))")
  fi
  if green && [ "$(git -C "$REPO" rev-parse HEAD)" != "$before" ] && [ -z "$(git -C "$REPO" status --porcelain)" ]; then status=ok; else status=FAILED; fi
  ds=$(wc -l < "$REPO/src/main/java/com/acme/backoffice/DataStore.java" | tr -d ' ')
  main=$(find "$REPO/src/main/java" -name '*.java' -exec cat {} + | wc -l | tr -d ' ')
  printf "%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\n" "$nn" "$title" "$ds" "$main" "$turns" "$cost" "$fixup" "$status" >> "$LOG"
  echo "  DataStore $ds lines, main $main lines, $turns turns, \$$cost, fixup $fixup: $status"
  [ "$status" = ok ] || { echo "stopping: feature $nn failed after a fix-up session"; exit 1; }
done
echo "=== done $(date +%H:%M:%S)"
