package jax;

import jax.command.Command;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {

    @Test
    public void parseCommand_validInput() throws JaxException {
        assertEquals(Command.LIST, Parser.parseCommand("list"));

        assertEquals(Command.TODO, Parser.parseCommand("todo read book"));
    }

    @Test
    public void parseCommand_invalidInputException() {
        try {
            Parser.parseCommand("blah");
            fail();
        } catch (JaxException e) {
            assertEquals("Error - Invalid Input.", e.getMessage());
        }
    }

    @Test
    public void parseTodo_emptyDescriptionException() {
        String[] input = {"todo"};

        JaxException thrown = assertThrows(JaxException.class, () -> {
            Parser.parseTodo(input);
        });

        assertEquals("Error - Todo description cannot be empty.", thrown.getMessage());
    }

    @Test
    public void parseDeadline_invalidFormatException() {
        String[] input = {"deadline", "submit assignment /by tomorrow"};

        JaxException thrown = assertThrows(JaxException.class, () -> {
            Parser.parseDeadline(input);
        });

        assertEquals("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)", thrown.getMessage());
    }
}