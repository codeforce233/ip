package sage.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import sage.core.TaskList;
import sage.exception.SageException;
import sage.storage.Storage;
import sage.task.Task;
import sage.task.Todo;
import sage.ui.Ui;

class CommandTest {
    @TempDir
    Path tempDir;

    @Test
    void addCommand_executesAndSavesTask() {
        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempDir.resolve("tasks.txt").toString());
        Ui ui = new Ui();

        String output = captureOutput(() -> new AddCommand(new Todo("read chapter")).execute(tasks, ui, storage));

        assertEquals(1, tasks.size());
        assertEquals("read chapter", tasks.get(0).getDescription());
        assertTrue(output.contains("Got it. I've added this task:"));
    }

    @Test
    void deleteCommand_removesTaskAndPersistsChange() throws SageException {
        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempDir.resolve("tasks.txt").toString());
        Ui ui = new Ui();
        Task task = new Todo("write summary");
        tasks.add(task);

        String output = captureOutput(() -> {
            try {
                new DeleteCommand(1).execute(tasks, ui, storage);
            } catch (SageException e) {
                throw new RuntimeException(e);
            }
        });

        assertEquals(0, tasks.size());
        assertTrue(output.contains("Noted. I've removed this task:"));
    }

    @Test
    void markAndUnmarkCommands_toggleCompletionState() throws SageException {
        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempDir.resolve("tasks.txt").toString());
        Ui ui = new Ui();
        tasks.add(new Todo("review PR"));

        String markOutput = captureOutput(() -> {
            try {
                new MarkCommand(1).execute(tasks, ui, storage);
            } catch (SageException e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals("X", tasks.get(0).getStatusIcon());
        assertTrue(markOutput.contains("Nice! I've marked this task as done:"));

        String unmarkOutput = captureOutput(() -> {
            try {
                new UnmarkCommand(1).execute(tasks, ui, storage);
            } catch (SageException e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals(" ", tasks.get(0).getStatusIcon());
        assertTrue(unmarkOutput.contains("OK, I've marked this task as not done yet:"));
    }

    @Test
    void listAndExitCommandsDisplayExpectedOutput() {
        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempDir.resolve("tasks.txt").toString());
        Ui ui = new Ui();
        tasks.add(new Todo("plan sprint"));

        String listOutput = captureOutput(() -> new ListCommand().execute(tasks, ui, storage));
        String exitOutput = captureOutput(() -> new ExitCommand().execute(tasks, ui, storage));

        assertTrue(listOutput.contains("Here are the tasks in your list:"));
        assertTrue(exitOutput.contains("Bye. Hope to see you again soon!"));
        assertTrue(new ExitCommand().isExit());
    }

    private String captureOutput(Runnable action) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            action.run();
            return captured.toString();
        } finally {
            System.setOut(originalOut);
        }
    }
}
