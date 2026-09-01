package sage.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sage.task.Deadline;
import sage.task.Event;
import sage.task.Task;
import sage.task.Todo;

/**
 * Loads and saves the task list to a text file on disk.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage object for the given file path.
     *
     * @param filePath the location of the serialized task data
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads all tasks from disk.
     *
     * @return the tasks recovered from the file, or an empty list if the file is missing or invalid
     */
    public List<Task> load() {
        List<Task> loadedTasks = new ArrayList<>();
        try {
            Path directory = filePath.getParent();
            if (directory != null) {
                Files.createDirectories(directory);
            }
            if (Files.notExists(filePath)) {
                return loadedTasks;
            }

            List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
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

    /**
     * Saves the current task list to disk.
     *
     * @param tasks the tasks to persist
     */
    public void save(List<Task> tasks) {
        try {
            Path directory = filePath.getParent();
            if (directory != null) {
                Files.createDirectories(directory);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    writer.write(serializeTask(task));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save tasks to disk.");
        }
    }

    /**
     * Parses a single serialized task line from the storage file.
     *
     * @param line the serialized task entry
     * @return the reconstructed task object
     * @throws IllegalArgumentException if the line format is invalid
     */
    private Task parseTaskLine(String line) {
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

    /**
     * Serializes a task into the format used by the storage file.
     *
     * @param task the task to convert to text
     * @return the persisted representation of the task
     */
    private String serializeTask(Task task) {
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
