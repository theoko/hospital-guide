package controllers.maps;

import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import map.MapDisplay;
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMapController extends MapController {

    public static Label welcomeMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    @Override
    public void btnFloor3_Click(MouseEvent mouseEvent) {
        showFloor3();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloor2_Click(MouseEvent mouseEvent) {
        showFloor2();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloor1_Click(MouseEvent mouseEvent) {
        showFloor1();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloorG_Click(MouseEvent mouseEvent) {
        showFloorG();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        showFloorL1();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        showFloorL2();
        MapDisplay.displayAdmin(this);
    }

    public static boolean isEnableAddNode() {
        return false;
    }

    public static void locationSelectEvent(Location loc) {
    }

    public static void controlWelcomeMessage(boolean condition) {
        welcomeMessage.setVisible(false);
    }

}

