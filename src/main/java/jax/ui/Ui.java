package jax.ui;

import java.util.Scanner;


/**
 * Handles all interactions with the user (reading input and printing output).
 */
public class Ui {

    private static final String SEPERATOR = "―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――";

    /**
     * Displays the welcome message to user.
     */
    public void greet() {
        // Start-up message text
        echo(" \uD83D\uDC4B你好! I'm Jax, your personal assistant chatbot!\n"
                + " What can I do for you?", 4);
    }

    /**
     * Displays the exit message to user.
     */
    public void exit() {
        // Exit message text
        echo(" \uD83D\uDC4B再见. Hope to see you again soon!", 4);
    }

    /**
     * Reads and cleans user input.
     */
    public String readCommand() {
        Scanner userInput = new Scanner(System.in);
        if (userInput.hasNextLine()) {
            return userInput.nextLine().trim();
        }
        return null;
    }

    /**
     * Prepares and prints user-friendly output.
     * @param text text to be displayed.
     * @param indentLevel Level of indentation, for user-readability.
     */
    public void echo(String text, int indentLevel) {
        StringBuilder sb = new StringBuilder();
        String newLine = System.lineSeparator();
        String indent = " ".repeat(Math.max(0, indentLevel));
        sb.append(indent).append(SEPERATOR).append(newLine);
        if (text != null && !text.isEmpty()) {
            text.lines().forEach(line ->
                    sb.append(indent)
                            .append(line)
                            .append(newLine)
            );
        }
        sb.append(indent).append(SEPERATOR).append(newLine);
        System.out.print(sb);
    }

    /**
     * Abstraction of error display.
     * @param error error message to be displayed.
     */
    public void showError(String error) {
        echo(error, 4);
    }
}

