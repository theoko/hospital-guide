package map;

import controllers.MapController;
import controllers.ScreenController;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.scene.control.ScrollPane;
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
    public final static Color nodeFill = Color.WHITE;
    public final static Color nodeStart = Color.GREEN;
    public final static Color nodeEnd = Color.RED;
    private final static Color hallFill = Color.GRAY;
    private final static Color nodeOutline = Color.BLACK;
    private final static Color edgeFill = Color.BLACK;

    /**
     * Display the graph on a map for the default user (no halls, info boxes)
     */
    public static void displayUser(AnchorPane[] panes, ScrollPane TextPane) {
        Map map = MapParser.parse();
        displayNodesUser(map, panes, TextPane);
    }

    /**
     * Display the graph of a map for employees (halls, info boxes with spill reporting)
     * @param panes
     */
    public static void displayEmployee(AnchorPane[] panes, ScrollPane TextPane) {
        Map map = MapParser.parse();
        displayNodesEmployee(map, panes, TextPane);
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
    public static void displayCust(MapController mc, AnchorPane[] panes, ScrollPane TextPane) {
        Map map = MapParser.parse();
        mc.setMap(map);
        displayNodesCust(map, panes, TextPane);
    }

    private static void displayNodesUser(Map map, AnchorPane[] panes, ScrollPane TextPane) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle = new Circle(xLoc, yLoc, locRadius);
                if (!loc.getNodeID().equals(MapController.getTempStart())) {
                    circle.setFill(nodeFill);
                } else {
                    circle.setFill(nodeStart);
                }
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setId(loc.getNodeID());
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp(Constants.Routes.USER_INFO, loc, map, panes, circle, TextPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                loc.setNodeCircle(circle);
                findPane(panes, loc.getFloor()).getChildren().add(circle);
            }
        }
    }

    private static void displayNodesCust(Map map, AnchorPane[] panes, ScrollPane TextPane) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle = new Circle(xLoc, yLoc, locRadius);
                if (!loc.getNodeID().equals(MapController.getTempStart())) {
                    circle.setFill(nodeFill);
                } else {
                    circle.setFill(nodeStart);
                }
                circle.setStroke(nodeOutline);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setId(loc.getNodeID());
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp(Constants.Routes.CUSTODIAN_INFO, loc, map, panes, circle, TextPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                loc.setNodeCircle(circle);
                findPane(panes, loc.getFloor()).getChildren().add(circle);
            }
        }
    }

    private static void displayNodesEmployee(Map map, AnchorPane[] panes, ScrollPane TextPane) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = scaleX(loc.getxCord());
                double yLoc = scaleY(loc.getyCord());
                Circle circle = new Circle(xLoc, yLoc, locRadius);
                if (!loc.getNodeID().equals(MapController.getTempStart())) {
                    circle.setFill(nodeFill);
                } else {
                    circle.setFill(nodeStart);
                }
                circle.setStroke(nodeOutline);
                circle.setStroke(nodeOutline);
                circle.setStrokeWidth(locWidth);
                circle.setId(loc.getNodeID());
                circle.setOnMouseClicked(event -> {
                    try {
                        event.consume();
                        ScreenController.popUp(Constants.Routes.EMPLOYEE_INFO, loc, map, panes, circle, TextPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                loc.setNodeCircle(circle);
                findPane(panes, loc.getFloor()).getChildren().add(circle);
            }
        }
    }

    private static void displayNodesAdmin(Map map, AnchorPane[] panes) {
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            double xLoc = scaleX(loc.getxCord());
            double yLoc = scaleY(loc.getyCord());
            Circle circle;
            if (loc.getNodeID().equals(MapController.getTempStart())) {
                circle = new Circle(xLoc, yLoc, locRadius, nodeStart);
            } else if (loc.getNodeType() != Constants.NodeType.HALL) {
                circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
            } else {
                circle = new Circle(xLoc, yLoc, hallRadius, hallFill);
            }
            circle.setStroke(nodeOutline);
            circle.setStrokeWidth(locWidth);
            UIHelpers.setAdminNodeClickEvent(map, panes, loc, circle);
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
                double x1 = scaleX(start.getxCord());
                double x2 = scaleX(end.getxCord());
                double y1 = scaleY(start.getyCord());
                double y2 = scaleY(end.getyCord());
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
            case "G":
                return panes[2];
            case "1":
                return panes[3];
            case "2":
                return panes[4];
            default:
                return panes[5];
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
