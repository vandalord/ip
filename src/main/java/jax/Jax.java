package jax;

import jax.command.Command;
import jax.storage.Storage;
import jax.task.TaskList;
import jax.ui.Ui;

/**
 * Main entry point and controller for the Jax chatbot application.
 * This class coordinates between the User Interface (Ui), the data Storage, and the TaskList logic.
 * It handles the main execution loop processing user commands until termination.
 */
@SuppressWarnings("FieldMayBeFinal")
public class Jax {

    /** Handles loading and saving tasks to the file system. */
    private Storage storage;
    /** Handles interactions with the user (input/output). */
    private Ui ui;
    /** Contains the list of tasks and business logic for task manipulation. */
    private TaskList tasks;

    /**
     * Initializes the chatbot components.
     * Attempts to load existing tasks from the save file. If loading fails
     * (e.g., file not found or corrupted), it initializes with an empty task list.
     */
    public Jax() {
        this.ui = new Ui();
        this.storage = new Storage();
        try {
            tasks = new TaskList(storage.readSavefile(), ui, this.storage);
        } catch (JaxException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList(ui, this.storage);
        }
    }

    /**
     * Performs necessary startup operations.
     * Loads the save file and displays the welcome greeting to the user.
     * @throws JaxException If there is an error reading the save file.
     */
    public void onStartup() throws JaxException {
        storage.readSavefile();
        ui.greet();
    }

    /**
     * Performs cleanup operations before the application exits.
     * Saves the current state of the task list to the hard disk and displays the exit message.
     * @throws JaxException If there is an error writing to the save file.
     */
    public void onShutdown() throws JaxException {
        storage.writeSavefile(tasks.getTasks());
        ui.exit();
    }

    /**
     * The main execution loop of the application.
     * Continuously reads user input, parses commands, executes the corresponding
     * task operations, and handles any exceptions that arise during execution.
     * The loop terminates when the user issues the 'BYE' command.
     */
    public void awaitInput() {
        while (true) {
            try {
                String line = ui.readCommand();

                if (line == null) {
                    return;
                }

                Command command = Parser.parseCommand(line);
                String[] input = Parser.splitCommand(line);

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
                    if (input.length < 2) {
                        throw new JaxException("Error - Specify a task number.");
                    }
                    try {
                        int taskIndex = Integer.parseInt(input[1]) - 1;
                        if (command == Command.MARK) {
                            tasks.markTask(taskIndex);
                        } else if (command == Command.UNMARK) {
                            tasks.unmarkTask(taskIndex);
                        } else {
                            tasks.deleteTask(taskIndex);
                        }
                    } catch (NumberFormatException e) {
                        throw new JaxException("Error - Invalid task number.");
                    }
                    break;
                case TODO:
                    tasks.insertTask(Parser.parseTodo(input));
                    break;
                case DEADLINE:
                    tasks.insertTask(Parser.parseDeadline(input));
                    break;
                case EVENT:
                    tasks.insertTask(Parser.parseEvent(input));
                    break;
                case FIND:
                    tasks.findTask(input);
                    break;
                default:
                    throw new JaxException("Error - Unknown command.");
                }
            } catch (JaxException e) {
                ui.echo(e.getMessage(), 4);
            }
        }
    }

    /**
     * The main method that launches the application.
     */
    public static void main() {
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
