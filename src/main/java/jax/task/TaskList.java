package jax.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import jax.main.JaxException;
import jax.storage.Storage;
import jax.ui.Ui;

/**
 * Contains the task list and operations to modify it (add, delete, mark, find).
 */
@SuppressWarnings("FieldMayBeFinal")
public class TaskList {

    private ArrayList<Task> tasks;
    private Ui ui;
    private Storage storage;

    /**
     * Instantiates a new TaskList object from the current tasks.
     * @param ui Current UI instance.
     * @param storage Current Storage instance.
     */
    public TaskList(Ui ui, Storage storage) {
        this.tasks = new ArrayList<>();
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Instantiates a TaskList object from the current tasks.
     * @param tasks The ArrayList of tasks to be passed into TaskList.
     * @param ui Current UI instance.
     * @param storage Current Storage instance.
     */
    public TaskList(ArrayList<Task> tasks, Ui ui, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
        this.ui = ui;
    }

    /**
     * Helper function to link tasklist to storage.
     */
    private void save() {
        try {
            storage.writeSavefile(tasks);
        } catch (JaxException e) {
            ui.echo("Warning: Failed to save changes to file!", 4);
        }
    }


    /**
     * Insert task object into tasklist, printing success.
     * @param task New task to be added to tasklist.
     */
    public void insertTask(Task task) {
        tasks.add(task);
        save();
        ui.echo("added: " + task + "\nNow you have " + tasks.size() + " tasks in the list", 4);
    }

    /**
     * Getter all tasks currently in the list.
     * @return ArrayList object of all tasks.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Prints all tasks currently in the list formatted as a numbered list.
     */
    public void printTasks() {
        if (tasks.isEmpty()) {
            ui.echo("List is empty.", 4);
            return;
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        ui.echo(sb.toString(), 4);
    }

    /**
     * Prints all tasks currently in the list formatted as a numbered list.
     * @param input Datetime in String format.
     */
    public void printTasksByDate(String[] input) throws JaxException {
        if (input.length < 2) {
            throw new JaxException("Error - Please specify a date (yyyy-MM-dd).");
        }

        LocalDate queryDate;
        try {
            queryDate = LocalDate.parse(input[1]); // Expecting yyyy-MM-dd
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid date format. Use yyyy-MM-dd.");
        }

        StringBuilder sb = new StringBuilder("Reminders for " + queryDate + ":\n");
        int count = 0;
        for (Task t : tasks) {
            if (t.occursOn(queryDate)) {
                count++;
                sb.append(count).append(".").append(t).append("\n");
            }
        }

        if (count == 0) {
            ui.echo("No tasks found on this date.", 4);
        } else {
            ui.echo(sb.toString().trim(), 4);
        }
    }

    /**
     * Marks tasks based on the index given.
     * @param cur Index of task to be marked.
     */
    public void markTask(int cur) throws JaxException {

        if (cur < 0 || cur >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task curr = tasks.get(cur);
        if (curr.markTask()) {
            save();
            ui.echo("Nice! I've marked this task as done:\n" + curr, 4);
        } else {
            ui.echo("This task has already been marked done:\n" + curr, 4);
        }
    }

    /**
     * Unmarks tasks based on the index given.
     * @param cur Index of task to be unmarked.
     */
    public void unmarkTask(int cur) throws JaxException {

        if (cur < 0 || cur >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task curr = tasks.get(cur);
        if (curr.unmarkTask()) {
            save();
            ui.echo("OK, I've marked this task as not done yet:\n" + curr, 4);
        } else {
            ui.echo("This task hasn't been marked done:\n" + curr, 4);
        }
    }

    /**
     * Deletes a task from the list at the specified index.
     * @param index Index of the task to delete.
     * @throws JaxException If the index is out of range.
     */
    public void deleteTask(int index) throws JaxException {
        if (index < 0 || index >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task removedTask = tasks.get(index);
        tasks.remove(index);
        save();

        ui.echo("Noted. I've removed this task:\n  "
                + removedTask
                + "\nNow you have "
                + tasks.size()
                + " tasks in the list.", 4);
    }

    /**
     * Deletes a task from the list at the specified index.
     * @param searchString Keyword for task
     */
    public void findTask(String[] searchString) throws JaxException {

        if (searchString.length < 2) {
            throw new JaxException("Error - Please specify a single keyword.");
        }

        if (searchString.length > 2) {
            throw new JaxException("Error - Insert a single keyword.");
        }

        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        int count = 0;
        for (Task t : tasks) {
            if (t.contains(searchString[1])) {
                count++;
                sb.append(count).append(".").append(t).append("\n");
            }
        }

        if (count == 0) {
            ui.echo("No tasks found with this keyword.", 4);
        } else {
            ui.echo(sb.toString().trim(), 4);
        }
    }
}
