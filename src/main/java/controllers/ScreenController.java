package controllers;

import controllers.maps.AdminMapController;
import controllers.maps.MapController;
import controllers.node.*;
import controllers.user.PopUpControllerUser;
import controllers.welcome.LogoutController;
import helpers.Caretaker;
import helpers.Constants;
import helpers.Originator;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import models.map.Location;
import models.map.Map;
import models.user.User;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ScreenController {

    private static HashMap<String, String> screenMap = new HashMap<>();
    private static Stage stage;
    public static Scene sceneThing = null;
    public static long mouseCnt = 0;
    public static long theCnt = 0;
    public static long secCnt = 0;
    static Originator org = new Originator();
    static Caretaker car = new Caretaker();


    public ScreenController(Stage stage) {
        ScreenController.stage = stage;
        try {
            this.initializeScreens(ScreenController.stage);
            activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread t = new Thread(() -> {
            System.out.println("here");
            while(true) {
                LogoutController lc = new LogoutController() {
                    @Override
                    public void initialize(URL location, ResourceBundle resources) {

                    }
                };
                //int timeCnt = lc.getTime();
                car.add(org.saveStateToMemento());
                theCnt = mouseCnt;
                if (secCnt > lc.time*60) {
                    Platform.runLater(() -> {
                        try {
                            ScreenController.activate(Constants.Routes.WELCOME);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    secCnt = 0;
                }
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (car.get(car.mementoList.size() - 1) == theCnt) {
                    secCnt += 1;
                    System.out.println(secCnt);
                }
                else {
                    secCnt = 0L;
                    mouseCnt = 0L;
                    theCnt = 0L;
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static HashMap<String, String> getScreenMap() {
        return screenMap;
    }

    public static void setScreenMap(HashMap<String, String> screenMap) {
        ScreenController.screenMap = screenMap;
    }

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        ScreenController.stage = stage;
    }

    public void initializeScreens(Stage stage) {
        // Initialize screen controller to switch between different scenes
        this.addScreen(Constants.Routes.LOGO, "/fxml/UI/welcome/Logo.fxml");
        this.addScreen(Constants.Routes.WELCOME, "/fxml/UI/welcome/Welcome.fxml");
        this.addScreen(Constants.Routes.USER_MAP, "/fxml/UI/maps/UserMap.fxml");
        this.addScreen(Constants.Routes.LOGIN, "/fxml/UI/auth/Login.fxml");
        this.addScreen(Constants.Routes.ADMIN_MAP, "/fxml/UI/maps/AdminMap.fxml");
        this.addScreen(Constants.Routes.EDIT_LOCATION, "/fxml/UI/node/EditLocation.fxml");
        this.addScreen(Constants.Routes.DOWNLOAD, "/fxml/UI/download/Download.fxml");
        this.addScreen(Constants.Routes.DOWNLOADED, "/fxml/UI/download/Downloaded.fxml");
        this.addScreen(Constants.Routes.USER_INFO, "/fxml/UI/user/UserInfo.fxml");
        this.addScreen(Constants.Routes.EMPLOYEE_INFO, "/fxml/UI/employee/EmployeeInfo.fxml");
        this.addScreen(Constants.Routes.EMPLOYEE_MAP, "/fxml/UI/maps/EmployeeMap.fxml");
        this.addScreen(Constants.Routes.SANITATION_REQUEST, "/fxml/UI/sanitation/SanitationRequest.fxml");
        this.addScreen(Constants.Routes.CUSTODIAN_MAP, "/fxml/UI/maps/CustodianMap.fxml");
        this.addScreen(Constants.Routes.CUSTODIAN_INFO, "/fxml/UI/custodian/CustodianInfo.fxml");
        this.addScreen(Constants.Routes.BOOKING_WINDOW, "/fxml/UI/booking/RoomBookingWindow.fxml");
        this.addScreen(Constants.Routes.CREATE_USER, "/fxml/UI/user/CreateUser.fxml");
        this.addScreen(Constants.Routes.USER_POPUP, "/fxml/UI/user/CreateUserPopUp.fxml");
        this.addScreen(Constants.Routes.EDIT_POPUP, "/fxml/UI/user/EditUserPopUp.fxml");
        this.addScreen(Constants.Routes.WORKSPACE_POPUP, "/fxml/UI/booking/WorkspacePopUp.fxml");
        this.addScreen(Constants.Routes.REQUESTS, "/fxml/UI/requests/Requests.fxml");
        this.addScreen(Constants.Routes.IT, "/fxml/UI/requests/ITServiceRequest.fxml");
        this.addScreen(Constants.Routes.PERSCRIPTION, "/fxml/UI/requests/Prescription.fxml");
        this.addScreen(Constants.Routes.INTERPRETER, "/fxml/UI/requests/Interpreter.fxml");
        this.addScreen(Constants.Routes.INTERNAL_TRANS, "/fxml/UI/requests/InternalTrans.fxml");
        this.addScreen(Constants.Routes.GIFT_STORE, "/fxml/UI/requests/GiftStore.fxml");
        this.addScreen(Constants.Routes.FLOURIST, "/fxml/UI/requests/Flourist.fxml");
        this.addScreen(Constants.Routes.SECURITY, "/fxml/UI/requests/Security.fxml");
        this.addScreen(Constants.Routes.VISUAL_AUDIO, "/fxml/UI/requests/VisualAudio.fxml");
        this.addScreen(Constants.Routes.EXTERNAL_TRANS, "/fxml/UI/requests/ExternalTrans.fxml");
        this.addScreen(Constants.Routes.PATIENT_INFO, "/fxml/UI/requests/PatientInfo.fxml");
        this.addScreen(Constants.Routes.ADD, "/fxml/UI/node/AddPopUp.fxml");
        this.addScreen(Constants.Routes.CALENDAR, "/fxml/UI/booking/CalendarTab.fxml");
        this.addScreen(Constants.Routes.EDGE_EDITOR, "/fxml/UI/node/EdgeEditor.fxml");
        this.addScreen(Constants.Routes.TWO_NODE_SEARCH, "/fxml/UI/search/TwoLocSearchPopup.fxml");
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

        if (sceneThing == null) {
            sceneThing = new Scene(root);
            addStyles(sceneThing);
            autoLog(sceneThing);
            stage.setTitle("Brigham and Women's Pathfinder Application");
            stage.setScene(sceneThing);
            stage.setResizable(true);
            stage.setMaximized(true);
            stage.show();
        } else {
            sceneThing.setRoot(root);
        }
    }

    public static EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            mouseCnt += 1;
            secCnt = 0L;
            System.out.println(mouseCnt);
        }
    };

    public static void autoLog(Scene scene) throws Exception {
        System.out.println("mousemoved");
        scene.setOnMouseClicked(mouseHandler);
        scene.setOnMouseMoved(mouseHandler);
        scene.setOnMousePressed(mouseHandler);
        scene.setOnMouseReleased(mouseHandler);
    }

    public static void infoPopUp(Constants.Routes route, Location loc, MapController mc, Map map) throws IOException {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        InfoController ic = loader.getController();
        ic.setLoc(loc);
        ic.setMc(mc);
        ic.setMap(map);
        displayPopUp(root);
    }

    public static void adminPopUp(Constants.Routes route, Location loc, MapController mc) throws IOException {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        EditController ec = loader.getController();
        ec.setLoc(loc);
        ec.setMc(mc);

        displayPopUp(root);
    }

    public static void addPopUp(AdminMapController mc, int xCoord, int yCoord, Map map) throws IOException {
        stage = new Stage();
        URL url = routeToURL(Constants.Routes.ADD);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        AddPopUpController ac = loader.getController();
        ac.setMc(mc);
        ac.setxCoord(xCoord);
        ac.setyCoord(yCoord);
        ac.setMap(map);
        displayPopUp(root);
    }

    public static void edgePopUp(MapController mc, String edgeId, Map map) throws IOException {
        stage = new Stage();
        URL url = routeToURL(Constants.Routes.EDGE_EDITOR);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        EdgeEditorController ec = loader.getController();
        ec.setMc(mc);
        ec.setEdgeId(edgeId);
        ec.setMap(map);
        displayPopUp(root);
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

    public static void popUp(Constants.Routes route, Location ws, Circle circle, LocalTime StartTime, LocalDate StartDate, LocalTime EndTime, LocalDate EndDate) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setCircle(circle);
        pc.setWorkspace(ws);
        pc.setStartDate(StartDate);
        pc.setStartTime(StartTime);
        pc.setEndDate(EndDate);
        pc.setEndTime(EndTime);
        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc, Map map, Pane pane) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);
        pc.setMap(map);
        pc.setPane(pane);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc, Map map, Pane pane, Circle circle) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);
        pc.setMap(map);
        pc.setPane(pane);
        pc.setCircle(circle);

        displayPopUp(root);
    }

    public static void popUp(Constants.Routes route, Location loc, Map map, Pane pane, Circle circle, ScrollPane txtPane) throws Exception {
        stage = new Stage();
        URL url = routeToURL(route);

        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        PopUpController pc = loader.getController();

        pc.setLoc(loc);
        pc.setMap(map);
        pc.setPane(pane);
        pc.setCircle(circle);
        pc.setTxtPane(txtPane);

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
     *
     * @param n Node from the window the logout is from
     */
    public static void logOut(Node n) {
        //((Stage) n.getScene().getWindow()).close();
    }

    private static URL routeToURL(Constants.Routes route) throws MalformedURLException {
        return new URL(ScreenController.class.getResource(screenMap.get(route.name())).toString().replaceAll("%20", " "));
    }
}
