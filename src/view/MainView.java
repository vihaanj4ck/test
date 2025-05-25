package view;  // Must match your directory structure

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.Orientation;
import javafx.collections.FXCollections;
import view.components.AlertCard;
import view.components.ZoneMap;

public class MainView {
    private final BorderPane root = new BorderPane();
    private final AlertCard alertCard = new AlertCard();
    private final ZoneMap zoneMap = new ZoneMap();
    private final Button newAlertButton = new Button("New Alert");
    private final Button refreshButton = new Button("Refresh");
    private final ComboBox<String> filterCombo = new ComboBox<>();
    private final SplitPane contentPane = new SplitPane();

    public MainView() {
        initializeUI();
        setupEventHandlers();
        applyStyles();
    }

    private void initializeUI() {
        configureToolbar();
        configureContentPane();
    }

    private void configureToolbar() {
        // Setup filter options
        filterCombo.setItems(FXCollections.observableArrayList(
                "All Alerts", "Critical", "Warning", "Info"
        ));
        filterCombo.setPromptText("Filter alerts...");
        filterCombo.getSelectionModel().selectFirst();

        // Configure buttons
        newAlertButton.setId("new-alert-btn");
        refreshButton.setId("refresh-btn");
        filterCombo.setId("filter-combo");

        // Create toolbar with spacer
        ToolBar toolbar = new ToolBar();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolbar.getItems().addAll(
                newAlertButton,
                refreshButton,
                spacer,
                filterCombo
        );

        root.setTop(toolbar);
    }

    private void configureContentPane() {
        contentPane.getItems().addAll(
                zoneMap.getView(),
                alertCard.getView()
        );
        contentPane.setDividerPositions(0.7);
        root.setCenter(contentPane);
    }

    private void setupEventHandlers() {
        newAlertButton.setOnAction(e -> handleNewAlert());
        refreshButton.setOnAction(e -> handleRefresh());
        filterCombo.setOnAction(e -> handleFilterChange());
    }

    private void applyStyles() {
        try {
            String cssPath = getClass().getResource("/styles.css").toExternalForm();
            root.getStylesheets().add(cssPath);
            root.getStyleClass().add("main-view");
        } catch (NullPointerException e) {
            System.err.println("Warning: Could not load styles.css");
        }
    }

    // Event handlers
    private void handleNewAlert() {
        System.out.println("New Alert clicked");
        // Implementation here
    }

    private void handleRefresh() {
        System.out.println("Refresh clicked");
        // Implementation here
    }

    private void handleFilterChange() {
        String filter = filterCombo.getValue();
        System.out.println("Filter changed to: " + filter);
        // Implementation here
    }

    public BorderPane getView() {
        return root;
    }

    // Getters for controller access
    public AlertCard getAlertCard() {
        return alertCard;
    }

    public ZoneMap getZoneMap() {
        return zoneMap;
    }

    public Button getNewAlertButton() {
        return newAlertButton;
    }

    public javafx.scene.control.ChoiceBox<Object> getAlertList() {
    }
}