package map;

import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Location;
import models.map.Map;
import java.util.HashMap;

public class MapDisplay {

    private final static double locRadius = 15.0;
    private final static double hallRadius = 5.0;
    private final static double locWidth = 1.0;
    private final static double xShift = 1095.0;
    private final static double yShift = 565.0;
    private final static double scale = 1.6;
    private final static double edgeWidth = 2.5;

    /**
     * Display the graph on a map for the default user (no halls, info boxes)
     * @param pane
     * @param building
     * @param floor
     */
    public static void displayUser(AnchorPane pane, String building, String floor) {
        Map map = MapParser.parse();
        displayNodesUser(map, pane, building, floor);
    }

    /**
     * Display the graph of a map for employees (halls, info boxes with spill reporting)
     * @param pane
     * @param building
     * @param floor
     */
    public static void displayEmployee(AnchorPane pane, String building, String floor) {
        Map map = MapParser.parse();
        displayNodesEmployee(map, pane, building, floor);
    }

    /**
     * Display the graph on a map for the admin (halls, edit boxes)
     * @param pane
     * @param building
     * @param floor
     */
    public static void displayAdmin(AnchorPane pane, String building, String floor) {
        Map map = MapParser.parse();
        displayEdges(map, pane, building, floor);
        displayNodesAdmin(map, pane, building, floor);
    }

    private static void displayNodesUser(Map map, AnchorPane pane, String building, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getBuilding().equals(building) && loc.getFloor().equals(floor) && loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = (loc.getxCord() - xShift) * scale;
                double yLoc = (loc.getyCord() - yShift) * scale;
                Color color = Color.WHITE;
                Circle circle = new Circle(xLoc, yLoc, locRadius, color);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp("info", loc, map, pane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                pane.getChildren().add(circle);
            }
        }
    }

    private static void displayNodesEmployee(Map map, AnchorPane pane, String building, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getBuilding().equals(building) && loc.getFloor().equals(floor) && loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = (loc.getxCord() - xShift) * scale;
                double yLoc = (loc.getyCord() - yShift) * scale;
                Color color = Color.WHITE;
                Circle circle = new Circle(xLoc, yLoc, locRadius, color);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp("employee-info", loc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                pane.getChildren().add(circle);
            }
        }
    }

    private static void displayNodesAdmin(Map map, AnchorPane pane, String building, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getBuilding().equals(building) && loc.getFloor().equals(floor)) {
                double xLoc = (loc.getxCord() - xShift) * scale;
                double yLoc = (loc.getyCord() - yShift) * scale;
                Circle circle;
                if (loc.getNodeType() != Constants.NodeType.HALL) {
                    Color color = Color.WHITE;
                    circle = new Circle(xLoc, yLoc, locRadius, color);
                } else {
                    Color color = Color.GRAY;
                    circle = new Circle(xLoc, yLoc, hallRadius, color);
                }

                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(locWidth);

                circle.setOnMouseClicked(event -> {
                    try{
                        event.consume();
                        ScreenController.popUp("edit", loc);
                    }
                    catch (Exception e) {
                        throw new UnsupportedOperationException(e);
                    }
                });

                pane.getChildren().add(circle);
            }
        }
    }

    private static void displayEdges(Map map, AnchorPane pane, String building, String floor) {
        HashMap<String, Edge> lstEdges = map.getAllEdges();
        for (Edge edge : lstEdges.values()) {
            Location start = edge.getStart();
            Location end = edge.getEnd();
            if (start.getBuilding().equals(building) && start.getFloor().equals(floor) &&
                    end.getBuilding().equals(building) && end.getFloor().equals(floor)) {
                double x1 = (start.getxCord() - xShift) * scale;
                double x2 = (end.getxCord() - xShift) * scale;
                double y1 = (start.getyCord() - yShift) * scale;
                double y2 = (end.getyCord() - yShift) * scale;
                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(edgeWidth);
                pane.getChildren().add(line);
            }
        }
    }

    public static double scaleX(double x) {
        return (x - xShift) * scale;
    }

    public static double scaleY(double y) {
        return (y- yShift) * scale;
    }

//    private static Color nodeColor(Location loc) {
//        switch (loc.getNodeType()) {
//            case BATH:
//                return Color.GRAY;
//            case CONF:
//                return Color.GRAY;
//            case DEPT:
//                return Color.GRAY;
//            case ELEV:
//                return Color.YELLOW;
//            case EXIT:
//                return Color.GRAY;
//            case HALL:
//                return Color.RED;
//            case INFO:
//                return Color.GRAY;
//            case LABS:
//                return Color.GRAY;
//            case REST:
//                return Color.BLUE;
//            case RETL:
//                return Color.WHITE;
//            case SERV:
//                return Color.GRAY;
//            default:
//                return Color.YELLOW;
//        }
//    }
}
