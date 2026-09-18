package sage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
                "Noted. One less thing to remember:",
                "  [T][ ] read book",
                "1 item in your list."), addResponse);
        assertEquals(String.join(System.lineSeparator(),
                "A little progress. Marked as done:",
                "  [T][X] read book"), markResponse);
        assertEquals(String.join(System.lineSeparator(),
                "Here's your list:",
                "1. [T][X] read book"), listResponse);
        assertFalse(addResponse.contains(Ui.LINE));
        assertFalse(markResponse.contains(Ui.LINE));
        assertFalse(listResponse.contains(Ui.LINE));
    }

    @Test
    void getResponse_invalidCommandReturnsErrorWithoutChangingState() {
        Sage sage = new Sage(tempDir.resolve("tasks.txt").toString());
        sage.getResponse("todo read book");

        String errorResponse = sage.getResponse("mark 99");

        assertEquals("Let's try that again. The task number is invalid. Use a number from the current list.",
                errorResponse);
        assertFalse(errorResponse.contains(Ui.LINE));
        assertFalse(sage.isExit());
        assertEquals(String.join(System.lineSeparator(),
                "Here's your list:",
                "1. [T][ ] read book"), sage.getResponse("list"));
    }

    @Test
    void getResponse_mutatingCommandsPersistAcrossInstances() {
        Path dataFile = tempDir.resolve("tasks.txt");
        Sage firstSession = new Sage(dataFile.toString());
        firstSession.getResponse("todo submit report");
        firstSession.getResponse("mark 1");

        Sage secondSession = new Sage(dataFile.toString());

        assertEquals(String.join(System.lineSeparator(),
                "Here's your list:",
                "1. [T][X] submit report"), secondSession.getResponse("list"));
    }

    @Test
    void isExit_byeThenAnotherCommand_tracksMostRecentSuccessfulCommand() {
        Sage sage = new Sage(tempDir.resolve("tasks.txt").toString());
        assertFalse(sage.isExit());

        String byeResponse = sage.getResponse("bye");

        assertEquals("Take care. One step at a time.", byeResponse);
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

        assertTrue(response.startsWith("Let's try that again. "));
        assertFalse(sage.isExit());
    }

    @Test
    void hasError_invalidThenValidCommand_resetsErrorState() {
        Sage sage = new Sage(tempDir.resolve("tasks.txt").toString());
        assertFalse(sage.hasError());
        assertEquals("", sage.getStartupMessage());

        sage.getResponse("mark 1");
        assertTrue(sage.hasError());

        sage.getResponse("list");
        assertFalse(sage.hasError());
    }

    @Test
    void getResponse_find_retainsTaskNumbersForSubsequentCommands() {
        Sage sage = new Sage(tempDir.resolve("tasks.txt").toString());
        sage.getResponse("todo Read chapter");
        sage.getResponse("todo Write report");

        assertTrue(sage.getResponse("find report").contains("2. [T][ ] Write report"));
        assertTrue(sage.getResponse("mark 2").contains("[T][X] Write report"));
        assertTrue(sage.getResponse("list").contains("1. [T][ ] Read chapter"));
    }

    @Test
    void getResponse_corruptFile_preservesOriginalAndExplainsRecovery() throws Exception {
        Path dataFile = tempDir.resolve("tasks.txt");
        String originalData = "T|0|keep this task\nnot a valid record\n";
        Files.writeString(dataFile, originalData);

        Sage sage = new Sage(dataFile.toString());

        assertFalse(sage.getStartupMessage().isBlank());
        sage.getResponse("todo replacement");
        assertTrue(sage.hasError());
        assertEquals(originalData, Files.readString(dataFile));
        assertFalse(sage.getResponse("list").contains("replacement"));
    }

    @Test
    void getResponse_failedSave_doesNotReportSuccessOrKeepUnsavedTask() throws Exception {
        Path dataFolder = tempDir.resolve("data");
        Files.createDirectory(dataFolder);
        Sage sage = new Sage(dataFolder.resolve("tasks.txt").toString());
        Files.delete(dataFolder);
        Files.writeString(dataFolder, "a file now occupies the data directory");

        sage.getResponse("todo cannot save");

        assertTrue(sage.hasError());
        assertFalse(sage.getResponse("list").contains("cannot save"));
        assertEquals("a file now occupies the data directory", Files.readString(dataFolder));
    }

    @Test
    void run_endOfInput_exitsCleanlyWithoutInventingACommand() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setIn(new ByteArrayInputStream(new byte[0]));
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
        try {
            new Sage(tempDir.resolve("tasks.txt").toString()).run();

            String output = captured.toString(StandardCharsets.UTF_8);
            assertTrue(output.contains("Sage"));
            assertFalse(output.contains("Let's try that again."));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
