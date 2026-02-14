package jax.main;

import jax.command.Command;
import jax.contact.Contact;
import jax.contact.ContactList;
import jax.storage.Storage;
import jax.task.TaskList;

/**
 * Main entry point and controller for the Jax chatbot application.
 * This class coordinates between the User Interface (Ui), the data Storage, and the TaskList logic.
 * It handles the main execution loop processing user commands until termination.
 */
@SuppressWarnings("FieldMayBeFinal")
public class Jax {

    /** Handles loading and saving tasks to the file system. */
    private Storage storage;
    /** Contains the list of tasks and business logic for task manipulation. */
    private TaskList tasks;

    private ContactList contacts;

    private final static String GREET = "\uD83D\uDC4B Top of the morning to you! I'm Jax, your personal assistant chatbot!\n"
                                    + "What can I do for you?";
    private final static String GOODBYE = "\uD83D\uDC4B Aww shucks. Hope to see you again soon!\n"
                                    + "Closing in 3 seconds..." ;
    private final static String HELP_MESSAGE = """
            Here are the commands you can use:
            1.  todo <description>
            2.  deadline <desc> /by <yyyy-MM-dd>
            3.  event <desc> /from <yyyy-MM-dd> /to <yyyy-MM-dd>
            4.  list
            5.  mark <index>
            6.  unmark <index>
            7.  delete <index>
            8.  find <keyword>
            9.  clear
            10. contact <name> /p <phone> /e <email>
            11. delcontact <name>
            12. contacts
            13. bye""";

    /**
     * Initializes the chatbot components.
     * Attempts to load existing tasks from the save file.
     * If loading fails, it initializes with an empty task list.
     */
    public Jax() {
        this.storage = new Storage();
        try {
            tasks = new TaskList(storage.loadTasks(), this.storage);
        } catch (JaxException e) {
            tasks = new TaskList(this.storage);
        }

        try {
            contacts = new ContactList(storage.loadContacts(), this.storage);
        } catch (JaxException e) {
            contacts = new ContactList(this.storage);
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
     * Saves the current state of the task list and contact list to the hard disk and displays the exit message.
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
                case DELCONTACT:
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
