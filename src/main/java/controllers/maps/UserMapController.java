package controllers.maps;

import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.input.MouseEvent;
import map.MapDisplay;

import java.net.URL;
import java.util.ResourceBundle;

public class UserMapController extends MapController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        MapDisplay.displayUser(this);
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        super.btnReturn_Click(mouseEvent);
        ScreenController.activate(Constants.Routes.WELCOME);
    }

    @Override
    public void btnFloor3_Click(MouseEvent mouseEvent) {
        super.btnFloor3_Click(mouseEvent);
        MapDisplay.displayUser(this);
    }

    @Override
    public void btnFloor2_Click(MouseEvent mouseEvent) {
        super.btnFloor2_Click(mouseEvent);
        MapDisplay.displayUser(this);
    }

    @Override
    public void btnFloor1_Click(MouseEvent mouseEvent) {
        super.btnFloor1_Click(mouseEvent);
        MapDisplay.displayUser(this);
    }

    @Override
    public void btnFloorG_Click(MouseEvent mouseEvent) {
        super.btnFloorG_Click(mouseEvent);
        MapDisplay.displayUser(this);
    }

    @Override
    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        super.btnFloorL1_Click(mouseEvent);
        MapDisplay.displayUser(this);
    }

    @Override
    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        super.btnFloorL2_Click(mouseEvent);
        MapDisplay.displayUser(this);
    }
}
