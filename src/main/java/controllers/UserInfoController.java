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

public class UserInfoController extends PopUpController implements Initializable {
    public Label lblNodeID;
    public Label lblLocation;
    public Label lblFloor;
    public Label lblBuilding;
    public Label lblNodeType;
    public Label lblLongName;
    public Label lblShortName;

    public JFXButton btnDirections;
    public JFXButton btnCancel;

    private static boolean bolSelectedUser = false;
    private static Location locSelectedUser;

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
        if (bolSelectedUser) { // Two locations
            if (!loc.equals(locSelectedUser)) {
                ScreenController.popUp(Constants.Routes.DIRECTIONS, loc, locSelectedUser, map, panes);
            }
            locSelectedUser = null;
            bolSelectedUser = false;
        } else { // One location
            locSelectedUser = loc;
            bolSelectedUser = true;
        }
    }

    public void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }
}
