package sage.task;

/**
 * Represents a single task in the task list.
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;

    /**
     * Creates a task with the given description and category.
     *
     * @param description the user-facing description of the task
     * @param type the category of the task
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    /**
     * Returns the status marker used in the console output.
     *
     * @return "X" when complete and a blank space otherwise
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the task type.
     *
     * @return the category of the task
     */
    public TaskType getType() {
        return type;
    }

    /**
     * Marks the task as complete.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not complete.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the task description.
     *
     * @return the description entered by the user
     */
    public String getDescription() {
        return description;
    }

    /**
     * Formats the task for display in the user interface.
     *
     * @return the string representation of the task
     */
    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
