package jax.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import jax.JaxException;
import jax.task.Task;

/**
 * Handles the loading and saving of task data to persistent memory.
 */
@SuppressWarnings("FieldMayBeFinal")
public class Storage {

    private static String savefileURL = "savefile.txt";

    /**
     * Saves the current list of tasks to file.
     * @param tasks The ArrayList of tasks to serialize and save.
     * @throws JaxException If the file cannot be written to.
     */
    public void writeSavefile(ArrayList<Task> tasks) throws JaxException {
        try (FileOutputStream fos = new FileOutputStream(savefileURL);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tasks);
        } catch (IOException ioe) {
            throw new JaxException("Error writing data.");
        }
    }

    /**
     * Loads the list of tasks from saved file.
     * If the file does not exist, an empty list is returned.
     * @return An ArrayList of Task objects loaded from the file.
     * @throws JaxException If the file is corrupted or cannot be read.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Task> readSavefile() throws JaxException {

        File f = new File(savefileURL);

        if (!f.exists()) {
            return new ArrayList<>();
        }

        try (FileInputStream fis = new FileInputStream(savefileURL);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (ArrayList<Task>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new JaxException("Error reading data.");
        }
    }
}
