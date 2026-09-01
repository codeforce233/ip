package sage.command;

import sage.core.TaskList;
import sage.storage.Storage;
import sage.task.Task;
import sage.ui.Ui;

public class AddCommand extends Command {
    private final Task task;

    public AddCommand(Task task) {
        this.task = task;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.add(task);
        storage.save(tasks.getTasks());
        ui.showAddedTask(task, tasks.size());
    }
}
