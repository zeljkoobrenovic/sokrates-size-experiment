# Below the agent’s read budget, length is not a cost driver

*Splitting long files and units does not save an AI coding agent tokens: a controlled experiment, and a comparison with "The Economic Benefit of Refactoring".*

*2026-10-09. Both series are complete: 240 runs each, 10 per task and variant, no failed acceptance test. The experiment repository, with the harness, the tasks and every run's record, is `sokrates-size-experiment`; the results page is `docs/index.html`.*

## The answer first

The same twelve changes, made by the same coding agent in two versions of one codebase that differ only in the length of their files and units, cost the same input tokens, output tokens, turns, time and money. Pooled over the ten tasks that land in split files, variant B costs 1.03 times variant A in input tokens with Claude Sonnet 5.5 (95% interval 0.97 to 1.08) and 1.01 with Claude Haiku 5.5 (0.94 to 1.08); two control tasks on byte-identical code scatter more than that. For files that fit in the agent's read budget, file and unit length is not a cost driver. What the agent pays for is the number of places a change touches and the context it re-sends every turn. Sokrates' AI ease-of-change score has been changed accordingly.

## Where the claim came from

In July 2026 Giles Edwards-Alexander published [The Economic Benefit of Refactoring](https://martinfowler.com/articles/exploring-gen-ai/refactoring-economic-benefit.html) on martinfowler.com. The claim: refactoring an agent-built codebase lowers the token cost of later changes, so refactoring is spending tokens now to spend fewer later. The evidence: one Rust application of about 150k lines, written by agents, whose data access layer had grown into a single 17,155-line file. After a 13-step refactoring plan that brought the largest file down to 3,695 lines, the same change request, run once after each step in a fresh agent, went from 159,564 input tokens to 27,360, a drop of 83%. Output tokens rose 24% and time per change rose 33%.

Sokrates makes a related assumption in its "AI ease of change" score and its AI Cost Estimator: long files and long units make every change more expensive for an agent, because the agent has to read more to make the same change. I wanted to know whether that holds when measured with repeats, controls and acceptance tests, on a mature human-written codebase. So I ran the experiment on Sokrates itself.

## The setup

**Two variants of one codebase.** Variant A is Sokrates as it was on 2025-09-20: 489 main Java files, 48,137 lines of code, with 14 files over 500 LOC (the largest 1,587 LOC, 1,836 physical lines) and 60 units over 50 LOC or McCabe 25. Variant B is A with every main file brought under 500 LOC and every unit under 50 LOC and McCabe 25, and nothing else changed: only *extract method* and *extract class*, 52 new package-private helper classes in the original packages, every original class keeping its name and public API and delegating. The split was done in five parallel batches by coding agents under a strict brief, with the module tests and a golden-output check after every file: the reports both variants generate for a fixture of two repositories and a landscape are byte-identical across 850 normalized files. B has 2.6% more LOC than A, from constructors and delegations.

Sokrates' own predictions moved the way the model expects: Human ease of change 6.4 (C) to 7.2 (B), AI ease of change 6.6 (B) to 7.7 (B), predicted context lines per change 1,690 to 774.

**Twelve tasks with acceptance tests.** The tasks are real changes made to this code in the year after the snapshot, mined from the git history and re-derived against the snapshot. Each is a behaviour description (what should happen, not where), with a JUnit acceptance test verified to fail on each untouched variant and to pass with a reference patch, on A and on B. Ten *target* tasks land in files that were split. Two *control* tasks land in files that are byte-identical in A and B, so they should show no difference; if they do, the difference is noise or bias, not length.

**The runs.** Claude Code headless (`claude -p`, Sonnet), the same prompt, tools, permissions and turn limit for every run, in a fresh one-commit copy of the variant with a neutral folder name, so the agent sees nothing about variants or the split. A hook logs every tool call: file reads with line counts, shell commands and their output size, edits, test runs. Token counts are the CLI's own, with input split into fresh input, cache writes and cache reads. Ten repeats per task and variant, 240 runs in all.

## The result

Pooled over the ten target tasks, the geometric mean of the per-task B/A ratios, with a 95% bootstrap interval, Claude Sonnet 5.5:

| measure | target tasks (split files) | control tasks (identical files) |
|---|---|---|
| input tokens incl. cache reads | 1.03 (0.97–1.08) | 0.99 (0.80–1.22) |
| output tokens | 1.02 (0.97–1.06) | 0.97 (0.80–1.19) |
| cost | 1.02 (0.99–1.06) | 0.99 (0.85–1.17) |
| turns | 1.04 (0.99–1.08) | 0.99 (0.84–1.17) |
| lines seen (Read tool plus shell output) | 1.00 (0.92–1.10) | 1.06 (0.79–1.37) |
| wall time | 1.01 (0.96–1.06) | 0.97 (0.82–1.16) |

All 240 runs passed their acceptance test, in both variants. The series cost $40.89.

**The split changed nothing measurable.** The target tasks cost the same in B as in A, with an interval of minus three to plus eight percent. The two controls, on identical code, scatter more than that.

Per task, input tokens (median of 10 runs each):

| task | kind | A | B | B/A |
|---|---|---|---|---|
| c01 template literals in unit duplication | control | 485k | 385k | 0.79 |
| c02 component duplication metrics | control | 271k | 282k | 1.04 |
| t01 undefined team keeps active contributors | target | 220k | 191k | 0.87 |
| t02 per-extension metric names | target | 654k | 592k | 0.91 |
| t03 scoping conventions that never matched | target | 293k | 293k | 1.00 |
| t04 mock folder is test code | target | 247k | 282k | 1.14 |
| t05 missing sub-landscape config | target | 212k | 173k | 0.82 |
| t06 all contributors exported | target | 194k | 184k | 0.95 |
| t07 past-90-days block | target | 151k | 156k | 1.03 |
| t08 scope file lists named after aspects | target | 243k | 313k | 1.29 |
| t09 analyzeLandscape command | target | 303k | 267k | 0.88 |
| t10 group dependency NPE | target | 200k | 251k | 1.25 |

The per-task ratios run from 0.82 to 1.29 on the targets and from 0.79 to 1.04 on the controls. The same task on the same variant varies by 20% between runs, up to 51%. Nothing on the target side stands outside what the controls do by chance.

Two tasks lean the way the theory predicts, in opposite directions. t10's fix touches two places that are one file in A and two helper classes in B, and the agent spends a few more turns finding the second one. t09's fix is in the CLI class, which shrank from 744 to 406 LOC, and the agent reads half as many lines there. Both are inside the noise band.

## The same with a weaker model

The series was repeated with Claude Haiku 5.5, the weakest current model, through the same harness, tasks and
permissions: 240 runs, 10 per cell, none failed, $4.48 in total.

| measure | target tasks (split files) | control tasks (identical files) |
|---|---|---|
| input tokens incl. cache reads | 1.01 (0.94–1.08) | 0.89 (0.77–1.05) |
| output tokens | 1.00 (0.94–1.05) | 0.92 (0.82–1.03) |
| cost | 1.03 (0.94–1.11) | 0.93 (0.83–1.05) |
| turns | 0.99 (0.95–1.04) | 0.92 (0.83–1.03) |
| lines seen | 1.18 (1.06–1.30) | 1.02 (0.88–1.19) |

Haiku behaves differently from Sonnet: it reads about 500 lines of code per run against Sonnet's 150, mostly whole
files, takes 15 turns against 13, and spends 450k input tokens per run against 250k, at a tenth of the cost. That is
the reading habit under which length could matter, and tokens, turns and cost still sit at 1.0. The one measure
that moves is lines seen: in B Haiku looks at about a fifth more code, on seven of the ten target tasks, while
the controls stay at 1.0. The split spreads a feature over more files, and an agent that opens files whole opens
more of them. The extra reading does not reach the bill, because the re-sent context dominates it.

## Why length did not matter here

The tool logs explain it. In 240 runs the agent used the Read tool 122 times and the shell 1,749 times. It does not read files; it greps for the names in the task, then prints a window of 40 to 70 lines around the hit with `sed -n` or a bounded Read. The 1,133-LOC results class costs it the same window as the 229-LOC helper that replaced it. The median run sees 149 lines of code.

Where do the 290k input tokens of a mean run go, then? Into the conversation re-sent on every turn. A run is 13 turns on median, and each turn re-reads the whole context as cache reads. The bill is set by the number of turns, which is set by how many greps, edits and test runs the task needs, and the split neither adds nor removes those on average. Maven's build output is the other big consumer, the same in both variants.

## Comparison with the martinfowler.com experiment

The two experiments agree on the mechanism and disagree on the result, and the disagreement is informative.

**Agreement on the mechanism.** Edwards-Alexander attributes his 83% drop to the agent reading less code, not to there being less code, and argues that "randomly cutting the file into smaller files is unlikely to help as much." Our logs say the same thing from the other side: what the agent reads is decided by how it searches, not by how long the file is. Length only costs when it forces the agent to read more.

**Why his agent read more and ours did not.** The difference in starting points is a factor of ten. His largest file was 17,155 lines; ours was 1,836. Claude Code's Read tool returns up to 2,000 lines at a time, and a grep in a 17,000-line file returns many hits, so an agent working on his file had to page through it or read large chunks blind. In ours, every file fit in one read and a grep landed on one or two hits. If the cost of length is a step that starts around the agent's reading window rather than a slope that starts at zero, both experiments are consistent: he was above the step, we were below it. That is a hypothesis, not a finding, and it is the one to test next.

**Design differences that matter for reading his numbers.**

- **Repeats.** He ran the task once after each refactoring step; we ran each cell seven to eight times. Our within-cell variation is 20% typically and 51% at worst, and his table has three pairs of consecutive steps with identical figures in every column, which suggests some steps were not re-measured. An 83% drop is far larger than our noise, so the headline survives, but the step-by-step curve should not be read as more than a sketch.
- **Token accounting.** He approximated tokens as characters divided by four, reported by the agent itself, because the CLI gave no reliable live count. We used the CLI's own counts, split by cache state. Cache reads were the bulk of our input tokens; a character count of what the agent said it read would have missed them, so his input figure and ours do not measure the same thing.
- **Output tokens and time.** In his data output rose 24% and time 33% after refactoring, which he puts down to noise. In ours output tokens, turns and time all track input within a few percent, in both variants. Output tokens cost five times input, so a saving in input with a rise in output is a smaller economic saving than the input figure suggests.
- **Controls and acceptance.** He discarded each result without checking it; we kept every diff and ran an acceptance test, and every run passed. Control tasks on identical code are what let us say the target ratios are noise rather than effect.
- **The code.** His was greenfield, written by agents, never reviewed, one developer. Ours is years of human-written Java with a test suite and names that a grep finds. Agent-written code at scale may be exactly the kind that forces an agent to read rather than search.

**The economics.** He estimates 39.7 cents saved per change against an upper bound of five million tokens spent refactoring, so roughly 38 changes of that kind to break even on input pricing. Our split cost about 1.5 million tokens of agent work across the five batches and saves nothing per change, so it never breaks even on token cost. The split may still be worth doing for people, which is what the Human ease-of-change score is about, but that is a different argument.

## What changed in Sokrates

For an agent that searches by name and reads windows, file and unit length below the agent's read budget is not
a token cost driver, and the AI ease-of-change score weighted it heavily: file size 1.75 and unit size 1 out of a
total of about 9.5. On 2026-10-09 the scoring was changed in response to the results:

- unit size and the 500-LOC file size have AI weight 0 (they stay in the Human score);
- a new sub-score, "Files beyond read budget", counts the share of main code in files of more than 2,000 physical
  lines, what one read does not return, at AI weight 0.75;
- context per change counts at most a 200-line window per touched file, a window around the place rather than
  the file;
- the explanations of the complexity sub-scores say they are reasoning about correctness, not a measured cost.

Re-scored, the two variants sit at 7.2 and 7.3 (they were 6.6 and 7.7) and the predicted context per change at
571 and 552 lines (it was 1,690 and 774), in line with the measured cost, which was the same for both. The
measures that carry the AI score now are about how many places a change touches and how much the agent must read
around each. A findability measure, how many files a grep for a commit's identifiers hits, is the obvious next
addition and needs a design of its own.

## Limits and next steps

One agent harness and two models, one Java codebase, tasks that name their feature in words a grep finds, and a mechanical split. The original layout is public and plausibly in the model's training data, which would favour A, but with ratios at 1.0 it is not hiding an effect. An agent that reads files whole, or tasks that require understanding a class end to end, could show the cost; this experiment did not create that situation.

The next experiment is the one the comparison points at: the same harness on a codebase with a file far above the reading window, a few thousand to twenty thousand lines, split to under the window. If the cost of length is a step, that is where it will show, and Sokrates' thresholds for agents should move to where the step is rather than where the human thresholds are.

## Method summary

- Snapshot: Sokrates at commit `5e6aa97a` (2025-09-20). Analysis reference date 2025-09-20 for both variants.
- Variant B: file ≤ 500 LOC, unit ≤ 50 LOC and McCabe ≤ 25 as measured by Sokrates; 0 files and 0 units above after the split; module tests green; golden output identical. One recorded deviation: 141 trivial accessors of one class written one per line. Predictions before and after the scoring change are in the README.
- Tasks: mined with `scripts/mine_tasks.py` from 533 commits after the snapshot; each with `task.md`, `meta.json`, an acceptance test and a reference patch per variant; re-verified by `scripts/check_tasks.py`.
- Runs: `harness/run.py`, one worker per variant with its own Maven repository, Claude Code 2.1.295 with `claude-sonnet-5-5` (then `claude-haiku-5-5`, results in `results/runs-haiku.csv`), `--max-turns 80`, `--permission-mode acceptEdits`, a fixed allow-list of shell commands, `--setting-sources project`. Per run: fresh copy, prebuild, agent, tool log, diff, acceptance test with upstream modules built from the working tree.
- Analysis: `harness/compare.py` (pooled ratios, bootstrap) and `harness/summarize.py` (per-task quartiles).
