package jax.storage;


import jax.task.Task;
import jax.JaxException;

import java.io.*;
import java.util.ArrayList;

public class Storage {

    private static String savefileURL = "savefile.txt";

    public void writeSavefile(ArrayList<Task> tasks) throws JaxException {
        try (FileOutputStream fos = new FileOutputStream(savefileURL);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tasks);
        } catch (IOException ioe) {
            throw new JaxException("Error writing data.");
        }
    }

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
