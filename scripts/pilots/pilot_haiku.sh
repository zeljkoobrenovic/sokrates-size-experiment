#!/usr/bin/env bash
# Haiku 5.5 pilot of arms p and g: worker a = every task on A; g = every task on g; p = each target task on its p variant.
cd "$(dirname "$0")/../.."
W="$1"; ALL=$(ls tasks | grep -E '^(t|c|h)[0-9]'); TARGETS=$(ls tasks | grep '^t')
for rep in 1 2 3; do
  for t in $([ "$W" = p ] && echo "$TARGETS" || echo "$ALL"); do
    case "$W" in a) v=a ;; g) v=g ;; p) v=p-${t%%-*} ;; esac
    python3 harness/run.py --tasks "$t" --variants "$v" --repeats 1 --first-repeat $rep --model claude-haiku-5-5 \
      --results pilot-haiku.csv --m2 work/m2-haiku-$W
  done
done
echo "worker $W done"
