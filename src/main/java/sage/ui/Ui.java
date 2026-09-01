package sage.ui;

import java.util.List;
import java.util.Scanner;

import sage.task.Task;

/**
 * Handles reading user input and printing output for the terminal interface.
 */
public class Ui {
    public static final String LINE = "____________________________________________________________";
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Reads the next user command from standard input.
     *
     * @return the command string entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Prints the welcome banner and instructions for the user.
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
     * Prints the horizontal divider used throughout the interface.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints the exit message when the user leaves the application.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints the entire list of tasks in order.
     *
     * @param tasks the tasks to display
     */
    public void showTaskList(List<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
        System.out.println(LINE);
    }

    /**
     * Prints information after a task has been added.
     *
     * @param task the newly added task
     * @param count the total number of tasks after the addition
     */
    public void showAddedTask(Task task, int count) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + count + " tasks in the list.");
    }

    /**
     * Prints information after a task has been removed.
     *
     * @param task the removed task
     * @param count the total number of tasks remaining
     */
    public void showRemovedTask(Task task, int count) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + count + " tasks in the list.");
    }

    /**
     * Prints confirmation that a task has been marked complete.
     *
     * @param task the completed task
     */
    public void showMarkedDone(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Prints confirmation that a task has been marked incomplete.
     *
     * @param task the task that is no longer complete
     */
    public void showMarkedUndone(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Prints a user-facing error message prefixed with the app's standard error format.
     *
     * @param message the error description to display
     */
    public void showError(String message) {
        System.out.println("OOPS!!! " + message);
        System.out.println(LINE);
    }
}
