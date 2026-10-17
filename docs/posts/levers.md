# What lowers a coding agent's bill: the request, the CLAUDE.md and the session, not the code

*Levers experiment, 2026-10-17. 1,440 single-task runs and 40 six-step chains with Claude Code 2.1.300, Sonnet and
Haiku, $175. Design and hypotheses: `plans/levers-experiment.md` (pre-registered). Data:
`results/levers-{sonnet,haiku}.csv`, `results/levers-chains-{sonnet,haiku}.csv`, analyses in `results/levers-*-analysis.txt`,
every transcript in `results/transcripts/`.*

## In short

The code-shape experiments found no cheap change to the code that lowers what an agent costs. The three free levers
tested here all move the bill more than any code change did, and two of them move it the wrong way when used naively.

| lever | Sonnet | Haiku |
|---|---|---|
| name the method(s) to change in the request | **0.89** (0.83–0.93) | **0.82** (0.74–0.88) |
| name only the file(s) | 0.92 (0.88–0.96) | 0.90 (0.81–0.96) |
| CLAUDE.md as written by `/init` | 1.02 (0.97–1.06) | 1.02 (0.92–1.13) |
| `/init` + a code index (20–27k characters) | **1.27** (1.23–1.34) | **1.22** (1.10–1.30) |
| `/init` + the same length of irrelevant text | 1.26 (1.20–1.32) | 1.16 (1.03–1.25) |
| six changes in one continued session vs six fresh sessions | **0.60** (0.56–0.77) | **3.16** (2.62–3.77) |

Cost against the base condition (fresh session, no hint, no CLAUDE.md), the geometric mean of per-task median ratios over
12 tasks (two codebases, six tasks each, 10 repeats), with 95% bootstrap intervals; chains: the median chain cost,
over 10 chains per mode. Acceptance: 1,437 of 1,440 single runs and all 240 chain steps passed; no condition lowered
success.

## 1. Hints: worth it when the request is vague and the change is in one place

Two hint levels were appended to each task, derived mechanically from the reference change: the files it touches, or
the files plus the methods of the original code that it changes (an upper bound; whoever knows that has nearly located
the change).

- On BackOffice, whose tasks describe behaviour only, naming the method cut Sonnet's cost to **0.82** (0.74–0.89) and
  Haiku's to **0.74** (0.68–0.81). Most of it is on the one-method fixes: a01 0.69, a02 0.68, a03 0.76 with Sonnet.
- On the multi-site changes (a new field through every look-alike) it did little (Sonnet a11 1.01, a12 0.98,
  a13 0.90), although the hint listed every method to change: the cost there is in reading and editing, not finding.
- On Sokrates, whose tasks already name classes, both hint levels gave 0.91–0.95.
- Mechanism (Sonnet, BackOffice): search calls per run 3.5 → 1.5, all calls 12.3 → 9.2.

H1 predicted ≤ 0.80 for the method hint on BackOffice: Haiku met it, Sonnet came close (0.82).

## 2. CLAUDE.md: every token is paid on every call, and a useful map doesn't pay for itself

- The `/init` file (37–50 lines) is about free: 1.02 for both models.
- Adding a code index (every package, its classes and public methods) did what a map should: Sonnet's search calls fell
  from 3.5 to 1.4 (BackOffice) and from 4.7 to 3.3 (Sokrates), calls from 12.3 to 9.5. It still cost **1.27**, the same
  as an equally long block of inert team-handbook text (1.26). The extra 8–15k tokens are re-sent with every call; the
  fixed-context share of Sonnet's cost rose from about 45% to 59–65%.
- The bigger the codebase's index relative to the work, the worse: Sokrates 1.44 (index) and 1.37 (padding) with
  Sonnet; BackOffice 1.13 and 1.16.

H2 (+3–8% for the larger file) was far too low: +26% with Sonnet. H3 (index cheaper than padding) failed: the map's
savings in calls were cancelled by its own size. A map would pay only if it were much shorter than what it saves the
agent from reading.

## 3. Sessions: cheaper to continue, until the context crosses a price tier

The six BackOffice tasks in a row in one repository (a01, a11, a02, a12, a03, a13, each change committed before the
next), either each in a fresh session or all in one continued session (`claude -p --resume`).

- **Sonnet: the continued session cost 0.60 of fresh.** From step 2 on, a continued step needs about half the calls
  (a02: 6 against 10; a03: 5 against 9), because the agent already knows DataStore, the record pattern and the test
  command. The growing context is re-read from cache at a tenth of the input price, which is less than the searches it
  saves. Per step: 1.07, 0.68, 0.62, 0.52, 0.50, 0.57. Sonnet's context stayed below about 100k tokens.
- **Haiku: the continued session cost 3.16× fresh.** Haiku's calls also fell a little (turns 0.88), and at a flat price
  the two modes would cost the same (weighted tokens 1.01). But Haiku is wordier: its sessions passed **100k tokens of
  context at step 4 or 5**, and above that line Claude Code prices Haiku 5× higher per token ($0.50 against $0.10 per
  million price-weighted tokens; below the line the implied price is flat). Per step: 1.06, 0.98, 1.32, 2.86, 8.98, 5.26.

H4 (continued sessions cost more) failed for Sonnet and held for Haiku, but for a reason other than the one predicted:
not the re-sent context as such, but the price tier it crosses.

## What this means in practice

1. **Say where, when you know.** Naming the method saves about a quarter on small fixes described as behaviour. Naming
   only the file saves under 10%.
2. **Keep CLAUDE.md short.** Adding 20–27k characters cost 13–44% more on every task, depending on the codebase; even
   a useful code map didn't earn that back. `/init` size is fine.
3. **Continue a session for related changes, but watch the context size.** With Sonnet, continuing six related changes
   cost 40% less than fresh sessions. Where a model has a long-context price tier, start a fresh session (or compact)
   before crossing it; for Haiku that came after three or four changes.

Combined, these are worth more than every code change measured in this repository, and they cost nothing to apply.

## Limits

- Two codebases, twelve tasks, one agent and version; the chain is one fixed order of six related BackOffice tasks
  (fixed because later tasks change constructors that earlier tasks' tests use). Unrelated changes in one session may
  gain less.
- Costs are those Claude Code reports from its price table (`total_cost_usd`; cumulative over a resumed session, so a
  step's cost is the difference). The 100k tier is inferred from those reports, not checked against an invoice.
- `num_turns` in Claude Code's JSON result sometimes reports 1 for runs with many calls; turns are therefore shown only
  as reported, and the mechanism uses calls counted from the transcripts.
- The symbol hint is an upper bound; a real developer's hint is usually vaguer.
- Index and padding are equal in characters; code tokenizes denser, so the index adds more tokens (first call: ~30k
  against ~27k with Sonnet).
