package controllers.maps;

import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;
import map.MapDisplay;

import java.net.URL;
import java.util.ResourceBundle;

public class CustodianMapController extends MapController {
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        MapDisplay.displayCust(this);
        initDirections();
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    @Override
    public void btnFloor3_Click(MouseEvent mouseEvent) {
        showFloor3();
        MapDisplay.displayCust(this);
    }

    @Override
    public void btnFloor2_Click(MouseEvent mouseEvent) {
        showFloor2();
        MapDisplay.displayCust(this);
    }

    @Override
    public void btnFloor1_Click(MouseEvent mouseEvent) {
        showFloor1();
        MapDisplay.displayCust(this);
    }

    @Override
    public void btnFloorG_Click(MouseEvent mouseEvent) {
        showFloorG();
        MapDisplay.displayCust(this);
    }

    @Override
    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        showFloorL1();
        MapDisplay.displayCust(this);
    }

    @Override
    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        showFloorL2();
        MapDisplay.displayCust(this);
    }

    @Override
    public void displayPath(Path line) {
        super.displayPath(line);
        MapDisplay.displayCust(this);
    }
}


