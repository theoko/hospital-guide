package controllers;

import helpers.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.map.Location;
import models.map.Map;
import models.user.User;

import java.net.MalformedURLException;
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

            // Activate beginning screen
            this.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initializeScreens(Stage stage) throws Exception {
        // Initialize screen controller to switch between different scenes
        this.addScreen(Constants.Routes.LOGO,"/Logo.fxml");
        this.addScreen(Constants.Routes.WELCOME,"/Welcome.fxml");
        this.addScreen(Constants.Routes.USER_MAP, "/UserMap.fxml");
        this.addScreen(Constants.Routes.LOGIN,"/Login.fxml");
        this.addScreen(Constants.Routes.ADMIN_MAP, "/AdminMap.fxml");
        this.addScreen(Constants.Routes.EDIT_LOCATION, "/EditLocation.fxml");
        this.addScreen(Constants.Routes.DOWNLOAD, "/Download.fxml");
        this.addScreen(Constants.Routes.DOWNLOADED, "/Downloaded.fxml");
        this.addScreen(Constants.Routes.USER_INFO, "/UserInfo.fxml");
        this.addScreen(Constants.Routes.EMPLOYEE_INFO, "/EmployeeInfo.fxml");
        this.addScreen(Constants.Routes.EMPLOYEE_MAP, "/EmployeeMap.fxml");
        this.addScreen(Constants.Routes.SANITATION_REQUEST, "/SanitationRequest.fxml");
        this.addScreen(Constants.Routes.DIRECTIONS, "/Directions.fxml");
        this.addScreen(Constants.Routes.CUSTODIAN_MAP, "/CustodianMap.fxml");
        this.addScreen(Constants.Routes.CUSTODIAN_INFO, "/CustodianInfo.fxml");
        this.addScreen(Constants.Routes.BOOKING_WINDOW, "/RoomBookingWindow.fxml");
        this.addScreen(Constants.Routes.CREATE_USER, "/CreateUser.fxml");
        this.addScreen(Constants.Routes.USER_POPUP, "/CreateUserPopUp.fxml");
        this.addScreen(Constants.Routes.EDIT_POPUP, "/EditUserPopUp.fxml");
        this.addScreen(Constants.Routes.WORKSPACE, "/Workspace.fxml");
        this.addScreen(Constants.Routes.REQUESTS, "/requests/Requests.fxml");
        this.addScreen(Constants.Routes.IT, "/requests/ITServiceRequest.fxml");
        this.addScreen(Constants.Routes.PERSCRIPTION, "/requests/Prescription.fxml");
        this.addScreen(Constants.Routes.INTERPRETER, "/requests/Interpreter.fxml");
        this.addScreen(Constants.Routes.INTERNAL_TRANS, "/requests/InternalTrans.fxml");
        this.addScreen(Constants.Routes.GIFT_STORE, "/requests/GiftStore.fxml");
        this.addScreen(Constants.Routes.FLOURIST, "/requests/Flourist.fxml");
        this.addScreen(Constants.Routes.SECURITY, "/requests/Security.fxml");
        this.addScreen(Constants.Routes.VISUAL_AUDIO, "/requests/VisualAudio.fxml");
        this.addScreen(Constants.Routes.EXTERNAL_TRANS, "/requests/ExternalTrans.fxml");
        this.addScreen(Constants.Routes.PATIENT_INFO, "/requests/PatientInfo.fxml");
    }

    public void addScreen(Constants.Routes route, String layout) {
        screenMap.put(route.name(), layout);
    }

    private static void addStyles(Scene scene) {
        scene.getStylesheets().add(ScreenController.class.getResource("/css/jfoenix-components.css").toExternalForm());
        scene.getStylesheets().add(ScreenController.class.getResource("/css/custom.css").toExternalForm());
        scene.getStylesheets().add(ScreenController.class.getResource("/css/colorScheme.css").toExternalForm());
    }

    public static void deactivate() {
        stage.close();
    }

    public static void activate(Constants.Routes route) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        Scene s = new Scene(root);
        addStyles(s);
        stage.setTitle("Brigham and Women's Pathfinder Application");
        stage.setScene(s);
        stage.setResizable(true);
       // stage.setMaximized(true);
        stage.show();
    }

    public static void popUp(Constants.Routes route) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        displayPopUp(root);
    }

    public static void popUpUser(Constants.Routes route, User user) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpControllerUser pcu = loader.getController();
        pcu.setUser(user);
        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc, Map map, AnchorPane[] panes) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);
        pc.setMap(map);
        pc.setPanes(panes);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc, Map map, AnchorPane[] panes, Circle circle) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);
        pc.setMap(map);
        pc.setPanes(panes);
        pc.setCircle(circle);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc1, Location loc2, Map map, AnchorPane[] panes) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        DirectionsController pc = loader.getController();

        pc.setLoc(loc1);
        pc.setLoc2(loc2);
        pc.setMap(map);
        pc.setPanes(panes);

        displayPopUp(root);
    }

    private static void displayPopUp(Parent root) {
        Scene s = new Scene(root);
        addStyles(s);
        stage.setScene(s);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    /**
     * Closes out of a current window with selected
     * @param n Node from the window the logout is from
     */
    public static void logOut(Node n) {
        ((Stage) n.getScene().getWindow()).close();
    }

    private static URL routeToURL(Constants.Routes route) throws MalformedURLException {
        return new URL(ScreenController.class.getResource(screenMap.get(route.name())).toString().replaceAll("%20", " "));
    }
}
