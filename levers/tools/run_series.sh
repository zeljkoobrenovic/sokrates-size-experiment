#!/bin/sh
# The levers series for one model (plans/levers-experiment.md): one worker per (set, condition) plus one per chain mode,
# each with its own Maven repository. Usage: levers/tools/run_series.sh sonnet|haiku [repeats]
set -e
cd "$(dirname "$0")/../.."
M=$1; N=${2:-10}
case $M in sonnet) MODEL=claude-sonnet-5-5;; haiku) MODEL=claude-haiku-5-5;; *) echo "model?"; exit 1;; esac
AMB="a01-po-count-per-rep-cancelled a02-region-tickets-priority-order a03-warehouse-stock-empty-lines a11-supplier-payment-terms a12-ticket-assigned-agent a13-promotion-description"
SOK="t02-extension-metric-names t03-scoping-conventions-never-matched t09-analyzelandscape-command t04-mock-folder-is-test-code t08-scope-file-lists-named-after-aspects t01-undefined-team-active"
for set in ambiguity sokrates; do
  if [ $set = ambiguity ]; then TASKS=$AMB; SETARG="--set ambiguity"; else TASKS=$SOK; SETARG=""; fi
  for c in base hint-file hint-symbol md-init md-index md-padding; do
    case $c in
      hint-*) EXTRA="--hint ${c#hint-}";;
      md-*) EXTRA="--claude-md levers/claude-md/$set-${c#md-}.md";;
      *) EXTRA="";;
    esac
    nohup python3 -I harness/run.py $SETARG --tasks $TASKS --variants a --repeats $N $EXTRA --condition $c --model $MODEL \
      --results levers-$M.csv --m2 work/m2-levers-$set-$c > results/levers-worker-$M-$set-$c.log 2>&1 &
  done
done
for mode in fresh session; do
  nohup python3 -I harness/chain.py --set ambiguity --tasks a01 a11 a02 a12 a03 a13 --mode $mode --repeats $N --model $MODEL \
    --results levers-chains-$M.csv --m2 work/m2-levers-chain-$mode > results/levers-worker-$M-chain-$mode.log 2>&1 &
done
echo launched
