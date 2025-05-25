package view;// ZoneMap.java
import javafx.scene.web.WebView;
import javafx.scene.layout.StackPane;

public class ZoneMap {
    private final WebView webView = new WebView();

    public ZoneMap() {
        webView.getEngine().load(
                "https://www.openstreetmap.org/#map=10/19.0760/72.8777"
        );
    }

    public StackPane getView() {
        return new StackPane(webView);
    }
}