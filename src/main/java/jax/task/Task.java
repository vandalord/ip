package jax.task;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a generic task in the Jax application.
 * The base class for specific task types like Todo, Deadline, and Event.
 * It implements Serializable to allow task objects to be saved directly to a file.
 */
public class Task implements Serializable {

    /** The description of the task. */
    protected String description;
    /** The completion status of the task (true if done, false otherwise). */
    protected boolean isDone;

    /**
     * Creates a new Task with the specified description.
     * The task is originally marked not done.
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon representing the completion status.
     * @return "[X]" if task is done, or "[ ]" if not done.
     */
    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    /**
     * Marks the task as completed.
     * @return true if the task was successfully marked as done, false if the task was already marked as done.
     */
    public boolean markTask() {
        if (isDone) {
            return false;
        }
        isDone = true;
        return true;
    }

    /**
     * Marks the task as not completed.
     * @return true if the task was successfully marked as not done, false if the task was already not done.
     */
    public boolean unmarkTask() {
        if (!isDone) {
            return false;
        }
        isDone = false;
        return true;
    }

    /**
     * Checks if the task occurs on a specific date.
     * This default implementation returns false and should be overridden.
     * by subclasses with date attributes.
     * @param date The date to check for.
     * @return false by default.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    /**
     * Checks if the task description contains the specified search keyword, case-insensitive.
     * @param search The keyword to search for.
     * @return true if the description contains the keyword, false otherwise.
     */
    public boolean contains(String search) {
        return this.description.toLowerCase().contains(search.toLowerCase());
    }

    /**
     * Returns the string representation of the task.
     * @return A string in the format "[Status] Description".
     */
    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}

