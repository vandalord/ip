package jax;

import jax.command.Command;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
}