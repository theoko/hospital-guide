package controllers.node;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import controllers.maps.MapController;
import database.LocationTable;
import helpers.Constants;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import map.MapDisplay;
import models.map.Location;
import models.map.Map;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPopUpController implements Initializable {

    public JFXTextField txtLongName;
    public JFXTextField txtShortName;
    public JFXComboBox cmbNodeType;
    public JFXComboBox cmbBuilding;
    public JFXButton btnAdd;
    private MapController mc;
    private Map map;
    private int xCoord;
    private int yCoord;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbNodeType.valueProperty().addListener(((observable, oldValue, newValue) -> {
            checkFields();
        }));
        cmbBuilding.valueProperty().addListener(((observable, oldValue, newValue) -> {
            checkFields();
        }));
        checkFields();
    }

    private void checkFields(){
        boolean bolCheck = cmbBuilding.getValue() == null || cmbNodeType.getValue() == null;
        btnAdd.setDisable(bolCheck);
    }

    public void setMc(MapController mc) {
        this.mc = mc;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void btnAdd_Clicked(MouseEvent mouseEvent) {
        String floor = mc.getFloor();
        String longName = txtLongName.getText();
        String shortName = txtShortName.getText();
        String stringType = (String) cmbNodeType.getValue();
        Constants.NodeType nodeType = Constants.NodeType.valueOf(stringType.substring(0, stringType.indexOf(":")));
        String building = (String) cmbBuilding.getValue();
        String id = LocationTable.generateUniqueNodeID(nodeType, floor);
        Location loc = new Location(id, xCoord, yCoord, floor, building, nodeType, longName, shortName);

        Circle newCirle;
        if (!nodeType.equals(Constants.NodeType.HALL)) {
            newCirle = MapDisplay.createCircle(mc, loc, MapDisplay.NodeStyle.REGULAR, 1.0, Constants.Routes.EDIT_LOCATION, true);
        } else {
            newCirle = MapDisplay.createCircle(mc, loc, MapDisplay.NodeStyle.POINT, 1.0, Constants.Routes.EDIT_LOCATION, true);
        }
        mc.panMap.getChildren().add(newCirle);
        map.addLocation(id, loc);
        LocationTable.addLocation(loc);
        ScreenController.deactivate();
    }

    public void btnCancel_Clicked(MouseEvent mouseEvent) {
        ScreenController.deactivate();
    }
}
