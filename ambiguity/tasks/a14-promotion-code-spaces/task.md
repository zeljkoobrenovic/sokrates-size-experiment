# Spaces around a promotion code break the promotion

When marketing sets up a promotion and the code is pasted with a space before or after it (for example "  SPRING10 "),
the promotion is saved with those spaces. Looking the promotion up by its code SPRING10 then finds nothing, the active
promotions list shows the code with the spaces, and a second promotion with the same code SPRING10 can be created next
to it. Spaces around a promotion code should simply be dropped when the promotion is set up, so that the promotion is
stored and shown as SPRING10, is found by that code, and setting up SPRING10 again (with or without spaces) is rejected
as a duplicate.
