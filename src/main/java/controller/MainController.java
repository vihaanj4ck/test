package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
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

    // Icon images
    private Image addIcon;
    private Image refreshIcon;

    @FXML
    public void initialize() {
        loadIcons();            // 1. Load images
        setupButtonIcons();     // 2. Attach to buttons
        loadAlerts();           // 3. Load alert data
        setupButtonActions();   // 4. Setup button click handlers
    }

    private void loadIcons() {
        try {
            addIcon = new Image(getClass().getResourceAsStream("/icons/floppydisk.webp"));
            refreshIcon = new Image(getClass().getResourceAsStream("/icons/recircular arrow.webp"));
        } catch (NullPointerException e) {
            System.err.println("Error loading icons: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupButtonIcons() {
        if (addIcon != null) {
            newAlertButton.setGraphic(new ImageView(addIcon));
        }
        if (refreshIcon != null) {
            refreshButton.setGraphic(new ImageView(refreshIcon));
        }
    }

    private void loadAlerts() {
        try {
            alertListView.getItems().setAll(alertDAO.getRecentAlerts(10));
        } catch (Exception e) {
            System.err.println("Error loading alerts: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewAlert(ActionEvent event) {
        System.out.println("New Alert button clicked");
        // TODO: Implement actual alert creation
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
                    getClass().getResource("/styles.css")
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

    public void handleNextPage(ActionEvent actionEvent) {}
    public void handlePreviousPage(ActionEvent actionEvent) {}
    public void handleFilterChange(ActionEvent actionEvent) {}
    public void handleSearch(KeyEvent keyEvent) {}
}
