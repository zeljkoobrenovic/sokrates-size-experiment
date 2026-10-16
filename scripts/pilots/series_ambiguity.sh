#!/usr/bin/env bash
# Targeted series of the ambiguity experiment: the kind-1 and kind-4 tasks, 10 repeats, one variant and model per worker.
#   scripts/pilots/series_ambiguity.sh <variant> <sonnet|haiku> [first repeat] [last repeat]
cd "$(dirname "$0")/../.."
V="$1"; M="$2"; FIRST="${3:-1}"; LAST="${4:-10}"
case "$M" in sonnet) MODEL=claude-sonnet-5-5 ;; haiku) MODEL=claude-haiku-5-5 ;; esac
TASKS="a01-po-count-per-rep-cancelled a02-region-tickets-priority-order a03-warehouse-stock-empty-lines a11-supplier-payment-terms a12-ticket-assigned-agent a13-promotion-description"
for rep in $(seq "$FIRST" "$LAST"); do
  for t in $TASKS; do
    python3 harness/run.py --set ambiguity --tasks "$t" --variants "$V" --repeats 1 --first-repeat $rep --model "$MODEL" \
      --results series-ambiguity-$M.csv --m2 work/m2-series-$V-$M
  done
done
echo "worker $V $M done"
