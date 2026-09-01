package sage.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import sage.command.AddCommand;
import sage.command.Command;
import sage.command.DeleteCommand;
import sage.command.ExitCommand;
import sage.command.FindCommand;
import sage.command.ListCommand;
import sage.command.MarkCommand;
import sage.command.UnmarkCommand;
import sage.exception.SageException;
import sage.storage.Storage;
import sage.task.TaskType;
import sage.ui.Ui;

class ParserTest {
    @TempDir
    Path tempDir;

    @Test
    void parse_validCommandsReturnExpectedCommandTypes() throws SageException {
        Command bye = Parser.parse("bye");
        assertInstanceOf(ExitCommand.class, bye);

        Command list = Parser.parse("list");
        assertInstanceOf(ListCommand.class, list);

        Command find = Parser.parse("find book");
        assertInstanceOf(FindCommand.class, find);

        Command todo = Parser.parse("todo read book");
        assertInstanceOf(AddCommand.class, todo);

        Command deadline = Parser.parse("deadline write report /by 2025-01-02");
        assertInstanceOf(AddCommand.class, deadline);

        Command event = Parser.parse("event meeting /from 2025-01-02 /to 2025-01-03");
        assertInstanceOf(AddCommand.class, event);

        Command mark = Parser.parse("mark 2");
        assertInstanceOf(MarkCommand.class, mark);

        Command unmark = Parser.parse("unmark 2");
        assertInstanceOf(UnmarkCommand.class, unmark);

        Command delete = Parser.parse("delete 2");
        assertInstanceOf(DeleteCommand.class, delete);
    }

    @Test
    void parse_invalidCommandsThrowSageException() {
        assertThrows(SageException.class, () -> Parser.parse(""));
        assertThrows(SageException.class, () -> Parser.parse("find "));
        assertThrows(SageException.class, () -> Parser.parse("todo "));
        assertThrows(SageException.class, () -> Parser.parse("deadline finish /by "));
        assertThrows(SageException.class, () -> Parser.parse("event meeting /from 2025-01-03 /to 2025-01-02"));
        assertThrows(SageException.class, () -> Parser.parse("mark"));
    }

    @Test
    void parse_executesCommandOnTaskList() throws SageException {
        TaskList tasks = new TaskList();
        Storage storage = new Storage(tempDir.resolve("tasks.txt").toString());
        Ui ui = new Ui();

        Parser.parse("todo read book").execute(tasks, ui, storage);
        assertEquals(1, tasks.size());
        assertEquals("read book", tasks.get(0).getDescription());
        assertEquals(TaskType.TODO, tasks.get(0).getType());

        Parser.parse("deadline submit /by 2025-01-02").execute(tasks, ui, storage);
        assertEquals(2, tasks.size());
        assertTrue(tasks.get(1).toString().contains("submit"));
    }
}
