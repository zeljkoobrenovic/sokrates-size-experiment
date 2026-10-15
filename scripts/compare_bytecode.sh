#!/usr/bin/env bash
# Compare the compiled main classes of two variants with javap (code, private members, constants), after a
# build of each (mvn -o compile). A merged variant (scripts/make_variant_m.py) must differ only in the source
# file name and in the public flag of the moved classes; the script prints every other difference.
#
# Usage: scripts/compare_bytecode.sh a m3
set -euo pipefail
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
OUT="$ROOT/work/bytecode"; rm -rf "$OUT"; mkdir -p "$OUT"
for V in "$1" "$2"; do
  ( cd "$ROOT/variants/$V" && mvn -q -o compile )
  ( cd "$ROOT/variants/$V" && find . -path '*/target/classes/*' -name '*.class' | sort ) > "$OUT/$V.list"
  mkdir -p "$OUT/$V"
  while read -r c; do
    javap -c -p -constants "$ROOT/variants/$V/$c" 2>/dev/null | grep -v '^Compiled from' \
      | sed -E 's/^public (((final|abstract) )*(class|interface) )/\1/' > "$OUT/$V/$(echo "$c" | tr '/' '_').txt"
  done < "$OUT/$V.list"
done
diff "$OUT/$1.list" "$OUT/$2.list" && echo "same $(wc -l < "$OUT/$1.list") classes"
diff -r "$OUT/$1" "$OUT/$2" && echo "bytecode identical (ignoring source file names and top-level public flags)"
