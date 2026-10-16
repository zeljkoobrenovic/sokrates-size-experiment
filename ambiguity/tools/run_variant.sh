#!/usr/bin/env bash
# Build one blind variant (D, D-names or R) of the ambiguity experiment: a claude -p session (Opus 5.5) on BRIEF.md in
# an isolated folder (code/ as a git repository, check.sh, reference/baseline.jar). Up to two continuation sessions if
# the work is not committed; then the driver re-runs check.sh itself and reverts to the baseline if it fails.
#   run_variant.sh <folder>
set -u
F="$(cd "${1:?folder}" && pwd)"; cd "$F"; mkdir -p logs
TOOLS="Read,Edit,Write,MultiEdit,Grep,Glob,Agent,Bash(./check.sh:*),Bash(../check.sh:*),Bash(bash ./check.sh:*),Bash($F/check.sh:*),Bash(mvn:*),Bash(cd:*),Bash(git -C code:*),Bash(git add:*),Bash(git commit:*),Bash(git diff:*),Bash(git status:*),Bash(git log:*),Bash(git show:*),Bash(ls:*),Bash(grep:*),Bash(rg:*),Bash(find:*),Bash(cat:*),Bash(head:*),Bash(tail:*),Bash(sed -n:*),Bash(wc:*),Bash(diff:*),Bash(sort:*),Bash(uniq:*),Bash(python3:*)"
base=$(git -C code rev-parse HEAD)
session() { # <n> <prompt>
  env -u CLAUDECODE claude -p "$2" --model claude-opus-5-5 --output-format json --max-turns 400 \
    --permission-mode acceptEdits --setting-sources project --allowedTools "$TOOLS" > "logs/session$1.json" 2> "logs/session$1.stderr"
  python3 -c "import json;r=json.load(open('logs/session$1.json'));print('  session $1: turns',r.get('num_turns'),'cost',round(r.get('total_cost_usd') or 0,2),'error',r.get('is_error'))" 2>/dev/null || echo "  session $1: no result json"
}
echo "=== start $(date +%H:%M:%S)"
session 1 "$(cat BRIEF.md)"
for n in 2 3; do
  [ "$(git -C code rev-parse HEAD)" != "$base" ] && [ -z "$(git -C code status --porcelain)" ] && break
  session $n "$(cat BRIEF.md)

You have already started on this in an earlier session: the working tree of code/ holds that work in progress (see
\`git -C code status\` and \`git -C code diff\`, and CHANGES.md or renames.tsv if they exist). Continue from there:
finish the work, make ./check.sh pass, write CHANGES.md and renames.tsv, and commit."
done
if [ "$(git -C code rev-parse HEAD)" = "$base" ]; then echo "  no commit: reverting"; git -C code reset -q --hard "$base"; git -C code clean -qfd; exit 1; fi
if ./check.sh > logs/driver.check 2>&1; then
  echo "  driver check: PASSED; $(git -C code diff --shortstat "$base" HEAD)"
else
  echo "  driver check: FAILED ($(tail -1 logs/driver.check)), reverting"; git -C code reset -q --hard "$base"; git -C code clean -qfd; exit 1
fi
echo "=== done $(date +%H:%M:%S)"
