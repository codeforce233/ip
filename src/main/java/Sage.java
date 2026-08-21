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
        String[] tasks = new String[100];
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
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
                System.out.println(line);
                continue;
            }

            if (taskCount < tasks.length) {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println("added: " + input);
            }
            System.out.println(line);
        }
    }
}
