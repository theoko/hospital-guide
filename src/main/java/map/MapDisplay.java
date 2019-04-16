package map;

import controllers.ScreenController;
import controllers.maps.AdminMapController;
import controllers.maps.MapController;
import database.EdgeTable;
import database.LocationTable;
import helpers.Constants;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Location;
import models.map.Map;
import models.map.SubPath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapDisplay {
    private final static double locRadius = 15;
    private final static double hallRadius = 5;
    private final static double locWidth = 2.0;
    public final static double edgeWidth = 1.5;
    private final static double xShift = -2110.0;
    private final static double yShift = 730.0;
    private final static double scale = 0.475;
    public final static double opac = 0.25;
    public final static Color nodeFill = Color.NAVY;
    public final static Color nodeStart = Color.GREEN;
    public final static Color nodeEnd = Color.RED;
    private final static Color edgeOutline = Color.BLACK;
    private final static Color nodeOutline = Color.GOLD;
    private final static Color edgeFill = Color.BLACK;

    private static Map map;

    public enum NodeStyle {
        REGULAR, START, END, POINT
    }

    /**
     * Display the graph on a map for the default user (no halls, info boxes)
     */
    public static void displayUser(MapController mc) {
        displayNodes(mc, Constants.Routes.USER_INFO, false);
    }

    public static void displayEmployee(MapController mc) {
        displayNodes(mc, Constants.Routes.EMPLOYEE_INFO, false);
    }

    public static void displayCust(MapController mc) {
        displayNodes(mc, Constants.Routes.CUSTODIAN_INFO, false);
    }

    public static void displayAdmin(MapController mc) {
        displayNodes(mc, Constants.Routes.EDIT_LOCATION, true);
        displayEdges(mc);
    }

    private static void displayNodes(MapController mc, Constants.Routes route, boolean isAdmin) {
        map = MapParser.parse();
        mc.setMap(map);
        String start = MapController.getTempStart();
        String end = "";
        List<Node> lstNodes = new ArrayList<>();
        for (Node n : mc.panMap.getChildren()) {
            if (n instanceof Circle) {
                if (((Circle) n).getFill().equals(nodeEnd)) {
                    end = n.getId();
                }
                lstNodes.add(n);
            }
        }
        mc.panMap.getChildren().removeAll(lstNodes);

        HashMap<String, Location> lstLocations = map.getAllLocations();
        String floor = mc.getFloor();
        for (Location loc : lstLocations.values()) {
            if (loc.getNodeID().equals(start)) {
                if (loc.getFloor().equals(floor)) {
                    mc.panMap.getChildren().add(createCircle(mc, loc, NodeStyle.START, 1, route, isAdmin));
                } else {
                    mc.panMap.getChildren().add(createCircle(mc, loc, NodeStyle.START, opac, route, isAdmin));
                }
            } else if (loc.getNodeID().equals(end)) {
                if (loc.getFloor().equals(floor)) {
                    mc.panMap.getChildren().add(createCircle(mc, loc, NodeStyle.END, 1, route, isAdmin));
                } else {
                    mc.panMap.getChildren().add(createCircle(mc, loc, NodeStyle.END, opac, route, isAdmin));
                }
            } else if (loc.getFloor().equals(mc.getFloor())) {
                if (loc.getNodeType() != Constants.NodeType.HALL) {
                    mc.panMap.getChildren().add(createCircle(mc, loc, NodeStyle.REGULAR, 1, route, isAdmin));
                } else if (mc.isAdmin()) {
                    mc.panMap.getChildren().add(0, createCircle(mc, loc, NodeStyle.POINT, 1, route, isAdmin));
                }
            }
        }
    }

    private static void displayEdges(MapController mc) {
        HashMap<String, Edge> lstEdges = map.getAllEdges();
        for (Edge edge : lstEdges.values()) {
            Location start = edge.getStart();
            Location end = edge.getEnd();
            String floor = mc.getFloor();
            if (start.getFloor().equals(floor) && end.getFloor().equals(floor)) {
                Line line = new Line();
                bindLineCircle(line, start, end);
                line.setStroke(edgeFill);
                line.setStrokeWidth(edgeWidth);
                line.setId(edge.getEdgeID());
                mc.panMap.getChildren().add(0, line);
                edge.setLine(line);
            }
        }
    }

    private static void bindLineCircle(Line line, Location start, Location end) {
        line.startXProperty().bind(start.getNodeCircle().centerXProperty());
        line.startYProperty().bind(start.getNodeCircle().centerYProperty());
        line.endXProperty().bind(end.getNodeCircle().centerXProperty());
        line.endYProperty().bind(end.getNodeCircle().centerYProperty());
    }

    static class Delta {
        double x, y;
        boolean dragged;
    }

    public static Circle createCircle(MapController mc, Location loc, NodeStyle nodeStyle, double opacity, Constants.Routes route, boolean isAdmin) {
        double xLoc = loc.getxCord();
        double yLoc = loc.getyCord();
        Circle circle = new Circle(xLoc, yLoc, locRadius);
        circle.setStroke(nodeOutline);
        circle.setStrokeWidth(locWidth);
        circle.setOpacity(opacity);
        circle.setId(loc.getNodeID());

        switch (nodeStyle){
            case REGULAR:
                circle.setFill(nodeFill);
                break;
            case START:
                circle.setFill(nodeStart);
                break;
            case END:
                circle.setFill(nodeEnd);
                break;
            default:
                circle.setFill(edgeFill);
                circle.setRadius(hallRadius);
                circle.setStroke(edgeOutline);
                break;
        }

        if (!isAdmin) {
            circle.setOnMouseClicked(event -> {
                try {
                    ScreenController.infoPopUp(route, loc, mc, map);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            final Delta dragDelta = new Delta();
            circle.setOnMousePressed((e) -> {
                if (AdminMapController.getEdgLoc() == null) {
                    dragDelta.dragged = false;
                    dragDelta.x = circle.getCenterX() - e.getX();
                    dragDelta.y = circle.getCenterY() - e.getY();
                    e.consume();
                }
            });
            circle.setOnMouseDragged((e) -> {
                if (AdminMapController.getEdgLoc() == null) {
                    dragDelta.dragged = true;
                    circle.setCenterX(e.getX() + dragDelta.x);
                    circle.setCenterY(e.getY() + dragDelta.y);
                    e.consume();
                }
            });
            circle.setOnMouseReleased((e) -> {
                if (!dragDelta.dragged) {
                    if (AdminMapController.getEdgLoc() == null) {
                        try {
                            ScreenController.adminPopUp(route, loc, mc);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        Location loc1 = AdminMapController.getEdgLoc();
                        Location loc2 = map.getLocation(circle.getId());
                        if (!loc1.getNodeID().equals(loc2.getNodeID())) {
                            String id = loc1.getNodeID() + "_" + loc2.getNodeID();
                            Edge edge = new Edge(id, loc1, loc2);
                            int x1 = loc1.getxCord();
                            int y1 = loc1.getyCord();
                            int x2 = loc2.getxCord();
                            int y2 = loc2.getyCord();
                            EdgeTable.addEdge(edge);

                            Line line = new Line(x1, y1, x2, y2);
                            line.setId(id);
                            bindLineCircle(line, loc1, loc2);
                            line.setStrokeWidth(MapDisplay.edgeWidth);
                            mc.panMap.getChildren().add(0, line);
                        } else {
                            AdminMapController.setEdgLoc(null);
                        }
                    }
                } else {
                    loc.setxCord((int) circle.getCenterX());
                    loc.setyCord((int) circle.getCenterY());
                    LocationTable.updateLocation(loc);
                }
                e.consume();
            });
        }

        loc.setNodeCircle(circle);
        return circle;
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
