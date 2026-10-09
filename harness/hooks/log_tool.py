#!/usr/bin/env python3
"""Claude Code PostToolUse hook: append one JSON line per tool call to the log file given as argv[1].

Records the tool name, the file and the number of lines for Read (from the tool response when it says,
else counted on disk from offset/limit), the pattern for Grep/Glob, the command for Bash, the file for
edits, and the size of the tool response in characters (a proxy for the tokens the result cost).
Never blocks the agent: every failure is swallowed and the exit code is always 0.
"""
import json
import sys
import time


def read_lines(inp, resp):
    lines = None
    total = None
    if isinstance(resp, dict):
        f = resp.get("file") if isinstance(resp.get("file"), dict) else resp
        lines = f.get("numLines")
        total = f.get("totalLines")
        if lines is None and isinstance(f.get("content"), str):
            lines = f["content"].count("\n") + 1
    if lines is None and inp.get("file_path"):
        try:
            with open(inp["file_path"], encoding="utf-8", errors="replace") as fh:
                total = sum(1 for _ in fh)
            offset = int(inp.get("offset") or 0)
            limit = inp.get("limit")
            lines = max(0, total - offset) if limit is None else max(0, min(int(limit), total - offset))
        except (OSError, ValueError):
            pass
    return lines, total


def main():
    try:
        log = sys.argv[1]
        data = json.load(sys.stdin)
        name = data.get("tool_name", "")
        inp = data.get("tool_input") or {}
        resp = data.get("tool_response")
        entry = {"t": round(time.time(), 3), "tool": name}
        if name == "Read":
            entry["file"] = inp.get("file_path")
            entry["offset"] = inp.get("offset")
            entry["limit"] = inp.get("limit")
            entry["lines"], entry["total_lines"] = read_lines(inp, resp)
        elif name in ("Grep", "Glob"):
            entry["pattern"] = inp.get("pattern")
            entry["path"] = inp.get("path")
        elif name == "Bash":
            entry["command"] = (inp.get("command") or "")[:500]
            out = resp.get("stdout") if isinstance(resp, dict) else resp if isinstance(resp, str) else ""
            entry["lines"] = (out or "").count("\n") + (1 if out and not out.endswith("\n") else 0)
        elif name in ("Edit", "Write", "MultiEdit", "NotebookEdit"):
            entry["file"] = inp.get("file_path") or inp.get("notebook_path")
        else:
            entry["input"] = json.dumps(inp)[:300]
        try:
            entry["response_chars"] = len(resp) if isinstance(resp, str) else len(json.dumps(resp)) if resp is not None else 0
        except (TypeError, ValueError):
            entry["response_chars"] = 0
        with open(log, "a") as f:
            f.write(json.dumps(entry) + "\n")
    except Exception:
        pass
    sys.exit(0)


if __name__ == "__main__":
    main()
