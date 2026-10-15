#!/usr/bin/env bash
# Pilot of arm g. Worker g: every task on g. Worker a: A runs of the tasks the p pilot does not cover.
cd "$(dirname "$0")/.."
W="$1"
if [ "$W" = g ]; then TASKS=$(ls tasks | grep -E '^(t|c|h)[0-9]'); V=g; else TASKS=$(ls tasks | grep -E '^(c|h)[0-9]'); V=a; fi
for rep in 1 2 3; do
  for t in $TASKS; do
    python3 harness/run.py --tasks "$t" --variants $V --repeats 1 --first-repeat $rep --results pilot-g.csv --m2 work/m2-pilot-g$W
  done
done
echo "worker $W done"
