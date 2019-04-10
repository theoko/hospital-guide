package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.DatabaseHelpers;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class InfoController extends PopUpController {
    public Label lblNodeID;
    public Label lblLocation;
    public Label lblFloor;
    public Label lblBuilding;
    public Label lblNodeType;
    public Label lblLongName;
    public Label lblShortName;
    public JFXButton btnDirections;
    public JFXButton btnCancel;
    public JFXButton btnStartHere;

    @Override
    public final void initialize(URL location, ResourceBundle resources) {

    }

    public final void setLoc(Location loc) {
        this.loc = loc;
        lblNodeID.setText(loc.getNodeID());
        lblLocation.setText("(" + loc.getxCord() + ", " + loc.getyCord() + ")");
        lblFloor.setText(loc.getFloor());
        lblBuilding.setText(loc.getBuilding());
        lblNodeType.setText(DatabaseHelpers.enumToString(loc.getNodeType()));
        lblLongName.setText(loc.getLongName());
        lblShortName.setText(loc.getShortName());
    }

    public final void btnDirections_OnClick(MouseEvent event) throws Exception {
        event.consume();
        PathFinder.printPath(panes, TextPane, map, kiosk, loc);
        ScreenController.deactivate();
    }

    public final void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

    public final void btnStartHere_OnClick(MouseEvent mouseEvent) {
        MapController.setTempStart(loc.getNodeID());
        for (AnchorPane pane : panes) {
            List<Node> lstNodes1 = new ArrayList<>();
            for (Node n : pane.getChildren()) {
                if (n instanceof Line) {
                    lstNodes1.add(n);
                } else if (n instanceof Circle) {
                    Circle circle = (Circle) n;
                    if (circle.getFill().equals(MapDisplay.nodeEnd) || circle.getFill().equals(MapDisplay.nodeStart)) {
                        circle.setFill(MapDisplay.nodeFill);
                    }
                }
            }
            for (Node n : lstNodes1) {
                pane.getChildren().remove(n);
            }
        }
        circle.setFill(MapDisplay.nodeStart);
        ScreenController.deactivate();
    }
}
