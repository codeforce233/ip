package sage.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import sage.task.Deadline;
import sage.task.Event;
import sage.task.Task;
import sage.task.Todo;

class StorageTest {
    @TempDir
    Path tempDir;

    @Test
    void saveAndLoad_roundTripsTasks() throws Exception {
        Path file = tempDir.resolve("tasks.txt");
        Storage storage = new Storage(file.toString());

        Todo todo = new Todo("read book");
        todo.markAsDone();
        Deadline deadline = new Deadline("submit report", "2025-02-14");
        Event event = new Event("team sync", "2025-02-14 09:00", "2025-02-14 10:30");

        storage.save(List.of(todo, deadline, event));
        List<String> lines = Files.readAllLines(file);
        assertEquals(3, lines.size());
        assertTrue(lines.get(0).contains("T | 1 | read book"));
        assertTrue(lines.get(1).contains("D | 0 | submit report"));
        assertTrue(lines.get(2).contains("E | 0 | team sync"));

        List<Task> loaded = storage.load();
        assertEquals(3, loaded.size());
        assertTrue(loaded.get(0) instanceof Todo);
        assertTrue(loaded.get(1) instanceof Deadline);
        assertTrue(loaded.get(2) instanceof Event);
        assertEquals("X", loaded.get(0).getStatusIcon());
    }

    @Test
    void load_withCorruptedTaskFile_returnsEmptyList() throws Exception {
        Path file = tempDir.resolve("broken.txt");
        Files.write(file, List.of("bad | data"));

        Storage storage = new Storage(file.toString());
        assertTrue(storage.load().isEmpty());
    }
}
