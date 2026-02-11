package jax.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 */
public class DialogBox extends HBox {

    @SuppressWarnings("unused")
    @FXML
    private Label dialog;
    @SuppressWarnings("unused")
    @FXML
    private ImageView displayPicture;

    private static final Logger logger = Logger.getLogger(DialogBox.class.getName());

    private DialogBox(String text, Image img) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/views/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();

            URL cssUrl = this.getClass().getResource("/css/dialogbox.css");
            if (cssUrl != null) {
                this.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                logger.warning("CSS file not found. Check path loadout: /css/dialogbox.css");
            }

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load DialogBox FXML.", e);
        } catch (NullPointerException e) {
            System.out.println("Error: CSS file not found. Check path loadout");
        }

        dialog.setText(text);
        displayPicture.setImage(img);
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.BOTTOM_LEFT);
    }

    public static DialogBox getUserDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.dialog.getStyleClass().add("user-label");
        return db;
    }

    public static DialogBox getJaxDialog(String text, Image img) {
        var db = new DialogBox(text, img);
        db.flip();
        db.dialog.getStyleClass().add("jax-label");
        return db;
    }
}
