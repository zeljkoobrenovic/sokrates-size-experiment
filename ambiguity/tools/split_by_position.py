#!/usr/bin/env python3
"""Variant S of the ambiguity experiment: cut DataStore.java by position into parts of about N lines, without touching
names or code.

Every public instance method of DataStore moves, in its original order, into package-private classes
DataStorePart01, DataStorePart02, … (a new part starts when the current one reaches the target length); everything
else (fields, constructors, static members, nested types, non-public methods) stays in DataStore. DataStore keeps
every public signature and delegates each moved method to its part. Inside a part, calls to DataStore methods that
are not in the same part, and uses of DataStore's instance fields, are rewritten to go through `store.` (fields and
non-public methods used that way become package-private). Nested types stay where they are and are imported into the
parts. Method bodies are otherwise copied byte for byte.

Usage: split_by_position.py <codebase root> [--lines 1000]
"""
import argparse
import os
import re

PKG_DIR = "src/main/java/com/acme/backoffice"
METHOD_HEAD = re.compile(r"^\s*((?:@\w+(?:\([^)]*\))?\s+)*)((?:(?:public|protected|private|static|final|synchronized|abstract)\s+)*)"
                         r"(<[^>]+>\s+)?([\w<>\[\],.? ]+?)\s+(\w+)\s*\(")


def members(lines, body_start, body_end):
    """Split the class body (line indexes) into member chunks: (first line, last line, kind, header info)."""
    out, i, pending = [], body_start, None
    while i < body_end:
        s = lines[i].strip()
        if not s:
            i += 1
            continue
        start = pending if pending is not None else i
        if s.startswith(("/**", "/*", "//", "*", "@")) and not METHOD_HEAD.match(lines[i]):
            if s.startswith("/*") and "*/" not in s:  # block comment: take it whole
                while "*/" not in lines[i]:
                    i += 1
            pending = start
            i += 1
            continue
        pending = None
        # a member: read until its braces close (methods, types, initializers) or until ';' at depth 0 (fields)
        depth, j, seen_brace = 0, i, False
        while j < body_end:
            code = re.sub(r'"(?:[^"\\]|\\.)*"', '""', lines[j])
            code = re.sub(r"//.*", "", code)
            depth += code.count("{") - code.count("}")
            seen_brace |= "{" in code
            if (seen_brace and depth == 0) or (not seen_brace and code.rstrip().endswith(";") and depth == 0):
                break
            j += 1
        header = " ".join(l.strip() for l in lines[i:j + 1])
        header = header[:header.index("{")] if "{" in header else header
        m = METHOD_HEAD.match(lines[i]) if "(" in header and not re.match(r"^\s*(?:\w+\s+)*(?:class|record|enum|interface)\s", lines[i]) else None
        if m and "=" not in header.split("(")[0]:
            mods, name = m.group(2), m.group(5)
            kind = "ctor" if m.group(4).strip().split()[-1] == "DataStore" and name == "DataStore" else "method"
            if re.match(r"^\s*(?:public\s+)?DataStore\s*\(", lines[i]):
                kind = "ctor"
            out.append((start, j, kind, {"mods": mods, "name": name, "header": header.strip()}))
        elif re.search(r"\b(class|record|enum|interface)\b", header.split("(")[0]):
            out.append((start, j, "type", {"header": header.strip()}))
        else:
            out.append((start, j, "field", {"header": header.strip()}))
        i = j + 1
    return out


def split_params(params):
    out, depth, cur = [], 0, ""
    for ch in params:
        if ch in "<([":
            depth += 1
        elif ch in ">)]":
            depth -= 1
        if ch == "," and depth == 0:
            out.append(cur); cur = ""
        else:
            cur += ch
    if cur.strip():
        out.append(cur)
    return [p.strip() for p in out]


def main():
    ap = argparse.ArgumentParser()
    ap.add_argument("root")
    ap.add_argument("--lines", type=int, default=1000)
    args = ap.parse_args()
    path = os.path.join(args.root, PKG_DIR, "DataStore.java")
    lines = open(path, encoding="utf-8").read().split("\n")
    cls = next(i for i, l in enumerate(lines) if re.match(r"^public class DataStore\b", l))
    end = max(i for i, l in enumerate(lines) if l.strip() == "}")
    head = lines[:cls]
    ms = members(lines, cls + 1, end)
    movable = [m for m in ms if m[2] == "method" and "public" in m[3]["mods"] and "static" not in m[3]["mods"]]
    staying = [m for m in ms if m not in movable]
    method_names = {m[3]["name"] for m in ms if m[2] == "method"}
    instance_fields = set()
    for m in staying:
        if m[2] == "field" and "static" not in m[3]["header"]:
            fm = re.search(r"(\w+)\s*(?:=|;)", m[3]["header"])
            if fm:
                instance_fields.add(fm.group(1))
    type_names = {re.search(r"\b(?:class|record|enum|interface)\s+(\w+)", m[3]["header"]).group(1) for m in staying if m[2] == "type"}
    # parts by position
    parts, cur, cur_len = [], [], 0
    for m in movable:
        n = m[1] - m[0] + 1
        if cur and cur_len + n > args.lines:
            parts.append(cur); cur, cur_len = [], 0
        cur.append(m); cur_len += n
    if cur:
        parts.append(cur)
    widened = set()
    has_statics = any("static" in m[3]["header"].split("(")[0].split("=")[0] for m in staying if m[2] in ("field", "method"))
    pkg_line = next(l for l in head if l.startswith("package "))
    imports = [l for l in head if l.startswith("import ")]
    for k, part in enumerate(parts, 1):
        own = {m[3]["name"] for m in part}
        body = []
        for s, e, _, info in part:
            text = "\n".join(lines[s:e + 1])
            # rewrite unqualified calls to DataStore methods outside this part, and DataStore's instance fields
            for name in method_names - own:
                text2 = re.sub(r"\bthis::" + name + r"\b", "store::" + name, text)  # method references
                if text2 != text:
                    widened.add(name); text = text2
                new = re.sub(r"(?<![\w.:])" + name + r"\s*\(", "store." + name + "(", text)
                if new != text:
                    widened.add(name); text = new
            for f in instance_fields - {"connection"}:
                new = re.sub(r"(?<![\w.])(?:this\.)?" + f + r"\b", "store." + f, text)
                if new != text:
                    widened.add(f); text = new
            body.append(text)
        cname = f"DataStorePart{k:02d}"
        src = [pkg_line, ""] + imports + ([f"import com.acme.backoffice.DataStore.*;"] if type_names else []) + (["import static com.acme.backoffice.DataStore.*;"] if has_statics else []) + [
            "", f"class {cname} {{", "    private final DataStore store;", "    private final java.sql.Connection connection;", "",
            f"    {cname}(DataStore store, java.sql.Connection connection) {{", "        this.store = store;",
            "        this.connection = connection;", "    }", ""] + ["\n\n".join(body), "}", ""]
        open(os.path.join(args.root, PKG_DIR, cname + ".java"), "w", encoding="utf-8").write("\n".join(src))
    # the new DataStore: staying members (widened where parts use them), lazy parts, one delegate per moved method
    out = head + [lines[cls]]
    part_text = "\n".join("\n".join(lines[s:e + 1]) for part in parts for s, e, _, _ in part)
    for s, e, kind, info in staying:
        text = "\n".join(lines[s:e + 1])
        if kind == "type":  # a nested type the parts use: package-private, and so are its private members
            tn = re.search(r"\b(?:class|record|enum|interface)\s+(\w+)", info["header"]).group(1)
            if re.search(r"\b" + tn + r"\b", part_text) or any(re.search(r"\b" + tn + r"\b", "\n".join(lines[s2:e2 + 1])) for s2, e2, k2, _ in staying if k2 != "type"):
                text = re.sub(r"(?m)^(\s*)private\s+", r"\1", text)
                widened.add(tn)
        if kind in ("method", "field") and "private" in info.get("mods", info["header"].split("(")[0]):
            nm = info.get("name") or (re.search(r"(\w+)\s*(?:=|;)", info["header"]) or [None, None])[1]
            if nm and nm != "connection" and re.search(r"(?<![\w.])(?:DataStore::|store\.)?" + nm + r"\b", part_text):  # e.g. DataStore::customerRow
                widened.add(nm)
            if nm in widened:
                text = re.sub(r"\bprivate\s+", "", text, count=1)
        out.append(text)
        out.append("")
    for k in range(1, len(parts) + 1):
        out += [f"    private DataStorePart{k:02d} part{k:02d};", "",
                f"    private DataStorePart{k:02d} part{k:02d}() {{",
                f"        if (part{k:02d} == null) {{", f"            part{k:02d} = new DataStorePart{k:02d}(this, connection);",
                "        }", f"        return part{k:02d};", "    }", ""]
    for k, part in enumerate(parts, 1):
        for s, e, _, info in part:
            h = info["header"]
            sig = h[:h.rindex(")") + 1]
            throws = h[h.rindex(")") + 1:].strip()
            params = split_params(sig[sig.index("(") + 1:-1])
            names = [re.sub(r"\.\.\.$", "", p.split()[-1]) for p in params]
            ret = METHOD_HEAD.match(lines[next(i for i in range(s, e + 1) if METHOD_HEAD.match(lines[i]))]).group(4).strip()
            call = f"part{k:02d}().{info['name']}({', '.join(names)})"
            out += [f"    {sig}{(' ' + throws) if throws else ''} {{", f"        {'' if ret == 'void' else 'return '}{call};", "    }", ""]
    out += ["}", ""]
    open(path, "w", encoding="utf-8").write("\n".join(out))
    print(f"{len(movable)} methods moved into {len(parts)} parts "
          f"({', '.join(str(sum(m[1] - m[0] + 1 for m in p)) for p in parts)} lines); kept in DataStore: "
          f"{len(staying)} members; widened to package-private: {sorted(widened) or 'none'}")


if __name__ == "__main__":
    main()
