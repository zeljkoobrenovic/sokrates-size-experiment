#!/usr/bin/env bash
# Pilot of arm p: per repeat, each target task on A (worker a) or on its own p-tNN variant (worker p).
cd "$(dirname "$0")/.."
W="$1"
for rep in 1 2 3; do
  for t in $(ls tasks | grep '^t'); do
    id=${t%%-*}; v=$([ "$W" = a ] && echo a || echo p-$id)
    python3 harness/run.py --tasks "$t" --variants "$v" --repeats 1 --first-repeat $rep --results pilot-p.csv --m2 work/m2-pilot-$W
  done
done
echo "worker $W done"
