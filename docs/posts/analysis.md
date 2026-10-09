# Does splitting long files save an AI agent tokens? A controlled experiment, and a comparison with "The Economic Benefit of Refactoring"

*Draft, 2026-10-09. Numbers are from 169 of the planned 240 runs (7 to 8 runs per task and variant); they will be refreshed when the series completes. The experiment repository, with the harness, the tasks and every run's record, is `sokrates-size-experiment`.*

## The question

In July 2026 Giles Edwards-Alexander published [The Economic Benefit of Refactoring](https://martinfowler.com/articles/exploring-gen-ai/refactoring-economic-benefit.html) on martinfowler.com. The claim: refactoring an agent-built codebase lowers the token cost of later changes, so refactoring is spending tokens now to spend fewer later. The evidence: one Rust application of about 150k lines, written by agents, whose data access layer had grown into a single 17,155-line file. After a 13-step refactoring plan that brought the largest file down to 3,695 lines, the same change request, run once after each step in a fresh agent, went from 159,564 input tokens to 27,360, a drop of 83%. Output tokens rose 24% and time per change rose 33%.

Sokrates makes a related assumption in its "AI ease of change" score and its AI Cost Estimator: long files and long units make every change more expensive for an agent, because the agent has to read more to make the same change. I wanted to know whether that holds when measured with repeats, controls and acceptance tests, on a mature human-written codebase. So I ran the experiment on Sokrates itself.

## The setup

**Two variants of one codebase.** Variant A is Sokrates as it was on 2025-09-20: 489 main Java files, 48,137 lines of code, with 14 files over 500 LOC (the largest 1,587 LOC, 1,836 physical lines) and 60 units over 50 LOC or McCabe 25. Variant B is A with every main file brought under 500 LOC and every unit under 50 LOC and McCabe 25, and nothing else changed: only *extract method* and *extract class*, 52 new package-private helper classes in the original packages, every original class keeping its name and public API and delegating. The split was done in five parallel batches by coding agents under a strict brief, with the module tests and a golden-output check after every file: the reports both variants generate for a fixture of two repositories and a landscape are byte-identical across 850 normalized files. B has 2.6% more LOC than A, from constructors and delegations.

Sokrates' own predictions moved the way the model expects: Human ease of change 6.4 (C) to 7.2 (B), AI ease of change 6.6 (B) to 7.7 (B), predicted context lines per change 1,690 to 774.

**Twelve tasks with acceptance tests.** The tasks are real changes made to this code in the year after the snapshot, mined from the git history and re-derived against the snapshot. Each is a behaviour description (what should happen, not where), with a JUnit acceptance test verified to fail on each untouched variant and to pass with a reference patch, on A and on B. Ten *target* tasks land in files that were split. Two *control* tasks land in files that are byte-identical in A and B, so they should show no difference; if they do, the difference is noise or bias, not length.

**The runs.** Claude Code headless (`claude -p`, Sonnet), the same prompt, tools, permissions and turn limit for every run, in a fresh one-commit copy of the variant with a neutral folder name, so the agent sees nothing about variants or the split. A hook logs every tool call: file reads with line counts, shell commands and their output size, edits, test runs. Token counts are the CLI's own, with input split into fresh input, cache writes and cache reads. Ten repeats per task and variant are planned; the tables below have seven to eight.

## The result so far

Pooled over the ten target tasks, the geometric mean of the per-task B/A ratios, with a 95% bootstrap interval:

| measure | target tasks (split files) | control tasks (identical files) |
|---|---|---|
| input tokens incl. cache reads | 1.02 (0.96–1.08) | 1.07 (0.82–1.36) |
| output tokens | 1.00 (0.96–1.05) | 1.03 (0.80–1.30) |
| cost | 1.01 (0.98–1.05) | 1.02 (0.83–1.24) |
| turns | 1.03 (0.98–1.07) | 1.07 (0.89–1.27) |
| lines seen (Read tool plus shell output) | 0.99 (0.89–1.10) | 1.01 (0.71–1.38) |
| wall time | 1.02 (0.96–1.08) | 1.06 (0.86–1.29) |

All 169 runs passed their acceptance test, in both variants.

**The split changed nothing measurable.** The target tasks cost the same in B as in A, with an interval of plus or minus six percent. The two controls, on identical code, scatter more than that.

Per task, input tokens (median of 7 to 8 runs each):

| task | kind | A | B | B/A |
|---|---|---|---|---|
| c01 template literals in unit duplication | control | 485k | 362k | 0.75 |
| c02 component duplication metrics | control | 244k | 300k | 1.23 |
| t01 undefined team keeps active contributors | target | 179k | 209k | 1.16 |
| t02 per-extension metric names | target | 631k | 547k | 0.87 |
| t03 scoping conventions that never matched | target | 356k | 286k | 0.80 |
| t04 mock folder is test code | target | 238k | 249k | 1.04 |
| t05 missing sub-landscape config | target | 203k | 175k | 0.86 |
| t06 all contributors exported | target | 227k | 185k | 0.81 |
| t07 past-90-days block | target | 152k | 147k | 0.97 |
| t08 scope file lists named after aspects | target | 241k | 313k | 1.30 |
| t09 analyzeLandscape command | target | 312k | 261k | 0.84 |
| t10 group dependency NPE | target | 176k | 252k | 1.43 |

The per-task ratios run from 0.80 to 1.43 on the targets and from 0.75 to 1.23 on the controls. The same task on the same variant varies by 18% between runs, up to 56%. Nothing on the target side stands outside what the controls do by chance.

Two tasks lean the way the theory predicts, in opposite directions. t10's fix touches two places that are one file in A and two helper classes in B, and the agent spends a few more turns finding the second one. t09's fix is in the CLI class, which shrank from 744 to 406 LOC, and the agent reads half as many lines there. Both are inside the noise band.

## Why length did not matter here

The tool logs explain it. In 169 runs the agent used the Read tool 95 times and the shell 1,229 times. It does not read files; it greps for the names in the task, then prints a window of 40 to 70 lines around the hit with `sed -n` or a bounded Read. The 1,133-LOC results class costs it the same window as the 229-LOC helper that replaced it. The median run sees 147 lines of code.

Where do the 290k input tokens of a median run go, then? Into the conversation re-sent on every turn. A run is 13 turns on median, and each turn re-reads the whole context as cache reads. The bill is set by the number of turns, which is set by how many greps, edits and test runs the task needs, and the split neither adds nor removes those on average. Maven's build output is the other big consumer, the same in both variants.

## Comparison with the martinfowler.com experiment

The two experiments agree on the mechanism and disagree on the result, and the disagreement is informative.

**Agreement on the mechanism.** Edwards-Alexander attributes his 83% drop to the agent reading less code, not to there being less code, and argues that "randomly cutting the file into smaller files is unlikely to help as much." Our logs say the same thing from the other side: what the agent reads is decided by how it searches, not by how long the file is. Length only costs when it forces the agent to read more.

**Why his agent read more and ours did not.** The difference in starting points is a factor of ten. His largest file was 17,155 lines; ours was 1,836. Claude Code's Read tool returns up to 2,000 lines at a time, and a grep in a 17,000-line file returns many hits, so an agent working on his file had to page through it or read large chunks blind. In ours, every file fit in one read and a grep landed on one or two hits. If the cost of length is a step that starts around the agent's reading window rather than a slope that starts at zero, both experiments are consistent: he was above the step, we were below it. That is a hypothesis, not a finding, and it is the one to test next.

**Design differences that matter for reading his numbers.**

- **Repeats.** He ran the task once after each refactoring step; we ran each cell seven to eight times. Our within-cell variation is 18% typically and 56% at worst, and his table has three pairs of consecutive steps with identical figures in every column, which suggests some steps were not re-measured. An 83% drop is far larger than our noise, so the headline survives, but the step-by-step curve should not be read as more than a sketch.
- **Token accounting.** He approximated tokens as characters divided by four, reported by the agent itself, because the CLI gave no reliable live count. We used the CLI's own counts, split by cache state. Cache reads were the bulk of our input tokens; a character count of what the agent said it read would have missed them, so his input figure and ours do not measure the same thing.
- **Output tokens and time.** In his data output rose 24% and time 33% after refactoring, which he puts down to noise. In ours output tokens, turns and time all track input within a few percent, in both variants. Output tokens cost five times input, so a saving in input with a rise in output is a smaller economic saving than the input figure suggests.
- **Controls and acceptance.** He discarded each result without checking it; we kept every diff and ran an acceptance test, and every run passed. Control tasks on identical code are what let us say the target ratios are noise rather than effect.
- **The code.** His was greenfield, written by agents, never reviewed, one developer. Ours is years of human-written Java with a test suite and names that a grep finds. Agent-written code at scale may be exactly the kind that forces an agent to read rather than search.

**The economics.** He estimates 39.7 cents saved per change against an upper bound of five million tokens spent refactoring, so roughly 38 changes of that kind to break even on input pricing. Our split cost about 1.5 million tokens of agent work across the five batches and saves nothing per change, so it never breaks even on token cost. The split may still be worth doing for people, which is what the Human ease-of-change score is about, but that is a different argument.

## What this means for Sokrates

For an agent that searches by name and reads windows, file and unit length below roughly the agent's reading window is not a token cost driver, and the AI ease-of-change score weights it too heavily. The sub-scores that would survive this experiment are the ones about how many places a change touches and how findable they are by name: the change-entropy and context-per-change measures, if the latter is computed from what an agent actually reads rather than from file sizes. The AI Cost Estimator's "read size" of a change should be the window around the edit, not the file, until the file exceeds what a single read returns.

## Limits and next steps

One agent and one model, one Java codebase, tasks that name their feature in words a grep finds, and a mechanical split. The original layout is public and plausibly in the model's training data, which would favour A, but with ratios at 1.0 it is not hiding an effect. An agent that reads files whole, or tasks that require understanding a class end to end, could show the cost; this experiment did not create that situation.

The next experiment is the one the comparison points at: the same harness on a codebase with a file far above the reading window, a few thousand to twenty thousand lines, split to under the window. If the cost of length is a step, that is where it will show, and Sokrates' thresholds for agents should move to where the step is rather than where the human thresholds are.

## Method summary

- Snapshot: Sokrates at commit `5e6aa97a` (2025-09-20). Analysis reference date 2025-09-20 for both variants.
- Variant B: file ≤ 500 LOC, unit ≤ 50 LOC and McCabe ≤ 25 as measured by Sokrates; 0 files and 0 units above after the split; module tests green; golden output identical. One recorded deviation: 141 trivial accessors of one class written one per line.
- Tasks: mined with `scripts/mine_tasks.py` from 533 commits after the snapshot; each with `task.md`, `meta.json`, an acceptance test and a reference patch per variant; re-verified by `scripts/check_tasks.py`.
- Runs: `harness/run.py`, one worker per variant with its own Maven repository, Claude Code 2.1.295 with `claude-sonnet-5-5`, `--max-turns 80`, `--permission-mode acceptEdits`, a fixed allow-list of shell commands, `--setting-sources project`. Per run: fresh copy, prebuild, agent, tool log, diff, acceptance test with upstream modules built from the working tree.
- Analysis: `harness/compare.py` (pooled ratios, bootstrap) and `harness/summarize.py` (per-task quartiles).
