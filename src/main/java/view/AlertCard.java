package view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

// Use your own Alert class, not javafx.scene.control.Alert
import model.Alert; // Update this import based on your package

public class AlertCard {
    private final ListView<Alert> alertList = new ListView<>();
    private final VBox container = new VBox();
    private final Label headerLabel = new Label("Active Alerts");
    private final HBox filterBar = new HBox();
    private final ComboBox<String> severityFilter = new ComboBox<>();
    private final TextField searchField = new TextField();

    public AlertCard() {
        initializeUI();
        setupCellFactory();
        configureFilters();
    }

    private void initializeUI() {
        container.setSpacing(10);
        container.setPadding(new Insets(10));

        headerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        alertList.setPlaceholder(new Label("No active alerts"));
        alertList.setMinHeight(300);

        container.getChildren().addAll(headerLabel, filterBar, alertList);
    }

    private void setupCellFactory() {
        alertList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Alert> call(ListView<Alert> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Alert alert, boolean empty) {
                        super.updateItem(alert, empty);

                        if (empty || alert == null) {
                            setText(null);
                            setGraphic(null);
                            setStyle("");
                        } else {
                            createAlertCell(alert, this);
                        }
                    }
                };
            }
        });
    }

    private void createAlertCell(Alert alert, ListCell<Alert> cell) {
        VBox contentBox = new VBox(5);
        contentBox.setPadding(new Insets(8));

        HBox titleRow = new HBox(10);
        Label typeLabel = new Label(alert.getType());
        typeLabel.setStyle("-fx-font-weight: bold;");

        Label severityLabel = new Label(alert.getModality());
        severityLabel.setTextFill(getSeverityColor(alert.getModality()));

        titleRow.getChildren().addAll(typeLabel, severityLabel);

        Label detailsLabel = new Label(
                String.format("%s | %s", alert.getLocation(), alert.getTimestamp())
        );
        detailsLabel.setStyle("-fx-font-size: 0.9em; -fx-text-fill: #555;");

        contentBox.getChildren().addAll(titleRow, detailsLabel);

        cell.setStyle(getCellStyle(alert.getModality()));
        cell.setGraphic(contentBox);
    }

    private void configureFilters() {
        severityFilter.setItems(FXCollections.observableArrayList("All", "High", "Medium", "Low"));
        severityFilter.setValue("All");
        severityFilter.setPromptText("Filter by severity");

        searchField.setPromptText("Search alerts...");
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            // Implement search logic here
        });

        filterBar.setSpacing(10);
        filterBar.getChildren().addAll(
                new Label("Filter:"), severityFilter,
                new Label("Search:"), searchField
        );
    }

    private Color getSeverityColor(String severity) {
        return switch (severity) {
            case "High" -> Color.RED;
            case "Medium" -> Color.ORANGE;
            case "Low" -> Color.GREEN;
            default -> Color.BLACK;
        };
    }

    private String getCellStyle(String severity) {
        String baseStyle = "-fx-background-radius: 5; -fx-border-radius: 5; -fx-padding: 5; ";
        return baseStyle + switch (severity) {
            case "High" -> "-fx-background-color: #ffeeee; -fx-border-color: #ffcccc; -fx-border-width: 1;";
            case "Medium" -> "-fx-background-color: #fff8e1; -fx-border-color: #ffecb3; -fx-border-width: 1;";
            case "Low" -> "-fx-background-color: #f1f8e9; -fx-border-color: #dcedc8; -fx-border-width: 1;";
            default -> "-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-width: 1;";
        };
    }

    public VBox getView() {
        return container;
    }

    public ListView<Alert> getAlertList() {
        return alertList;
    }

    public void addAlert(Alert alert) {
        alertList.getItems().add(alert);
    }

    public void clearAlerts() {
        alertList.getItems().clear();
    }
}
