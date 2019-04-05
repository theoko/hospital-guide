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
     * @param pane
     * @param floor
     */
    public static void displayUser(AnchorPane pane, String floor) {
        Map map = MapParser.parse();
        displayNodesUser(map, pane, floor);
    }

    /**
     * Display the graph of a map for employees (halls, info boxes with spill reporting)
     * @param pane
     * @param floor
     */
    public static void displayEmployee(AnchorPane pane, String floor) {
        Map map = MapParser.parse();
        displayNodesEmployee(map, pane, floor);
    }

    /**
     * Display the graph on a map for the admin (halls, edit boxes)
     * @param pane
     * @param floor
     */
    public static void displayAdmin(AnchorPane pane, String floor) {
        Map map = MapParser.parse();
        displayEdges(map, pane, floor);
        displayNodesAdmin(map, pane, floor);
    }

    /**
     * Display the graph on a map for the custodian (no halls)
     * @param pane
     * @param floor
     */
    public static void displayCust(AnchorPane pane, String floor) {
        Map map = MapParser.parse();
        displayNodesCust(map, pane, floor);
    }

    private static void displayNodesUser(Map map, AnchorPane pane, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getFloor().equals(floor) && loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp(Constants.Routes.USER_INFO, loc, map, pane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                pane.getChildren().add(circle);
            }
        }
    }

    private static void displayNodesCust(Map map, AnchorPane pane, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getFloor().equals(floor) && loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp(Constants.Routes.CUSTODIAN_INFO, loc, map, pane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                pane.getChildren().add(circle);
            }
        }
    }

    private static void displayNodesEmployee(Map map, AnchorPane pane, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getFloor().equals(floor) && loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp(Constants.Routes.EMPLOYEE_INFO, loc, map, pane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                pane.getChildren().add(circle);
            }
        }
    }

    private static void displayNodesAdmin(Map map, AnchorPane pane, String floor) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getFloor().equals(floor)) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle;
                if (loc.getNodeType() != Constants.NodeType.HALL) {
                    circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                } else {
                    circle = new Circle(xLoc, yLoc, hallRadius, hallFill);
                }
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                UIHelpers.setAdminNodeClickEvent(circle, loc);
                loc.setNodeCircle(circle);
                pane.getChildren().add(circle);
            }
        }
    }

    private static void displayEdges(Map map, AnchorPane pane, String floor) {
        HashMap<String, Edge> lstEdges = map.getAllEdges();
        for (Edge edge : lstEdges.values()) {
            Location start = edge.getStart();
            Location end = edge.getEnd();
            if (start.getFloor().equals(floor) && end.getFloor().equals(floor)) {
                double x1 = scaleX(start.getxCord());
                double x2 = scaleX(end.getxCord());
                double y1 = scaleY(start.getyCord());
                double y2 = scaleY(end.getyCord());
                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(edgeFill);
                line.setStrokeWidth(edgeWidth);
                pane.getChildren().add(line);
            }
        }
    }

    public static double scaleX(double x) {
        return (x - xShift) * scale;
    }

    public static double scaleY(double y) {
        return (y - yShift) * scale;
    }

    public static double revScaleX(double x) {
        return x / scale + xShift;
    }

    public static double revScaleY(double y) {
        return y / scale + yShift;
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
