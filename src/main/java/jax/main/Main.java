package jax.main;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    private final Jax jax = new Jax();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/views/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            URL cssUrl = this.getClass().getResource("/css/main.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                logger.warning("CSS file not found. Check path loadout: /css/main.css");
            }

            stage.setScene(scene);
            stage.setTitle("Jax");
            stage.setMinHeight(220);
            stage.setMinWidth(417);
            fxmlLoader.<MainWindow>getController().setJax(jax);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load MainWindow FXML.", e);
        }
    }
}
