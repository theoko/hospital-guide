package controllers;


import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.map.Map;
import models.map.SubPath;

import java.net.URL;
import java.util.*;

public class DirectionsController extends PopUpController implements Initializable {
    public Label lblStart;
    public Label lblEnd;
    public JFXButton btnCancel;
    public JFXButton btnGo;

    private Location loc2;

    private final double lineWidth = 2.5;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setLoc(Location loc) {
        this.loc = loc;
        lblEnd.setText(loc.getLongName());
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
        lblStart.setText(loc2.getLongName());
    }

    public void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

    public void btnGo_OnClick(MouseEvent event) {
        event.consume();

        ObservableList<Node> lstNodes =  pane.getChildren();
        List<Node> lstLines = new ArrayList<>();
        for (Node n : lstNodes) {
            if (n instanceof Line) {
                lstLines.add(n);
            }
        }
        for (Node l : lstLines) {
            lstNodes.remove(l);
        }

        Stack<SubPath> path = PathFinder.findPath(map, loc, loc2);
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (SubPath sub : path) {
            String id = sub.getEdgeID();
            if (id != "") {
                String locID1 = id.substring(0, id.indexOf("_"));
                String locID2 = id.substring(id.indexOf("_") + 1);
                Location loc1 = lstLocations.get(locID1);
                Location loc2 = lstLocations.get(locID2);
                Line line = new Line(MapDisplay.scaleX(loc1.getxCord()), MapDisplay.scaleY(loc1.getyCord()), MapDisplay.scaleX(loc2.getxCord()), MapDisplay.scaleY(loc2.getyCord()));
                line.setStroke(Color.RED);
                line.setStrokeWidth(lineWidth);
                pane.getChildren().add(1, line);
            }
        }
        ScreenController.deactivate();
    }
}