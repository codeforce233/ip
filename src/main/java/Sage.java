public class Sage {
    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    public Sage(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        tasks = new TaskList(storage.load());
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            String fullCommand = ui.readCommand();
            boolean isListCommand = "list".equals(fullCommand.trim());
            boolean commandErrored = false;
            try {
                ui.showLine();
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (SageException e) {
                ui.showError(e.getMessage());
                commandErrored = true;
            } finally {
                if (!isListCommand && !commandErrored) {
                    ui.showLine();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Sage("data/sage.txt").run();
    }
}
