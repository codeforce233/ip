package sage.core;

import sage.command.AddCommand;
import sage.command.Command;
import sage.command.DeleteCommand;
import sage.command.ExitCommand;
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
    /**
     * Parses a full command string into a command instance.
     *
     * @param fullCommand the user-entered command text
     * @return the corresponding command object
     * @throws SageException if the command is empty or malformed
     */
    public static Command parse(String fullCommand) throws SageException {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new SageException("I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, list, mark, unmark, delete, or bye.");
        }

        String input = fullCommand.trim();

        if ("bye".equals(input)) {
            return new ExitCommand();
        }
        if ("list".equals(input)) {
            return new ListCommand();
        }
        if (input.startsWith("todo")) {
            String description = input.length() > 4 ? input.substring(4).trim() : "";
            if (description.isEmpty()) {
                throw new SageException("The description of a todo cannot be empty. Try: todo <task>");
            }
            return new AddCommand(new Todo(description));
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
            return new AddCommand(new Deadline(description, by));
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
            return new AddCommand(event);
        }
        if (input.startsWith("mark")) {
            String indexText = input.length() > 4 ? input.substring(4).trim() : "";
            if (indexText.isEmpty()) {
                throw new SageException("The task number is missing. Try: mark <task number>");
            }
            return new MarkCommand(parseIndex(indexText));
        }
        if (input.startsWith("unmark")) {
            String indexText = input.length() > 6 ? input.substring(6).trim() : "";
            if (indexText.isEmpty()) {
                throw new SageException("The task number is missing. Try: unmark <task number>");
            }
            return new UnmarkCommand(parseIndex(indexText));
        }
        if (input.startsWith("delete")) {
            String indexText = input.length() > 6 ? input.substring(6).trim() : "";
            if (indexText.isEmpty()) {
                throw new SageException("The task number is missing. Try: delete <task number>");
            }
            return new DeleteCommand(parseIndex(indexText));
        }

        throw new SageException("I'm sorry, but I don't know what that means. Try a valid command like todo, deadline, event, list, mark, unmark, delete, or bye.");
    }

    /**
     * Parses a task index from user input.
     *
     * @param text the numeric text entered by the user
     * @return the parsed task number
     * @throws SageException if the text is not a valid integer
     */
    private static int parseIndex(String text) throws SageException {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            throw new SageException("The task number must be a valid integer. Try: mark <number>, unmark <number>, or delete <number>");
        }
    }
}
