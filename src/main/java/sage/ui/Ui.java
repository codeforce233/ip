package sage.ui;

import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Consumer;

import sage.task.Task;

/**
 * Reads console commands and sends Sage messages to a configured output destination.
 */
public class Ui {
    /**
     * Separates command responses in the console.
     */
    public static final String LINE = "____________________________________________________________";

    /**
     * Reads console commands, or is null when this interface is configured for output only.
     */
    private final Scanner scanner;

    /**
     * Receives each line produced by the display methods.
     */
    private final Consumer<String> output;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
        output = line -> System.out.println(line);
    }

    /**
     * Creates a user interface that sends messages to the given output consumer.
     * This output-only interface does not read commands from standard input.
     *
     * @param output The consumer that receives each line of user-facing output.
     */
    public Ui(Consumer<String> output) {
        scanner = null;
        this.output = Objects.requireNonNull(output);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return The next line from standard input.
     * @throws IllegalStateException If this interface was created for output only.
     */
    public String readCommand() {
        if (scanner == null) {
            throw new IllegalStateException("This user interface does not accept command input.");
        }
        return scanner.nextLine();
    }

    /**
     * Shows the Sage banner and initial greeting.
     */
    public void showWelcome() {
        String banner = "  ____                       \n"
                + " / ___|  __ _  __ _  ___     \n"
                + " \\___ \\ / _` |/ _` |/ _ \\    \n"
                + "  ___) | (_| | (_| |  __/    \n"
                + " |____/ \\__,_|\\__, |\\___|    \n"
                + "              |___/          \n";
        output.accept(LINE);
        output.accept(banner);
        output.accept("Hello! I'm Sage.");
        output.accept("What can I do for you?");
        output.accept(LINE);
    }

    /**
     * Shows the separator used between command responses.
     */
    public void showLine() {
        output.accept(LINE);
    }

    /**
     * Shows the farewell message.
     */
    public void showBye() {
        output.accept("Bye. Hope to see you again soon!");
    }

    /**
     * Shows all tasks in their current list order.
     *
     * @param tasks The tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        output.accept("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            output.accept((i + 1) + "." + tasks.get(i));
        }
        output.accept(LINE);
    }

    /**
     * Shows tasks matching a search, or a message when no tasks match.
     *
     * @param tasks The matching tasks to display.
     */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            output.accept("There are no matching tasks in your list.");
            output.accept(LINE);
            return;
        }

        output.accept("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            output.accept((i + 1) + "." + tasks.get(i));
        }
        output.accept(LINE);
    }

    /**
     * Prints information after a task has been added.
     *
     * @param task The newly added task.
     * @param taskCount The total number of tasks after the addition.
     */
    public void showAddedTask(Task task, int taskCount) {
        output.accept("Got it. I've added this task:");
        output.accept("  " + task);
        output.accept("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows the removed task and the number of remaining tasks.
     *
     * @param task The task that was removed.
     * @param taskCount The number of tasks remaining after removal.
     */
    public void showRemovedTask(Task task, int taskCount) {
        output.accept("Noted. I've removed this task:");
        output.accept("  " + task);
        output.accept("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows confirmation that a task was marked as done.
     *
     * @param task The task that was marked as done.
     */
    public void showMarkedDone(Task task) {
        output.accept("Nice! I've marked this task as done:");
        output.accept("  " + task);
    }

    /**
     * Shows confirmation that a task was marked as not done.
     *
     * @param task The task that was marked as not done.
     */
    public void showMarkedUndone(Task task) {
        output.accept("OK, I've marked this task as not done yet:");
        output.accept("  " + task);
    }

    /**
     * Shows a user-facing error message followed by a separator.
     *
     * @param message The error details to display.
     */
    public void showError(String message) {
        output.accept("OOPS!!! " + message);
        output.accept(LINE);
    }
}
