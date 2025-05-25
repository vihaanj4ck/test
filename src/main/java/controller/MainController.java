package controller;  // Only one package declaration needed

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import dao.AlertDAO;
import java.util.Objects;

public class MainController {
    @FXML private BorderPane rootLayout;
    @FXML private StackPane mapContainer;
    @FXML private ListView<String> alertListView;
    @FXML private Button newAlertButton;
    @FXML private Button refreshButton;

    private final AlertDAO alertDAO = new AlertDAO();

    @FXML
    public void initialize() {
        loadAlerts();
        setupButtonActions();
    }

    private void loadAlerts() {
        try {
            alertListView.getItems().setAll(
                    alertDAO.getRecentAlerts(10)
            );
        } catch (Exception e) {
            System.err.println("Error loading alerts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewAlert(ActionEvent event) {
        try {
            // Implement your new alert logic here
            System.out.println("New Alert button clicked");

            // Example: Create a new alert and add to database
            // Alert newAlert = new Alert(...);
            // alertDAO.logAlert(newAlert);
            // loadAlerts(); // Refresh the list

        } catch (Exception e) {
            System.err.println("Error creating new alert: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh(ActionEvent event) {
        loadAlerts();
        System.out.println("Alerts refreshed");
    }

    public Scene getMainScene() {
        Scene scene = new Scene(rootLayout, 1200, 800);
        applyStyles(scene);
        return scene;
    }

    private void applyStyles(Scene scene) {
        try {
            String cssPath = Objects.requireNonNull(
                    getClass().getResource("/styles.css")  // Fixed path
            ).toExternalForm();
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("Error loading styles: " + e.getMessage());
        }
    }

    private void setupButtonActions() {
        newAlertButton.setOnAction(this::handleNewAlert);
        refreshButton.setOnAction(this::handleRefresh);
    }

    public void handleNextPage(ActionEvent actionEvent) {
    }

    public void handlePreviousPage(ActionEvent actionEvent) {
    }

    public void handleFilterChange(ActionEvent actionEvent) {
    }

    public void handleSearch(KeyEvent keyEvent) {
    }
}