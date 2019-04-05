package map;

import controllers.ScreenController;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Location;
import models.map.Map;
import java.util.HashMap;

public class MapDisplay {

    private final static double locRadius = 7.5;
    private final static double hallRadius = 2.5;
    private final static double locWidth = 2.0;
    private final static double edgeWidth = 1.5;
    private final static double xShift = -2110.0;
    private final static double yShift = 730.0;
    private final static double scale = 0.475;
    private final static Color nodeFill = Color.WHITE;
    private final static Color hallFill = Color.GRAY;
    private final static Color nodeOutline = Color.BLACK;
    private final static Color edgeFill = Color.BLACK;

    /**
     * Display the graph on a map for the default user (no halls, info boxes)
     */
    public static void displayUser(AnchorPane[] panes) {
        Map map = MapParser.parse();
        displayNodesUser(map, panes);
    }

    /**
     * Display the graph of a map for employees (halls, info boxes with spill reporting)
     * @param panes
     */
    public static void displayEmployee(AnchorPane[] panes) {
        Map map = MapParser.parse();
        displayNodesEmployee(map, panes);
    }

    /**
     * Display the graph on a map for the admin (halls, edit boxes)
     * @param panes
     */
    public static void displayAdmin(AnchorPane[] panes) {
        Map map = MapParser.parse();
        displayEdges(map, panes);
        displayNodesAdmin(map, panes);
    }

    /**
     * Display the graph on a map for the custodian (no halls)
     * @param panes
     */
    public static void displayCust(AnchorPane[] panes) {
        Map map = MapParser.parse();
        displayNodesCust(map, panes);
    }

    private static void displayNodesUser(Map map, AnchorPane[] panes) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = (loc.getxCord() - xShift) * scale;
                double yLoc = (loc.getyCord() - yShift) * scale;
                Color color = nodeFill;
                Circle circle = new Circle(xLoc, yLoc, locRadius, color);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp("info", loc, map, panes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                findPane(panes, loc.getFloor()).getChildren().add(circle);
            }
        }
    }

    private static void displayNodesCust(Map map, AnchorPane[] panes) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                boolean correctCoordinates = loc.getNodeID().substring(0,1).equals("X");
                double xLoc = (loc.getxCord() - xShift) * scale;
                double yLoc = (loc.getyCord() - yShift) * scale;
                Color color = nodeFill;
                Circle circle = new Circle(xLoc, yLoc, locRadius, color);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp("custodian-info", loc, map, panes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                findPane(panes, loc.getFloor()).getChildren().add(circle);
            }
        }
    }

    private static void displayNodesEmployee(Map map, AnchorPane[] panes) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = (loc.getxCord() - xShift) * scale;
                double yLoc = (loc.getyCord() - yShift) * scale;
                Color color = nodeFill;
                Circle circle = new Circle(xLoc, yLoc, locRadius, color);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp("employee-info", loc, map, panes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                findPane(panes, loc.getFloor()).getChildren().add(circle);
            }
        }
    }

    private static void displayNodesAdmin(Map map, AnchorPane[] panes) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            boolean correctCoordinates = loc.getNodeID().substring(0, 1).equals("X");
            double xLoc = correctCoordinates ? loc.getxCord() : (loc.getxCord() - xShift) * scale;
            double yLoc = correctCoordinates ? loc.getyCord() : (loc.getyCord() - yShift) * scale;
            Circle circle;
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                Color color = nodeFill;
                circle = new Circle(xLoc, yLoc, locRadius, color);
            } else {
                Color color = hallFill;
                circle = new Circle(xLoc, yLoc, hallRadius, color);
            }

            circle.setStroke(nodeOutline);
            circle.setStrokeWidth(locWidth);
            UIHelpers.setAdminNodeClickEvent(circle, loc);
            loc.setNodeCircle(circle);
            findPane(panes, loc.getFloor()).getChildren().add(circle);
        }
    }

    private static void displayEdges(Map map, AnchorPane[] panes) {
        HashMap<String, Edge> lstEdges = map.getAllEdges();
        for (Edge edge : lstEdges.values()) {
            Location start = edge.getStart();
            Location end = edge.getEnd();
            if (start.getFloor().equals(end.getFloor())) {
                double x1 = (start.getxCord() - xShift) * scale;
                double x2 = (end.getxCord() - xShift) * scale;
                double y1 = (start.getyCord() - yShift) * scale;
                double y2 = (end.getyCord() - yShift) * scale;
                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(edgeFill);
                line.setStrokeWidth(edgeWidth);
                findPane(panes, start.getFloor()).getChildren().add(line);
            }
        }
    }

    private static AnchorPane findPane(AnchorPane[] panes, String floor) {
        switch (floor) {
            case "L2":
                return panes[0];
            case "L1":
                return panes[1];
            case "1":
                return panes[2];
            case "2":
                return panes[3];
            default:
                return panes[4];
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
public static double getxShift() {
    return xShift;
}

    public static double getyShift() {
        return yShift;
    }

    public static double getScale() {
        return scale;
    }

    public static double getLocRadius() {
        return locRadius;
    }

    public static double getHallRadius() {
        return hallRadius;
    }

    public static double getLocWidth() {
        return locWidth;
    }

    public static double getEdgeWidth() {
        return edgeWidth;
    }
}
