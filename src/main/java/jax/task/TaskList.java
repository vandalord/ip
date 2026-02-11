package jax.task;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import jax.main.JaxException;
import jax.storage.Storage;


/**
 * Contains the task list and operations to modify it (add, delete, mark, find).
 */
@SuppressWarnings("FieldMayBeFinal")
public class TaskList {

    private ArrayList<Task> tasks;
    private Storage storage;

    /**
     * Instantiates a new TaskList object from the current tasks.
     * @param storage Current Storage instance.
     */
    public TaskList(Storage storage) {
        this.tasks = new ArrayList<>();
        this.storage = storage;
    }

    /**
     * Instantiates a TaskList object from the current tasks.
     * @param tasks The ArrayList of tasks to be passed into TaskList.
     * @param storage Current Storage instance.
     */
    public TaskList(ArrayList<Task> tasks, Storage storage) {
        this.tasks = tasks;
        this.storage = storage;
    }

    /**
     * Helper function to link tasklist to storage.
     */
    private void saveToStorage() throws JaxException {
        storage.saveTasks(tasks);
    }


    /**
     * Insert task object into tasklist, printing success.
     * @param task New task to be added to tasklist.
     */
    public String insertTask(Task task) throws JaxException {
        tasks.add(task);
        saveToStorage();
        return ("added: " + task + "\nNow you have " + tasks.size() + " tasks in the list");
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
    public String printTasks() {
        if (tasks.isEmpty()) {
            return "List is empty.";
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * Prints all tasks currently in the list formatted as a numbered list.
     * @param input Datetime in String format.
     */
    public String printTasksByDate(String[] input) throws JaxException {
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
            return "No tasks found on this date.";
        } else {
            return sb.toString().trim();
        }
    }

    /**
     * Marks tasks based on the index given.
     * @param cur Index of task to be marked.
     */
    public String markTask(int cur) throws JaxException {

        if (cur < 0 || cur >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task curr = tasks.get(cur);
        if (curr.markTask()) {
            saveToStorage();
            return "Nice! I've marked this task as done:\n" + curr;
        } else {
            return "This task has already been marked done:\n" + curr;
        }
    }

    /**
     * Unmarks tasks based on the index given.
     * @param cur Index of task to be unmarked.
     */
    public String unmarkTask(int cur) throws JaxException {

        if (cur < 0 || cur >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task curr = tasks.get(cur);
        if (curr.unmarkTask()) {
            saveToStorage();
            return "OK, I've marked this task as not done yet:\n" + curr;
        } else {
            return "This task hasn't been marked done:\n" + curr;
        }
    }

    /**
     * Deletes a task from the list at the specified index.
     * @param index Index of the task to delete.
     * @throws JaxException If the index is out of range.
     */
    public String deleteTask(int index) throws JaxException {
        if (index < 0 || index >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task removedTask = tasks.get(index);
        tasks.remove(index);
        saveToStorage();

        return ("Noted. I've removed this task:\n  "
                + removedTask
                + "\nNow you have "
                + tasks.size()
                + " tasks in the list.");
    }

    /**
     * Deletes a task from the list at the specified index.
     * @param keyword Keyword for task
     */
    public String findTasks(String keyword) throws JaxException {

        List<Task> filteredTasks = tasks.stream()
                .filter(t -> t.contains(keyword))
                .toList();

        if (filteredTasks.isEmpty()) {
            return "No tasks found with this keyword.";
        }

        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");

        IntStream.range(0, filteredTasks.size())
                .forEach(i -> sb.append(i + 1).append(".").append(filteredTasks.get(i)).append("\n"));

        return sb.toString().trim();
    }
}
