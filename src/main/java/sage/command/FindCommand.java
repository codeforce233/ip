package sage.command;

import java.util.List;

import sage.core.TaskList;
import sage.exception.SageException;
import sage.storage.Storage;
import sage.task.Task;
import sage.ui.Ui;

public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws SageException {
        List<Task> matches = tasks.find(keyword);
        ui.showMatchingTasks(matches);
    }
}
