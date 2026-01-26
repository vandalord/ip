package jax.ui;

import java.util.Scanner;

public class Ui {

    static String separator = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";

    public void greet() {
        // Start-up message text
        echo(" \uD83D\uDC4B你好! I'm jax.Jax, your personal assistant chatbot!\n"
                + " What can I do for you?", 4);
    }

    public void exit() {
        // Exit message text
        echo(" \uD83D\uDC4B再见. Hope to see you again soon!", 4);
    }

    public String readCommand() {
        Scanner userInput = new Scanner(System.in);
        String line = userInput.nextLine();
        return line;
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

    public void showError(String error) {
         echo(error, 4);
    }
}

