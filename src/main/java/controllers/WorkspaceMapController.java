package controllers;

import com.jfoenix.controls.JFXButton;
import database.LocationTable;
import database.WorkspaceTable;
import helpers.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.map.Location;
import models.map.Workspace;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class WorkspaceMapController {

    private final static double xShift = 20;
    private final static double yShift = -262;
    private final static double scale = 0.44;
    private final static double locRadius = 5.5;
    private final static Color nodeFill = Color.WHITE;
    private final static Color nodeOutline = Color.BLACK;
    private final static double locWidth = 2.0;
    protected double sceneX, sceneY;
    protected double translateX, translateY;

    public JFXButton btnReturn;

    @FXML
    AnchorPane workS;

    public void initialize() {

        HashMap<String, Workspace> lstWorkspaces = WorkspaceTable.getWorkspaces();
        if (lstWorkspaces != null) {
            for (Workspace ws : lstWorkspaces.values()) {
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

    public void floorOneMapOnMousePressed(MouseEvent event) {

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }

    public final void floorOneMapOnMouseDragged(MouseEvent event) {

        // Handle onMouseDragged event
        double offsetX = event.getSceneX() - sceneX;
        double offsetY = event.getSceneY() - sceneY;
        double newTranslateX = translateX + offsetX;
        double newTranslateY = translateY + offsetY;

        ((AnchorPane) event.getSource()).setTranslateX(newTranslateX);
        ((AnchorPane) event.getSource()).setTranslateY(newTranslateY);
    }

    public final void floorOneMapScroll(ScrollEvent event) {
        ((AnchorPane) event.getSource()).setTranslateX(((AnchorPane) event.getSource()).getTranslateX() + event.getDeltaX());
        ((AnchorPane) event.getSource()).setTranslateY(((AnchorPane) event.getSource()).getTranslateY() + event.getDeltaY());

        if (((AnchorPane) event.getSource()).getTranslateX() <= -500) {
            ((AnchorPane) event.getSource()).setTranslateX(-500);
        }

        if (((AnchorPane) event.getSource()).getTranslateX() >= 200) {
            ((AnchorPane) event.getSource()).setTranslateX(200);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() <= -600) {
            ((AnchorPane) event.getSource()).setTranslateY(-600);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() >= 250) {
            ((AnchorPane) event.getSource()).setTranslateY(250);
        }
    }

    public final void floorOneMapZoom(ZoomEvent event) {
        ((AnchorPane) event.getSource()).setScaleX(((AnchorPane) event.getSource()).getScaleX() * event.getZoomFactor());
        ((AnchorPane) event.getSource()).setScaleY(((AnchorPane) event.getSource()).getScaleY() * event.getZoomFactor());
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.45 && ((AnchorPane) event.getSource()).getScaleY() <= 0.45) {
            ((AnchorPane) event.getSource()).setScaleX(0.45);
            ((AnchorPane) event.getSource()).setScaleY(0.45);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 8 && ((AnchorPane) event.getSource()).getScaleY() >= 8) {
            ((AnchorPane) event.getSource()).setScaleX(8);
            ((AnchorPane) event.getSource()).setScaleY(8);
        }
    }

    public final void floorOneMapZoomDone(ZoomEvent event) {
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.55 && ((AnchorPane) event.getSource()).getScaleY() <= 0.55) {
            ((AnchorPane) event.getSource()).setScaleX(0.55);
            ((AnchorPane) event.getSource()).setScaleY(0.55);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 4 && ((AnchorPane) event.getSource()).getScaleY() >= 4) {
            ((AnchorPane) event.getSource()).setScaleX(4);
            ((AnchorPane) event.getSource()).setScaleY(4);
        }
    }

    public static double scaleX(double x) {
        return (x - xShift) * scale;
    }

    public static double scaleY(double y) {
        return (y - yShift) * scale;
    }

    public void logOut(MouseEvent event) {
        event.consume();
        ScreenController.logOut(btnReturn);
        try {
            ScreenController.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
