package jax;

import jax.command.Command;
import jax.storage.Storage;
import jax.task.TaskList;
import jax.ui.Ui;

/**
 * Contains the chatbot run functions, encompassing interactive processes.
 */
public class Jax {

    private Storage storage;
    private Ui ui;
    private TaskList tasks;

    public Jax() {
        this.ui = new Ui();
        this.storage = new Storage();
        try {
            tasks = new TaskList(storage.readSavefile(), ui, this.storage);
        } catch (JaxException e) {
            ui.showError(e.getMessage());
            tasks = new TaskList(ui, this.storage);
        }
    }

    public void onStartup() throws JaxException {
        storage.readSavefile();
        ui.greet();
    }

    public void onShutdown() throws JaxException {
        storage.writeSavefile(tasks.getTasks());
        ui.exit();
    }

    public void awaitInput() {
        while (true) {
            try {
                String line = ui.readCommand();

                if (line == null) {
                    return;
                }

                Command command = Parser.parseCommand(line);
                String[] input = Parser.splitCommand(line);

                switch (command) {
                case BYE:
                    return;
                case LIST:
                    tasks.printTasks();
                    break;
                case REMIND:
                    tasks.printTasksByDate(input);
                    break;
                case MARK:
                case UNMARK:
                case DELETE:
                    if (input.length < 2) throw new JaxException("Error - Specify a task number.");
                    try {
                        int taskIndex = Integer.parseInt(input[1]) - 1;
                        if (command == Command.MARK) tasks.markTask(taskIndex);
                        else if (command == Command.UNMARK) tasks.unmarkTask(taskIndex);
                        else tasks.deleteTask(taskIndex);
                    } catch (NumberFormatException e) {
                        throw new JaxException("Error - Invalid task number.");
                    }
                    break;
                case TODO:
                    tasks.insertTask(Parser.parseTodo(input));
                    break;
                case DEADLINE:
                    tasks.insertTask(Parser.parseDeadline(input));
                    break;
                case EVENT:
                    tasks.insertTask(Parser.parseEvent(input));
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
        try {
            bot.onStartup();
            bot.awaitInput();
            bot.onShutdown();
        } catch (JaxException e) {
            System.out.println(e.getMessage());
        }
    }
}
