# Internal assertions

These checks use Java's `assert` keyword in production code. They document programmer assumptions;
they do not replace exceptions for invalid user input, invalid indexes, capacity limits, or corrupt files.
Each assertion has a diagnostic message, and none performs a required state change.

| Location | Assumption and justification |
| --- | --- |
| `TaskList.add` after insertion | The list remains within `MAX_TASKS`. The preceding exception check handles a full list; this postcondition detects a future change that accidentally inserts too many tasks. It does not assume that lists loaded from disk were already within the limit. |
| `TaskList.markDone` after updating the task | The task reports the completed status. Callers next save the task and show confirmation, so a subclass that fails to honor `markAsDone` must be detected before those steps. |
| `TaskList.markUndone` after updating the task | The task reports the incomplete status. This checks the reverse state-transition contract before the change is saved and reported to the user. |
| `Storage.serializeTask` before serialization | The task's type matches the fields available from its runtime class. A deadline or event token without its corresponding date fields would produce a record that cannot be loaded correctly. This is an internal model inconsistency; malformed records read from disk still use ordinary validation. A base `Task` with the `TODO` type remains supported. |

Assertions are explicitly enabled for Gradle tests. To enable them while running the application,
add `-ea` to the IDE's VM options, or launch the packaged application with `java -ea -jar build/libs/sage.jar`.
Without `-ea`, these checks are skipped; normal validation still runs.

Existing tests exercise successful additions, the capacity boundary, both completion transitions,
and serialization of all three task types. Additional regression tests check that invalid indexes still
raise their normal exceptions and that an inconsistent task type triggers the serialization assertion.
