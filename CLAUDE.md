@AGENTS.md

# UI testing workflow

After each code update that affects user-visible CLI behavior:
1. Review whether `test/ui-test-plan.md` needs a matching update.
2. Invoke the project skill `test-ui` via:
   `python3 .codex/skills/test-ui/scripts/run-ui-tests.py --plan test/ui-test-plan.md`
3. If a case fails, stop immediately and report both actual and expected outputs.
