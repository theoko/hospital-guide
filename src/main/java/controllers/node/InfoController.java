package controllers.node;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import controllers.maps.MapController;
import helpers.DatabaseHelpers;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
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

    private MapController mc;

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
        PathFinder.printPath(mc, kiosk, loc);
        ScreenController.deactivate();
    }

    public final void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

    public final void btnStartHere_OnClick(MouseEvent mouseEvent) {
        MapController.setTempStart(loc.getNodeID());
        List<Node> lstNodes = new ArrayList<>();
        for (Node n : mc.panMap.getChildren()) {
            if (n instanceof Path) {
                lstNodes.add(n);
            } else if (n instanceof Circle) {
                Circle circle = (Circle) n;
                if (circle.getFill().equals(MapDisplay.nodeEnd) || circle.getFill().equals(MapDisplay.nodeStart)) {
                    circle.setFill(MapDisplay.nodeFill);
                }
            }
        }
        mc.panMap.getChildren().removeAll(lstNodes);
        loc.getNodeCircle().setFill(MapDisplay.nodeStart);
        mc.txtPane.setContent(null);
        ScreenController.deactivate();
    }

    public void setMc(MapController mc) {
        this.mc = mc;
    }
}
