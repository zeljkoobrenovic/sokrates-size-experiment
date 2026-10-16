#!/usr/bin/env python3
"""The CLAUDE.md conditions of the levers experiment (plans/levers-experiment.md), per set (ambiguity, sokrates):
  <set>-init.md     what Claude Code's /init wrote for variant A (kept as generated)
  <set>-index.md    init + a "Code index" section from code_index.py (--budget 30000)
  <set>-padding.md  init + the inert handbook text of padding-source.md, cut at a paragraph boundary to the same
                    length as the index section, so index and padding differ in content, not size.
Usage: make_claude_md.py"""
import os, subprocess, sys
HERE = os.path.dirname(os.path.abspath(__file__)); EXP = os.path.dirname(os.path.dirname(HERE)); OUT = os.path.join(EXP, "levers", "claude-md")
ROOTS = {"ambiguity": os.path.join(EXP, "ambiguity", "variants", "a"), "sokrates": os.path.join(EXP, "variants", "a")}
for s, root in ROOTS.items():
    init = open(os.path.join(OUT, f"{s}-init.md")).read().rstrip() + "\n"
    idx = subprocess.run([sys.executable, os.path.join(HERE, "code_index.py"), root, "--budget", "30000"], capture_output=True, text=True, check=True).stdout
    section = ("\n## Code index\n\nEvery package of main code, with its classes and, in parentheses, their public methods "
               "(\"+k\" means k more not listed).\n\n" + idx)
    pad, out = open(os.path.join(OUT, "padding-source.md")).read().split("\n\n"), ""
    for para in pad:
        if len(out) + len(para) + 2 > len(section):
            break
        out += "\n" + para + "\n"
    open(os.path.join(OUT, f"{s}-index.md"), "w").write(init + section)
    open(os.path.join(OUT, f"{s}-padding.md"), "w").write(init + out)
    print(s, "init", len(init), "index section", len(section), "padding section", len(out))
