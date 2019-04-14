package map;

import controllers.maps.MapController;
import controllers.maps.UserMapController;
import controllers.maps.MapController1;
import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Location;
import models.map.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapDisplay {
    private final static double locRadius = 15;
    private final static double hallRadius = 3.5;
    private final static double locWidth = 2.0;
    private final static double edgeWidth = 1.5;
    private final static double xShift = -2110.0;
    private final static double yShift = 730.0;
    private final static double scale = 0.475;
    public final static double opac = 0.25;
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
    public static void displayUser(MapController mc) {
        displayNodes(mc, Constants.Routes.USER_INFO);
    }

    public static void displayEmployee(MapController mc) {
        displayNodes(mc, Constants.Routes.EMPLOYEE_INFO);
    }

    public static void displayCust(MapController mc) {
        displayNodes(mc, Constants.Routes.CUSTODIAN_INFO);
    }

    public static void displayAdmin(MapController mc) {
        displayEdges(mc);
        displayNodes(mc, Constants.Routes.EDIT_LOCATION);
    }

    private static void displayNodes(MapController mc, Constants.Routes route) {
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
                    mc.panMap.getChildren().add(createCircle(mc, loc, nodeStart, 1, route));
                } else {
                    mc.panMap.getChildren().add(createCircle(mc, loc, nodeStart, opac, route));
                }
            } else if (loc.getNodeID().equals(end)) {
                if (loc.getFloor().equals(floor)) {
                    mc.panMap.getChildren().add(createCircle(mc, loc, nodeEnd, 1, route));
                } else {
                    mc.panMap.getChildren().add(createCircle(mc, loc, nodeEnd, opac, route));
                }
            } else if (loc.getFloor().equals(mc.getFloor()) && loc.getNodeType() != Constants.NodeType.HALL) {
                mc.panMap.getChildren().add(createCircle(mc, loc, nodeFill, 1, route));
            }
        }
    }

    private static void displayEdges(MapController mc) {
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
                mc.panMap.getChildren().add(line);
            }
        }
    }

    private static Circle createCircle(MapController mc, Location loc, Color color, double opacity, Constants.Routes route) {
        double xLoc = loc.getxCord();
        double yLoc = loc.getyCord();
        Circle circle = new Circle(xLoc, yLoc, locRadius);
        circle.setStroke(nodeOutline);
        circle.setStrokeWidth(locWidth);
        circle.setFill(color);
        circle.setOpacity(opacity);
        circle.setId(loc.getNodeID());
        circle.setOnMouseClicked(event -> {
            try {
                event.consume();
                ScreenController.infoPopUp(route, loc, mc, map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
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
