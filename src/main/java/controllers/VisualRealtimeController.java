package controllers;

import helpers.UIHelpers;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.map.Location;

public class VisualRealtimeController {
    public static AnchorPane panMap;

    public static void setPanMap(AnchorPane panMap) {
        VisualRealtimeController.panMap = panMap;
    }
    public static void removeCircle(Circle c) {
        panMap.getChildren().remove(c);

    }
    public static void addCircle(Circle c) {
        panMap.getChildren().add(c);

    }
    public static void updateCircle(Circle oldCirc, Circle newCirc) {
        removeCircle(oldCirc);
        addCircle(newCirc);
    }
    public static void visuallyDeselectCircle(Location c) {
        UIHelpers.updateCircleForNodeType(c);
    }
    public static void visuallySelectCircle(Location c) {
        changeCircleColor(c.getNodeCircle(), Color.RED);
    }
    public static void changeCircleColor(Circle circ, Color color) {
        removeCircle(circ);
        circ.setFill(Color.RED);
        addCircle(circ);
    }
    public static void addLine(Line l) {
        l.toBack();
        panMap.getChildren().add(l);
    }
    public static void removeLine(Line l) {
        panMap.getChildren().remove(l);
    }
}
