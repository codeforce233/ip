package sage.command;

import sage.core.TaskList;
import sage.exception.SageException;
import sage.storage.Storage;
import sage.ui.Ui;

public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws SageException;

    public boolean isExit() {
        return false;
    }
}
