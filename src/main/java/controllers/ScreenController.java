package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public class ScreenController {

    private static HashMap<String, String> screenMap = new HashMap<>();
    private static Stage stage;

    public ScreenController(Stage stage) {
        this.stage = stage;

        try {
            // Initialize screens
            this.initializeScreens(this.stage);

            // Activate login screen
            this.activate("login");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initializeScreens(Stage stage) throws Exception {

        // Initialize screen controller to switch between different scenes
        this.addScreen("login","/Login.fxml");
        this.addScreen("main","/Main.fxml");

    }

    public static void moveTo(String name) throws Exception {
        activate(name);
    }

    public void addScreen(String name, String layout) {
        screenMap.put(name, layout);
    }

    public static void removeScreen(String name) {
        screenMap.remove(name);
    }

    public static void deactivate() {
        stage.hide();
    }

    private static void addStyles(Scene scene) {
        scene.getStylesheets().add(ScreenController.class.getResource("/css/jfoenix-components.css").toExternalForm());
        scene.getStylesheets().add(ScreenController.class.getResource("/css/custom.css").toExternalForm());
    }

    public static void activate(String name) throws Exception {

        stage = new Stage();

        // Init scene
        Parent root = FXMLLoader.load(ScreenController.class.getResource(screenMap.get(name)));
        Scene s = new Scene(root);

        // Add CSS to scene
        addStyles(s);

        stage.setTitle(Character.toUpperCase(name.charAt(0)) + name.substring(1));
        stage.setScene(s);
        stage.setResizable(true);

        stage.show();

    }
}
