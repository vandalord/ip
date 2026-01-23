import java.io.*;
import java.time.format.DateTimeParseException;

public class Jax {

    private Storage storage;
    private Ui ui;
    private TaskList tasks;

    public Jax() {
        this.ui = new Ui();
        this.storage = new Storage();
        try {
            tasks = new TaskList(storage.readSavefile(), ui);
        } catch (JaxException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList(ui);
        }
    }

    public void parseTodo(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Todo description cannot be empty.");
        }
        tasks.insertTask(new Todo(input[1]));
    }

    public void parseDeadline(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Deadline description cannot be empty.");
        }
        String[] segments = input[1].split(" /by ");
        if (segments.length < 2) {
            throw new JaxException("Error - Deadline time not specified.");
        }

        try {
            tasks.insertTask(new Deadline(segments[0], segments[1]));
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)");
        }
    }

    public void parseEvent(String[] input) throws JaxException {
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
            tasks.insertTask(new Event(fromSplit[0], toSplit[0], toSplit[1]));
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)");
        }
    }

    public void onStartup() throws JaxException {
        storage.readSavefile();
        ui.greet();
    }

    public void onShutdown() throws JaxException {
        storage.writeSavefile(tasks.getTasks());
        ui.exit();
    }


    public void awaitInput() {
        while (true) {
            try {
                String line = ui.readCommand();
                String[] input = line.split(" ", 2);
                String commandStr = input[0].toUpperCase();

                Command command;

                try {
                    command = Command.valueOf(commandStr);
                } catch (IllegalArgumentException e) {
                    throw new JaxException("Error - Invalid Input.");
                }

                switch (command) {
                    case BYE:
                        return;
                    case LIST:
                        tasks.printTasks();
                        break;
                    case REMIND:
                        tasks.printTasksByDate(input);
                        break;
                    case MARK:
                    case UNMARK:
                    case DELETE:
                        if (input.length < 2) throw new JaxException("Error - Specify a task number.");
                        try {
                            int taskIndex = Integer.parseInt(input[1]) - 1;
                            if (command == Command.MARK) tasks.markTask(taskIndex);
                            else if (command == Command.UNMARK) tasks.unmarkTask(taskIndex);
                            else tasks.deleteTask(taskIndex);
                        } catch (NumberFormatException e) {
                            throw new JaxException("Error - Invalid task number.");
                        }
                        break;
                    case TODO:
                        parseTodo(input);
                        break;
                    case DEADLINE:
                        parseDeadline(input);
                        break;
                    case EVENT:
                        parseEvent(input);
                        break;
                }
            }
            catch (JaxException e) {
                ui.echo(e.getMessage(), 4);
            }
        }
    }

    public static void main(String[] args) {
        Jax bot = new Jax();
        try {
            bot.onStartup();
            bot.awaitInput();
            bot.onShutdown();
        } catch (JaxException e) {
            System.out.println(e.getMessage());
        }
    }
}
