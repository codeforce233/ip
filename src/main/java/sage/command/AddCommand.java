package sage.command;

import sage.core.TaskList;
import sage.storage.Storage;
import sage.task.Task;
import sage.ui.Ui;

/**
 * Adds a new task to the list and persists the updated collection.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Creates a command to add a specific task.
     *
     * @param task the task to add
     */
    public AddCommand(Task task) {
        this.task = task;
    }

    /**
     * Adds the task to the list, saves it, and shows confirmation.
     *
     * @param tasks the task list to update
     * @param ui the user interface to display the result
     * @param storage the storage system to persist the update
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showAddedTask(task, tasks.size());
    }
}
