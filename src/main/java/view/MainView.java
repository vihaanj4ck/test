package view;

import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

public class MainView {
    private final BorderPane root = new BorderPane();
    private final Button newAlertButton = new Button("New Alert");
    private final Button refreshButton = new Button("Refresh");
    private final ComboBox<String> filterCombo = new ComboBox<>();
    private final SplitPane contentPane = new SplitPane();
    private final ListView<String> alertListView = new ListView<>(); // Added ListView

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
        filterCombo.setItems(FXCollections.observableArrayList(
                "All Alerts", "Critical", "Warning", "Info"
        ));
        filterCombo.setPromptText("Filter alerts...");
        filterCombo.getSelectionModel().selectFirst();

        newAlertButton.setId("new-alert-btn");
        refreshButton.setId("refresh-btn");
        filterCombo.setId("filter-combo");

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
        contentPane.setDividerPositions(0.7);
        contentPane.getItems().addAll(alertListView, new StackPane()); // Added ListView to content
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
    }

    private void handleRefresh() {
        System.out.println("Refresh clicked");
    }

    private void handleFilterChange() {
        String filter = filterCombo.getValue();
        System.out.println("Filter changed to: " + filter);
    }

    public BorderPane getView() {
        return root;
    }

    // Getters for controller access
    public Button getNewAlertButton() {
        return newAlertButton;
    }

    public ListView<String> getAlertList() {
        return alertListView; // Fixed the return value
    }
}