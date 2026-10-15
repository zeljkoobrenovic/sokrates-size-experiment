#!/usr/bin/env bash
# Runs the acceptance test against a scratch copy of code/ (code/ itself is not changed). Prints FAILING or PASSING.
set -u
HERE="$(cd "$(dirname "$0")" && pwd)"; W="$HERE/.accept"
rm -rf "$W"; rsync -a --exclude target --exclude .git "$HERE/code/" "$W/"; cp -R "$HERE/acceptance/." "$W/"
cd "$W" && mvn -q -o -pl codeanalyzer -am test -Dtest=UndefinedTeamActiveContributorsTest -Dsurefire.failIfNoSpecifiedTests=false > "$HERE/.accept.log" 2>&1; rc=$?
if [ $rc -eq 0 ]; then echo "acceptance test PASSING (the change is made: not allowed)"; exit 1
elif grep -q "COMPILATION ERROR\|Compilation failure" "$HERE/.accept.log"; then echo "acceptance test does not COMPILE (see .accept.log): not allowed"; exit 1
else echo "acceptance test FAILING (as it must)"; fi
