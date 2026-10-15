#!/usr/bin/env bash
# One claude -p session per refactoring, in the proposal's order; after each, the driver re-runs the full check
# itself and reverts the step if it does not pass.
set -u
cd "$(dirname "$0")"; mkdir -p logs
TOOLS="Read,Edit,Write,MultiEdit,Grep,Glob,Agent,Bash(./check.sh:*),Bash(mvn:*),Bash(cd:*),Bash(git -C code:*),Bash(git add:*),Bash(git commit:*),Bash(git diff:*),Bash(git status:*),Bash(git log:*),Bash(git show:*),Bash(ls:*),Bash(grep:*),Bash(rg:*),Bash(find:*),Bash(cat:*),Bash(head:*),Bash(tail:*),Bash(sed -n:*),Bash(wc:*),Bash(diff:*),Bash(sort:*),Bash(uniq:*)"
for R in R7 R1 R2 R6 R4 R5 R3 R9 R8 R10; do
  before=$(git -C code rev-parse HEAD)
  echo "=== $R start $(date +%H:%M:%S)"
  env -u CLAUDECODE claude -p "$(cat IMPLEMENT.md)

The refactoring to implement now: **$R**." --model claude-opus-5-5 --output-format json --max-turns 300 \
    --permission-mode acceptEdits --setting-sources project --allowedTools "$TOOLS" > "logs/$R.json" 2> "logs/$R.stderr"
  python3 -c "import json;r=json.load(open('logs/$R.json'));print('  agent: turns',r.get('num_turns'),'cost',round(r.get('total_cost_usd') or 0,2),'error',r.get('is_error'))" 2>/dev/null || echo "  agent: no result json"
  after=$(git -C code rev-parse HEAD)
  git -C code status --short | head -3 > "logs/$R.dirty"
  if [ "$after" = "$before" ]; then echo "  no commit"; git -C code reset -q --hard "$before"; git -C code clean -qfd; continue; fi
  if ./check.sh > "logs/$R.check" 2>&1; then
    echo "  driver check: PASSED; $(git -C code show --shortstat --format=%s HEAD | tr '\n' ' ')"
  else
    echo "  driver check: FAILED, reverting to $before"; tail -3 "logs/$R.check"
    git -C code reset -q --hard "$before"; git -C code clean -qfd
  fi
done
echo "=== done $(date +%H:%M:%S)"
