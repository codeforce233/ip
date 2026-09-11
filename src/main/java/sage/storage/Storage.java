package sage.storage;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import sage.task.Task;

/**
 * Loads and saves the task list to a text file on disk.
 */
public class Storage {
    private final Path filePath;

    /**
     * Creates a storage object for the given file path.
     *
     * @param filePath the location of the serialized task data.
     */
    public Storage(String filePath) {
        this.filePath = Paths.get(filePath);
    }

    /**
     * Loads all tasks from disk.
     *
     * @return the tasks recovered from the file, or an empty list if the file is missing or invalid.
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
                loadedTasks.add(TaskCodec.parse(trimmed));
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
     * Attempts to save the current task list to disk.
     * Prints a warning if the data cannot be written.
     *
     * @param tasks the tasks to persist.
     */
    public void save(List<Task> tasks) {
        try {
            Path directory = filePath.getParent();
            if (directory != null) {
                Files.createDirectories(directory);
            }
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8)) {
                for (Task task : tasks) {
                    writer.write(TaskCodec.serialize(task));
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Warning: could not save tasks to disk.");
        }
    }

}
