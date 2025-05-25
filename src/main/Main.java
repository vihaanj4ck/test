package main;

import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainController mainController = new MainController();
        primaryStage.setScene(mainController.getMainScene());
        primaryStage.setTitle("Your App Title");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);  // This starts the JavaFX runtime and calls start()
    }
}
