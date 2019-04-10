package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import map.PathFinder;
import models.map.Map;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class MapController implements Initializable {
    public JFXButton btnReturn;
    public  AnchorPane panFloor3;
    public  AnchorPane panFloor2;
    public  AnchorPane panFloor1;
    public AnchorPane panFloorG;
    public  AnchorPane panFloorL1;
    public  AnchorPane panFloorL2;
    public ScrollPane TextPane;
    protected AnchorPane[] panes;
    protected double sceneX, sceneY;
    protected double translateX, translateY;
    protected Map map;
    protected static String tempStart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panes = new AnchorPane[] {panFloorL2, panFloorL1, panFloorG, panFloor1, panFloor2, panFloor3};
    }

    public void floorOneMapOnMousePressed(MouseEvent event) {
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        for (AnchorPane pane : panes) {
            translateX = pane.getTranslateX();
            translateY = pane.getTranslateY();
        }
    }

    public void floorOneMapOnMouseDragged(MouseEvent event) {
        double offsetX = event.getSceneX() - sceneX;
        double offsetY = event.getSceneY() - sceneY;
        double newTranslateX = translateX + offsetX;
        double newTranslateY = translateY + offsetY;

        for (AnchorPane pane : panes) {
            pane.setTranslateX(newTranslateX);
            pane.setTranslateY(newTranslateY);
        }
    }

    public final void floorOneMapScroll(ScrollEvent event) {
        for (AnchorPane pane : panes) {
            double xTrans = pane.getTranslateX() + event.getDeltaX();
            double yTrans = pane.getTranslateY() + event.getDeltaY();
            xTrans = (xTrans <= -861) ? -861 : xTrans;
            xTrans = (xTrans >= 10) ? 10 : xTrans;
            yTrans = (yTrans <= -210) ? -210 : yTrans;
            yTrans = (yTrans >= 510) ? 510 : yTrans;
            pane.setTranslateX(xTrans);
            pane.setTranslateY(yTrans);
        }
    }

    public final void floorOneMapZoom(ZoomEvent event) {
        for (AnchorPane pane : panes) {
            double xScale = pane.getScaleX() * event.getZoomFactor();
            double yScale = pane.getScaleY() * event.getZoomFactor();
            xScale = (xScale <= 0.65) ? 0.65 : xScale;
            xScale = (xScale >= 8) ? 8 : xScale;
            yScale = (yScale <= 0.65) ? 0.65 : yScale;
            yScale = (yScale >= 8) ? 8 : yScale;
            pane.setScaleX(xScale);
            pane.setScaleY(yScale);
        }
    }

    public final void floorOneMapZoomDone(ZoomEvent event) {
        for (AnchorPane pane : panes) {
            double xScale = pane.getScaleX();
            double yScale = pane.getScaleY();
            xScale = (xScale <= 0.85) ? 0.85 : xScale;
            xScale = (xScale >= 4) ? 4 : xScale;
            yScale = (yScale <= 0.85) ? 0.85 : yScale;
            yScale = (yScale >= 4) ? 4 : yScale;
            pane.setScaleX(xScale);
            pane.setScaleY(yScale);
        }
    }

    public void logOut(MouseEvent event) {
        event.consume();
        tempStart = PathFinder.getDefLocation();
        ScreenController.logOut(btnReturn);
        try {
            ScreenController.activate(Constants.Routes.LOGIN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getTempStart() {
        return tempStart;
    }

    public static void setTempStart(String tempStart) {
        MapController.tempStart = tempStart;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public AnchorPane determinePanMapFromFloor(String floor) {
        switch(floor) {
            case "1":
                return panFloor1;
            case "2":
                return panFloor2;
            case "3":
                return panFloor3;
            case "G":
                return panFloorG;
            case "L1":
                return panFloorL1;
            default:
                return panFloorL2;
        }
    }
}
