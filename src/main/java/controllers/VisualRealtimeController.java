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
    public static void removeCircle(Location c) {

        panMap.getChildren().remove(c.getNodeCircle());

    }
    public static void addCircle(Circle c) {
        panMap.getChildren().add(c);

    }
//    public static void updateCircle(Circle oldCirc, Circle newCirc) {
//        removeCircle(oldCirc);
//        addCircle(newCirc);
//    }
    public static void visuallyDeselectCircle(Location c) {
        UIHelpers.updateCircleForNodeType(c);
    }
    public static void visuallySelectCircle(Location c) {
        changeCircleColor(c, Color.RED);
    }
    public static void changeCircleColor(Location c, Color color) {
//        removeCircle(c);
        c.getNodeCircle().setFill(Color.RED);
//        addCircle(circ);
    }
    public static void addLine(Line l) {
        l.toBack();
        panMap.getChildren().add(l);
    }
    public static void removeLine(Line l) {
        panMap.getChildren().remove(l);
    }
}
