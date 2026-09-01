import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private static final int MAX_TASKS = 100;
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> tasks) {
        this.tasks = new ArrayList<>(tasks);
    }

    public void add(Task task) {
        if (tasks.size() >= MAX_TASKS) {
            throw new IllegalStateException("You have reached the maximum number of tasks.");
        }
        tasks.add(task);
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public void markDone(int index) {
        tasks.get(index).markAsDone();
    }

    public void markUndone(int index) {
        tasks.get(index).markAsNotDone();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
