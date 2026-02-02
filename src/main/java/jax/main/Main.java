package jax.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import jax.gui.MainWindow;

/**
 * A GUI for Jax using FXML.
 */
public class Main extends Application {

    private Jax jax = new Jax();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            // Style with CSS
            String css = this.getClass().getResource("/css/main.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setJax(jax);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
