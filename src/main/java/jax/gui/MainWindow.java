package jax.gui;

import java.util.Objects;

import jax.main.Jax;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jax.main.JaxException;


/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Jax jax;

    private final Image userImage = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/DaUser.png")));
    private final Image jaxImage = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/DaDuke.png")));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Jax instance */
    public void setJax(Jax d) {
        jax = d;
        showJaxMessage(jax.getGreeting());
    }

    /**
     * Helper method to display the user's message.
     */
    private void showUserMessage(String ... messages) {
        for (String m : messages) {
            if (m == "") {
                return;
            }
            dialogContainer.getChildren().add(
                    DialogBox.getUserDialog(m, jaxImage)
            );
        }
    }

    /**
     * Helper method to display the bot's message.
     */
    private void showJaxMessage(String ... messages) {
        for (String m : messages) {
            if (m == "") {
                return;
            }
            dialogContainer.getChildren().add(
                    DialogBox.getJaxDialog(m, jaxImage)
            );
        }
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        String response = jax.getResponse(input);

        if (input.isEmpty()) {
            return;
        }

        showUserMessage(input);
        showJaxMessage(response);

        userInput.clear();

        if (input.trim().equalsIgnoreCase("bye")) {
            showJaxMessage((jax.getGoodbye()));
            exit();
        }

        if (input.equalsIgnoreCase("clear")) {
            dialogContainer.getChildren().clear();
            userInput.clear();
        }
    }

    /**
     * Handles cleanup and exit logic.
     */
    private void exit() {
        double delayTime = 1.5;

        try {
            jax.saveData();
        } catch (JaxException e) {
            showJaxMessage("Error: Could not save data!\n" + e.getMessage());

            delayTime = 3.0;
        } finally {
            PauseTransition delay = new PauseTransition(Duration.seconds(delayTime));
            delay.setOnFinished(event -> Platform.exit());
            delay.play();
        }
    }
}
