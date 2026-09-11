package sage.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

class TaskDateTimeTest {
    @Test
    void parse_supportedDateTimes_preservesEveryInputFormat() {
        LocalDateTime expected = LocalDateTime.of(2025, 2, 14, 9, 30);
        for (String input : List.of("14/2/2025 0930", "14/2/2025 09:30", "2025-02-14 0930",
                "2025-02-14 09:30", "2025-02-14T09:30:00", " 2025-02-14 09:30 ")) {
            assertEquals(expected, Deadline.parseDateTime(input), input);
            assertEquals(expected, Event.parseDateTime(input), input);
        }
    }

    @Test
    void parse_dateOnly_usesMidnight() {
        LocalDateTime expected = LocalDateTime.of(2025, 2, 14, 0, 0);
        assertEquals(expected, TaskDateTime.parse("14/2/2025"));
        assertEquals(expected, TaskDateTime.parse("2025-02-14"));
    }

    @Test
    void parse_unrecognizedOrEmptyInput_returnsNull() {
        assertNull(TaskDateTime.parse(null));
        for (String input : List.of("", "   ", "Sunday", "2025-13-14", "2025-02-14 25:00")) {
            assertNull(TaskDateTime.parse(input), input);
        }
    }

    @Test
    void toString_unparsedAndMissingTimes_preservesFallbackText() {
        assertEquals("[D][ ] read (by: Sunday)", new Deadline("read", " Sunday ").toString());
        assertEquals("[E][ ] meet (from: Mon 2pm to: 4pm)",
                new Event("meet", " Mon 2pm ", "4pm").toString());
        assertEquals("[D][ ] read (by: )", new Deadline("read", (String) null).toString());
        assertEquals("[E][ ] meet (from:  to: )", new Event("meet", (String) null, null).toString());
    }

    @Test
    void toString_midnightEvent_omitsTime() {
        assertEquals("[E][ ] meet (from: Feb 14 2025 to: Feb 15 2025)",
                new Event("meet", "2025-02-14", "2025-02-15").toString());
    }
}
