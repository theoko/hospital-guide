package controllers.maps;

import controllers.ScreenController;
import controllers.search.SearchEngineController;
import helpers.Constants;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;
import map.MapDisplay;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMapController extends MapController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        SearchEngineController.setParentController(this);
        MapDisplay.displayEmployee(this);
        initDirections();
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    @Override
    public void btnFloor3_Click(MouseEvent mouseEvent) {
        showFloor("3");
    }

    @Override
    public void btnFloor2_Click(MouseEvent mouseEvent) {
        showFloor("2");
    }

    @Override
    public void btnFloor1_Click(MouseEvent mouseEvent) {
        showFloor("1");
    }

    @Override
    public void btnFloorG_Click(MouseEvent mouseEvent) {
        showFloor("G");
    }

    @Override
    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        showFloor("L1");
    }

    @Override
    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        showFloor("L2");
    }

    @Override
    public void showFloor(String newFloor) {
        super.showFloor(newFloor);
        MapDisplay.displayEmployee(this);
    }

    @Override
    public void displayPath(Path line) {
        super.displayPath(line);
        MapDisplay.displayEmployee(this);
    }
}
