package map;

import controllers.MapController;
import controllers.ScreenController;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Location;
import models.map.Map;

import java.util.HashMap;

public class MapDisplay {
    private final static double locRadius = 15;
    private final static double hallRadius = 3.5;
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

    private static Map map = MapParser.parse();

    /**
     * Display the graph on a map for the default user (no halls, info boxes)
     */
    public static void displayUser(Pane mapPane, ScrollPane txtPane, String floor) {
        displayNodesUser(mapPane, txtPane, floor);
    }

    public static void displayEmployee(Pane mapPane, ScrollPane txtPane, String floor) {
        displayNodesEmployee(mapPane, txtPane, floor);
    }

    public static void displayAdmin(Pane mapPane, String floor) {
        displayEdges(mapPane, floor);
        displayNodesAdmin(mapPane, floor);
    }

    public static void displayCust(MapController mc, Pane mapPane, ScrollPane txtPane, String floor) {
        mc.setMap(map);
        displayNodesCust(mapPane, txtPane, floor);
    }

    private static void displayNodesUser(Pane mapPane, ScrollPane txtPane, String floor) {
        mapPane.getChildren().clear();
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getFloor().equals(floor) && loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = loc.getxCord();
                double yLoc = loc.getyCord();
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
                        ScreenController.popUp(Constants.Routes.USER_INFO, loc, map, mapPane, circle, txtPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                loc.setNodeCircle(circle);
                mapPane.getChildren().add(circle);
            }
        }
    }

    private static void displayNodesCust(Pane mapPane, ScrollPane txtPane, String floor) {
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
                        //ScreenController.popUp(Constants.Routes.CUSTODIAN_INFO, loc, map, panes, circle, TextPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                loc.setNodeCircle(circle);
                mapPane.getChildren().add(circle);
            }
        }
    }

    private static void displayNodesEmployee(Pane mapPane, ScrollPane txtPane, String floor) {
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
                        //ScreenController.popUp(Constants.Routes.EMPLOYEE_INFO, loc, map, panes, circle, TextPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                loc.setNodeCircle(circle);
                mapPane.getChildren().add(circle);
            }
        }
    }

    private static void displayNodesAdmin(Pane mapPane, String floor) {
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
            //UIHelpers.setAdminNodeClickEvent(map, mapPane, loc, circle);
            loc.setNodeCircle(circle);
            mapPane.getChildren().add(circle);
        }
    }

    private static void displayEdges(Pane mapPane, String floor) {
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
                mapPane.getChildren().add(line);
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
