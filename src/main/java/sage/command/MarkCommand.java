package sage.command;

import sage.core.TaskList;
import sage.exception.SageException;
import sage.storage.Storage;
import sage.ui.Ui;

/**
 * Marks a task as complete and saves the updated task list.
 */
public class MarkCommand extends Command {
    private final int index;

    /**
     * Creates a command that marks the task at the given one-based position as done.
     *
     * @param index the one-based position of the task to mark complete.
     */
    public MarkCommand(int index) {
        this.index = index;
    }

    /**
     * Marks the selected task as complete, persists the change, and shows a confirmation.
     *
     * @param tasks the task list to modify.
     * @param ui the user interface used to display feedback.
     * @param storage the storage system used to save the list.
     * @throws SageException if the task number is invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SageException {
        if (index <= 0 || index > tasks.size()) {
            throw new SageException("The task number is invalid. Use a number from the current list.");
        }
        tasks.markDone(index - 1);
        storage.save(tasks.getTasks());
        ui.showMarkedDone(tasks.get(index - 1));
    }
}
