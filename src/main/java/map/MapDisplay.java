package map;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.*;
import java.util.HashMap;

public class MapDisplay {

    private final static double defRadius = 10.0;
    private final static double xShift = 1110.0;
    private final static double yShift = 575.0;

    public static void display(Map map, AnchorPane pane, String building, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getBuilding().equals(building) && loc.getFloor().equals(floor)) {
                if (loc.getNodeType() != NodeType.HALL) {
                    double xLoc = (double) loc.getxCord() - xShift;
                    double yLoc = (double) loc.getyCord() - yShift;
                    System.out.println("(" + xLoc + ", " + yLoc + ")");
                    Color color = nodeColor(loc);
                    Circle circle = new Circle(xLoc, yLoc, defRadius, color);
                    circle.setStroke(Color.BLACK);
                    pane.getChildren().add(circle);
                }
            }
        }
    }

    private static Color nodeColor(Location loc) {
        switch (loc.getNodeType()) {
            case BATH:
                return Color.GRAY;
            case CONF:
                return Color.GRAY;
            case DEPT:
                return Color.GRAY;
            case ELEV:
                return Color.YELLOW;
            case EXIT:
                return Color.GRAY;
            case HALL:
                return Color.RED;
            case INFO:
                return Color.GRAY;
            case LABS:
                return Color.GRAY;
            case REST:
                return Color.BLUE;
            case RETL:
                return Color.WHITE;
            case SERV:
                return Color.GRAY;
            default:
                return Color.YELLOW;
        }
    }
}
