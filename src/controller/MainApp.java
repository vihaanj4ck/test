// MainApp.java
package view;
import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        MainController controller = new MainController();
        primaryStage.setScene(controller.getMainScene());
        primaryStage.setTitle("Disaster Alert System");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}