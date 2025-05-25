package view;

import controller.MainController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entry point for the Disaster Alert System JavaFX application.
 * Configures and launches the primary application window.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize main controller and get the root scene
            MainController controller = new MainController();
            Scene mainScene = controller.getMainScene();

            // Apply CSS styling
            mainScene.getStylesheets().add(
                    getClass().getResource("/styles.css").toExternalForm()
            );

            // Configure primary stage
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Disaster Alert System - v1.0");

            // Set minimum window size
            primaryStage.setMinWidth(1024);
            primaryStage.setMinHeight(768);

            // Start maximized but allow resizing
            primaryStage.setMaximized(true);

            // Set application icon (ensure icon.png exists in resources)
            try {
                primaryStage.getIcons().add(new Image(
                        getClass().getResourceAsStream("/images/icon.png")
                ));
            } catch (Exception e) {
                System.err.println("Warning: Could not load application icon");
            }

            // Handle window close request
            primaryStage.setOnCloseRequest(event -> {
                System.out.println("Application shutdown initiated");
                // Add any cleanup logic here
            });

            primaryStage.show();

        } catch (Exception e) {
            System.err.println("Fatal error during application startup:");
            e.printStackTrace();
            showErrorDialog("Application failed to initialize", e);
        }
    }

    public static void main(String[] args) {
        try {
            // Enable better font rendering
            System.setProperty("prism.lcdtext", "false");
            System.setProperty("prism.text", "t2k");

            launch(args);
        } catch (Exception e) {
            System.err.println("Critical failure in main():");
            e.printStackTrace();
        }
    }

    /**
     * Displays an error dialog when startup fails
     */
    private void showErrorDialog(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Startup Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());

        TextArea textArea = new TextArea(e.toString());
        textArea.setEditable(false);
        alert.getDialogPane().setExpandableContent(textArea);

        alert.showAndWait();
    }
}