package sage.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import sage.task.Deadline;
import sage.task.Event;
import sage.task.Task;
import sage.task.TaskType;
import sage.task.Todo;

class TaskCodecTest {
    @Test
    void serialize_allTaskTypes_preservesExactRecordFormat() {
        Todo todo = new Todo("read book");
        todo.markAsDone();

        assertEquals("T | 1 | read book", TaskCodec.serialize(todo));
        assertEquals("T | 0 | plain task", TaskCodec.serialize(new Task("plain task", TaskType.TODO)));
        assertEquals("D | 0 | submit | 2025-02-14T00:00:00",
                TaskCodec.serialize(new Deadline("submit", "2025-02-14")));
        assertEquals("E | 0 | meet | 2025-02-14T09:00:00 | 2025-02-14T10:30:00",
                TaskCodec.serialize(new Event("meet", "2025-02-14 09:00", "2025-02-14 10:30")));
    }

    @Test
    void serialize_unparsedTimes_preservesOriginalText() {
        assertEquals("D | 0 | submit | Sunday", TaskCodec.serialize(new Deadline("submit", "Sunday")));
        assertEquals("E | 0 | meet | Monday | Tuesday",
                TaskCodec.serialize(new Event("meet", "Monday", "Tuesday")));
    }

    @Test
    void parse_whitespaceAndCompletionFlag_restoresTask() {
        Task task = TaskCodec.parse(" E | 1 | meet | Monday | Tuesday ");
        Event event = assertInstanceOf(Event.class, task);
        assertEquals("meet", event.getDescription());
        assertEquals("Monday", event.getFromText());
        assertEquals("Tuesday", event.getToText());
        assertEquals("X", event.getStatusIcon());
    }

    @Test
    void parse_invalidRecords_rejectsMissingFieldsAndUnknownValues() {
        for (String line : List.of("T | 0", "T | 0 | ", "D | 0 | submit", "E | 0 | meet | Monday",
                "Q | 0 | unknown", "T | 2 | read")) {
            assertThrows(IllegalArgumentException.class, () -> TaskCodec.parse(line), line);
        }
    }

    @Test
    void parse_emptyTrailingTimeFields_preservesLegacyRecords() {
        Deadline deadline = assertInstanceOf(Deadline.class, TaskCodec.parse("D | 0 | submit | "));
        Event event = assertInstanceOf(Event.class, TaskCodec.parse("E | 0 | meet | | "));
        assertEquals("", deadline.getByText());
        assertEquals("", event.getFromText());
        assertEquals("", event.getToText());
    }
}
