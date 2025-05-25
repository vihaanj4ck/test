package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import utils.InputValidator;
import utils.ImageUtil;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private ImageView logoImage;

    @FXML
    public void initialize() {
        // Load logo image safely
        logoImage.setImage(ImageUtil.loadImage("/logo.png"));

        // Focus on email field by default
        emailField.requestFocus();

        // Handle login on button click
        loginButton.setOnAction(e -> handleLogin());
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Check for empty inputs
        if (InputValidator.isNullOrEmpty(email) || InputValidator.isNullOrEmpty(password)) {
            showAlert(Alert.AlertType.ERROR, "Please enter both email and password.");
            return;
        }

        // Validate email format
        if (!InputValidator.isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Please enter a valid email address.");
            return;
        }

        // TODO: Replace with DAO-based login check
        if (email.equals("admin@example.com") && password.equals("admin")) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful!");
            // TODO: Switch to dashboard scene here
        } else {
            showAlert(Alert.AlertType.ERROR, "Invalid credentials.");
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Login");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
