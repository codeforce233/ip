package sage.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import sage.task.Task;
import sage.task.TaskType;

class TaskListTest {
    @Test
    void addAndGetTrackTasksInOrder() {
        TaskList tasks = new TaskList();
        Task first = new Task("read", TaskType.TODO);
        Task second = new Task("write", TaskType.TODO);

        tasks.add(first);
        tasks.add(second);

        assertEquals(2, tasks.size());
        assertSame(first, tasks.get(0));
        assertSame(second, tasks.get(1));
    }

    @Test
    void deleteAndMarkingOperationsChangeTaskState() {
        TaskList tasks = new TaskList();
        Task task = new Task("study", TaskType.TODO);
        tasks.add(task);

        Task removed = tasks.delete(0);
        assertSame(task, removed);
        assertEquals(0, tasks.size());

        tasks.add(task);
        tasks.markDone(0);
        assertEquals("X", tasks.get(0).getStatusIcon());

        tasks.markUndone(0);
        assertEquals(" ", tasks.get(0).getStatusIcon());
    }

    @Test
    void addBeyondLimitThrowsIllegalStateException() {
        TaskList tasks = new TaskList();

        for (int i = 0; i < 100; i++) {
            tasks.add(new Task("task " + i, TaskType.TODO));
        }

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> tasks.add(new Task("overflow", TaskType.TODO)));
        assertEquals("You have reached the maximum number of tasks.", thrown.getMessage());
    }
}
