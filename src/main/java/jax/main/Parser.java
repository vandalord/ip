package jax.main;

import java.time.format.DateTimeParseException;

import jax.command.Command;
import jax.contact.Contact;
import jax.task.Deadline;
import jax.task.Event;
import jax.task.Todo;


/**
 * Parses user input into actionable commands and task objects.
 * This class handles the logic of deciphering strings into specific operations
 * and validating the format of arguments (like dates and descriptions).
 */
public class Parser {

    /**
     * Parses the first word of the user's input to determine the command type.
     * @param line The full raw input string entered by the user.
     * @return The corresponding {@link Command} enum value.
     * @throws JaxException If the command word is not recognized or invalid.
     */
    public static Command parseCommand(String line) throws JaxException {
        String[] input = line.split(" ", 2);
        String commandStr = input[0].toUpperCase();
        try {
            return Command.valueOf(commandStr);
        } catch (IllegalArgumentException e) {
            throw new JaxException("Error - Invalid Input.");
        }
    }

    /**
     * Splits the user's input line into two parts: the command word and the arguments.
     * @param line The full raw input string.
     * @return A String array of size 2. Index 0 is the command, Index 1 is the rest of the string.
     */
    public static String[] splitCommand(String line) {
        return line.split(" ", 2);
    }

    /**
     * Parses the arguments for a Todo task.
     * Validates that the description is not empty.
     * @param input The split input array where index 1 contains the description.
     * @return A new {@link Todo} object.
     * @throws JaxException If the description is missing or empty.
     */
    public static Todo parseTodo(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Todo description cannot be empty.");
        }
        return new Todo(input[1]);
    }

    /**
     * Parses the arguments for a Deadline task.
     * Splits the arguments by the "/by" delimiter to separate description and time.
     * @param input The split input array where index 1 contains the arguments.
     * @return A new {@link Deadline} object.
     * @throws JaxException If the description is empty, the time is missing, or the date format is invalid.
     */
    public static Deadline parseDeadline(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Deadline description cannot be empty.");
        }
        String[] segments = input[1].split(" /by ");
        if (segments.length < 2) {
            throw new JaxException("Error - Deadline time not specified.");
        }
        try {
            return new Deadline(segments[0], segments[1]);
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)");
        }
    }

    /**
     * Parses the arguments for an Event task.
     * Splits the arguments by "/from" and "/to" delimiters to extract timing details.
     * @param input The split input array where index 1 contains the arguments.
     * @return A new {@link Event} object.
     * @throws JaxException If the description, start time, or end time are missing, or if date formats are invalid.
     */
    public static Event parseEvent(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Event description cannot be empty.");
        }
        String[] fromSplit = input[1].split(" /from ");
        if (fromSplit.length < 2) {
            throw new JaxException("Error - Start time not specified.");
        }
        String[] toSplit = fromSplit[1].split(" /to ");
        if (toSplit.length < 2) {
            throw new JaxException("Error - End time not specified.");
        }

        try {
            return new Event(fromSplit[0], toSplit[0], toSplit[1]);
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)");
        }
    }
    /**
     * Parses the keyword for finding task.
     * Splits the arguments " " delimiters to extract keyword.
     * @param input The split input array where index 1 contains the arguments.
     * @return keyword in the String format.
     * @throws JaxException If no keywords are present.
     */
    public static String parseFind(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Please specify a keyword to search for.");
        }

        return input[1].trim();
    }

    /**
     * Parses user input into a Contact object.
     * Expected format: contact <name> /p <phone> /e <email>
     */
    public static Contact parseContact(String[] input) throws JaxException {
        if (input.length < 2) {
            throw new JaxException("Error - Contact command format: contact <name> /p <phone> /e <email>");
        }

        String fullDetails = input[1];

        try {
            // Split by the phone and email delimiters
            String name = fullDetails.split(" /p ")[0].trim();
            String contactInfo = fullDetails.split(" /p ")[1];
            String phone = contactInfo.split(" /e ")[0].trim();
            String email = contactInfo.split(" /e ")[1].trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                throw new JaxException("Error - Name, phone, and email cannot be empty.");
            }

            return new Contact(name, phone, email);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JaxException("Error - Missing /p or /e flags for contact details.");
        }
    }

    /**
     * Extracts the contact name for deletion.
     * Expected format: delcontact <name>
     */
    public static String parseDeleteContact(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Please specify a contact name to delete.");
        }
        return input[1].trim();
    }
}
