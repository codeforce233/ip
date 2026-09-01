import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sage {
    private static final int MAX_TASKS = 100;
    private static final String LINE = "____________________________________________________________";

    public static void main(String[] args) {
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

        Scanner scanner = new Scanner(System.in);
        List<Task> tasks = new ArrayList<>(MAX_TASKS);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            System.out.println(LINE);

            try {
                if ("bye".equals(input)) {
                    System.out.println("Bye. Hope to see you again soon!");
                    System.out.println(LINE);
                    break;
                }

                if ("list".equals(input)) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + "." + tasks.get(i));
                    }
                    System.out.println(LINE);
                    continue;
                }

                if (input.startsWith("todo")) {
                    String description = input.length() > 4 ? input.substring(4).trim() : "";
                    if (description.isEmpty()) {
                        throw new SageException("The description of a todo cannot be empty. Try: todo <task>");
                    }
                    addTask(tasks, new Todo(description));
                    System.out.println(LINE);
                    continue;
                }

                if (input.startsWith("deadline")) {
                    String rest = input.length() > 8 ? input.substring(8).trim() : "";
                    int byIndex = rest.indexOf(" /by ");
                    if (rest.isEmpty() || byIndex < 0) {
                        throw new SageException("The deadline format is invalid. Try: deadline <task> /by <time>");
                    }
                    String description = rest.substring(0, byIndex).trim();
                    if (description.isEmpty()) {
                        throw new SageException("The description of a deadline cannot be empty. Try: deadline <task> /by <time>");
                    }
                    String by = rest.substring(byIndex + 5).trim();
                    if (by.isEmpty()) {
                        throw new SageException("The deadline time cannot be empty. Try: deadline <task> /by <time>");
                    }
                    addTask(tasks, new Deadline(description, by));
                    System.out.println(LINE);
                    continue;
                }

                if (input.startsWith("event")) {
                    String rest = input.length() > 5 ? input.substring(5).trim() : "";
                    int fromIndex = rest.indexOf(" /from ");
                    int toIndex = rest.indexOf(" /to ");
                    if (rest.isEmpty() || fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex) {
                        throw new SageException("The event format is invalid. Try: event <task> /from <start> /to <end>");
                    }
                    String description = rest.substring(0, fromIndex).trim();
                    if (description.isEmpty()) {
                        throw new SageException("The description of an event cannot be empty. Try: event <task> /from <start> /to <end>");
                    }
                    String from = rest.substring(fromIndex + 7, toIndex).trim();
                    String to = rest.substring(toIndex + 4).trim();
                    if (from.isEmpty() || to.isEmpty()) {
                        throw new SageException("The event timings cannot be empty. Try: event <task> /from <start> /to <end>");
                    }
                    addTask(tasks, new Event(description, from, to));
                    System.out.println(LINE);
                    continue;
                }

                if (input.startsWith("mark")) {
                    String indexText = input.length() > 4 ? input.substring(4).trim() : "";
                    if (indexText.isEmpty()) {
                        throw new SageException("The task number is missing. Try: mark <task number>");
                    }
                    int index = Integer.parseInt(indexText);
                    if (index <= 0 || index > tasks.size()) {
                        throw new SageException("The task number is invalid. Use a number from the current list.");
                    }
                    tasks.get(index - 1).markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks.get(index - 1));
                    System.out.println(LINE);
                    continue;
                }

                if (input.startsWith("unmark")) {
                    String indexText = input.length() > 6 ? input.substring(6).trim() : "";
                    if (indexText.isEmpty()) {
                        throw new SageException("The task number is missing. Try: unmark <task number>");
                    }
                    int index = Integer.parseInt(indexText);
                    if (index <= 0 || index > tasks.size()) {
                        throw new SageException("The task number is invalid. Use a number from the current list.");
                    }
                    tasks.get(index - 1).markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks.get(index - 1));
                    System.out.println(LINE);
                    continue;
                }

                if (input.startsWith("delete")) {
                    String indexText = input.length() > 6 ? input.substring(6).trim() : "";
                    if (indexText.isEmpty()) {
                        throw new SageException("The task number is missing. Try: delete <task number>");
                    }
                    int index = Integer.parseInt(indexText);
                    if (index <= 0 || index > tasks.size()) {
                        throw new SageException("The task number is invalid. Use a number from the current list.");
                    }
                    Task removed = tasks.remove(index - 1);
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removed);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println(LINE);
                    continue;
                }

                throw new SageException("I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, list, mark, unmark, delete, or bye.");
            } catch (SageException e) {
                System.out.println("OOPS!!! " + e.getMessage());
                System.out.println(LINE);
            } catch (NumberFormatException e) {
                System.out.println("OOPS!!! The task number must be a valid integer. Try: mark <number>, unmark <number>, or delete <number>");
                System.out.println(LINE);
            }
        }
    }

    private static void addTask(List<Task> tasks, Task task) {
        if (tasks.size() < MAX_TASKS) {
            tasks.add(task);
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + task);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        }
    }
}
