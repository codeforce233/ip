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
        Task[] tasks = new Task[MAX_TASKS];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            System.out.println(LINE);

            if ("bye".equals(input)) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(LINE);
                break;
            }

            if ("list".equals(input)) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("todo ")) {
                String description = input.substring(5).trim();
                if (!description.isEmpty()) {
                    taskCount = addTask(tasks, taskCount, new Todo(description));
                }
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("deadline ")) {
                String rest = input.substring(9).trim();
                int byIndex = rest.indexOf(" /by ");
                if (byIndex >= 0) {
                    String description = rest.substring(0, byIndex).trim();
                    String by = rest.substring(byIndex + 5).trim();
                    taskCount = addTask(tasks, taskCount, new Deadline(description, by));
                }
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("event ")) {
                String rest = input.substring(6).trim();
                int fromIndex = rest.indexOf(" /from ");
                int toIndex = rest.indexOf(" /to ");
                if (fromIndex >= 0 && toIndex > fromIndex) {
                    String description = rest.substring(0, fromIndex).trim();
                    String from = rest.substring(fromIndex + 7, toIndex).trim();
                    String to = rest.substring(toIndex + 5).trim();
                    taskCount = addTask(tasks, taskCount, new Event(description, from, to));
                }
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("mark ")) {
                try {
                    int index = Integer.parseInt(input.substring(5).trim());
                    if (index > 0 && index <= taskCount) {
                        tasks[index - 1].markAsDone();
                        System.out.println("Nice! I've marked this task as done:");
                        System.out.println("  " + tasks[index - 1]);
                    }
                } catch (NumberFormatException e) {
                    if (taskCount < MAX_TASKS) {
                        tasks[taskCount] = new Todo(input);
                        taskCount++;
                        System.out.println("added: " + input);
                    }
                }
                System.out.println(LINE);
                continue;
            }

            if (input.startsWith("unmark ")) {
                try {
                    int index = Integer.parseInt(input.substring(7).trim());
                    if (index > 0 && index <= taskCount) {
                        tasks[index - 1].markAsNotDone();
                        System.out.println("OK, I've marked this task as not done yet:");
                        System.out.println("  " + tasks[index - 1]);
                    }
                } catch (NumberFormatException e) {
                    if (taskCount < MAX_TASKS) {
                        tasks[taskCount] = new Todo(input);
                        taskCount++;
                        System.out.println("added: " + input);
                    }
                }
                System.out.println(LINE);
                continue;
            }

            if (taskCount < MAX_TASKS) {
                tasks[taskCount] = new Todo(input);
                taskCount++;
                System.out.println("added: " + input);
            }
            System.out.println(LINE);
        }
    }

    private static int addTask(Task[] tasks, int taskCount, Task task) {
        if (taskCount < MAX_TASKS) {
            tasks[taskCount] = task;
            System.out.println("Got it. I've added this task:");
            System.out.println("  " + task);
            System.out.println("Now you have " + (taskCount + 1) + " tasks in the list.");
            return taskCount + 1;
        }
        return taskCount;
    }
}
