package jax.task;


/**
 * Todo - task with description, no deadline or timing.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo with the given description.
     * @param description The description of the task.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of the Todo.
     * Adds "[T]" string identifier to the standard string.
     * @return Formatted string representing the Todo.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
