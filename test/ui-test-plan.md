# UI Test Plan

This file stores the planned command-driven UI tests for the chatbot CLI. The JSON block below is the ground truth for automated execution.

```json
{
  "app_command": "cd /Users/bytedance/IdeaProjects/ip && javac src/main/java/Task.java src/main/java/Todo.java src/main/java/Deadline.java src/main/java/Event.java src/main/java/Sage.java && java -cp src/main/java Sage",
  "cases": [
    {
      "id": "todo-list-and-exit",
      "aim": "Verify a todo can be added, listed, and the session exits cleanly.",
      "input": "todo borrow book\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] borrow book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] borrow book\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "deadline-and-event-creation",
      "aim": "Verify deadline and event tasks are created with their descriptors and prefixes.",
      "input": "deadline return book /by Sunday\nevent project meeting /from Mon 2pm /to 4pm\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [D][ ] return book (by: Sunday)\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [E][ ] project meeting (from: Mon 2pm to: 4pm)\nNow you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    }
  ]
}
```

## Notes

- The app command compiles the project before execution, then feeds the generated stdin sequence into the CLI.
- Use the script in `.codex/skills/test-ui/scripts/run-ui-tests.py` to execute the plan.
- If any case fails, the script exits immediately and prints both expected and actual output.
