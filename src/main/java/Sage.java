import java.util.Scanner;

public class Sage {
    public static void main(String[] args) {
        String line = "____________________________________________________________";
        String banner = "  ____                       \n"
                + " / ___|  __ _  __ _  ___     \n"
                + " \\___ \\ / _` |/ _` |/ _ \\    \n"
                + "  ___) | (_| | (_| |  __/    \n"
                + " |____/ \\__,_|\\__, |\\___|    \n"
                + "              |___/          \n";
        System.out.println(line);
        System.out.println(banner);
        System.out.println("Hello! I'm Sage.");
        System.out.println("What can I do for you?");
        System.out.println(line);

        Scanner scanner = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int taskCount = 0;

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            System.out.println(line);
            if ("bye".equals(input)) {
                System.out.println("Bye. Hope to see you again soon!");
                System.out.println(line);
                break;
            }

            if ("list".equals(input)) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
                System.out.println(line);
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
                    if (taskCount < tasks.length) {
                        tasks[taskCount] = new Task(input);
                        taskCount++;
                        System.out.println("added: " + input);
                    }
                }
                System.out.println(line);
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
                    if (taskCount < tasks.length) {
                        tasks[taskCount] = new Task(input);
                        taskCount++;
                        System.out.println("added: " + input);
                    }
                }
                System.out.println(line);
                continue;
            }

            if (taskCount < tasks.length) {
                tasks[taskCount] = new Task(input);
                taskCount++;
                System.out.println("added: " + input);
            }
            System.out.println(line);
        }
    }
}
