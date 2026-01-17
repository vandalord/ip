import java.util.Arrays;
import java.util.Scanner;

public class Jax {

    static String separator = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";

    int idx = 0;
    Task[] tasks = new Task[100];

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
        this.tasks[idx] = task;
        this.idx += 1;
        echo("added: " + task + "\nNow you have " + idx + " tasks in the list", 4);
    }

    public void print_list() {
        Task[] currentList = Arrays.copyOf(tasks, idx);
        StringBuilder sb = new StringBuilder();
        if (currentList.length == 0) {
            echo("List is empty.", 4);
            return;
        }
        for(int i = 0; i < currentList.length; i++) {
            sb.append((i + 1)).append(". ").append(currentList[i]);
            if (i < currentList.length - 1) sb.append("\n");
        }
        echo(sb.toString(), 4);
    }

    public void mark_task(int cur) {

        if (cur < 0 || cur >= idx) {
            echo("Invalid task number.", 4);
            return;
        }

        Task curr = tasks[cur];
        if (curr.mark_task()) {
            echo("Nice! I've marked this task as done:\n" + curr, 4);
        } else {
            echo("This task has already been marked done:\n" + curr, 4);
        }
    }

    public void unmark_task(int cur) {

        if (cur < 0 || cur >= idx) {
            echo("Invalid task number.", 4);
            return;
        }

        Task curr =  tasks[cur];
        if (curr.unmark_task()) {
            echo("OK, I've marked this task as not done yet:\n" + curr, 4);
        } else {
            echo("This task hasn't been marked done:\n" + curr, 4);
        }
    }

    public void await_input() {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            String line = userInput.nextLine();
            String[] input = line.split(" ", 2);
            String command = input[0];

            if (command.equalsIgnoreCase("bye")) {
                break;
            } else if (command.equalsIgnoreCase("list")) {
                print_list();
            } else if (command.equalsIgnoreCase("mark")) {
                mark_task(Integer.parseInt(input[1]) - 1);
            } else if (command.equalsIgnoreCase("unmark")) {
                unmark_task(Integer.parseInt(input[1]) - 1);
            } else if (command.equalsIgnoreCase("todo")) {
                if (input.length < 2) {
                    echo("Error - Todo description cannot be empty.", 4);
                    continue;
                }
                insert_task(new Todo(input[1]));
            } else if (command.equalsIgnoreCase("deadline")) {
                if (input.length < 2) {
                    echo("Error - Deadline description cannot be empty.", 4);
                    continue;
                }
                String[] segments = input[1].split(" /by ");
                if (segments.length < 2) {
                    echo("Error - Deadline time not specified.", 4);
                    continue;
                }
                insert_task(new Deadline(segments[0], segments[1]));
            } else if (command.equalsIgnoreCase("event")) {

                if (input.length < 2) {
                    echo("Error - Event description cannot be empty.", 4);
                    continue; }

                String[] fromSplit = input[1].split(" /from ");
                if (fromSplit.length < 2) {
                    echo("Error - Start time not specified.", 4);
                    continue;
                }

                String[] toSplit = fromSplit[1].split(" /to ");
                if (toSplit.length < 2) {
                    echo("Error - End time not specified.", 4);
                    continue;
                }

                insert_task(new Event(fromSplit[0], toSplit[0], toSplit[1]));
            } else {
                insert_task(new Task(input[1]));
            }
        }
    }

    public static void main(String[] args) {
        Jax bot = new Jax();
        bot.greet();
        bot.await_input();
        bot.exit();
    }
}
