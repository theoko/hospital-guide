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
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPopUpController implements Initializable {

    public JFXTextField txtLongName;
    public JFXTextField txtShortName;
    public JFXComboBox cmbNodeType;
    public JFXComboBox cmbBuilding;
    public JFXButton btnAdd;
    private MapController mc;
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
    }

    private void checkFields(){
        boolean bolCheck = cmbBuilding.getValue() != null && cmbNodeType.getValue() != null;
        btnAdd.setDisable(!bolCheck);
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

    public void btnAdd_Clicked(MouseEvent mouseEvent) {
        String longName = txtLongName.getText();
        String shortName = txtShortName.getText();
        String stringType = (String) cmbNodeType.getValue();
        Constants.NodeType nodeType = Constants.NodeType.valueOf(stringType.substring(0, stringType.indexOf(":")));
        String building = (String) cmbBuilding.getValue();
        Location loc = new Location("", xCoord, yCoord, mc.getFloor(), building, nodeType, longName, shortName);
        LocationTable.addLocation(loc);
    }

    public void btnCancel_Clicked(MouseEvent mouseEvent) {
        ScreenController.deactivate();
    }
}
