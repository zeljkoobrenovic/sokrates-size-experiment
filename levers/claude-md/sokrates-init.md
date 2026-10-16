# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Overview

Sokrates is a source-code analysis tool (a structured, regex-driven "code spelunking" tool) that produces HTML/JSON reports and has an interactive JavaFX explorer. Java, multi-module Maven build (groupId `nl.obren.sokrates`, version 1.0). Tests use JUnit 5 (Jupiter).

## Build and test

```
mvn clean install                      # builds all modules, runs tests
mvn clean install -DskipTests          # what the Dockerfile does
mvn test -pl codeanalyzer              # tests for one module
mvn test -pl codeanalyzer -Dtest=SourceFileTest             # single test class
mvn test -pl codeanalyzer -Dtest=SourceFileTest#methodName  # single test method
```

Outputs (fat jars from maven-assembly-plugin):
- `cli/target/cli-1.0-jar-with-dependencies.jar` — main class `nl.obren.sokrates.cli.CommandLineInterface`
- `codeexplorer/target/codeexplorer-1.0-jar-with-dependencies.jar` — main class `nl.obren.sokrates.codeexplorer.CodeExplorerLauncher`

Notes:
- The pom targets Java 1.8 source/target, but the Dockerfile builds with JDK 11 and buildspec uses Corretto 11. The explorer needs JavaFX (pulled in as Maven dependencies).
- Graphviz (`dot`) is needed at runtime for dependency/diagram rendering (`GRAPHVIZ_DOT` env var in Docker; `-internalGraphviz` CLI option exists).
- Docker: `docker build -t sokrates .` then `docker run -v "$(pwd):/code" -w /code sokrates init`.

## CLI usage

Commands are defined in `cli/.../Commands.java` (name, description, and argument constants) and dispatched from `CommandLineInterface.java`. Main commands: `init`, `generateReports`, `updateConfig`, `updateLandscape`, `createConventionsFile`, `exportStandardConventions`, `extractGitHistory`, `extractGitSubHistory`, `extractFiles`. Typical flow: `init` creates an analysis config JSON (default under `_sokrates/`), then `generateReports` consumes it. Add a new command by adding constants/options in `Commands` and handling it in `CommandLineInterface`.

## Architecture

Module dependency chain (see root `pom.xml` `<modules>` order): `common` → `codeanalyzer` → `reports` → `cli`; `codeexplorer` depends on `common`, `codeanalyzer`, `reports`, and `cli`.

- **common** — shared utilities: JSON (de)serialization (`JsonMapper`/`JsonGenerator`), Graphviz helpers, rendering utilities and chart builders (`renderingutils/`), `UserProperties`.
- **codeanalyzer** (package `nl.obren.sokrates.sourcecode`) — the analysis engine, with no UI. Key pieces:
  - `core/CodeConfiguration` / `AnalysisConfig` — the JSON analysis configuration (source files scoping, logical decompositions, concerns, goals/controls, etc.) that drives everything.
  - `analysis/CodeAnalyzer` orchestrates the per-aspect analyzers in `analysis/files/` (basics, file size, units, duplication, dependencies, concerns, contributors, file history, controls, logical decomposition) and fills the `analysis/results/*` model classes (`CodeAnalysisResults` is the top-level result).
  - `aspects/` — the "aspect" model: logical decompositions/components, concerns, dependency finders, meta-rules. `scoping/`, `cleaners/`, `lang/`, `units/`, `duplication/`, `dependencies/`, `githistory/`/`filehistory/`, `landscape/`, `search/` are the supporting domains.
- **reports** (package `nl.obren.sokrates.reports`) — turns `CodeAnalysisResults` into HTML reports/visualizations and landscape (multi-project aggregate) reports. Large resource tree (`src/main/resources`: `templates`, `style`, `icons`, `lang`, `prompts`) holds the HTML templates and static assets embedded in the output.
- **cli** — thin command-line front end plus git history extraction (`cli/git/GitHistoryExtractor`, uses JGit).
- **codeexplorer** — JavaFX desktop GUI (code browser, search, dependencies, duplicates, units, config editor) that reuses the analyzer and report modules.

Notable convention: analysis behavior is configuration-driven; changing what gets analyzed usually means changing the config model classes in `core/` / `aspects/` and the corresponding analyzer, then the result class and the report renderer that displays it.

## Misc

- `.ignore` / `.gitignore` exclude `target/`, IDE files, and `/_sokrates/reports`.
- Tests live under `src/test/java` in `codeanalyzer` and `codeexplorer`; some `codeexplorer` tests are in package `nl.obren.sokrates.reports.utils`.
