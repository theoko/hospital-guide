package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import helpers.DatabaseHelpers;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import models.map.Location;

import java.net.URL;
import java.util.ResourceBundle;

public class CustodianInfoController extends PopUpController implements Initializable {
    public Label lblNodeID;
    public Label lblLocation;
    public Label lblFloor;
    public Label lblBuilding;
    public Label lblNodeType;
    public Label lblLongName;
    public Label lblShortName;

    public JFXButton btnDirections;
    public JFXButton btnCancel;

    private static boolean bolSelectedCust = false;
    private static Location locSelectedCust;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setLoc(Location loc) {
        this.loc = loc;
        lblNodeID.setText(loc.getNodeID());
        lblLocation.setText("(" + loc.getxCord() + ", " + loc.getyCord() + ")");
        lblFloor.setText(loc.getFloor());
        lblBuilding.setText(loc.getBuilding());
        lblNodeType.setText(DatabaseHelpers.enumToString(loc.getNodeType()));
        lblLongName.setText(loc.getLongName());
        lblShortName.setText(loc.getShortName());
    }

    public void btnDirections_OnClick(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        checkSelected();
    }

    private void checkSelected() throws Exception {
        if (bolSelectedCust) { // Two locations
            if (!loc.equals(locSelectedCust)) {
                ScreenController.popUp(Constants.Routes.DIRECTIONS, loc, locSelectedCust, map, panes);
            }
            locSelectedCust = null;
            bolSelectedCust = false;
        } else { // One location
            locSelectedCust = loc;
            bolSelectedCust = true;
        }
    }

    public void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }
}
