#!/usr/bin/env bash
# Compare the CLI's output of two jars for every command, with and without -help, plus edge cases.
A="$1"; B="$2"; CMDS="$3"
W=$(mktemp -d); cd "$W"
norm() { sed -E 's/[A-Z][a-z]{2} [0-9]{1,2}, [0-9]{4} [0-9:]+ [AP]M//; s/[0-9]+ms//g; s/\(([A-Za-z]+)\.java:[0-9]+\)/(\1.java)/; s/[0-9]{4}-[A-Za-z]{3}-[0-9]{2} [0-9:]{8}//'; }
n=0; d=0
for c in "" help foo INIT Init $(cat "$CMDS"); do
  for h in "" "-help"; do
    ra=$(SOKRATES_OFFLINE=1 java -jar "$A" $c $h 2>&1 | norm; echo "rc=${PIPESTATUS[0]}")
    rb=$(SOKRATES_OFFLINE=1 java -jar "$B" $c $h 2>&1 | norm; echo "rc=${PIPESTATUS[0]}")
    n=$((n+1))
    if [ "$ra" != "$rb" ]; then d=$((d+1)); echo "DIFF: '$c $h'"; diff <(echo "$ra") <(echo "$rb") | head -6; fi
  done
done
echo "$n invocations, $d differ (timestamps, timings and stack line numbers normalized)"
rm -rf "$W"
