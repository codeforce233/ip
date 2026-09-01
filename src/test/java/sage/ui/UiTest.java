package sage.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import org.junit.jupiter.api.Test;

import sage.task.Task;
import sage.task.TaskType;

class UiTest {
    @Test
    void readCommand_readsLineFromInput() {
        InputStream originalIn = System.in;
        System.setIn(new ByteArrayInputStream("todo read book\n".getBytes()));
        try {
            Ui ui = new Ui();
            assertEquals("todo read book", ui.readCommand());
        } finally {
            System.setIn(originalIn);
        }
    }

    @Test
    void displayMethods_printExpectedContent() {
        Ui ui = new Ui();
        Task task = new Task("watch lecture", TaskType.TODO);

        String welcomeOutput = captureOutput(ui::showWelcome);
        assertTrue(welcomeOutput.contains("Hello! I'm Sage."));

        String listOutput = captureOutput(() -> ui.showTaskList(List.of(task)));
        assertTrue(listOutput.contains("Here are the tasks in your list:"));
        assertTrue(listOutput.contains("watch lecture"));

        String matchedOutput = captureOutput(() -> ui.showMatchingTasks(List.of(task)));
        assertTrue(matchedOutput.contains("Here are the matching tasks in your list:"));
        assertTrue(matchedOutput.contains("watch lecture"));

        String addedOutput = captureOutput(() -> ui.showAddedTask(task, 1));
        assertTrue(addedOutput.contains("Got it. I've added this task:"));

        String removedOutput = captureOutput(() -> ui.showRemovedTask(task, 0));
        assertTrue(removedOutput.contains("Noted. I've removed this task:"));

        String markedOutput = captureOutput(() -> ui.showMarkedDone(task));
        assertTrue(markedOutput.contains("Nice! I've marked this task as done:"));

        String unmarkedOutput = captureOutput(() -> ui.showMarkedUndone(task));
        assertTrue(unmarkedOutput.contains("OK, I've marked this task as not done yet:"));

        String errorOutput = captureOutput(() -> ui.showError("bad input"));
        assertTrue(errorOutput.contains("OOPS!!! bad input"));

        String byeOutput = captureOutput(ui::showBye);
        assertTrue(byeOutput.contains("Bye. Hope to see you again soon!"));
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
