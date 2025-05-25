package java.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button; // Fixed: Using JavaFX Button
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Alert; // Ensure this points to your Alert model class

public class MainView extends BorderPane {
    public MainView() {
        // 1. Create UI Components
        Label header = new Label("Disaster Alert System");
        header.getStyleClass().add("header");

        Button newAlertBtn = new Button("New Alert"); // JavaFX Button
        Button refreshBtn = new Button("Refresh");   // JavaFX Button

        TableView<Alert> alertTable = createAlertTable();

        // 2. Layout Organization
        HBox toolbar = new HBox(10, newAlertBtn, refreshBtn);
        toolbar.setPadding(new Insets(10)); // JavaFX Insets
        toolbar.setAlignment(Pos.CENTER_LEFT);

        VBox mainContainer = new VBox(20, header, toolbar, alertTable);
        mainContainer.setPadding(new Insets(20)); // JavaFX Insets

        // 3. Apply Styles
        this.getStyleClass().add("main-pane");
        this.setCenter(mainContainer);

        // 4. Responsive Design
        alertTable.prefHeightProperty().bind(this.heightProperty().multiply(0.8));
    }

    private TableView<Alert> createAlertTable() {
        TableView<Alert> table = new TableView<>();

        TableColumn<Alert, String> idCol = new TableColumn<>("ID");
        TableColumn<Alert, String> typeCol = new TableColumn<>("Type");
        TableColumn<Alert, String> severityCol = new TableColumn<>("Severity");

        // Configure columns (add cell value factories here)
        idCol.setCellValueFactory(cellData -> cellData.getValue().alertIdProperty());
        typeCol.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        severityCol.setCellValueFactory(cellData -> cellData.getValue().severityProperty());

        table.getColumns().addAll(idCol, typeCol, severityCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        return table;
    }
}