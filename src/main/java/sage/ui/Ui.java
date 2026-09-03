package sage.ui;

import java.util.List;
import java.util.Scanner;

import sage.task.Task;

/**
 * Handles console input and presents user-facing messages for Sage.
 */
public class Ui {
    /**
     * Separates command responses in the console.
     */
    public static final String LINE = "____________________________________________________________";
    private final Scanner scanner;

    /**
     * Creates a user interface that reads from standard input.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command entered by the user.
     *
     * @return The next line from standard input.
     */
    public String readCommand() {
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
        System.out.println(LINE);
        System.out.println(banner);
        System.out.println("Hello! I'm Sage.");
        System.out.println("What can I do for you?");
        System.out.println(LINE);
    }

    /**
     * Shows the separator used between command responses.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Shows the farewell message.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Shows all tasks in their current list order.
     *
     * @param tasks The tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Shows tasks matching a search, or a message when no tasks match.
     *
     * @param tasks The matching tasks to display.
     */
    public void showMatchingTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("There are no matching tasks in your list.");
            System.out.println(LINE);
            return;
        }

        System.out.println("Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Prints information after a task has been added.
     *
     * @param task The newly added task.
     * @param taskCount The total number of tasks after the addition.
     */
    public void showAddedTask(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows the removed task and the number of remaining tasks.
     *
     * @param task The task that was removed.
     * @param taskCount The number of tasks remaining after removal.
     */
    public void showRemovedTask(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Shows confirmation that a task was marked as done.
     *
     * @param task The task that was marked as done.
     */
    public void showMarkedDone(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Shows confirmation that a task was marked as not done.
     *
     * @param task The task that was marked as not done.
     */
    public void showMarkedUndone(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Shows a user-facing error message followed by a separator.
     *
     * @param message The error details to display.
     */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
        System.out.println(LINE);
    }
}
