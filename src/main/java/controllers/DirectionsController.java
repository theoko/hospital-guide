package controllers;


import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import map.PathFinder;
import models.map.Location;
import models.map.Map;
import models.map.SubPath;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Stack;

public class DirectionsController extends PopUpController implements Initializable {
    public Label lblStart;
    public Label lblEnd;
    public JFXButton btnCancel;
    public JFXButton btnGo;

    private Location loc2;

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

        for (Line line : lstLines.values()) {
            line.setStroke(Color.GREY);
        }

        Stack<SubPath> path = PathFinder.findPath(map, loc, loc2);
        for (SubPath sub : path) {
            String id = sub.getEdgeID();
            if (id != "") {
                System.out.println(id);
                Line line = lstLines.get(id);
                if (line != null) {
                    line.setStroke(Color.RED);
                    line.setStrokeWidth(2.5);
                }
            }
        }
        ScreenController.deactivate();
    }
}