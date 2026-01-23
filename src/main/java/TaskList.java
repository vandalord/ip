import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class TaskList {

    private ArrayList<Task> tasks;
    Ui ui;

    public TaskList(Ui ui) {
        this.tasks = new ArrayList<>();
        this.ui = ui;
    }

    public TaskList(ArrayList<Task> tasks, Ui ui) {
        this.tasks = tasks;
        this.ui = ui;
    }

    public void insertTask(Task task) {
        tasks.add(task);
        ui.echo("added: " + task + "\nNow you have " + tasks.size() + " tasks in the list", 4);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void printTasks() {
        if (tasks.isEmpty()) {
            ui.echo("List is empty.", 4);
            return;
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for(int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) sb.append("\n");
        }
        ui.echo(sb.toString(), 4);
    }

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

    public void markTask(int cur) throws JaxException {

        if (cur < 0 || cur >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task curr = tasks.get(cur);
        if (curr.markTask()) {
            ui.echo("Nice! I've marked this task as done:\n" + curr, 4);
        } else {
            ui.echo("This task has already been marked done:\n" + curr, 4);
        }
    }

    public void unmarkTask(int cur) throws JaxException {

        if (cur < 0 || cur >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task curr = tasks.get(cur);
        if (curr.unmarkTask()) {
            ui.echo("OK, I've marked this task as not done yet:\n" + curr, 4);
        } else {
            ui.echo("This task hasn't been marked done:\n" + curr, 4);
        }
    }

    public void deleteTask(int index) throws JaxException {
        if (index < 0 || index >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task removedTask = tasks.get(index);
        tasks.remove(index);

        ui.echo("Noted. I've removed this task:\n  " + removedTask + "\nNow you have " + tasks.size() + " tasks in the list.", 4);
    }
}
