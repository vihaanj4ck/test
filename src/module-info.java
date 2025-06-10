module JavaFx.Tutorial {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
    requires org.jetbrains.annotations;
    requires java.desktop;
    requires org.junit.jupiter.api;

    exports xjaw;
    exports controller;
    exports main;
    opens main to javafx.fxml;

    opens xjaw to javafx.fxml;
    opens controller to javafx.fxml;
}
