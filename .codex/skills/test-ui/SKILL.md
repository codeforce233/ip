---
name: test-ui
description: Run UI-style acceptance tests for the chatbot CLI by feeding command lists to the program, matching stdout against expected output, and recording the exact console transcript.
---

# Test UI

Use this skill when the current code change affects the chatbot's interactive CLI behavior and you need to validate output against a written test plan.

## Required workflow

1. Update `test/ui-test-plan.md` when the behavior or expected output changed.
2. Run the UI test script from the repository root:

   ```bash
   python3 .codex/skills/test-ui/scripts/run-ui-tests.py --plan test/ui-test-plan.md
   ```

3. The script must:
   - run the app once per test case
   - feed the case input as stdin
   - compare the actual stdout to the expected output exactly
   - print the console input and output for the test session
   - stop immediately on the first failing case and print both actual and expected output

4. If any case fails, report the failing case ID, the actual stdout, the expected stdout, and the command transcript.

## Test plan format

The file `test/ui-test-plan.md` contains a JSON block defining the app command and the list of cases. Keep it in this structure:

```json
{
  "app_command": "cd /path/to/repo && javac src/main/java/*.java && printf '%s' '...input...' | java -cp src/main/java Sage",
  "cases": [
    {
      "id": "todo-list",
      "aim": "Verify todo creation and listing.",
      "input": "todo borrow book\nlist\nbye\n",
      "expected": "... exact stdout ..."
    }
  ]
}
```

The JSON is the source of truth for test execution; the surrounding markdown is for human-readable documentation.
