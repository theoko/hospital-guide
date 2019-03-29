package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.map.Location;

import java.net.URL;
import java.util.HashMap;

public class ScreenController {

    private static HashMap<String, String> screenMap = new HashMap<>();
    private static Stage stage;

    public ScreenController(Stage stage) {
        this.stage = stage;

        try {
            // Initialize screens
            this.initializeScreens(this.stage);

            // Activate logo screen
            this.activate("welcome");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initializeScreens(Stage stage) throws Exception {

        // Initialize screen controller to switch between different scenes
        this.addScreen("logo","/Logo.fxml");
        this.addScreen("welcome","/Welcome.fxml");
        this.addScreen("regMain","/RegMain.fxml");
        this.addScreen("login","/Login.fxml");
        this.addScreen("main","/Main.fxml");
        this.addScreen("popUp","/PopUp.fxml");
        this.addScreen("download", "/Download.fxml");
        this.addScreen("downloaded", "/Downloaded.fxml");
        this.addScreen("info", "/Info.fxml");

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
        stage.close();
    }

    private static void addStyles(Scene scene) {
        scene.getStylesheets().add(ScreenController.class.getResource("/css/jfoenix-components.css").toExternalForm());
        scene.getStylesheets().add(ScreenController.class.getResource("/css/custom.css").toExternalForm());
        scene.getStylesheets().add(ScreenController.class.getResource("/css/colorScheme.css").toExternalForm());
    }

    public static void activate(String name) throws Exception {

        stage = new Stage();

        // Init parent
        URL url = new URL(ScreenController.class.getResource(screenMap.get(name)).toString().replaceAll("%20", " "));

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        // Init scene
        Scene s = new Scene(root);

        // Add CSS to scene
        addStyles(s);

        stage.setTitle(Character.toUpperCase(name.charAt(0)) + name.substring(1));
        stage.setScene(s);
        stage.setResizable(true);

        stage.show();
    }

    public static void activate(String name, Location loc) throws Exception {

        stage = new Stage();

        // Init parent
        URL url = new URL(ScreenController.class.getResource(screenMap.get(name)).toString().replaceAll("%20", " "));

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        System.out.println(loc.getNodeID());
        PopUpController pc = loader.getController();

        pc.setLoc(loc);

        // Init scene
        Scene s = new Scene(root);

        // Add CSS to scene
        addStyles(s);

        stage.setTitle(Character.toUpperCase(name.charAt(0)) + name.substring(1));
        stage.setScene(s);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
