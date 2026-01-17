import java.util.NoSuchElementException;
import java.util.Scanner;

public class Jax {

    static String separator = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";

    public static void greet() {
        // Start-up message text
        echo(" \uD83D\uDC4B你好! I'm Jax, your personal assistant chatbot!'\n"
                + " What can I do for you?", 4);
    }

    public static void exit() {
        // Exit message text
        echo(" \uD83D\uDC4B再见. Hope to see you again soon!", 4);
    }

    public static void separate_text() {
        String line = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";
        System.out.println(line);
    }

    public static void echo(String text, int indentLevel) {
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

    public static void await_input() {
        Scanner userInput = new Scanner(System.in);
        while (true) {
            String line = userInput.nextLine();
            if (line.equalsIgnoreCase("bye")) {
                break;
            } else {
                echo(line, 4);
            }
        }
    }

    public static void main(String[] args) {
        greet();
        await_input();
        exit();
    }
}
