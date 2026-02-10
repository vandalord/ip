package jax.task;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

import jax.contact.Contact;
import jax.main.JaxException;
import jax.storage.Storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageTest {
    @Test
    public void storage_saveAndLoadContacts_integrityMaintained() throws JaxException {
        Storage storage = new Storage("test_tasks.txt", "test_contacts.txt");
        HashMap<String, Contact> original = new HashMap<>();
        original.put("Alice", new Contact("Alice", "123", "alice@test.com"));

        storage.saveContacts(original);
        HashMap<String, Contact> loaded = storage.loadContacts();

        assertEquals(original.size(), loaded.size());
        assertEquals(original.get("Alice").getPhoneNumber(), loaded.get("Alice").getPhoneNumber());

        new java.io.File("test_tasks.txt").delete();
        new java.io.File("test_contacts.txt").delete();
    }
}
