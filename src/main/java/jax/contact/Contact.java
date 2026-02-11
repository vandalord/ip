package jax.contact;

import java.io.Serializable;

/**
 * Represents a generic contact in the Jax application.
 * It implements Serializable to allow contacts to be saved directly to a file.
 */
public class Contact implements Serializable {

    /** The name of the contact. */
    protected String name;
    /** The contact number of the contact. */
    protected String contactNumber;
    /** The class or category this contact belongs to, with respect to the user (e.g. Friend, Colleague) */
    protected String email;

    /**
     * Creates a new Contact with the specified name, contact number and category.
     * @param name The name of the contact.
     * @param contactNumber The contact number of the contact.
     * @param email The email of the contact.
     */
    public Contact(String name, String contactNumber, String email) {
        this.name = name;
        this.contactNumber =  contactNumber;
        this.email = email;
    }

    /**
     * Getter for contact name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for phone number.
     */
    public String getPhoneNumber() {
        return this.contactNumber;
    }

    /**
     * Returns the string representation of the Contact.
     * @return A string in the format "<name> <contact number> <email>".
     */
    @Override
    public String toString() {
        return this.name + " " + this.contactNumber + " " + this.email;
    }
}

