module JavaFx.Tutorial {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;
    requires org.jetbrains.annotations;
    requires java.desktop;

    exports xjaw;
    opens xjaw to javafx.fxml, javafx.graphics;
    opens controller to javafx.fxml;
}