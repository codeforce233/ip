package sage.storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import sage.task.Deadline;
import sage.task.Event;
import sage.task.Note;
import sage.task.Task;
import sage.task.TaskType;
import sage.task.Todo;

/**
 * Converts tasks to and from the pipe-separated records stored on disk.
 */
final class TaskCodec {
    private static final String FIELD_SEPARATOR = " | ";
    private static final String FIELD_SPLIT_PATTERN = "\\s*\\|\\s*";
    private static final String DONE_FLAG = "1";
    private static final String NOT_DONE_FLAG = "0";
    private static final int TYPE_INDEX = 0;
    private static final int STATUS_INDEX = 1;
    private static final int DESCRIPTION_INDEX = 2;
    private static final int START_OR_DUE_TIME_INDEX = 3;
    private static final int END_TIME_INDEX = 4;
    private static final int COMMON_FIELD_COUNT = 3;
    private static final int DEADLINE_FIELD_COUNT = 4;
    private static final int EVENT_FIELD_COUNT = 5;

    private TaskCodec() {
    }

    /**
     * Reconstructs one task from its persisted fields and completion flag.
     *
     * @param line The serialized task entry.
     * @return The reconstructed task.
     * @throws IllegalArgumentException If required fields or the completion flag are invalid.
     */
    static Task parse(String line) {
        // Preserve empty trailing fields so legacy records retain their original meaning.
        // Notes use the entire remainder as text, including any literal pipe characters.
        int splitLimit = line.stripLeading().startsWith("N") ? COMMON_FIELD_COUNT : -1;
        String[] fields = line.split(FIELD_SPLIT_PATTERN, splitLimit);
        if (fields.length < COMMON_FIELD_COUNT) {
            throw new IllegalArgumentException("Invalid task format");
        }
        String description = fields[DESCRIPTION_INDEX].trim();
        if (description.isEmpty()) {
            throw new IllegalArgumentException("Missing description");
        }

        Task task = createTask(fields, description);
        String completionFlag = fields[STATUS_INDEX].trim();
        if (DONE_FLAG.equals(completionFlag)) {
            task.markAsDone();
        } else if (!NOT_DONE_FLAG.equals(completionFlag)) {
            throw new IllegalArgumentException("Invalid completion flag");
        }
        return task;
    }

    /**
     * Creates the task subtype after checking its required fields.
     *
     * @param fields The record fields, including the common fields.
     * @param description The validated task description.
     * @return The task before its completion state is restored.
     * @throws IllegalArgumentException If the type is unknown or a required time field is absent.
     */
    private static Task createTask(String[] fields, String description) {
        switch (fields[TYPE_INDEX].trim()) {
        case "T":
            return new Todo(description);
        case "N":
            return new Note(description);
        case "D":
            if (fields.length < DEADLINE_FIELD_COUNT) {
                throw new IllegalArgumentException("Deadline missing due date");
            }
            return new Deadline(description, fields[START_OR_DUE_TIME_INDEX].trim());
        case "E":
            if (fields.length < EVENT_FIELD_COUNT) {
                throw new IllegalArgumentException("Event missing times");
            }
            return new Event(description, fields[START_OR_DUE_TIME_INDEX].trim(), fields[END_TIME_INDEX].trim());
        default:
            throw new IllegalArgumentException("Unknown task type");
        }
    }

    /**
     * Serializes common task fields followed by any deadline or event times.
     *
     * @param task The task to convert to text.
     * @return The record in the existing storage format.
     */
    static String serialize(Task task) {
        // A type token must agree with the fields available on its runtime class.
        assert task.getType() == getExpectedType(task) : "Task type must match its serialized fields";
        List<String> fields = new ArrayList<>();
        fields.add(task.getType().getSymbol());
        fields.add("X".equals(task.getStatusIcon()) ? DONE_FLAG : NOT_DONE_FLAG);
        fields.add(task.getDescription());

        if (task instanceof Deadline deadline) {
            fields.add(formatStoredTime(deadline.getBy(), deadline.getByText()));
        } else if (task instanceof Event event) {
            fields.add(formatStoredTime(event.getFrom(), event.getFromText()));
            fields.add(formatStoredTime(event.getTo(), event.getToText()));
        }
        return String.join(FIELD_SEPARATOR, fields);
    }

    /**
     * Determines the record type supported by a task's runtime class.
     *
     * @param task The task being serialized.
     * @return The type whose fields the serializer can write.
     */
    private static TaskType getExpectedType(Task task) {
        if (task instanceof Deadline) {
            return TaskType.DEADLINE;
        }
        if (task instanceof Event) {
            return TaskType.EVENT;
        }
        if (task instanceof Note) {
            return TaskType.NOTE;
        }
        return TaskType.TODO;
    }

    /**
     * Stores parsed times as ISO text while retaining unparsed time descriptions.
     *
     * @param dateTime The parsed time, possibly null.
     * @param fallbackText The original text used when no parsed time exists.
     * @return The time field for the storage record.
     */
    private static String formatStoredTime(LocalDateTime dateTime, String fallbackText) {
        if (dateTime == null) {
            return fallbackText;
        }
        return dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
