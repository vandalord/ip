package jax.contact;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.naming.ldap.Control;

import jax.main.JaxException;
import jax.storage.Storage;
import jax.ui.Ui;

/**
 * Contains the contact list and operations to modify it (add, delete, mark, find).
 */
@SuppressWarnings("FieldMayBeFinal")
public class ContactList implements Serializable {

    private HashMap<String, Contact> contacts;
    private Ui ui;
    private Storage storage;

    /**
     * Instantiates a new TaskList object from the current tasks.
     * @param ui Current UI instance.
     * @param storage Current Storage instance.
     */
    public ContactList(Ui ui, Storage storage) {
        this.contacts = new HashMap<>();
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Instantiates a TaskList object from the current tasks.
     * @param contacts The ArrayList of tasks to be passed into TaskList.
     * @param ui Current UI instance.
     * @param storage Current Storage instance.
     */
    public ContactList(HashMap<String, Contact> contacts, Ui ui, Storage storage) {
        this.contacts = contacts;
        this.storage = storage;
        this.ui = ui;
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
        contacts.put(contact.getName(), contact);
        saveToStorage();
        return String.format("Added: %s\nTotal contacts: %d", contact, contacts.size());
    }

    /**
     * Deletes by name (Key) rather than index, which is more efficient for HashMaps.
     */
    public String deleteContact(String name) throws JaxException {
        if (!contacts.containsKey(name)) {
            throw new JaxException("Error - Contact not found.");
        }

        Contact removed = contacts.remove(name);
        saveToStorage();
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
