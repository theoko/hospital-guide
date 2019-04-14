package controllers.maps;

import controllers.ScreenController;
import controllers.search.SearchEngineController;
import helpers.Constants;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import map.MapDisplay;
import models.map.Location;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMapController extends MapController {

    public static Label welcomeMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        SearchEngineController.setParentController(this);
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    @Override
    public void btnFloor3_Click(MouseEvent mouseEvent) {
        clearEdges();
        showFloor3();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloor2_Click(MouseEvent mouseEvent) {
        clearEdges();
        showFloor2();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloor1_Click(MouseEvent mouseEvent) {
        clearEdges();
        showFloor1();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloorG_Click(MouseEvent mouseEvent) {
        clearEdges();
        showFloorG();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        clearEdges();
        showFloorL1();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        clearEdges();
        showFloorL2();
        MapDisplay.displayAdmin(this);
    }

    private void clearEdges() {
        List<Node> lstNodes = new ArrayList<>();
        for (Node n : panMap.getChildren()) {
            if (n instanceof Line) {
                lstNodes.add(n);
            }
        }
        panMap.getChildren().removeAll(lstNodes);
    }

    @Override
    public boolean isAdmin() {
        return true;
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

