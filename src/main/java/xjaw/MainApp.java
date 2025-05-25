package java.xjaw;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Minimal scene with label
            Scene scene = new Scene(new Label("Hello, JavaFX!"), 800, 600);

            // Add CSS if available
            if (getClass().getResource("/styles.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            }

            primaryStage.setScene(scene);
            primaryStage.setTitle("Disaster Alert System - v1.0");
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.setMaximized(true);

            // Load all available icons
            loadApplicationIcons(primaryStage);

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Application failed to initialize", e);
        }
    }

    private void loadApplicationIcons(Stage primaryStage) {
        List<String> iconPaths = List.of(
                "/icons/floppydisk.jpg",    // Save icon
                "/icons/magnifying.jpg",    // Search icon
                "/icons/person.jpg",        // User icon
                "/icons/refresh.jpg", // Refresh icon
                "/icons/trashbin.jpg"       // Delete icon
        );

        List<Image> loadedIcons = new ArrayList<>();

        for (String path : iconPaths) {
            try (InputStream iconStream = getClass().getResourceAsStream(path)) {
                if (iconStream != null) {
                    Image iconImage = new Image(iconStream);
                    if (!iconImage.isError()) {
                        loadedIcons.add(iconImage);
                        System.out.println("Successfully loaded icon: " + path);
                    } else {
                        System.err.println("Error loading icon (corrupted): " + path);
                    }
                } else {
                    System.err.println("Icon not found at path: " + path);
                }
            } catch (Exception e) {
                System.err.println("Error loading icon from " + path + ": " + e.getMessage());
            }
        }

        // Set the first successfully loaded icon as application icon
        if (!loadedIcons.isEmpty()) {
            primaryStage.getIcons().addAll(loadedIcons);
            System.out.println("Set " + loadedIcons.size() + " application icons");
        } else {
            System.err.println("Warning: No application icons were loaded successfully");
        }
    }

    public static void main(String[] args) {
        // Better font rendering settings
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");

        // Additional JavaFX performance tweaks
        System.setProperty("javafx.animation.fullspeed", "true");
        System.setProperty("javafx.pulseLogger", "false");

        launch(args);
    }

    private void showErrorDialog(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Startup Error");
        alert.setHeaderText(message);

        // Create expandable Exception content
        TextArea textArea = new TextArea(e.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        alert.getDialogPane().setContentText(e.getMessage());
        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait();
    }
}