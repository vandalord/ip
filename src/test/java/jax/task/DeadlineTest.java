package jax.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DeadlineTest {
    @Test
    public void testToString_validDateMatch() {
        Deadline deadline = new Deadline("read book", "2019-10-15 1800");
        assertEquals("[D][ ] read book (by: Oct 15 2019, 6:00pm)", deadline.toString());
    }
}
