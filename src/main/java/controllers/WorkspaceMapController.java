package controllers;

import database.LocationTable;
import database.WorkspaceTable;
import helpers.Constants;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.map.Location;
import models.map.Workspace;

import java.util.HashMap;

public class WorkspaceMapController {

    private final static double xShift = -60;
    private final static double yShift = 595;
    private final static double scale = 0.44;
    private final static double locRadius = 5.5;
    private final static Color nodeFill = Color.WHITE;
    private final static Color nodeOutline = Color.BLACK;
    private final static double locWidth = 2.0;

    @FXML
    AnchorPane workS;

    public void initialize() {

        HashMap<String, Workspace> lstWorkspaces = WorkspaceTable.getWorkspaces();
        if(lstWorkspaces != null) {
            for (Workspace ws: lstWorkspaces.values()) {
                if (ws.getNodeType() != null) {
                    double xLoc = scaleX(ws.getxCord());
                    double yLoc = scaleY(ws.getyCord());
                    Circle circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                    circle.setStroke(nodeOutline);
                    circle.setStrokeWidth(locWidth);
                    circle.setOnMouseClicked(event -> {
                        try {
                            event.consume();
                            //ScreenController.popUp(Constants.Routes.USER_INFO, ws, map, panes);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    workS.getChildren().add(circle);
                }
            }
        }
    }

    public static double scaleX(double x) {
        return (x - xShift) * scale;
    }

    public static double scaleY(double y) {
        return (y - yShift) * scale;
    }
}
