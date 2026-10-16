#!/usr/bin/env bash
# Pilot of the ambiguity experiment (plans/ambiguity-experiment.md): every task on one variant, 3 repeats, Sonnet 5.5.
#   scripts/pilots/pilot_ambiguity.sh <variant> [repeats]
cd "$(dirname "$0")/../.."
V="$1"; R="${2:-3}"
for rep in $(seq 1 "$R"); do
  for t in $(ls ambiguity/tasks); do
    python3 harness/run.py --set ambiguity --tasks "$t" --variants "$V" --repeats 1 --first-repeat $rep \
      --results pilot-ambiguity.csv --m2 work/m2-amb-$V
  done
done
echo "worker $V done"
