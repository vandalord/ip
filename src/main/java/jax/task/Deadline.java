package jax.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A Deadline is a task with a specific date and time.
 */
@SuppressWarnings("CanBeFinal")
public class Deadline extends Task {

    /**
     * The date and time by which the task must be completed.
     */
    protected LocalDateTime by;

    /**
     * Creates a new Deadline task.
     * @param description The description of the task.
     * @param by The deadline in the format "yyyy-MM-dd HHmm".
     * @throws DateTimeParseException If the 'by' string does not match the format.
     */
    public Deadline(String description, String by) throws DateTimeParseException {
        super(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        this.by = LocalDateTime.parse(by, formatter);
    }

    /**
     * Checks if the deadline of this task occurs on the specified date.
     * @param date The date to check for.
     * @return true if the deadline falls on the given date, false otherwise.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        return by.toLocalDate().equals(date);
    }


    /**
     * Returns the string representation of the Deadline.
     * Adds "[D]" string identifier to the standard string.
     * @return Formatted string representing the Deadline.
     */
    @Override
    public String toString() {
        return "[D]"
            + super.toString()
            + " (by: "
            + by.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"))
            + ")";
    }
}
