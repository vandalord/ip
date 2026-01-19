import java.util.ArrayList;
import java.util.Scanner;

public class Jax {

    static String separator = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";

    ArrayList<Task> tasks = new ArrayList<>();

    public void greet() {
        // Start-up message text
        echo(" \uD83D\uDC4B你好! I'm Jax, your personal assistant chatbot!'\n"
                + " What can I do for you?", 4);
    }

    public void exit() {
        // Exit message text
        echo(" \uD83D\uDC4B再见. Hope to see you again soon!", 4);
    }

    public void echo(String text, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        String indent = " ".repeat(Math.max(0, indentLevel));

        sb.append(indent).append(separator).append(newLine);

        if (text != null && !text.isEmpty()) {
            text.lines().forEach(line ->
                    sb.append(indent)
                            .append(line)
                            .append(newLine)
            );
        }

        sb.append(indent).append(separator).append(newLine);

        System.out.print(sb);
    }

    public void insert_task(Task task) {
        tasks.add(task);
        echo("added: " + task + "\nNow you have " + tasks.size() + " tasks in the list", 4);
    }

    public void print_list() {
        if (tasks.isEmpty()) {
            echo("List is empty.", 4);
            return;
        }
        StringBuilder sb = new StringBuilder("Here are the tasks in your list:\n");
        for(int i = 0; i < tasks.size(); i++) {
            sb.append((i + 1)).append(".").append(tasks.get(i));
            if (i < tasks.size() - 1) sb.append("\n");
        }
        echo(sb.toString(), 4);
    }

    public void mark_task(int cur) {

        if (cur < 0 || cur >= tasks.size()) {
            echo("Invalid task number.", 4);
            return;
        }

        Task curr = tasks.get(cur);
        if (curr.mark_task()) {
            echo("Nice! I've marked this task as done:\n" + curr, 4);
        } else {
            echo("This task has already been marked done:\n" + curr, 4);
        }
    }

    public void unmark_task(int cur) {

        if (cur < 0 || cur >= tasks.size()) {
            echo("Invalid task number.", 4);
            return;
        }

        Task curr = tasks.get(cur);
        if (curr.unmark_task()) {
            echo("OK, I've marked this task as not done yet:\n" + curr, 4);
        } else {
            echo("This task hasn't been marked done:\n" + curr, 4);
        }
    }

    public void delete_task(int index) throws JaxException {
        if (index < 0 || index >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task removedTask = tasks.get(index);
        tasks.remove(index);

        echo("Noted. I've removed this task:\n  " + removedTask + "\nNow you have " + tasks.size() + " tasks in the list.", 4);
    }

    public void await_input() throws JaxException {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            try {
                String line = userInput.nextLine();
                String[] input = line.split(" ", 2);
                String command = input[0];

                if (command.equalsIgnoreCase("bye")) {
                    break;
                } else if (command.equalsIgnoreCase("list")) {
                    print_list();
                } else if (command.equalsIgnoreCase("mark") || command.equalsIgnoreCase("unmark") || command.equalsIgnoreCase("delete")) {
                    if (input.length < 2) throw new JaxException("Error - Specify a task number.");
                    try {
                        int task_idx = Integer.parseInt(input[1]) - 1;
                        if (command.equalsIgnoreCase("mark")) mark_task(task_idx);
                        else if (command.equalsIgnoreCase("unmark")) unmark_task(task_idx);
                        else if (command.equalsIgnoreCase("delete")) delete_task(task_idx);
                    } catch (NumberFormatException e) {
                        throw new JaxException("Error - Invalid task number.");
                    }
                } else if (command.equalsIgnoreCase("todo")) {
                    if (input.length < 2 || input[1].trim().isEmpty()) {
                        throw new JaxException("Error - Todo description cannot be empty.");
                    }
                    insert_task(new Todo(input[1]));
                } else if (command.equalsIgnoreCase("deadline")) {
                    if (input.length < 2 || input[1].trim().isEmpty()) {
                        throw new JaxException("Error - Deadline description cannot be empty.");
                    }
                    String[] segments = input[1].split(" /by ");
                    if (segments.length < 2) {
                        throw new JaxException("Error - Deadline time not specified.");
                    }
                    insert_task(new Deadline(segments[0], segments[1]));
                } else if (command.equalsIgnoreCase("event")) {
                    if (input.length < 2 || input[1].trim().isEmpty()) {
                        throw new JaxException("Error - Event description cannot be empty.");
                    }
                    String[] fromSplit = input[1].split(" /from ");
                    if (fromSplit.length < 2) {
                        throw new JaxException("Error - Start time not specified.");
                    }
                    String[] toSplit = fromSplit[1].split(" /to ");
                    if (toSplit.length < 2) {
                        throw new JaxException("Error - End time not specified.");
                    }
                    insert_task(new Event(fromSplit[0], toSplit[0], toSplit[1]));
                } else {
                    echo("Error - Invalid Input.", 4);
                }
            }
            catch (JaxException e) {
                echo(e.getMessage(), 4);
            }
        }
    }

    public static void main(String[] args) throws JaxException {
        Jax bot = new Jax();
        bot.greet();
        bot.await_input();
        bot.exit();
    }
}
