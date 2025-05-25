package java.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

public class AlertController {
    @FXML private ListView<String> alertList;
    @FXML private StackPane mapContainer;

    @FXML
    public void initialize() {
        // Initialization logic
        alertList.getItems().addAll(
                "Earthquake - San Francisco - High",
                "Flood - Mumbai - Medium"
        );
    }
}