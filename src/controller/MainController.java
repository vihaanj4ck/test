package controller;  // Add this at the top

import javafx.scene.Scene;
import javafx.scene.Parent;
import dao.AlertDAO;
import view.MainView;

public class MainController {
    private final MainView view = new MainView();
    private final AlertDAO alertDAO = new AlertDAO();
    private Parent rootLayout;

    public MainController() {
        loadAlerts();
    }

    private void loadAlerts() {
        view.getAlertList().getItems().setAll(
                alertDAO.getRecentAlerts(10)
        );
    }

    public Scene getMainScene() {
        // Create the main scene
        Scene scene = new Scene(view.getView(), 1200, 800);

        // Apply CSS stylesheet
        String cssPath = getClass().getResource("/styles.css").toExternalForm();
        scene.getStylesheets().add(cssPath);

        return scene;
    }

    // Optional: If you need a separate method to get stylesheets
    private void applyStyles(Scene scene) {
        try {
            String cssPath = getClass().getResource("/styles.css").toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (NullPointerException e) {
            System.err.println("Could not load styles.css - file not found");
        }
    }

    // Main method if needed (though typically this would be in a separate Main class)
    public static void main(String[] args) {
        // JavaFX application launch would typically be in a separate Main class
    }
}