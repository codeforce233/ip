package sage.core;

import java.util.ArrayList;
import java.util.List;

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
     * @param tasks the initial tasks to populate the list with
     */
    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the list if it is below the capacity limit.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        if (tasks.size() >= MAX_TASKS) {
            throw new IllegalStateException("You have reached the maximum number of tasks.");
        }
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given index.
     *
     * @param index the zero-based index of the task to delete
     * @return the removed task
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given index as complete.
     *
     * @param index the zero-based index of the task
     */
    public void markDone(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the given index as incomplete.
     *
     * @param index the zero-based index of the task
     */
    public void markUndone(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Returns the task at the given index.
     *
     * @param index the zero-based index of the task
     * @return the task at that index
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Gets the number of tasks currently in the list.
     *
     * @return the size of the task list
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the backing list of tasks.
     *
     * @return the current task collection
     */
    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> find(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }

        String normalizedKeyword = keyword.trim().toLowerCase();
        List<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(normalizedKeyword)) {
                matches.add(task);
            }
        }
        return matches;
    }
}
