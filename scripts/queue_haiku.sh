#!/usr/bin/env bash
# Waits until no harness worker is running, then starts the Haiku series: the same 12 tasks x 2 variants x 10 repeats,
# one worker per variant, results in results/runs-haiku.csv. Logs: results/worker-haiku-<variant>.log.
cd "$(dirname "$0")/.."
while pgrep -f 'harness/run.py' >/dev/null; do sleep 30; done
TASKS=$(ls -d tasks/*/ | xargs -n1 basename | grep -v _template | tr '\n' ' ')
for v in a b; do
  nohup python3 -I harness/run.py --tasks $TASKS --variants $v --repeats 10 --m2 work/m2-$v --model claude-haiku-5-5 --results runs-haiku.csv > results/worker-haiku-$v.log 2>&1 &
done
echo "haiku series started $(date)" >> results/queue.log
