import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Jax {

    static String separator = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";

    int idx = 0;
    String[] list_data = new String[100];

    public void greet() {
        // Start-up message text
        echo(" \uD83D\uDC4B你好! I'm Jax, your personal assistant chatbot!'\n"
                + " What can I do for you?", 4);
    }

    public void exit() {
        // Exit message text
        echo(" \uD83D\uDC4B再见. Hope to see you again soon!", 4);
    }

    public void separate_text() {
        String line = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";
        System.out.println(line);
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

        System.out.print(sb.toString());
    }

    public void insert_task(String line) {
        this.list_data[idx] = line;
        this.idx += 1;
        echo("added: " + line, 4);
    }

    public void print_list() {

        String[] currentList = Arrays.copyOf(list_data, idx);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < currentList.length; i++) {
            sb.append((i + 1)).append(". ").append(currentList[i]);
            if (i < currentList.length - 1) sb.append("\n");
        }

        echo(sb.toString(), 4);
    }

    public void await_input() {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            String line = userInput.nextLine();
            if (line.equalsIgnoreCase("bye")) {
                break;
            } else if (line.equalsIgnoreCase("list")) {
                print_list();
            } else {
                insert_task(line);
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
