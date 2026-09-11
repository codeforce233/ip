package sage.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import sage.task.Task;

/**
 * Stores and manages the collection of tasks in memory.
 */
public class TaskList {
    private static final int MAX_TASKS = 100;
    private final List<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with the provided tasks.
     *
     * @param tasks The initial tasks to populate the list with.
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list if it is below the capacity limit.
     *
     * @param task The task to add.
     * @throws IllegalStateException If the task list has reached its maximum capacity.
     */
    public void add(Task task) {
        if (tasks.size() >= MAX_TASKS) {
            throw new IllegalStateException("You have reached the maximum number of tasks.");
        }
        tasks.add(task);
        // A successful insertion must preserve the capacity enforced above.
        assert tasks.size() <= MAX_TASKS : "Adding a task must not exceed the task-list capacity";
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index The zero-based index of the task to delete.
     * @return The removed task.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given index as complete.
     *
     * @param index The zero-based index of the task.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public void markDone(int index) {
        tasks.get(index).markAsDone();
        // Task subclasses must honor the completion-state contract before callers persist the change.
        assert "X".equals(tasks.get(index).getStatusIcon()) : "Marked task must be complete";
    }

    /**
     * Marks the task at the given index as incomplete.
     *
     * @param index The zero-based index of the task.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public void markUndone(int index) {
        tasks.get(index).markAsNotDone();
        // Unmarking must restore the incomplete state before callers persist the change.
        assert " ".equals(tasks.get(index).getStatusIcon()) : "Unmarked task must be incomplete";
    }

    /**
     * Returns the task at the given index.
     *
     * @param index The zero-based index of the task.
     * @return The task at that index.
     * @throws IndexOutOfBoundsException If the index is outside the task list.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return The size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the backing list of tasks.
     *
     * @return The current task collection.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Returns tasks whose descriptions contain the given keyword, ignoring case.
     * A null or blank keyword produces an empty result.
     *
     * @param keyword The keyword to find in task descriptions.
     * @return A new mutable list containing the matching tasks in their original order.
     */
    public List<Task> find(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String normalizedKeyword = keyword.trim().toLowerCase();
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(normalizedKeyword))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
