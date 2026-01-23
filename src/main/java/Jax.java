import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Jax {

    private Ui ui;

    public Jax() {
        ui = new Ui();
    }

    private static final Logger log = Logger.getLogger(Jax.class.getName());

    static String savefileURL = "savefile.txt";

    ArrayList<Task> tasks = new ArrayList<>();

    public void insertTask(Task task) {
        tasks.add(task);
        ui.echo("added: " + task + "\nNow you have " + tasks.size() + " tasks in the list", 4);
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

    public void parseTodo(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Todo description cannot be empty.");
        }
        insertTask(new Todo(input[1]));
    }

    public void parseDeadline(String[] input) throws JaxException {
        if (input.length < 2 || input[1].trim().isEmpty()) {
            throw new JaxException("Error - Deadline description cannot be empty.");
        }
        String[] segments = input[1].split(" /by ");
        if (segments.length < 2) {
            throw new JaxException("Error - Deadline time not specified.");
        }

        try {
            insertTask(new Deadline(segments[0], segments[1]));
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)");
        }
    }

    public void parseEvent(String[] input) throws JaxException {
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
            insertTask(new Event(fromSplit[0], toSplit[0], toSplit[1]));
        } catch (DateTimeParseException e) {
            throw new JaxException("Error - Invalid Date Format. Please use: yyyy-MM-dd (e.g., 2019-10-15 1800)");
        }
    }

    public void onStartup() {
        readSavefile();
        ui.greet();
    }

    public void onShutdown() {
        writeSavefile();
        ui.exit();
    }

    public void writeSavefile() {
        try (FileOutputStream fos = new FileOutputStream(savefileURL);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tasks);
        } catch (FileNotFoundException e) {
            log.log(Level.SEVERE, "File not found: ", e);
            throw new RuntimeException(e);
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Error writing data : ", ioe);
        }
    }

    @SuppressWarnings("unchecked")
    public void readSavefile() {
        File f = new File(savefileURL);
        if (!f.exists()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(savefileURL);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            tasks = (ArrayList<Task>) ois.readObject();
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "Error reading data : ", ioe);
            throw new RuntimeException(ioe);
        } catch (ClassNotFoundException c) {
            log.log(Level.SEVERE, "File not found.", c);
        }
    }

    public void awaitInput() {
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
                        printTasks();
                        break;
                    case REMIND:
                        printTasksByDate(input);
                        break;
                    case MARK:
                    case UNMARK:
                    case DELETE:
                        if (input.length < 2) throw new JaxException("Error - Specify a task number.");
                        try {
                            int taskIndex = Integer.parseInt(input[1]) - 1;
                            if (command == Command.MARK) markTask(taskIndex);
                            else if (command == Command.UNMARK) unmarkTask(taskIndex);
                            else deleteTask(taskIndex);
                        } catch (NumberFormatException e) {
                            throw new JaxException("Error - Invalid task number.");
                        }
                        break;
                    case TODO:
                        parseTodo(input);
                        break;
                    case DEADLINE:
                        parseDeadline(input);
                        break;
                    case EVENT:
                        parseEvent(input);
                        break;
                }
            }
            catch (JaxException e) {
                ui.echo(e.getMessage(), 4);
            }
        }
    }

    public static void main(String[] args) {
        Jax bot = new Jax();
        bot.onStartup();
        bot.awaitInput();
        bot.onShutdown();
    }
}
