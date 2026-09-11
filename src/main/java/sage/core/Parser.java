package sage.core;

import sage.command.AddCommand;
import sage.command.Command;
import sage.command.DeleteCommand;
import sage.command.ExitCommand;
import sage.command.FindCommand;
import sage.command.ListCommand;
import sage.command.MarkCommand;
import sage.command.UnmarkCommand;
import sage.exception.SageException;
import sage.task.Deadline;
import sage.task.Event;
import sage.task.Note;
import sage.task.Todo;

/**
 * Converts raw user input into concrete command objects.
 */
public class Parser {
    private static final String BY_DELIMITER = " /by ";
    private static final String FROM_DELIMITER = " /from ";
    private static final String TO_DELIMITER = " /to ";
    private static final String UNKNOWN_COMMAND_MESSAGE = "I'm sorry, but I don't know what that means. "
            + "Try a valid command like todo, deadline, event, note, list, find, mark, unmark, delete, or bye.";

    /**
     * Prevents instantiation of this utility class.
     */
    private Parser() {
    }

    /**
     * Parses a full command string into a command instance.
     *
     * @param fullCommand The user-entered command text.
     * @return The corresponding command object.
     * @throws SageException If the command is empty, malformed, or unsupported.
     */
    public static Command parse(String fullCommand) throws SageException {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new SageException(UNKNOWN_COMMAND_MESSAGE);
        }

        String trimmedCommand = fullCommand.trim();

        if ("bye".equals(trimmedCommand)) {
            return new ExitCommand();
        }
        if ("list".equals(trimmedCommand)) {
            return new ListCommand();
        }
        if ("note".equals(trimmedCommand) || trimmedCommand.startsWith("note ")
                || trimmedCommand.startsWith("note\t")) {
            return parseNote(getArguments(trimmedCommand, "note"));
        }
        if (trimmedCommand.startsWith("find")) {
            return parseFind(getArguments(trimmedCommand, "find"));
        }
        if (trimmedCommand.startsWith("todo")) {
            return parseTodo(getArguments(trimmedCommand, "todo"));
        }
        if (trimmedCommand.startsWith("deadline")) {
            return parseDeadline(getArguments(trimmedCommand, "deadline"));
        }
        if (trimmedCommand.startsWith("event")) {
            return parseEvent(getArguments(trimmedCommand, "event"));
        }
        if (trimmedCommand.startsWith("mark")) {
            return new MarkCommand(parseTaskNumber(trimmedCommand, "mark"));
        }
        if (trimmedCommand.startsWith("unmark")) {
            return new UnmarkCommand(parseTaskNumber(trimmedCommand, "unmark"));
        }
        if (trimmedCommand.startsWith("delete")) {
            return new DeleteCommand(parseTaskNumber(trimmedCommand, "delete"));
        }

        throw new SageException(UNKNOWN_COMMAND_MESSAGE);
    }

    /**
     * Extracts arguments after an already matched command prefix.
     *
     * @param input The trimmed command text.
     * @param commandWord The command prefix already recognized by the dispatcher.
     * @return The trimmed arguments, possibly empty.
     */
    private static String getArguments(String input, String commandWord) {
        return input.substring(commandWord.length()).trim();
    }

    /**
     * Creates a search command after validating its keyword.
     *
     * @param keyword The search text.
     * @return The search command.
     * @throws SageException If the keyword is empty.
     */
    private static Command parseFind(String keyword) throws SageException {
        if (keyword.isEmpty()) {
            throw new SageException("The keyword of a find cannot be empty. Try: find <keyword>");
        }
        return new FindCommand(keyword);
    }

    /**
     * Creates a to-do command after validating its description.
     *
     * @param description The task description.
     * @return The command that adds the to-do task.
     * @throws SageException If the description is empty.
     */
    private static Command parseTodo(String description) throws SageException {
        if (description.isEmpty()) {
            throw new SageException("The description of a todo cannot be empty. Try: todo <task>");
        }
        return new AddCommand(new Todo(description));
    }

    /**
     * Creates a note after validating its single-line text.
     *
     * @param text The information to remember.
     * @return The command that adds the note.
     * @throws SageException If the note is empty or spans multiple lines.
     */
    private static Command parseNote(String text) throws SageException {
        if (text.isBlank()) {
            throw new SageException("The text of a note cannot be empty. Try: note <text>");
        }
        if (text.contains("\n") || text.contains("\r")) {
            throw new SageException("Please enter the note on a single line.");
        }
        return new AddCommand(new Note(text));
    }

    /**
     * Parses a deadline description and its due time.
     *
     * @param details The arguments following the deadline command.
     * @return The command that adds the deadline.
     * @throws SageException If a required deadline field is missing.
     */
    private static Command parseDeadline(String details) throws SageException {
        int byIndex = details.indexOf(BY_DELIMITER);
        if (byIndex < 0) {
            throw new SageException("The deadline format is invalid. Try: deadline <task> /by <time>");
        }
        String description = details.substring(0, byIndex).trim();
        if (description.isEmpty()) {
            throw new SageException("The description of a deadline cannot be empty. "
                    + "Try: deadline <task> /by <time>");
        }
        String deadlineTime = details.substring(byIndex + BY_DELIMITER.length()).trim();
        if (deadlineTime.isEmpty()) {
            throw new SageException("The deadline time cannot be empty. Try: deadline <task> /by <time>");
        }
        return new AddCommand(new Deadline(description, deadlineTime));
    }

    /**
     * Parses an event description and its start and end times.
     *
     * @param details The arguments following the event command.
     * @return The command that adds the event.
     * @throws SageException If a required field is missing or the end precedes the start.
     */
    private static Command parseEvent(String details) throws SageException {
        int fromIndex = details.indexOf(FROM_DELIMITER);
        int toIndex = details.indexOf(TO_DELIMITER);
        if (fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex) {
            throw new SageException("The event format is invalid. "
                    + "Try: event <task> /from <start> /to <end>");
        }
        String description = details.substring(0, fromIndex).trim();
        if (description.isEmpty()) {
            throw new SageException("The description of an event cannot be empty. "
                    + "Try: event <task> /from <start> /to <end>");
        }
        String startTime = details.substring(fromIndex + FROM_DELIMITER.length(), toIndex).trim();
        String endTime = details.substring(toIndex + TO_DELIMITER.length()).trim();
        if (startTime.isEmpty() || endTime.isEmpty()) {
            throw new SageException("The event timings cannot be empty. "
                    + "Try: event <task> /from <start> /to <end>");
        }
        Event event = new Event(description, startTime, endTime);
        if (event.getFrom() != null && event.getTo() != null && event.getFrom().isAfter(event.getTo())) {
            throw new SageException("The event end time must be after the start time.");
        }
        return new AddCommand(event);
    }

    /**
     * Extracts the required task number for mark, unmark, and delete commands.
     *
     * @param input The trimmed command text.
     * @param commandWord The matched command prefix.
     * @return The one-based task number, with range validation left to execution.
     * @throws SageException If the task number is missing or is not an integer.
     */
    private static int parseTaskNumber(String input, String commandWord) throws SageException {
        String indexText = getArguments(input, commandWord);
        if (indexText.isEmpty()) {
            throw new SageException("The task number is missing. Try: " + commandWord + " <task number>");
        }
        return parseIndex(indexText);
    }

    /**
     * Parses a task index from user input.
     *
     * @param text The numeric text entered by the user.
     * @return The parsed task number.
     * @throws SageException If the text is not a valid integer.
     */
    private static int parseIndex(String text) throws SageException {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException exception) {
            throw new SageException("The task number must be a valid integer. "
                    + "Try: mark <number>, unmark <number>, or delete <number>");
        }
    }
}
