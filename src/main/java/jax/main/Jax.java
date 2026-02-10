package jax.main;

import jax.command.Command;
import jax.contact.Contact;
import jax.contact.ContactList;
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

    private ContactList contacts;

    private final static String GREET = "\uD83D\uDC4B你好! I'm Jax, your personal assistant chatbot!\n"
                                    + "What can I do for you?";
    private final static String GOODBYE = "\uD83D\uDC4B再见. Hope to see you again soon!"
                                    + "Closing in 3 seconds..." ;
    private final static String HELP_MESSAGE = "Here are the commands you can use:\n"
            + "1.  todo <description>\n"
            + "2.  deadline <desc> /by <yyyy-MM-dd>\n"
            + "3.  event <desc> /from <yyyy-MM-dd> /to <yyyy-MM-dd>\n"
            + "4.  list\n"
            + "5.  mark <index>\n"
            + "6.  unmark <index>\n"
            + "7.  delete <index>\n"
            + "8.  find <keyword>\n"
            + "9.  clear\n"
            + "10. bye";

    /**
     * Initializes the chatbot components.
     * Attempts to load existing tasks from the save file. If loading fails
     * (e.g., file not found or corrupted), it initializes with an empty task list.
     */
    public Jax() {
        this.ui = new Ui();
        this.storage = new Storage();
        try {
            tasks = new TaskList(storage.loadTasks(), ui, this.storage);
        } catch (JaxException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList(ui, this.storage);
        }

        try {
            contacts = new ContactList(storage.loadContacts(), ui, this.storage);
        } catch (JaxException e) {
            contacts = new ContactList(ui, this.storage);
        }
    }

    /**
     * Returns the welcome message for the GUI to display.
     */
    public String getGreeting() {
        return GREET;
    }

    /**
     * Returns the ending message for the GUI to display.
     */
    public String getGoodbye() {
        return GOODBYE;
    }

    /**
     * Performs cleanup operations before the application exits.
     * Saves the current state of the task list to the hard disk and displays the exit message.
     * @throws JaxException If there is an error writing to the save file.
     */
    public void saveData() throws JaxException {
        storage.saveTasks(tasks.getTasks());
        storage.saveContacts(contacts.getContacts());
    }

    /**
     * Generates a response for the user's chat message.
     */
    public String getResponse(String line) {
        try {
                Command command = Parser.parseCommand(line);
                String[] input = Parser.splitCommand(line);

                switch (command) {
                case HELLO:
                    return getGreeting();
                case CLEAR:
                case BYE:
                    return "";
                case LIST:
                    return tasks.printTasks();
                case REMIND:
                    return tasks.printTasksByDate(input);
                case MARK:
                case UNMARK:
                case DELETE:
                    if (input.length < 2) {
                        throw new JaxException("Error - Specify a task number.");
                    }
                    try {
                        int taskIndex = Integer.parseInt(input[1]) - 1;
                        if (command == Command.MARK) {
                            return tasks.markTask(taskIndex);
                        } else if (command == Command.UNMARK) {
                            return tasks.unmarkTask(taskIndex);
                        } else {
                            return tasks.deleteTask(taskIndex);
                        }
                    } catch (NumberFormatException e) {
                        throw new JaxException("Error - Invalid task number.");
                    }
                case TODO:
                    return tasks.insertTask(Parser.parseTodo(input));
                case DEADLINE:
                    return tasks.insertTask(Parser.parseDeadline(input));
                case EVENT:
                    return tasks.insertTask(Parser.parseEvent(input));
                case FIND:
                    String keyword = Parser.parseFind(input);
                    return tasks.findTasks(keyword);
                case CONTACT:
                    Contact newContact = Parser.parseContact(input);
                    return contacts.insertContact(newContact);
                case CONTACTS:
                    return contacts.printContacts();
                case DELETE_CONTACT:
                    String nameToDelete = Parser.parseDeleteContact(input);
                    return contacts.deleteContact(nameToDelete);
                case HELP:
                    return HELP_MESSAGE;
                default:
                    return "Error - Unknown command.";
                }
            } catch (JaxException e) {
                return e.getMessage();
            }
    }
}
