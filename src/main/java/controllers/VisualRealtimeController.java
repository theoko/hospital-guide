package controllers;

import helpers.UIHelpers;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;
import models.map.Location;
import models.map.Workspace;

import java.io.File;
import java.util.ArrayList;

public class VisualRealtimeController {
    public static ArrayList<AnchorPane> mapPanes = new ArrayList<AnchorPane>();

    public static AnchorPane panMap;
    // used to deselect and select circles
    public static ArrayList<Location> selectedLocations = new ArrayList<Location>();
    public static void setPanMap(AnchorPane panMap) {
        VisualRealtimeController.panMap = panMap;
    }
    public static void removeCircle(Location c) {

        panMap.getChildren().remove(c.getNodeCircle());

    }
    public static void removeCircle(Workspace c) {

        panMap.getChildren().remove(c.getNodeCircle());

    }
    public static void pushCircleToFront(Location c) {
        panMap.getChildren().remove(c.getNodeCircle());
        panMap.getChildren().add(c.getNodeCircle());
    }
    public static void addCircle(Circle c) {
        panMap.getChildren().add(c);

    }
    public static void addPanMap(AnchorPane panMap) {
        mapPanes.add(panMap);
    }
    public static void visuallyDeselectAll() {
        for(Location c : selectedLocations) {
            UIHelpers.updateCircleForNodeType(c);
        }
        selectedLocations.clear();
    }
    public static WritableImage generateSnapshot(int[] boundaries, Location loc) {
        return generateSnapshot(boundaries, loc.getFloor());
    }
    public static WritableImage generateSnapshot(int[] boundaries, String s) {
        setSelectedPane(s);
        SnapshotParameters shp = new SnapshotParameters();
        Rectangle2D viewPort = new Rectangle2D(boundaries[0], boundaries[2], boundaries[1]-boundaries[0],
                boundaries[3]- boundaries[2]);
        shp.setViewport(viewPort);
        double scale = 512 / viewPort.getWidth();
        shp.setTransform(Transform.scale(scale, scale, panMap.getWidth() / 2, panMap.getHeight() / 2));
        WritableImage snap = panMap.snapshot(shp, null);

        return snap;
    }
    public static void visuallyDeselectCircle(Location c) {
        setSelectedPane(c);
        UIHelpers.updateCircleForNodeType(c);
    }
    public static void setSelectedPane(String floor) {
        for(AnchorPane p : mapPanes) {
            panMap = p.getId().equals(floor) ? p : panMap;
        }
    }
    public static void setSelectedPane(Location c) {
        setSelectedPane(c.getFloor());
    }

    public static void visuallySelectCircle(Location c) {
        setSelectedPane(c);
        selectedLocations.add(c);
        if(selectedLocations.size() == 1) {
            changeCircleColor(c, Color.RED);
        } else {
            changeCircleColor(c, Color.ORANGE);
        }

    }
    public static void changeCircleColor(Location c, Color color) {
        setSelectedPane(c);
        c.getNodeCircle().setFill(color);
    }
    public static void addLine(Line l) {
        panMap.getChildren().add(l);
        panMap.toBack();
//        l.toBack();

    }
    public static void removeLine(Line l) {
        panMap.getChildren().remove(l);
    }

}
