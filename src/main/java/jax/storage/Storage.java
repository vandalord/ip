package jax.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import jax.contact.Contact;
import jax.main.JaxException;
import jax.task.Task;

/**
 * Handles the loading and saving of task data to persistent memory.
 */
@SuppressWarnings("FieldMayBeFinal")
public class Storage {

    private final String TASKS_URL;
    private final String CONTACTS_URL;

    /**
     * Default constructor using standard file names.
     */
    public Storage() {
        this("data/tasks.txt", "data/contacts.txt");

        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    /**
     * Constructor allowing specific paths for both tasks and contacts.
     * Useful for redirecting saves during unit testing.
     */
    public Storage(String tasksPath, String contactsPath) {
        this.TASKS_URL = tasksPath;
        this.CONTACTS_URL = contactsPath;
    }

    /**
     * Generic helper to write any serializable object to a specific path.
     */
    private void writeToFile(String path, Object data) throws JaxException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(data);
        } catch (IOException ioe) {
            throw new JaxException("Error writing to " + path);
        }
    }

    public void saveTasks(ArrayList<Task> tasks) throws JaxException {
        writeToFile(TASKS_URL, tasks);
    }

    public void saveContacts(HashMap<String, Contact> contacts) throws JaxException {
        writeToFile(CONTACTS_URL, contacts);
    }

    /**
     * Loads the list of tasks from saved file.
     * If the file does not exist, an empty list is returned.
     * @return Objects loaded from the file.
     * @throws JaxException If the file is corrupted or cannot be read.
     */
    @SuppressWarnings("unchecked")
    public Object readSavefile(String path) throws JaxException {

        File f = new File(path);

        if (!f.exists()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(f);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new JaxException("Error reading data.");
        }
    }

    /**
     * Loads the list of tasks from the saved file.
     * @return An ArrayList of Task objects.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Task> loadTasks() throws JaxException {
        Object data = readSavefile(TASKS_URL);
        return data == null ? new ArrayList<>() : (ArrayList<Task>) data;
    }

    /**
     * Loads the map of contacts from the saved file.
     * @return A HashMap of Contact objects.
     */
    @SuppressWarnings("unchecked")
    public HashMap<String, Contact> loadContacts() throws JaxException {
        Object data = readSavefile(CONTACTS_URL);
        return data == null ? new HashMap<>() : (HashMap<String, Contact>) data;
    }
}
