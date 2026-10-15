#!/usr/bin/env bash
# One claude -p session per task folder; afterwards the driver re-runs check.sh (must pass) and accept.sh (the
# acceptance test must still fail) itself and reverts the preparation if either does not hold.
set -u
cd "$(dirname "$0")"; ROOT="$PWD"
TOOLS="Read,Edit,Write,MultiEdit,Grep,Glob,Agent,Bash(./check.sh:*),Bash(./accept.sh:*),Bash(mvn:*),Bash(cd:*),Bash(git -C code:*),Bash(git add:*),Bash(git commit:*),Bash(git diff:*),Bash(git status:*),Bash(git log:*),Bash(git show:*),Bash(ls:*),Bash(grep:*),Bash(rg:*),Bash(find:*),Bash(cat:*),Bash(head:*),Bash(tail:*),Bash(sed -n:*),Bash(wc:*),Bash(diff:*),Bash(sort:*),Bash(uniq:*)"
for T in t01 t02 t03 t04 t05 t06 t07 t08 t09 t10; do
  D=$(ls -d "$ROOT/$T"-*); cd "$D"; before=$(git -C code rev-parse HEAD)
  echo "=== $T start $(date +%H:%M:%S)"
  env -u CLAUDECODE claude -p "$(cat "$ROOT/PREPARE.md")" --model claude-opus-5-5 --output-format json --max-turns 200 \
    --permission-mode acceptEdits --setting-sources project --allowedTools "$TOOLS" > run.json 2> run.stderr
  python3 -c "import json;r=json.load(open('run.json'));print('  agent: turns',r.get('num_turns'),'cost',round(r.get('total_cost_usd') or 0,2),'error',r.get('is_error'))" 2>/dev/null || echo "  agent: no result json"
  if [ "$(git -C code rev-parse HEAD)" = "$before" ]; then echo "  no commit (no preparation)"; git -C code reset -q --hard; git -C code clean -qfd; continue; fi
  if ./check.sh > driver.check 2>&1 && ./accept.sh > driver.accept 2>&1; then
    echo "  driver: check PASSED, acceptance FAILING; $(git -C code show --shortstat --format= HEAD | tr -d '\n')"
  else
    echo "  driver: FAILED ($(tail -1 driver.check) / $(cat driver.accept 2>/dev/null)), reverting"
    git -C code reset -q --hard "$before"; git -C code clean -qfd
  fi
done
echo "=== done $(date +%H:%M:%S)"
