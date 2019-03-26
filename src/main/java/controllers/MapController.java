package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import map.Map;
import map.MapDisplay;
import map.MapParser;
import sun.plugin.javascript.navig.Anchor;

import java.awt.event.ActionEvent;
import java.util.Stack;

import static helpers.UIHelpers.MIN_PIXELS;

public class MapController {

    @FXML
    ImageView floorOneMap;

    @FXML
    AnchorPane panMap;

    @FXML
    JFXButton zoomIn;

    @FXML
    JFXButton zoomOut;

    double sceneX, sceneY;
    double translateX, translateY;

    public void initialize() {
        Map map = MapParser.parse("/data/nodes.csv", "/data/edges.csv");
        MapDisplay.display(map, panMap, "Tower", "1");
    }

    public void floorOneMapOnMousePressed(MouseEvent event) {

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }

    public void floorOneMapOnMouseDragged(MouseEvent event) {

        // Handle onMouseDragged event
        double offsetX = event.getSceneX() - sceneX;
        double offsetY = event.getSceneY() - sceneY;
        double newTranslateX = translateX + offsetX;
        double newTranslateY = translateY + offsetY;

        ((AnchorPane) event.getSource()).setTranslateX(newTranslateX);
        ((AnchorPane) event.getSource()).setTranslateY(newTranslateY);
    }

    public void floorOneMapScroll(ScrollEvent event) {
        ((AnchorPane) event.getSource()).setTranslateX(((AnchorPane) event.getSource()).getTranslateX() + event.getDeltaX());
        ((AnchorPane) event.getSource()).setTranslateY(((AnchorPane) event.getSource()).getTranslateY() + event.getDeltaY());
    }

    public void floorOneMapZoom(ZoomEvent event) {
        ((AnchorPane) event.getSource()).setScaleX(((AnchorPane) event.getSource()).getScaleX() * event.getZoomFactor());
        ((AnchorPane) event.getSource()).setScaleY(((AnchorPane) event.getSource()).getScaleY() * event.getZoomFactor());

    }

    public void floorOneMapZoomIn(MouseEvent event) {
        System.out.println("Zoom In function under construction: " + event.toString());
        floorOneMapZoom(new ZoomEvent(ZoomEvent.ZOOM, 0, 0, 0, 0, false, false, false, false,
                false, false, 4.2, 4.2, null));
        event.consume();
    }

    public void floorOneMapZoomOut(MouseEvent event) {
        System.out.println("Zoom Out function under construction: " + event.toString());
        System.out.println("Zoom In function under construction: " + event.toString());
        floorOneMapZoom(new ZoomEvent(ZoomEvent.ZOOM, 0, 0, 0, 0, false, false, false, false,
                false, false, 0.2, 0.2, null));
        event.consume();
    }
}
