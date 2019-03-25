package map;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;

public class MapDisplay {

    private final static double defRadius = 25.0;

    public static void display(Map map, Pane pane, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getFloor().equals(floor)) {
                double xLoc = (double) loc.getxCord();
                double yLoc = (double) loc.getyCord();
                Color color = nodeColor(loc);

                Circle circle = new Circle(xLoc, yLoc, defRadius, color);
                pane.getChildren().add(circle);
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
                return Color.GRAY;
            case EXIT:
                return Color.GRAY;
            case HALL:
                return Color.GRAY;
            case INFO:
                return Color.GRAY;
            case LABS:
                return Color.GRAY;
            case REST:
                return Color.GRAY;
            case RETL:
                return Color.GRAY;
            case SERV:
                return Color.GRAY;
            default:
                return Color.GRAY;
        }
    }
}
