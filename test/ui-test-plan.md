# UI Test Plan

This file stores the planned command-driven UI tests for the chatbot CLI. The JSON block below is the ground truth for automated execution.

```json
{
  "app_command": [
    "java",
    "-ea",
    "-Dfile.encoding=UTF-8",
    "-cp",
    "{classes}",
    "sage.Sage"
  ],
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
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nOOPS!!! I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, note, list, find, mark, unmark, delete, or bye.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] read book\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
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
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nOOPS!!! I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, note, list, find, mark, unmark, delete, or bye.\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "notes-add-find-delete",
      "aim": "Verify saved notes can be listed, searched, and deleted, while empty notes and marking are rejected.",
      "input": "note Waist: 32 inches\nnote Movie: Spirited Away | 千と千尋\nlist\nfind WAIST\nmark 1\ndelete 1\nlist\nnote\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've saved this note:\n  [N] Waist: 32 inches\n____________________________________________________________\n____________________________________________________________\nGot it. I've saved this note:\n  [N] Movie: Spirited Away | 千と千尋\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[N] Waist: 32 inches\n2.[N] Movie: Spirited Away | 千と千尋\n____________________________________________________________\n____________________________________________________________\nHere are the matching tasks in your list:\n1.[N] Waist: 32 inches\n____________________________________________________________\n____________________________________________________________\n____________________________________________________________\nOOPS!!! Notes cannot be marked or unmarked. Use delete to remove a note.\n____________________________________________________________\n____________________________________________________________\nNoted. I've removed this note:\n  [N] Waist: 32 inches\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[N] Movie: Spirited Away | 千と千尋\n____________________________________________________________\n____________________________________________________________\nOOPS!!! The text of a note cannot be empty. Try: note <text>\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "whitespace-and-exact-command-names",
      "aim": "Accept extra command spacing, but reject command-name prefixes without mutating the list.",
      "input": "  todo\tread book  \ntodolist\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] read book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nOOPS!!! I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, note, list, find, mark, unmark, delete, or bye.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] read book\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "invalid-date-and-event-range",
      "aim": "Reject impossible numeric dates, repeated time fields, and zero-length events.",
      "input": "deadline trip /by 2026-02-30\ndeadline trip /by Sunday /by Monday\nevent meeting /from 2026-10-01 14:00 /to 2026-10-01 14:00\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nOOPS!!! That date or time is invalid. Use a real date such as 2026-09-18 or 18/9/2026 1430.\n____________________________________________________________\n____________________________________________________________\nOOPS!!! Each time parameter must appear exactly once. The deadline format is invalid. Try: deadline <task> /by <time>\n____________________________________________________________\n____________________________________________________________\nOOPS!!! The event end time must be after the start time.\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "duplicate-completed-task",
      "aim": "Reject duplicate task details regardless of case, repeated description spaces, or completion state.",
      "input": "todo Read book\nmark 1\ntodo read  BOOK\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] Read book\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nNice! I've marked this task as done:\n  [T][X] Read book\n____________________________________________________________\n____________________________________________________________\nOOPS!!! That item is already in your list. Use list to find it.\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][X] Read book\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    },
    {
      "id": "end-of-input",
      "aim": "Exit cleanly when console input ends without an explicit bye command.",
      "input": "",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n"
    },
    {
      "id": "search-keeps-task-numbers",
      "aim": "Keep original task numbers in search results so mark targets the intended task.",
      "input": "todo Read chapter\ntodo Write report\nfind report\nmark 2\nlist\nbye\n",
      "expected": "____________________________________________________________\n  ____                       \n / ___|  __ _  __ _  ___     \n \\___ \\ / _` |/ _` |/ _ \\    \n  ___) | (_| | (_| |  __/    \n |____/ \\__,_|\\__, |\\___|    \n              |___/          \n\nHello! I'm Sage.\nWhat can I do for you?\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] Read chapter\nNow you have 1 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nGot it. I've added this task:\n  [T][ ] Write report\nNow you have 2 tasks in the list.\n____________________________________________________________\n____________________________________________________________\nHere are the matching tasks in your list:\n2.[T][ ] Write report\n____________________________________________________________\n____________________________________________________________\n____________________________________________________________\nNice! I've marked this task as done:\n  [T][X] Write report\n____________________________________________________________\n____________________________________________________________\nHere are the tasks in your list:\n1.[T][ ] Read chapter\n2.[T][X] Write report\n____________________________________________________________\n____________________________________________________________\nBye. Hope to see you again soon!\n____________________________________________________________\n"
    }
  ]
}
```

## Notes

- The app command compiles the CLI sources before execution, excluding the JavaFX entry point and GUI package.
- Each case uses isolated folders under `_temp` so the application's real task data remains untouched.
- Use the script in `.codex/skills/test-ui/scripts/run-ui-tests.py` to execute the plan.
- If any case fails, the script exits immediately and prints both expected and actual output.
