package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.Constants;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import map.PathFinder;
import models.map.Location;
import models.map.Map;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class MapController implements Initializable {
    public JFXButton btnReturn;
    public  AnchorPane panFloor3;
    public  AnchorPane panFloor2;
    public  AnchorPane panFloor1;
    public  AnchorPane panFloorL1;
    public  AnchorPane panFloorL2;
    protected AnchorPane[] panes;
    protected double sceneX, sceneY;
    protected double translateX, translateY;
    protected Map map;
    protected static String tempStart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        panes = new AnchorPane[] {panFloorL2, panFloorL1, panFloor1, panFloor2, panFloor3};
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
        ((AnchorPane) event.getSource()).setTranslateX(((AnchorPane) event.getSource()).getTranslateX() + event.getDeltaX());
        ((AnchorPane) event.getSource()).setTranslateY(((AnchorPane) event.getSource()).getTranslateY() + event.getDeltaY());

        if (((AnchorPane) event.getSource()).getTranslateX() <= -861) {
            ((AnchorPane) event.getSource()).setTranslateX(-861);
        }

        if (((AnchorPane) event.getSource()).getTranslateX() >= 10) {
            ((AnchorPane) event.getSource()).setTranslateX(10);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() <= -210) {
            ((AnchorPane) event.getSource()).setTranslateY(-210);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() >= 510) {
            ((AnchorPane) event.getSource()).setTranslateY(510);
        }
    }

    public final void floorOneMapZoom(ZoomEvent event) {
        ((AnchorPane) event.getSource()).setScaleX(((AnchorPane) event.getSource()).getScaleX() * event.getZoomFactor());
        ((AnchorPane) event.getSource()).setScaleY(((AnchorPane) event.getSource()).getScaleY() * event.getZoomFactor());
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.65 && ((AnchorPane) event.getSource()).getScaleY() <= 0.65) {
            ((AnchorPane) event.getSource()).setScaleX(0.65);
            ((AnchorPane) event.getSource()).setScaleY(0.65);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 8 && ((AnchorPane) event.getSource()).getScaleY() >= 8) {
            ((AnchorPane) event.getSource()).setScaleX(8);
            ((AnchorPane) event.getSource()).setScaleY(8);
        }
    }

    public final void floorOneMapZoomDone(ZoomEvent event) {
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.85 && ((AnchorPane) event.getSource()).getScaleY() <= 0.85) {
            ((AnchorPane) event.getSource()).setScaleX(0.85);
            ((AnchorPane) event.getSource()).setScaleY(0.85);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 4 && ((AnchorPane) event.getSource()).getScaleY() >= 4) {
            ((AnchorPane) event.getSource()).setScaleX(4);
            ((AnchorPane) event.getSource()).setScaleY(4);
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
            case "L1":
                return panFloorL1;
            case "L2":
                return panFloorL2;
        }
        return panFloor1;
    }
}
