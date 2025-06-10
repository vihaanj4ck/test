package xjaw;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Debug: Print classpath and resource paths
            System.out.println("Classpath: " + System.getProperty("java.class.path"));
            System.out.println("Attempting to load FXML from: " +
                    Objects.requireNonNull(getClass().getResource("/view/main-view.fxml")));

            // Load FXML UI
            FXMLLoader loader = new FXMLLoader(
                    Objects.requireNonNull(getClass().getResource("/view/main-view.fxml"))
            );
            Parent root = loader.load();

            // Create scene from FXML root
            Scene scene = new Scene(root);

            // Load CSS stylesheet
            String cssPath = "/styles.css";
            if (getClass().getResource(cssPath) != null) {
                scene.getStylesheets().add(
                        Objects.requireNonNull(getClass().getResource(cssPath)).toExternalForm()
                );
                System.out.println("Successfully loaded CSS: " + cssPath);
            } else {
                System.err.println("CSS file not found: " + cssPath);
            }

            // Set scene to stage
            primaryStage.setScene(scene);
            primaryStage.setTitle("Disaster Alert System - v1.0");
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);
            primaryStage.setMaximized(true);

            // Load and set application icons
            loadApplicationIcons(primaryStage);

            // Show the stage (window)
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("Application failed to initialize", e);
            System.exit(1); // Force exit if startup fails
        }
    }

    private void loadApplicationIcons(Stage primaryStage) {
        List<String> iconPaths = List.of(
                "/icons/floppydisk.jpg",  // Corrected path (no /view prefix)
                "/icons/magnifying.jpg",
                "/icons/person.jpg",
                "/icons/refresh.jpg",
                "/icons/trashbin.jpg"
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
                    // Debug: Print full resource URL attempt
                    System.err.println("Full resource URL: " + getClass().getResource(path));
                }
            } catch (Exception e) {
                System.err.println("Error loading icon from " + path + ": " + e.getMessage());
            }
        }

        if (!loadedIcons.isEmpty()) {
            primaryStage.getIcons().addAll(loadedIcons);
            System.out.println("Set " + loadedIcons.size() + " application icons");
        } else {
            System.err.println("Warning: No application icons were loaded successfully");
        }
    }

    private void showErrorDialog(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Startup Error");
        alert.setHeaderText(message);

        TextArea textArea = new TextArea(e.toString());
        textArea.setEditable(false);
        textArea.setWrapText(true);

        alert.getDialogPane().setContentText(e.getMessage());
        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        // JavaFX performance tweaks
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        System.setProperty("javafx.animation.fullspeed", "true");
        System.setProperty("javafx.pulseLogger", "false");

        // Additional fixes for resource loading
        System.setProperty("javafx.verbose", "true");
        System.setProperty("javafx.preloader", "none");

        launch(args);
    }
}