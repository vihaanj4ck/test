package controller;

import dao.AlertDAO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebView;
import model.Alert;
import service.AlertService;
import service.SMSService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class MainController {

    // FXML components
    @FXML private ListView<Alert> alertListView;
    @FXML private TextField searchField;
    @FXML private TextField descriptionField;
    @FXML private ComboBox<String> severityFilter;
    @FXML private ProgressBar progressBar;
    @FXML private Button newAlertButton, refreshButton, nextPageButton, previousPageButton;
    @FXML private Label statusLabel, lastUpdatedLabel, summaryTotalLabel, summaryRecentLabel, latestActivityLabel;
    @FXML private WebView mapView;
    @FXML private ProgressIndicator mapLoadingIndicator;
    @FXML private Label pageInfoLabel;
    @FXML private Label dbStatusLabel;
    @FXML private Label criticalCountLabel, highCountLabel, mediumCountLabel, lowCountLabel;
    @FXML private ProgressIndicator statsLoadingIndicator;
    @FXML private TextField typeField;
    @FXML private TextField locationField;
    @FXML private TextField latitudeField;
    @FXML private TextField longitudeField;
    @FXML private ComboBox<String> severityCombo;


    // Constants and fields
    private AlertDAO alertDAO;
    private final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final DateTimeFormatter SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, HH:mm");

    private List<Alert> allAlerts = new ArrayList<>();
    private List<Alert> filteredAlerts = new ArrayList<>();
    private int currentPage = 0;
    private int totalPages = 1;
    private static final int PAGE_SIZE = 10;
    private boolean mapInitialized = false;
    // Initialize services in your controller
    private final AlertService alertService;
    private final SMSService smsService;

    public MainController() {
        this.alertDAO = new AlertDAO(); // Your existing DAO
        this.alertService = new AlertService(alertDAO);
        this.smsService = new SMSService();
    }

    private void showSuccessAlert(String message) {
        Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }


    private void loadAlerts() {
        AlertDAO alertDAO = new AlertDAO();
        List<Alert> alerts = alertDAO.getAllAlerts(); // This should return a List<Alert>

        if (alerts != null) {
            alertListView.getItems().setAll(alerts); // Update the ListView
        } else {
            System.out.println("No alerts loaded or DB error.");
        }
    }

    @FXML
    public void initialize() {
        try {
            // Initialize DAO with proper error handling
            initializeDAO();
            initializeComponents();
            setupEventListeners();
            initializeMap();
            loadAlertsAsync();
            updateDatabaseStatus();
            System.out.println("MainController initialized successfully");
        } catch (Exception e) {
            System.err.println("Error during initialization: " + e.getMessage());
            e.printStackTrace();
            updateStatus("Initialization error: " + e.getMessage(), true);
        }
    }

    private void initializeDAO() {
        try {
            alertDAO = new AlertDAO();
            System.out.println("AlertDAO initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize AlertDAO: " + e.getMessage());
            // Create a mock DAO for testing if real one fails
            alertDAO = createMockDAO();
            updateStatus("Using mock database - check database connection", true);
        }
    }

    // Create a mock DAO for testing when database is not available
    private AlertDAO createMockDAO() {
        return new AlertDAO() {
            private List<Alert> mockAlerts = new ArrayList<>();
            private int idCounter = 1;

            @Override
            public List<Alert> getAllAlerts() {
                if (mockAlerts.isEmpty()) {
                    mockAlerts.add(new Alert(String.valueOf(idCounter++), "Earthquake", "San Francisco",
                            LocalDateTime.now().minusHours(2), "Critical", "user1",
                            37.7749, -122.4194, "Severe ground shaking"));
                    mockAlerts.add(new Alert(String.valueOf(idCounter++), "Flood", "New Orleans",
                            LocalDateTime.now().minusHours(1), "High", "user2",
                            29.9511, -90.0715, "Water levels rising rapidly"));
                    mockAlerts.add(new Alert(String.valueOf(idCounter++), "Wildfire", "Los Angeles",
                            LocalDateTime.now().minusMinutes(30), "Medium", "user3",
                            34.0522, -118.2437, "Wildfire spreading through dry areas"));
                }
                return new ArrayList<>(mockAlerts);
            }




            @Override
            public boolean insertAlert(Alert alert) {
                try {
                    alert.setAlertId(String.valueOf(idCounter++));
                    mockAlerts.add(alert);
                    System.out.println("Mock insert successful for: " + alert.getType());
                    return true;
                } catch (Exception e) {
                    System.err.println("Mock insert failed: " + e.getMessage());
                    return false;
                }
            }

            @Override
            public boolean testConnection() {
                return true; // Mock connection always works
            }
        };
    }

    private void initializeComponents() {
        // Initialize severity filter
        severityFilter.setItems(FXCollections.observableArrayList("All", "Critical", "High", "Medium", "Low"));
        severityFilter.setValue("All");

        // Setup enhanced alert list view
        setupEnhancedAlertListView();

        // Initialize loading indicators
        progressBar.setVisible(false);
        if (mapLoadingIndicator != null) {
            mapLoadingIndicator.setVisible(true);
        }
        if (statsLoadingIndicator != null) {
            statsLoadingIndicator.setVisible(false);
        }
    }

    private void setupEventListeners() {
        // Search field listener
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            currentPage = 0;
            filterAlerts();
        });

        // Severity filter listener
        severityFilter.valueProperty().addListener((obs, oldVal, newVal) -> {
            currentPage = 0;
            filterAlerts();
        });

        // Alert list view click listener
        alertListView.setOnMouseClicked(this::handleAlertSelection);
    }

    private void handleAlertSelection(MouseEvent mouseEvent) {
    }

    private void setupEnhancedAlertListView() {
        alertListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Alert alert, boolean empty) {
                super.updateItem(alert, empty);
                if (empty || alert == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("");
                    return;
                }

                try {
                    HBox container = new HBox(10);
                    container.setAlignment(Pos.CENTER_LEFT);

                    Label severityIcon = new Label();
                    severityIcon.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

                    VBox content = new VBox(2);

                    Label mainLabel = new Label();
                    Label timeLabel = new Label("Time: " + alert.getTimestamp().format(SHORT_TIME_FORMATTER));
                    timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #666;");

                    String severity = alert.getSeverity().toLowerCase();
                    String baseStyle = "-fx-padding: 10 15 10 15; -fx-background-radius: 6; -fx-border-radius: 6;";

                    switch (severity) {
                        case "critical" -> {
                            severityIcon.setText("🔴");
                            severityIcon.setStyle(severityIcon.getStyle() + " -fx-text-fill: #d32f2f;");
                            mainLabel.setStyle("-fx-text-fill: #d32f2f; -fx-font-weight: bold;");
                            setStyle(baseStyle + " -fx-background-color: linear-gradient(to right, #ffebee, #ffcdd2); -fx-border-color: #ef5350; -fx-border-width: 1;");
                        }
                        case "high" -> {
                            severityIcon.setText("🟠");
                            severityIcon.setStyle(severityIcon.getStyle() + " -fx-text-fill: #f57c00;");
                            mainLabel.setStyle("-fx-text-fill: #f57c00; -fx-font-weight: bold;");
                            setStyle(baseStyle + " -fx-background-color: linear-gradient(to right, #fff3e0, #ffe0b2); -fx-border-color: #ff9800; -fx-border-width: 1;");
                        }
                        case "medium" -> {
                            severityIcon.setText("🟡");
                            severityIcon.setStyle(severityIcon.getStyle() + " -fx-text-fill: #fbc02d;");
                            mainLabel.setStyle("-fx-text-fill: #f57f17; -fx-font-weight: bold;");
                            setStyle(baseStyle + " -fx-background-color: linear-gradient(to right, #fffde7, #fff9c4); -fx-border-color: #fbc02d; -fx-border-width: 1;");
                        }
                        case "low" -> {
                            severityIcon.setText("🟢");
                            severityIcon.setStyle(severityIcon.getStyle() + " -fx-text-fill: #388e3c;");
                            mainLabel.setStyle("-fx-text-fill: #388e3c; -fx-font-weight: bold;");
                            setStyle(baseStyle + " -fx-background-color: linear-gradient(to right, #e8f5e8, #c8e6c9); -fx-border-color: #4caf50; -fx-border-width: 1;");
                        }
                        default -> {
                            severityIcon.setText("⚪");
                            mainLabel.setStyle("-fx-text-fill: #424242;");
                            setStyle(baseStyle + " -fx-background-color: #f5f5f5; -fx-border-color: #bdbdbd; -fx-border-width: 1;");
                        }
                    }

                    mainLabel.setText(alert.getType() + " - " + alert.getLocation() + " (" + alert.getSeverity() + ")");
                    content.getChildren().addAll(mainLabel, timeLabel);
                    container.getChildren().addAll(severityIcon, content);
                    setGraphic(container);
                    setText(null);
                } catch (Exception e) {
                    System.err.println("Error updating list cell: " + e.getMessage());
                    setText(alert.getType() + " - " + alert.getLocation());
                    setGraphic(null);
                }
            }
        });
    }

    private void loadAlertsAsync() {
        setLoading(true);
        updateStatus("Loading alerts...", false);

        Task<List<Alert>> task = new Task<>() {
            @Override
            protected List<Alert> call() throws Exception {
                try {
                    if (alertDAO == null) {
                        throw new Exception("AlertDAO is not initialized");
                    }
                    return alertDAO.getAllAlerts();
                } catch (Exception e) {
                    System.err.println("Error loading alerts: " + e.getMessage());
                    throw e;
                }
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    try {
                        List<Alert> loadedAlerts = getValue();
                        if (loadedAlerts != null) {
                            allAlerts = new ArrayList<>(loadedAlerts);
                            currentPage = 0;
                            filterAlerts();
                            updateDashboardStats();

                            // Load alerts on map after a short delay
                            Platform.runLater(() -> {
                                try {
                                    Thread.sleep(1000); // Wait for map to be ready
                                    loadAlertsOnMap();
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            });

                            setLoading(false);
                            updateLastUpdatedLabel();
                            updateStatus("Alerts loaded successfully. Total: " + allAlerts.size(), false);
                        } else {
                            allAlerts = new ArrayList<>();
                            setLoading(false);
                            updateStatus("No alerts found.", false);
                        }
                    } catch (Exception e) {
                        System.err.println("Error in succeeded callback: " + e.getMessage());
                        setLoading(false);
                        updateStatus("Error processing loaded alerts.", true);
                    }
                });
            }

            private void updateLastUpdatedLabel() {
            }

            @Override
            protected void failed() {
                Platform.runLater(() -> {
                    Throwable ex = getException();
                    String errorMsg = ex != null ? ex.getMessage() : "Unknown error";
                    updateStatus("Failed to load alerts: " + errorMsg, true);
                    setLoading(false);
                    showErrorAlert("Load Error", "Could not load alerts from database.\n\nError: " + errorMsg + "\n\nPlease check your database connection.");
                    System.err.println("Failed to load alerts: " + errorMsg);
                });
            }
        };

        Thread loadThread = new Thread(task);
        loadThread.setDaemon(true);
        loadThread.start();
    }

    private void setLoading(boolean b) {
    }

    private void filterAlerts() {
        if (allAlerts == null) {
            allAlerts = new ArrayList<>();
        }

        try {
            String query = searchField.getText() != null ?
                    searchField.getText().toLowerCase().trim() : "";
            String severity = severityFilter.getValue() != null ?
                    severityFilter.getValue() : "All";

            filteredAlerts = allAlerts.stream()
                    .filter(a -> {
                        boolean severityMatch = severity.equals("All") ||
                                a.getSeverity().equalsIgnoreCase(severity);
                        boolean textMatch = query.isEmpty() ||
                                a.getType().toLowerCase().contains(query) ||
                                a.getLocation().toLowerCase().contains(query);
                        return severityMatch && textMatch;
                    })
                    .collect(Collectors.toList());

            totalPages = Math.max(1, (int) Math.ceil((double) filteredAlerts.size() / PAGE_SIZE));

            if (currentPage >= totalPages) {
                currentPage = Math.max(0, totalPages - 1);
            }

            updatePagination();
            updateListViewPage();
        } catch (Exception e) {
            System.err.println("Error filtering alerts: " + e.getMessage());
            updateStatus("Error filtering alerts: " + e.getMessage(), true);
        }
    }

    private void updatePagination() {
        previousPageButton.setDisable(currentPage == 0);
        nextPageButton.setDisable(currentPage >= totalPages - 1);
        pageInfoLabel.setText(String.format("Page %d of %d", currentPage + 1, totalPages));
    }

    private void updateListViewPage() {
        if (filteredAlerts == null || filteredAlerts.isEmpty()) {
            alertListView.setItems(FXCollections.observableArrayList());
            updateStatus("No alerts found.", false);
            return;
        }

        try {
            int fromIndex = currentPage * PAGE_SIZE;
            int toIndex = Math.min(fromIndex + PAGE_SIZE, filteredAlerts.size());
            List<Alert> pageItems = filteredAlerts.subList(fromIndex, toIndex);

            alertListView.setItems(FXCollections.observableArrayList(pageItems));
            updateStatus(String.format("Showing %d to %d of %d alerts",
                    fromIndex + 1, toIndex, filteredAlerts.size()), false);
        } catch (Exception e) {
            System.err.println("Error updating list view page: " + e.getMessage());
            updateStatus("Error displaying alerts.", true);
        }
    }

    private void updateDashboardStats() {
        try {
            if (allAlerts == null || allAlerts.isEmpty()) {
                Map<String, Integer> emptyStats = new HashMap<>();
                emptyStats.put("Total", 0);
                emptyStats.put("Recent", 0);
                emptyStats.put("Critical", 0);
                emptyStats.put("High", 0);
                emptyStats.put("Medium", 0);
                emptyStats.put("Low", 0);
                updateSummarySection(emptyStats);
                updateLatestActivity();
                return;
            }

            Map<String, Integer> stats = new HashMap<>();
            stats.put("Total", allAlerts.size());

            LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
            long recentCount = allAlerts.stream()
                    .filter(alert -> alert.getTimestamp().isAfter(twentyFourHoursAgo))
                    .count();
            stats.put("Recent", (int) recentCount);

            Map<String, Long> severityCounts = allAlerts.stream()
                    .collect(Collectors.groupingBy(Alert::getSeverity, Collectors.counting()));

            stats.put("Critical", severityCounts.getOrDefault("Critical", 0L).intValue());
            stats.put("High", severityCounts.getOrDefault("High", 0L).intValue());
            stats.put("Medium", severityCounts.getOrDefault("Medium", 0L).intValue());
            stats.put("Low", severityCounts.getOrDefault("Low", 0L).intValue());

            updateSummarySection(stats);
            updateLatestActivity();
        } catch (Exception e) {
            System.err.println("Error updating dashboard stats: " + e.getMessage());
        }
    }

    private void updateSummarySection(Map<String, Integer> stats) {
        if (summaryTotalLabel != null) {
            summaryTotalLabel.setText("Total: " + stats.getOrDefault("Total", 0));
        }
        if (summaryRecentLabel != null) {
            summaryRecentLabel.setText("Recent (24h): " + stats.getOrDefault("Recent", 0));
        }
        if (criticalCountLabel != null) {
            criticalCountLabel.setText(String.valueOf(stats.getOrDefault("Critical", 0)));
        }
        if (highCountLabel != null) {
            highCountLabel.setText(String.valueOf(stats.getOrDefault("High", 0)));
        }
        if (mediumCountLabel != null) {
            mediumCountLabel.setText(String.valueOf(stats.getOrDefault("Medium", 0)));
        }
        if (lowCountLabel != null) {
            lowCountLabel.setText(String.valueOf(stats.getOrDefault("Low", 0)));
        }
    }

    private void updateLatestActivity() {
        if (latestActivityLabel == null) return;

        if (allAlerts == null || allAlerts.isEmpty()) {
            latestActivityLabel.setText("No recent alerts");
            return;
        }

        Alert latest = allAlerts.stream()
                .max((a1, a2) -> a1.getTimestamp().compareTo(a2.getTimestamp()))
                .orElse(allAlerts.get(0));

        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        long recentCount = allAlerts.stream()
                .filter(a -> a.getTimestamp().isAfter(oneHourAgo))
                .count();

        if (recentCount > 0) {
            latestActivityLabel.setText("Latest: " + latest.getType() + " in " +
                    latest.getLocation() + " (" + recentCount + " in last hour)");
        } else {
            latestActivityLabel.setText("Latest: " + latest.getType() + " in " +
                    latest.getLocation() + " at " +
                    latest.getTimestamp().format(DateTimeFormatter.ofPattern("HH:mm")));
        }
    }

    private void updateStatus(String message, boolean isError) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: black;");
        }
        System.out.println((isError ? "ERROR: " : "INFO: ") + message);
    }

    private void showErrorAlert(String title, String message) {
        try {
            Platform.runLater(() -> {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle(title);
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();
            });
        } catch (Exception e) {
            System.err.println("Error showing alert dialog: " + e.getMessage());
        }
    }

    private void showAlertDetails(Alert alert) {
        try {
            javafx.scene.control.Alert infoAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Alert Details");
            infoAlert.setHeaderText("Alert ID: " + alert.getAlertId());
            infoAlert.setContentText(String.format(
                    "Type: %s\nLocation: %s\nSeverity: %s\nTime: %s\nUser ID: %s\nCoordinates: %.6f, %.6f",
                    alert.getType(),
                    alert.getLocation(),
                    alert.getSeverity(),
                    alert.getTimestamp().format(DATETIME_FORMATTER),
                    alert.getUserId(),
                    alert.getLatitude(),
                    alert.getLongitude()
            ));
            infoAlert.showAndWait();
        } catch (Exception e) {
            System.err.println("Error showing alert details: " + e.getMessage());
        }
    }

    @FXML
    private void handleNextPage() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            updatePagination();
            updateListViewPage();
        }
    }

    @FXML
    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            updatePagination();
            updateListViewPage();
        }
    }

    @FXML
    private void handleRefresh() {
        if (searchField != null) searchField.clear();
        if (severityFilter != null) severityFilter.setValue("All");
        currentPage = 0;
        loadAlertsAsync();
    }

    @FXML
    private void handleNewAlert() {
        try {
            Dialog<Alert> dialog = new Dialog<>();
            dialog.setTitle("New Alert");
            dialog.setHeaderText("Create a new disaster alert");
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

            TextField typeField = new TextField();
            typeField.setPromptText("Type (e.g., Flood, Earthquake)");

            TextField locationField = new TextField();
            locationField.setPromptText("Location (e.g., New York, Tokyo)");

            TextField latField = new TextField();
            latField.setPromptText("Latitude (e.g., 40.7128)");

            TextField lngField = new TextField();
            lngField.setPromptText("Longitude (e.g., -74.0060)");

            ComboBox<String> severityCombo = new ComboBox<>();
            severityCombo.setItems(FXCollections.observableArrayList("Critical", "High", "Medium", "Low"));
            severityCombo.setValue("Medium");

            grid.add(new Label("Type:"), 0, 0);
            grid.add(typeField, 1, 0);
            grid.add(new Label("Location:"), 0, 1);
            grid.add(locationField, 1, 1);
            grid.add(new Label("Latitude:"), 0, 2);
            grid.add(latField, 1, 2);
            grid.add(new Label("Longitude:"), 0, 3);
            grid.add(lngField, 1, 3);
            grid.add(new Label("Severity:"), 0, 4);
            grid.add(severityCombo, 1, 4);

            Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setDisable(true);

            // Enhanced validation
            Runnable validateInput = () -> {
                boolean valid = !typeField.getText().trim().isEmpty() &&
                        !locationField.getText().trim().isEmpty();

                // Optional: Validate coordinates if provided
                if (!latField.getText().trim().isEmpty() || !lngField.getText().trim().isEmpty()) {
                    try {
                        if (!latField.getText().trim().isEmpty()) {
                            double lat = Double.parseDouble(latField.getText().trim());
                            if (lat < -90 || lat > 90) {
                                valid = false;
                            }
                        }
                        if (!lngField.getText().trim().isEmpty()) {
                            double lng = Double.parseDouble(lngField.getText().trim());
                            if (lng < -180 || lng > 180) {
                                valid = false;
                            }
                        }
                    } catch (NumberFormatException e) {
                        valid = false;
                    }
                }

                okButton.setDisable(!valid);
            };

            typeField.textProperty().addListener((obs, oldVal, newVal) -> validateInput.run());
            locationField.textProperty().addListener((obs, oldVal, newVal) -> validateInput.run());
            latField.textProperty().addListener((obs, oldVal, newVal) -> validateInput.run());
            lngField.textProperty().addListener((obs, oldVal, newVal) -> validateInput.run());

            dialog.getDialogPane().setContent(grid);
            Platform.runLater(typeField::requestFocus);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    try {
                        // Handle coordinates properly as Double (nullable)
                        Double lat = null;
                        Double lng = null;

                        if (!latField.getText().trim().isEmpty() && !lngField.getText().trim().isEmpty()) {
                            lat = Double.parseDouble(latField.getText().trim());
                            lng = Double.parseDouble(lngField.getText().trim());

                            // Validate coordinate ranges
                            if (lat < -90 || lat > 90) {
                                showErrorAlert("Invalid Input", "Latitude must be between -90 and 90");
                                return null;
                            }
                            if (lng < -180 || lng > 180) {
                                showErrorAlert("Invalid Input", "Longitude must be between -180 and 180");
                                return null;
                            }
                        }

                        // Create Alert with correct parameter order
                        return new Alert(
                                null,                           // alertId
                                typeField.getText().trim(),     // type
                                locationField.getText().trim(), // location
                                LocalDateTime.now(),            // timestamp
                                severityCombo.getValue(),       // severity
                                "system",                      // userId
                                lat,                           // latitude (Double)
                                lng,                           // longitude (Double)
                                descriptionField.getText().trim()  // description (String)
                        );

                    } catch (NumberFormatException e) {
                        showErrorAlert("Invalid Input", "Please enter valid coordinates (numbers only)");
                        return null;
                    } catch (Exception e) {
                        showErrorAlert("Error", "Error creating alert: " + e.getMessage());
                        return null;
                    }
                }
                return null;
            });

            dialog.showAndWait().ifPresent(alert -> {
                setLoading(true);
                updateStatus("Creating new alert...", false);

                Task<Boolean> insertTask = new Task<>() {
                    @Override
                    protected Boolean call() throws Exception {
                        try {
                            if (alertDAO == null) {
                                throw new Exception("Database connection not available");
                            }

                            // Add debug logging
                            System.out.println("Attempting to insert alert:");
                            System.out.println("Type: " + alert.getType());
                            System.out.println("Location: " + alert.getLocation());
                            System.out.println("Severity: " + alert.getSeverity());
                            System.out.println("User ID: " + alert.getUserId());
                            System.out.println("Coordinates: " + alert.getLatitude() + ", " + alert.getLongitude());

                            boolean result = alertDAO.insertAlert(alert);
                            System.out.println("Insert result: " + result);
                            return result;
                        } catch (Exception e) {
                            System.err.println("Error inserting alert: " + e.getMessage());
                            e.printStackTrace();
                            throw e;
                        }
                    }

                    @Override
                    protected void succeeded() {
                        Platform.runLater(() -> {
                            Boolean result = getValue();
                            if (result != null && result) {
                                updateStatus("New alert created successfully.", false);
                                loadAlertsAsync(); // Reload all data

                                // Add to map immediately if coordinates are provided
                                if (alert.getLatitude() != 0.0 && alert.getLongitude() != 0.0) {
                                    addAlertToMap(
                                            alert.getType(),
                                            alert.getLocation(),
                                            alert.getLatitude(),
                                            alert.getLongitude(),
                                            alert.getSeverity(),
                                            alert.getTimestamp().format(SHORT_TIME_FORMATTER)
                                    );
                                }
                            } else {
                                updateStatus("Failed to create alert - database insertion failed.", true);
                                showErrorAlert("Insert Error",
                                        "Could not insert new alert into database.\n\n" +
                                                "This might be due to:\n" +
                                                "- Database connection issues\n" +
                                                "- Invalid data format\n" +
                                                "- Database constraints\n" +
                                                "- Missing required fields\n\n" +
                                                "Please check your database configuration and table structure.");
                            }
                            setLoading(false);
                        });
                    }

                    @Override
                    protected void failed() {
                        Platform.runLater(() -> {
                            Throwable ex = getException();
                            String errorMsg = ex != null ? ex.getMessage() : "Unknown database error";
                            updateStatus("Failed to create alert: " + errorMsg, true);
                            setLoading(false);
                            showErrorAlert("Insert Error",
                                    "Database error occurred while creating alert.\n\n" +
                                            "Error details: " + errorMsg + "\n\n" +
                                            "Please check:\n" +
                                            "- Database connection\n" +
                                            "- Database permissions\n" +
                                            "- Table structure\n" +
                                            "- Data constraints");
                        });
                    }
                };

                Thread insertThread = new Thread(insertTask);
                insertThread.setDaemon(true);
                insertThread.start();
            });
        } catch (Exception e) {
            System.err.println("Error in handleNewAlert: " + e.getMessage());
            e.printStackTrace();
            showErrorAlert("Dialog Error", "Could not open new alert dialog: " + e.getMessage());
        }
    }
    public void handleCreateAlert() {
        Alert alert = new Alert("A102", "Flood", "Delhi NCR",
                LocalDateTime.now(), "High", "user123",
                28.6139,      // latitude as Double
                77.2090,      // longitude as Double
                "Severe flood due to heavy rainfall"
        );  // <--- closing parenthesis and semicolon here

        // 🔍 Debug: Print all values before inserting
        System.out.println("DEBUG: alert_id = " + alert.getAlertId());
        System.out.println("DEBUG: type = " + alert.getType());
        System.out.println("DEBUG: location = " + alert.getLocation());
        System.out.println("DEBUG: timestamp = " + alert.getTimestamp());
        System.out.println("DEBUG: severity = " + alert.getSeverity());
        System.out.println("DEBUG: user_id = " + alert.getUserId());
        System.out.println("DEBUG: latitude = " + alert.getLatitude());
        System.out.println("DEBUG: longitude = " + alert.getLongitude());

        // Now try to insert
        boolean success = alertDAO.insertAlert(alert);

        if (!success) {
            System.out.println("Insert failed.");
            // optionally show an alert dialog
        }
    }



    private void initializeMap() {
        if (mapView == null) {
            System.err.println("MapView is null - cannot initialize map");
            return;
        }

        try {
            if (mapLoadingIndicator != null) {
                mapLoadingIndicator.setVisible(true);
            }

            String mapHtml = createMapHtml();
            mapView.getEngine().loadContent(mapHtml);

            mapView.getEngine().getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    mapInitialized = true;
                    if (mapLoadingIndicator != null) {
                        mapLoadingIndicator.setVisible(false);
                    }
                    System.out.println("Map initialized successfully");

                    // Load existing alerts after map is ready
                    Platform.runLater(() -> {
                        try {
                            Thread.sleep(500);
                            loadAlertsOnMap();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    });
                } else if (newState == Worker.State.FAILED) {
                    if (mapLoadingIndicator != null) {
                        mapLoadingIndicator.setVisible(false);
                    }
                    System.err.println("Failed to load map");
                    updateStatus("Failed to load map", true);
                }
            });
        } catch (Exception e) {
            System.err.println("Error initializing map: " + e.getMessage());
            if (mapLoadingIndicator != null) {
                mapLoadingIndicator.setVisible(false);
            }
            updateStatus("Map initialization failed", true);
        }
    }

    private String createMapHtml() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Disaster Alert Map</title>
                <meta charset="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <link rel="stylesheet" href="https://unpkg.com/leaflet@1.7.1/dist/leaflet.css" />
                <style>
                    body { margin: 0; padding: 0; }
                    #map { height: 100vh; width: 100%; }
                    .alert-popup {
                        font-family: Arial, sans-serif;
                        max-width: 250px;
                    }
                    .alert-popup h4 {
                        margin: 0 0 10px 0;
                        color: #333;
                    }
                    .alert-popup p {
                        margin: 5px 0;
                        font-size: 12px;
                    }
                    .severity-critical { color: #d32f2f; font-weight: bold; }
                    .severity-high { color: #f57c00; font-weight: bold; }
                    .severity-medium { color: #fbc02d; font-weight: bold; }
                    .severity-low { color: #388e3c; font-weight: bold; }
                </style>
            </head>
            <body>
                <div id="map"></div>
                <script src="https://unpkg.com/leaflet@1.7.1/dist/leaflet.js"></script>
                <script>
                    var map = L.map('map').setView([20.0, 0.0], 2);
                    
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: '© OpenStreetMap contributors'
                    }).addTo(map);
                    
                    var alertMarkers = L.layerGroup().addTo(map);
                    
                    function getMarkerIcon(severity) {
                        var colors = {
                            'Critical': '#d32f2f',
                            'High': '#f57c00', 
                            'Medium': '#fbc02d',
                            'Low': '#388e3c'
                        };
                        
                        var color = colors[severity] || '#666666';
                        
                        return L.divIcon({
                            html: '<div style="background-color: ' + color + '; width: 20px; height: 20px; border-radius: 50%; border: 2px solid white; box-shadow: 0 2px 4px rgba(0,0,0,0.3);"></div>',
                            iconSize: [20, 20],
                            className: 'custom-div-icon'
                        });
                    }
                    
                    function addAlertMarker(type, location, lat, lng, severity, timestamp) {
                        try {
                            if (lat === 0 && lng === 0) return;
                            
                            var marker = L.marker([lat, lng], {
                                icon: getMarkerIcon(severity)
                            });
                            
                            var popupContent = '<div class="alert-popup">' +
                                '<h4>' + type + '</h4>' +
                                '<p><strong>Location:</strong> ' + location + '</p>' +
                                '<p><strong>Severity:</strong> <span class="severity-' + severity.toLowerCase() + '">' + severity + '</span></p>' +
                                '<p><strong>Time:</strong> ' + timestamp + '</p>' +
                                '</div>';
                            
                            marker.bindPopup(popupContent);
                            alertMarkers.addLayer(marker);
                        } catch (e) {
                            console.error('Error adding marker:', e);
                        }
                    }
                    
                    function clearAllMarkers() {
                        alertMarkers.clearLayers();
                    }
                    
                    function focusLocation(lat, lng, zoom) {
                        map.setView([lat, lng], zoom || 10);
                    }
                    
                    // Make functions available to JavaFX
                    window.addAlertMarker = addAlertMarker;
                    window.clearAllMarkers = clearAllMarkers;
                    window.focusLocation = focusLocation;
                    
                    console.log('Map initialized successfully');
                </script>
            </body>
            </html>
            """;
    }

    private void loadAlertsOnMap() {
        if (!mapInitialized || allAlerts == null || allAlerts.isEmpty()) {
            return;
        }

        try {
            // Clear existing markers
            mapView.getEngine().executeScript("clearAllMarkers()");

            // Add all alerts to map
            for (Alert alert : allAlerts) {
                if (alert.getLatitude() != 0 || alert.getLongitude() != 0) {
                    addAlertToMap(
                            alert.getType(),
                            alert.getLocation(),
                            alert.getLatitude(),
                            alert.getLongitude(),
                            alert.getSeverity(),
                            alert.getTimestamp().format(SHORT_TIME_FORMATTER)
                    );
                }
            }
            System.out.println("Loaded " + allAlerts.size() + " alerts on map");
        } catch (Exception e) {
            System.err.println("Error loading alerts on map: " + e.getMessage());
        }
    }

    private void addAlertToMap(String type, String location, double lat, double lng, String severity, String timestamp) {
        if (!mapInitialized) {
            return;
        }

        try {
            String script = String.format(
                    "addAlertMarker('%s', '%s', %.6f, %.6f, '%s', '%s')",
                    escapeJavaScript(type),
                    escapeJavaScript(location),
                    lat, lng,
                    escapeJavaScript(severity),
                    escapeJavaScript(timestamp)
            );
            mapView.getEngine().executeScript(script);
        } catch (Exception e) {
            System.err.println("Error adding alert to map: " + e.getMessage());
        }
    }

    private void focusMapLocation(double lat, double lng, int zoom) {
        if (!mapInitialized) {
            return;
        }

        try {
            String script = String.format("focusLocation(%.6f, %.6f, %d)", lat, lng, zoom);
            mapView.getEngine().executeScript(script);
        } catch (Exception e) {
            System.err.println("Error focusing map location: " + e.getMessage());
        }
    }

    private String escapeJavaScript(String text) {
        if (text == null) return "";
        return text.replace("'", "\\'")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private void updateDatabaseStatus() {
        try {
            boolean isConnected = alertDAO != null && alertDAO.testConnection();
            if (dbStatusLabel != null) {
                dbStatusLabel.setText("Database: " + (isConnected ? "Connected" : "Disconnected"));
                dbStatusLabel.setStyle(isConnected ?
                        "-fx-text-fill: green; -fx-font-weight: bold;" :
                        "-fx-text-fill: red; -fx-font-weight: bold;");
            }
        } catch (Exception e) {
            System.err.println("Error checking database status: " + e.getMessage());
            if (dbStatusLabel != null) {
                dbStatusLabel.setText("Database: Error");
                dbStatusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
            }
        }
    }

    // Event handlers for keyboard shortcuts
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case F5 -> handleRefresh();
            case ESCAPE -> {
                if (searchField != null && searchField.isFocused()) {
                    searchField.clear();
                }
            }
            case ENTER -> {
                if (event.isControlDown()) {
                    handleNewAlert();
                }
            }
        }
    }

    // Additional helper methods for error handling and validation
    private boolean isValidCoordinate(String text) {
        try {
            double value = Double.parseDouble(text.trim());
            return value >= -180 && value <= 180;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidLatitude(String text) {
        try {
            double value = Double.parseDouble(text.trim());
            return value >= -90 && value <= 90;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidLongitude(String text) {
        try {
            double value = Double.parseDouble(text.trim());
            return value >= -180 && value <= 180;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Method to handle application shutdown
    public void shutdown() {
        try {
            if (alertDAO != null) {
                // Perform any necessary cleanup
                System.out.println("Shutting down MainController...");
            }
        } catch (Exception e) {
            System.err.println("Error during shutdown: " + e.getMessage());
        }
    }

    // Getter methods for testing
    public AlertDAO getAlertDAO() {
        return alertDAO;
    }

    public List<Alert> getAllAlerts() {
        return new ArrayList<>(allAlerts);
    }

    public List<Alert> getFilteredAlerts() {
        return new ArrayList<>(filteredAlerts);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public boolean isMapInitialized() {
        return mapInitialized;
    }

    public void handleSearch(KeyEvent keyEvent) {
    }

    public void handleFilterChange(ActionEvent actionEvent) {
    }

    public void exportAlerts(ActionEvent actionEvent) {
    }

    public void showDetailedStats(ActionEvent actionEvent) {
    }

    public void handleRefreshWithStats(ActionEvent actionEvent) {
    }

    public void handleDialogClose(DialogEvent dialogEvent) {
    }
}