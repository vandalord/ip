package jax;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import jax.contact.ContactList;
import jax.contact.Contact;
import jax.main.JaxException;
import jax.ui.Ui;
import jax.storage.Storage;


public class ContactListTest {
    @Test
    public void insertContact_newContact_increasesSize() throws JaxException {
        ContactList cl = new ContactList(new HashMap<>(), new Ui(), new Storage());
        Contact c = new Contact("Jax", "123", "jax@bot.com");
        cl.insertContact(c);
        assertTrue(cl.printContacts().contains("Jax"));
    }

    @Test
    public void deleteContact_existingName_removesContact() throws JaxException {
        HashMap<String, Contact> map = new HashMap<>();
        map.put("Cavan", new Contact("Cavan", "111", "c@c.com"));
        ContactList cl = new ContactList(map, new Ui(), new Storage());

        cl.deleteContact("Cavan");
        assertFalse(cl.printContacts().contains("Cavan"));
    }
}
