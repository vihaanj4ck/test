package main.controller;

import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;

import java.io.InputStream;

public class MainController {

    public void handleNewAlert(ActionEvent actionEvent) {
        // Test whether the image resource can be found at runtime
        InputStream stream = getClass().getResourceAsStream("/view/icons/floppydisk.jpg");
        if (stream == null) {
            System.out.println("❌ Image NOT FOUND at /view/icons/floppydisk.jpg");
        } else {
            System.out.println("✅ Image FOUND at /view/icons/floppydisk.jpg");
        }
    }

    public void handleRefresh(ActionEvent actionEvent) {
        System.out.println("Refresh clicked");
    }

    public void handleSearch(KeyEvent keyEvent) {
        System.out.println("Search key typed: " + keyEvent.getText());
    }

    public void handleFilterChange(ActionEvent actionEvent) {
        System.out.println("Filter changed");
    }

    public void handlePreviousPage(ActionEvent actionEvent) {
        System.out.println("Previous page clicked");
    }

    public void handleNextPage(ActionEvent actionEvent) {
        System.out.println("Next page clicked");
    }
}
