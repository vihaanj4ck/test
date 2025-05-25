package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private ImageView logoImage;

    @FXML
    public void initialize() {
        // Optional: Load logo image if available
        Image logo = new Image(getClass().getResourceAsStream("/logo.png"));
        logoImage.setImage(logo);

        loginButton.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email.isBlank() || password.isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Please enter both email and password.");
            return;
        }

        // TODO: Validate login via DAO
        if (email.equals("admin@example.com") && password.equals("admin")) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful!");
            // TODO: Switch to dashboard scene
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid credentials.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.show();
    }
}
