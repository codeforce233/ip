public class MarkCommand extends Command {
    private final int index;

    public MarkCommand(int index) {
        this.index = index;
    }

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
