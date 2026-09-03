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
import sage.task.Todo;

/**
 * Converts raw user input into concrete command objects.
 */
public class Parser {
    private static final String UNKNOWN_COMMAND_MESSAGE = "I'm sorry, but I don't know what that means. "
            + "Try a valid command like todo, deadline, event, list, find, mark, unmark, delete, or bye.";

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
        if (trimmedCommand.startsWith("find")) {
            String keyword = trimmedCommand.length() > 4 ? trimmedCommand.substring(4).trim() : "";
            if (keyword.isEmpty()) {
                throw new SageException("The keyword of a find cannot be empty. Try: find <keyword>");
            }
            return new FindCommand(keyword);
        }
        if (trimmedCommand.startsWith("todo")) {
            String description = trimmedCommand.length() > 4 ? trimmedCommand.substring(4).trim() : "";
            if (description.isEmpty()) {
                throw new SageException("The description of a todo cannot be empty. Try: todo <task>");
            }
            return new AddCommand(new Todo(description));
        }
        if (trimmedCommand.startsWith("deadline")) {
            String deadlineDetails = trimmedCommand.length() > 8 ? trimmedCommand.substring(8).trim() : "";
            int byIndex = deadlineDetails.indexOf(" /by ");
            if (deadlineDetails.isEmpty() || byIndex < 0) {
                throw new SageException("The deadline format is invalid. Try: deadline <task> /by <time>");
            }
            String description = deadlineDetails.substring(0, byIndex).trim();
            if (description.isEmpty()) {
                throw new SageException("The description of a deadline cannot be empty. "
                        + "Try: deadline <task> /by <time>");
            }
            String deadlineTime = deadlineDetails.substring(byIndex + 5).trim();
            if (deadlineTime.isEmpty()) {
                throw new SageException("The deadline time cannot be empty. Try: deadline <task> /by <time>");
            }
            return new AddCommand(new Deadline(description, deadlineTime));
        }
        if (trimmedCommand.startsWith("event")) {
            String eventDetails = trimmedCommand.length() > 5 ? trimmedCommand.substring(5).trim() : "";
            int fromIndex = eventDetails.indexOf(" /from ");
            int toIndex = eventDetails.indexOf(" /to ");
            if (eventDetails.isEmpty() || fromIndex < 0 || toIndex < 0 || toIndex <= fromIndex) {
                throw new SageException("The event format is invalid. "
                        + "Try: event <task> /from <start> /to <end>");
            }
            String description = eventDetails.substring(0, fromIndex).trim();
            if (description.isEmpty()) {
                throw new SageException("The description of an event cannot be empty. "
                        + "Try: event <task> /from <start> /to <end>");
            }
            String startTime = eventDetails.substring(fromIndex + 7, toIndex).trim();
            String endTime = eventDetails.substring(toIndex + 4).trim();
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
        if (trimmedCommand.startsWith("mark")) {
            String indexText = trimmedCommand.length() > 4 ? trimmedCommand.substring(4).trim() : "";
            if (indexText.isEmpty()) {
                throw new SageException("The task number is missing. Try: mark <task number>");
            }
            return new MarkCommand(parseIndex(indexText));
        }
        if (trimmedCommand.startsWith("unmark")) {
            String indexText = trimmedCommand.length() > 6 ? trimmedCommand.substring(6).trim() : "";
            if (indexText.isEmpty()) {
                throw new SageException("The task number is missing. Try: unmark <task number>");
            }
            return new UnmarkCommand(parseIndex(indexText));
        }
        if (trimmedCommand.startsWith("delete")) {
            String indexText = trimmedCommand.length() > 6 ? trimmedCommand.substring(6).trim() : "";
            if (indexText.isEmpty()) {
                throw new SageException("The task number is missing. Try: delete <task number>");
            }
            return new DeleteCommand(parseIndex(indexText));
        }

        throw new SageException(UNKNOWN_COMMAND_MESSAGE);
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
