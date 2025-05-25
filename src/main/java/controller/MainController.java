package controller;

import dao.AlertDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import model.Alert;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainController {

    @FXML
    private BorderPane rootLayout;

    @FXML
    private StackPane mapContainer;

    @FXML
    private WebView mapView;

    @FXML
    private ListView<String> alertListView;

    @FXML
    private Button newAlertButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> severityFilter;

    @FXML
    private Label statusLabel;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label lastUpdatedLabel;

    @FXML
    private ProgressIndicator mapLoadingIndicator;

    @FXML
    private Label pageInfoLabel;

    private final AlertDAO alertDAO = new AlertDAO();
    private List<Alert> currentAlerts;

    @FXML
    public void initialize() {
        severityFilter.setItems(FXCollections.observableArrayList("All", "Low", "Medium", "High", "Critical"));
        severityFilter.setValue("All");
        loadAlerts();
    }

    private void loadAlerts() {
        try {
            currentAlerts = alertDAO.getRecentAlerts(10);
            updateAlertListView(currentAlerts);
            statusLabel.setText("Loaded alerts successfully");
        } catch (Exception e) {
            statusLabel.setText("Failed to load alerts");
            e.printStackTrace();
        }
    }

    private void updateAlertListView(List<Alert> alerts) {
        List<String> displayList = alerts.stream()
                .map(alert -> alert.getType() + " - " + alert.getLocation() + " (" + alert.getSeverity() + ")")
                .collect(Collectors.toList());
        alertListView.getItems().setAll(displayList);
    }

    @FXML
    private void handleNewAlert(javafx.event.ActionEvent event) {
        String alertId = UUID.randomUUID().toString();
        Alert newAlert = new Alert(alertId, "New Incident", "New Location", LocalDateTime.now(), "Low", "test_user");
        alertDAO.logAlert(newAlert);
        loadAlerts();
        statusLabel.setText("New alert added");
    }

    @FXML
    private void handleRefresh(javafx.event.ActionEvent event) {
        loadAlerts();
        statusLabel.setText("Alerts refreshed");
    }

    @FXML
    private void handleSearch(KeyEvent event) {
        String query = searchField.getText().toLowerCase();
        List<Alert> filtered = currentAlerts.stream()
                .filter(alert -> alert.getType().toLowerCase().contains(query) ||
                        alert.getLocation().toLowerCase().contains(query))
                .collect(Collectors.toList());
        updateAlertListView(filtered);
    }

    @FXML
    private void handleFilterChange(javafx.event.ActionEvent event) {
        String selectedSeverity = severityFilter.getValue();
        List<Alert> filtered = currentAlerts.stream()
                .filter(alert -> selectedSeverity.equals("All") || alert.getSeverity().equals(selectedSeverity))
                .collect(Collectors.toList());
        updateAlertListView(filtered);
    }

    @FXML
    private void handleNextPage(javafx.event.ActionEvent event) {
        // Pagination logic can be implemented later if needed
        statusLabel.setText("Next page feature coming soon");
    }

    @FXML
    private void handlePreviousPage(javafx.event.ActionEvent event) {
        // Pagination logic can be implemented later if needed
        statusLabel.setText("Previous page feature coming soon");
    }
}
