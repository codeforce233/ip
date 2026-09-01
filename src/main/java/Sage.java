import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sage {
    private static final int MAX_TASKS = 100;
    private static final Path DATA_DIR = Paths.get("data");
    private static final Path DATA_FILE = DATA_DIR.resolve("sage.txt");
    private static final Ui ui = new Ui();

    public static void main(String[] args) {
        List<Task> tasks = loadTasks();
        ui.showWelcome();

        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            ui.showLine();

            try {
                if ("bye".equals(input)) {
                    ui.showBye();
                    break;
                }

                if ("list".equals(input)) {
                    ui.showTaskList(tasks);
                    continue;
                }

                if (input.startsWith("todo")) {
                    String description = input.length() > 4 ? input.substring(4).trim() : "";
                    if (description.isEmpty()) {
                        throw new SageException("The description of a todo cannot be empty. Try: todo <task>");
                    }
                    addTask(tasks, new Todo(description));
                    ui.showLine();
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
                    ui.showLine();
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
                    Event event = new Event(description, from, to);
                    if (event.getFrom() != null && event.getTo() != null && event.getFrom().isAfter(event.getTo())) {
                        throw new SageException("The event end time must be after the start time.");
                    }
                    addTask(tasks, event);
                    ui.showLine();
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
                    saveTasks(tasks);
                    ui.showMarkedDone(tasks.get(index - 1));
                    ui.showLine();
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
                    saveTasks(tasks);
                    ui.showMarkedUndone(tasks.get(index - 1));
                    ui.showLine();
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
                    saveTasks(tasks);
                    ui.showRemovedTask(removed, tasks.size());
                    ui.showLine();
                    continue;
                }

                throw new SageException("I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, list, mark, unmark, delete, or bye.");
            } catch (SageException e) {
                ui.showError(e.getMessage());
            } catch (NumberFormatException e) {
                ui.showError("The task number must be a valid integer. Try: mark <number>, unmark <number>, or delete <number>");
            }
        }
    }

    private static void addTask(List<Task> tasks, Task task) {
        if (tasks.size() < MAX_TASKS) {
            tasks.add(task);
            ui.showAddedTask(task, tasks.size());
            saveTasks(tasks);
        }
    }

    private static List<Task> loadTasks() {
        List<Task> loadedTasks = new ArrayList<>();
        try {
            if (Files.notExists(DATA_FILE)) {
                Files.createDirectories(DATA_DIR);
                return loadedTasks;
            }

            List<String> lines = Files.readAllLines(DATA_FILE, StandardCharsets.UTF_8);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty()) {
                    continue;
                }
                loadedTasks.add(parseTaskLine(trimmed));
            }
            return loadedTasks;
        } catch (IOException e) {
            System.out.println("Warning: task data file is corrupted or unreadable. Starting with a clean list.");
            return new ArrayList<>();
        } catch (IllegalArgumentException e) {
            System.out.println("Warning: task data file is corrupted. Starting with a clean list.");
            return new ArrayList<>();
        }
    }

    private static Task parseTaskLine(String line) {
        String[] parts = line.split("\\s*\\|\\s*", -1);
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid task format");
        }

        String typeToken = parts[0].trim();
        String doneToken = parts[1].trim();
        String description = parts[2].trim();

        if (description.isEmpty()) {
            throw new IllegalArgumentException("Missing description");
        }

        Task task;
        switch (typeToken) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            if (parts.length < 4) {
                throw new IllegalArgumentException("Deadline missing due date");
            }
            task = new Deadline(description, parts[3].trim());
            break;
        case "E":
            if (parts.length < 5) {
                throw new IllegalArgumentException("Event missing times");
            }
            task = new Event(description, parts[3].trim(), parts[4].trim());
            break;
        default:
            throw new IllegalArgumentException("Unknown task type");
        }

        if ("1".equals(doneToken)) {
            task.markAsDone();
        } else if (!"0".equals(doneToken)) {
            throw new IllegalArgumentException("Invalid completion flag");
        }

        return task;
    }

    private static void saveTasks(List<Task> tasks) {
        try {
            Files.createDirectories(DATA_DIR);
            try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    writer.write(serializeTask(task));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save tasks to disk.");
        }
    }

    private static String serializeTask(Task task) {
        StringBuilder builder = new StringBuilder();
        builder.append(task.getType().getSymbol()).append(" | ");
        builder.append(task.getStatusIcon().equals("X") ? "1" : "0").append(" | ");
        if (task instanceof Deadline) {
            Deadline deadline = (Deadline) task;
            builder.append(deadline.getDescription()).append(" | ");
            if (deadline.getBy() != null) {
                builder.append(deadline.getBy().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                builder.append(deadline.getByText());
            }
        } else if (task instanceof Event) {
            Event event = (Event) task;
            builder.append(event.getDescription()).append(" | ");
            if (event.getFrom() != null) {
                builder.append(event.getFrom().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                builder.append(event.getFromText());
            }
            builder.append(" | ");
            if (event.getTo() != null) {
                builder.append(event.getTo().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            } else {
                builder.append(event.getToText());
            }
        } else {
            builder.append(task.getDescription());
        }
        return builder.toString();
    }
}
