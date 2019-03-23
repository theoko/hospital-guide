package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class ScreenController {
    private HashMap<String, String> screenMap = new HashMap<>();
    private Stage stage;

    public ScreenController(Stage stage) {
        this.stage = stage;
    }

    public void addScreen(String name, String layout) {
        screenMap.put(name, layout);
    }

    public void removeScreen(String name) {
        screenMap.remove(name);
    }

    public void deactivate() {
        stage.hide();
    }

    public void activate(String name) throws Exception {

        this.stage = null;
        this.stage = new Stage();

        Scene s = new Scene(FXMLLoader.load(getClass().getResource(screenMap.get(name))));
        stage.setTitle(Character.toUpperCase(name.charAt(0)) + name.substring(1));
        stage.setScene(s);
        stage.setResizable(true);

        this.stage.show();

    }
}
