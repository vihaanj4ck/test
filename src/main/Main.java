package main;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MainView.fxml"));

        Parent root = loader.load();  // This loads the FXML and instantiates the controller

        // Optionally get controller if you need it
        MainController mainController = loader.getController();

        // Create Scene with loaded root
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Your App Title");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

