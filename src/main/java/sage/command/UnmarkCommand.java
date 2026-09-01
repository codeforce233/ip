package sage.command;

import sage.core.TaskList;
import sage.exception.SageException;
import sage.storage.Storage;
import sage.ui.Ui;

/**
 * Marks a task as not complete and saves the updated list.
 */
public class UnmarkCommand extends Command {
    private final int index;

    /**
     * Creates a command that marks the task at the given one-based position as undone.
     *
     * @param index the one-based position of the task to mark incomplete
     */
    public UnmarkCommand(int index) {
        this.index = index;
    }

    /**
     * Marks the selected task as incomplete, persists the change, and shows a confirmation.
     *
     * @param tasks the task list to modify
     * @param ui the UI used for feedback
     * @param storage the storage system used to save the list
     * @throws SageException if the task number is invalid
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SageException {
        if (index <= 0 || index > tasks.size()) {
            throw new SageException("The task number is invalid. Use a number from the current list.");
        }
        tasks.markUndone(index - 1);
        storage.save(tasks.getTasks());
        ui.showMarkedUndone(tasks.get(index - 1));
    }
}
