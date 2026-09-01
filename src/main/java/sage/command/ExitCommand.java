package sage.command;

import sage.core.TaskList;
import sage.storage.Storage;
import sage.ui.Ui;

/**
 * Terminates the application loop after displaying a farewell message.
 */
public class ExitCommand extends Command {
    /**
     * Shows the exit message to the user.
     *
     * @param tasks the task list, not used by this command
     * @param ui the UI used for the farewell message
     * @param storage the storage system, not used by this command
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showBye();
    }

    /**
     * Indicates that this command exits the application.
     *
     * @return true because exiting the program is the intended effect
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
