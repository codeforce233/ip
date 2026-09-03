package sage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import sage.ui.Ui;

class SageTest {
    @TempDir
    Path tempDir;

    @Test
    void getResponse_multipleCommandsPreserveStateAndHideConsoleSeparators() {
        Sage sage = new Sage(tempDir.resolve("tasks.txt").toString());

        String addResponse = sage.getResponse("todo read book");
        String markResponse = sage.getResponse("mark 1");
        String listResponse = sage.getResponse("list");

        assertEquals(String.join(System.lineSeparator(),
                "Got it. I've added this task:",
                "  [T][ ] read book",
                "Now you have 1 tasks in the list."), addResponse);
        assertEquals(String.join(System.lineSeparator(),
                "Nice! I've marked this task as done:",
                "  [T][X] read book"), markResponse);
        assertEquals(String.join(System.lineSeparator(),
                "Here are the tasks in your list:",
                "1.[T][X] read book"), listResponse);
        assertFalse(addResponse.contains(Ui.LINE));
        assertFalse(markResponse.contains(Ui.LINE));
        assertFalse(listResponse.contains(Ui.LINE));
    }

    @Test
    void getResponse_invalidCommandReturnsErrorWithoutChangingState() {
        Sage sage = new Sage(tempDir.resolve("tasks.txt").toString());
        sage.getResponse("todo read book");

        String errorResponse = sage.getResponse("mark 99");

        assertEquals("OOPS!!! The task number is invalid. Use a number from the current list.", errorResponse);
        assertFalse(errorResponse.contains(Ui.LINE));
        assertFalse(sage.isExit());
        assertEquals(String.join(System.lineSeparator(),
                "Here are the tasks in your list:",
                "1.[T][ ] read book"), sage.getResponse("list"));
    }

    @Test
    void getResponse_mutatingCommandsPersistAcrossInstances() {
        Path dataFile = tempDir.resolve("tasks.txt");
        Sage firstSession = new Sage(dataFile.toString());
        firstSession.getResponse("todo submit report");
        firstSession.getResponse("mark 1");

        Sage secondSession = new Sage(dataFile.toString());

        assertEquals(String.join(System.lineSeparator(),
                "Here are the tasks in your list:",
                "1.[T][X] submit report"), secondSession.getResponse("list"));
    }

    @Test
    void isExit_byeThenAnotherCommand_tracksMostRecentSuccessfulCommand() {
        Sage sage = new Sage(tempDir.resolve("tasks.txt").toString());
        assertFalse(sage.isExit());

        String byeResponse = sage.getResponse("bye");

        assertEquals("Bye. Hope to see you again soon!", byeResponse);
        assertTrue(sage.isExit());

        sage.getResponse("list");

        assertFalse(sage.isExit());
    }

    @Test
    void isExit_invalidCommandAfterBye_resetsExitState() {
        Sage sage = new Sage(tempDir.resolve("tasks.txt").toString());
        sage.getResponse("bye");
        assertTrue(sage.isExit());

        String response = sage.getResponse("not a command");

        assertTrue(response.startsWith("OOPS!!! "));
        assertFalse(sage.isExit());
    }
}
