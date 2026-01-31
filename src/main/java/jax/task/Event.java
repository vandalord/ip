package jax.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an "Event" task.
 * An Event is a task with a start time and end time.
 */
public class Event extends Task {

    /** The start date and time of the event. */
    protected LocalDateTime from;
    /** The end date and time of the event. */
    protected LocalDateTime to;

    /**
     * Creates a new Event task.
     * @param description The description of the event.
     * @param from The start time in the format "yyyy-MM-dd HHmm".
     * @param to The end time in the format "yyyy-MM-dd HHmm".
     * @throws DateTimeParseException If the 'from' or 'to' strings do not match the required format.
     */
    public Event(String description, String from, String to) throws DateTimeParseException {
        super(description);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        this.from = LocalDateTime.parse(from, formatter);
        this.to = LocalDateTime.parse(to, formatter);
    }

    /**
     * Checks if the event occurs on a specific date.
     * This implementation checks if the given date falls between the event's start and end dates (inclusive).
     * @param date The date to check against.
     * @return true if the date is between the start and end dates, false otherwise.
     */
    @Override
    public boolean occursOn(LocalDate date) {
        LocalDate fromDate = from.toLocalDate();
        LocalDate toDate = to.toLocalDate();

        return !date.isBefore(fromDate) && !date.isAfter(toDate);
    }

    /**
     * Returns the string representation of the Event.
     * Adds "[E]" string identifier to the standard string.
     * @return Formatted string representing the Event.
     */
    @Override
    public String toString() {
        return "[E]"
            + super.toString()
            + " (from: "
            + from.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma"))
            + " to: "
            + to.format(DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")) + ")";
    }
}
