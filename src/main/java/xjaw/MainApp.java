package xjaw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 1024, 768);

            // Load CSS
            if (getClass().getResource("/styles.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle("Disaster Alert System - v1.0");

            // Load Icon
            try (InputStream iconStream = getClass().getResourceAsStream("/icon.png")) {
                if (iconStream != null) {
                    primaryStage.getIcons().add(new Image(iconStream));
                } else {
                    System.err.println("Warning: Could not find application icon at /icon.png");
                }
            }

            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.setMaximized(true);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Application failed to initialize", e);
        }
    }

    public static void main(String[] args) {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        launch(args);
    }

    private void showErrorDialog(String message, Exception e) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Startup Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());

        javafx.scene.control.TextArea textArea = new javafx.scene.control.TextArea(e.toString());
        textArea.setEditable(false);
        alert.getDialogPane().setExpandableContent(textArea);

        alert.showAndWait();
    }
}
