package jax.contact;

import java.io.Serializable;
import java.util.HashMap;

import jax.main.JaxException;
import jax.storage.Storage;

/**
 * Contains the contact list and operations to modify it (add, delete, mark, find).
 */
@SuppressWarnings("FieldMayBeFinal")
public class ContactList implements Serializable {

    private HashMap<String, Contact> contacts;
    private Storage storage;

    /**
     * Instantiates a new TaskList object from the current tasks.
     * @param storage Current Storage instance.
     */
    public ContactList(Storage storage) {
        this.contacts = new HashMap<>();
        this.storage = storage;
    }

    /**
     * Instantiates a TaskList object from the current tasks.
     * @param contacts The ArrayList of tasks to be passed into TaskList.
     * @param storage Current Storage instance.
     */
    public ContactList(HashMap<String, Contact> contacts, Storage storage) {
        this.contacts = contacts;
        this.storage = storage;
    }

    /**
     * Helper function to link tasklist to storage.
     */
    private void saveToStorage() throws JaxException {
        storage.saveContacts(contacts);
    }

    /**
     * Uses the Contact's name as the unique key for the HashMap.
     */
    public String insertContact(Contact contact) throws JaxException {
        contacts.put(contact.getName().trim().toLowerCase(), contact);
        saveToStorage();
        return String.format("Added: %s\nTotal contacts: %d", contact, contacts.size());
    }

    /**
     * Deletes by name (Key) rather than index, which is more efficient for HashMaps.
     */
    public String deleteContact(String name) throws JaxException {

        String input = name.trim().toLowerCase();

        if (!contacts.containsKey(input)) {
            throw new JaxException("Error - Contact not found.");
        }

        int initialSize = contacts.size();

        Contact removed = contacts.remove(input);
        saveToStorage();

        assert contacts.size() == initialSize - 1 : "Contact list size did not decrease after deletion";

        return ("Noted. Removed: " + removed);
    }

    /**
     * Getter for all contacts in the list.
     * @return Collection object of all contacts.
     */
    public HashMap<String, Contact> getContacts() {
        return contacts;
    }

    /**
     * Prints all tasks currently in the list formatted as a numbered list.
     */
    public String printContacts() {
        if (contacts.isEmpty()) {
            return "Contact list is empty.";
        }
        StringBuilder sb = new StringBuilder("Here are the contacts in your list:\n");
        int i = 1;
        for (Contact c : contacts.values()) {
            sb.append(i++).append(". ").append(c).append("\n");
        }
        return sb.toString().trim();
    }
}
