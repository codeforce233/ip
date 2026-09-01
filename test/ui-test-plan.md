# UI Test Plan

This file stores the planned command-driven UI tests for the chatbot CLI. The JSON block below is the ground truth for automated execution.

```json
{
  "app_command": "cd /Users/bytedance/IdeaProjects/ip && rm -rf data out && mkdir -p data && find src/main/java -name '*.java' -print | sort | xargs javac -d out && java -cp out sage.Sage",
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
    },
    {
      "id": "deadline-date-time-parsing",
      "aim": "Verify deadline input values are parsed as real dates and printed in a readable format.",
      "input": "deadline return book /by 2/12/2019 1800\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [D][ ] return book (by: Dec 2 2019, 6:00PM)\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[D][ ] return book (by: Dec 2 2019, 6:00PM)\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "event-date-time-parsing",
      "aim": "Verify event input values are parsed as real date-times and printed in a readable format.",
      "input": "event project meeting /from 2019-10-15 14:00 /to 2019-10-15 16:00\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [E][ ] project meeting (from: Oct 15 2019, 2:00PM to: Oct 15 2019, 4:00PM)\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[E][ ] project meeting (from: Oct 15 2019, 2:00PM to: Oct 15 2019, 4:00PM)\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "mark-then-list-preserves-state",
      "aim": "Verify positive task creation and marking keep the internal state consistent across multiple commands.",
      "input": "todo read book\ntodo return book\nmark 1\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] return book\nNow you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nNice! I've marked this task as done:\n  [T][X] read book\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][X] read book\n2.[T][ ] return book\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "delete-task-removal-preserves-state",
      "aim": "Verify a valid delete removes the selected task and keeps the remaining task list correct after a negative input.",
      "input": "todo read book\ntodo return book\ndelete 99\nlist\ndelete 1\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] return book\nNow you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nOOPS!!! The task number is invalid. Use a number from the current list.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] read book\n2.[T][ ] return book\n____________________________________________________________\n____________________________________________________________\nNoted. I've removed this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] return book\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "invalid-mark-does-not-change-state",
      "aim": "Verify an invalid mark command does not mutate the list or corrupt task state.",
      "input": "todo read book\nmark 99\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nOOPS!!! The task number is invalid. Use a number from the current list.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] read book\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "invalid-command-after-valid-task",
      "aim": "Verify an incorrect input does not add a bogus task and keeps previously valid tasks intact.",
      "input": "todo read book\nblah\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nOOPS!!! I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, list, mark, unmark, delete, or bye.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] read book\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "empty-todo-error",
      "aim": "Verify empty todo entries are rejected with a clear error message.",
      "input": "todo\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nOOPS!!! The description of a todo cannot be empty. Try: todo <task>\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "unknown-command-error",
      "aim": "Verify unrecognized commands are rejected with a clear error message.",
      "input": "blah\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nOOPS!!! I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, list, mark, unmark, delete, or bye.\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    }
  ]
}
``` 

## Notes

- The app command compiles the project before execution, then feeds the generated stdin sequence into the CLI.
- Use the script in `.codex/skills/test-ui/scripts/run-ui-tests.py` to execute the plan.
- If any case fails, the script exits immediately and prints both expected and actual output.
