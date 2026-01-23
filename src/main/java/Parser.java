import java.time.format.DateTimeParseException;

public class Parser {
    public static Command parseCommand(String line) throws JaxException {
        String[] input = line.split(" ", 2);
        String commandStr = input[0].toUpperCase();
        try {
            return Command.valueOf(commandStr);
        } catch (IllegalArgumentException e) {
            throw new JaxException("Error - Invalid Input.");
        }
    }

    public static String[] splitCommand(String line) throws JaxException {
        return line.split(" ", 2);
    }

    public static Todo parseTodo(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Todo description cannot be empty.");
        }
        return new Todo(input[1]);
    }

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
}
