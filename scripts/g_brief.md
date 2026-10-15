You are in a folder with two things:

- `code/`: a Java/Maven codebase (Sokrates, a source-code analysis tool, as it was on 2025-09-20).
- `sokrates-reports/`: Sokrates' own analysis of that codebase (open `sokrates-reports/html/index.html` and the
  pages it links; `sokrates-reports/data/` has the data, including the git history up to 2025-09-20, duplication,
  file churn and files changed together).

Work only with what is in this folder. Do not look anywhere else on the disk, do not use the network, and do not
use what you may remember about later versions of this codebase.

## The goal

Propose a set of behaviour-preserving refactorings, made *now*, that would make the *future* changes to this
codebase cheaper for an AI coding agent. You do not know what those changes will be. Judge from where the code has
been changing (the history in the reports) and from what makes a change expensive, below.

## What makes a change expensive for the agent (measured, not assumed)

In 480 measured agent runs on this codebase (Claude Code, two models, 12 changes, each given as a description of the
wanted behaviour and checked by a test), the agent works like this: it searches (grep) for words from the task
description, opens windows of 40–70 lines around the hits, edits, compiles or runs one test, and finishes. Its cost
is set by **how many calls it makes**: within one change, one extra call costs 5–7% more, and the number of calls
explains the cost almost entirely (correlation 0.9). The calls that vary between runs are:

- **searching** (about a fifth of the cost): greps that hit many files the agent then does not need, or that miss
  because the code uses other words than the task description does, so the agent searches again;
- **reading** (a seventh to a quarter of the cost): the agent has to open several files to understand one behaviour,
  or follow the behaviour through several classes before it finds where to change it;
- the number of **files it has to open per change**, and for the weaker model the edits and fix-ups after a failed
  compile or test.

What did **not** matter: the length of files and methods (splitting every file to under 500 lines and every method
to under 50 changed nothing), and the size of the build output. Do not propose splitting long files or methods,
or making the build quieter.

So the refactorings worth making are the ones that let the agent **find the place for a change with its first
search, and make the change in one place**: knowledge that is spread over several files gathered in one, the same
logic or the same literal written in several places made into one, code that always changes together put together,
and the words people use for a feature present where a search for them finds the code.

## Constraints

- Behaviour-preserving: the generated reports must stay identical, and all existing tests must pass. You may
  change existing tests only where a refactoring moves what they test.
- Keep every public and package-private class, constructor, method and field signature that exists (other code and
  tests outside this folder use them). You may add new classes and methods, make an old method delegate to a new
  one, move private code, rename private members and locals, and add or improve comments and Javadoc.
- No new dependencies, no build changes.
- Prefer few refactorings with a clear expected effect over many small ones. At most 12.

## What to deliver

Write `proposal.md` in this folder (do not change `code/`), with one section per refactoring:

1. **id and title**
2. **what**: the change, concretely (which code moves where, what is unified into what, which words go where)
3. **where**: the files and methods involved
4. **evidence**: the signal from the reports or the code that made you choose it (history, duplication, files
   changed together, scattered literals, a feature's words missing from its code, …), with numbers or file:line
5. **expected effect**: which kind of future change gets cheaper and why (fewer searches, fewer files to open,
   one place to edit), and how often such changes happened in the history
6. **size and risk**: lines touched, and what could break

End with the list ranked by expected benefit per line changed.
