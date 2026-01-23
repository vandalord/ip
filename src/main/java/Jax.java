import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.format.DateTimeParseException;

public class Jax {

    private static final Logger log = Logger.getLogger(Jax.class.getName());

    static String separator = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";
    static String savefile_url = "savefile.txt";

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

    public void mark_task(int cur) throws JaxException {

        if (cur < 0 || cur >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
        }

        Task curr = tasks.get(cur);
        if (curr.mark_task()) {
            echo("Nice! I've marked this task as done:\n" + curr, 4);
        } else {
            echo("This task has already been marked done:\n" + curr, 4);
        }
    }

    public void unmark_task(int cur) throws JaxException {

        if (cur < 0 || cur >= tasks.size()) {
            throw new JaxException("Error - Invalid task number.");
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

    public void parse_todo(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Todo description cannot be empty.");
        }
        insert_task(new Todo(input[1]));
    }

    public void parse_deadline(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Deadline description cannot be empty.");
        }
        String[] segments = input[1].split(" /by ");
        if (segments.length < 2) {
            throw new JaxException("Error - Deadline time not specified.");
        }

        try {
            insert_task(new Deadline(segments[0], segments[1]));
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)");
        }
    }

    public void parse_event(String[] input) throws JaxException {
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

        try {
            insert_task(new Event(fromSplit[0], toSplit[0], toSplit[1]));
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)");
        }
    }

    public void on_startup() throws IOException, ClassNotFoundException {
        read_savefile();
        greet();
    }

    public void on_shutdown() throws IOException, ClassNotFoundException {
        write_savefile();
        exit();
    }

    public void write_savefile() throws IOException, ClassNotFoundException {
        try (FileOutputStream fos = new FileOutputStream(savefile_url);
             ObjectOutputStream oos = new ObjectOutputStream(fos);) {
            oos.writeObject(tasks);
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "File not found: ", e);
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Error writing data : ", ioe);
        }
    }

    @SuppressWarnings("unchecked")
    public void read_savefile() throws IOException, ClassNotFoundException {
        File f = new File(savefile_url);
        if (!f.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(savefile_url);
             ObjectInputStream ois = new ObjectInputStream(fis);) {
            tasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Error reading data : ", ioe);
            throw new RuntimeException(ioe);
        } catch (ClassNotFoundException c) {
            log.log(Level.SEVERE, "File not found.", c);
        }
    }

    public void await_input() throws JaxException, IOException, ClassNotFoundException {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            try {
                String line = userInput.nextLine();
                String[] input = line.split(" ", 2);
                String commandStr = input[0].toUpperCase();

                Command command;

                try {
                    command = Command.valueOf(commandStr);
                } catch (IllegalArgumentException e) {
                    throw new JaxException("Error - Invalid Input.");
                }

                switch (command) {
                    case BYE:
                        return;
                    case LIST:
                        print_list();
                        break;
                    case MARK:
                    case UNMARK:
                    case DELETE:
                        if (input.length < 2) throw new JaxException("Error - Specify a task number.");
                        try {
                            int task_idx = Integer.parseInt(input[1]) - 1;
                            if (command == Command.MARK) mark_task(task_idx);
                            else if (command == Command.UNMARK) unmark_task(task_idx);
                            else delete_task(task_idx);
                        } catch (NumberFormatException e) {
                            throw new JaxException("Error - Invalid task number.");
                        }
                        break;
                    case TODO:
                        parse_todo(input);
                        break;
                    case DEADLINE:
                        parse_deadline(input);
                        break;
                    case EVENT:
                        parse_event(input);
                        break;
                }
            }
            catch (JaxException e) {
                echo(e.getMessage(), 4);
            }
        }
    }

    public static void main(String[] args) throws JaxException, IOException, ClassNotFoundException {
        Jax bot = new Jax();
        bot.on_startup();
//        bot.greet();
        bot.await_input();
//        bot.exit();
        bot.on_shutdown();
    }
}
