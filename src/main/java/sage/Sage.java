package sage;

import sage.command.Command;
import sage.core.Parser;
import sage.core.TaskList;
import sage.exception.SageException;
import sage.storage.Storage;
import sage.ui.Ui;

/**
 * Entry point for the Sage task manager application.
 */
public class Sage {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates a Sage application instance backed by a data file.
     *
     * @param filePath The path where task data is stored.
     */
    public Sage(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    /**
     * Starts the interactive command loop for the application.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            boolean isListCommand = "list".equals(fullCommand.trim());
            boolean hasCommandErrored = false;
            try {
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (SageException e) {
                ui.showError(e.getMessage());
                hasCommandErrored = true;
            } finally {
                if (!isListCommand && !hasCommandErrored) {
                    ui.showLine();
                }
            }
        }
    }

    /**
     * Runs the task manager with the default persistent data path.
     *
     * @param args Command-line arguments, currently unused.
     */
    public static void main(String[] args) {
        new Sage("data/sage.txt").run();
    }
}
