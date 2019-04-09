package map;

import controllers.MapController;
import controllers.SettingsController;
import helpers.Constants;
import helpers.MapHelpers;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.util.Duration;
import models.map.Location;
import models.map.Map;
import models.map.SubPath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public abstract class PathFinder {

    protected final double FLOOR_HEURISTIC = 100000;
    protected final double STRAIGHT_ANGLE = 90.0;
    protected final double TURN_SENSITIVITY = 45.0;
    protected final double PIXEL_TO_METERS = 0.1;
    private static double LINE_WIDTH = 3.5;
    private static double LINE_LENGTH = 5.0;
    private static double LINE_GAP = 10.0;

    public static String defLocation;

    public PathFinder() {
    }

    /**
     * Finds a path from the start map to the end map using a*
     * @return A stack of locations that contains the path
     */
    public abstract Stack<Location> findPath(Location start, Location end);

    /**
     * Generates a path from the given parent map and end map
     * @param parent Hashmap of each node's parent
     * @param end The end map
     * @return A stack of locations containing the path
     */
    protected final Stack<Location> genPath(HashMap<String, SubPath> parent, SubPath end) {
        // Create an empty stack of locations
        Stack<Location> path = new Stack<>();
        // Start at the last node (end)
        SubPath prev = end;
        // Loop thru until the start is reached (parent == null)
        while (prev != null) {
            path.push(prev.getLocation());
            // prev := prev -> parent
            prev = parent.get(prev.getLocation().getNodeID());
        }
        return path;
    }

    public final String txtDirections(Stack<Location> path) {
        String directions = "";

        Location loc1 = null;
        Location loc2 = null;
        Location loc3;

        double totDist = 0.0;
        while (!path.isEmpty()) {
            loc3 = path.pop();
            if (loc1 != null && loc2 != null) {
                if (loc2.getNodeType() == Constants.NodeType.ELEV && loc3.getNodeType() == Constants.NodeType.ELEV) { // On and off the elevator
                    directions += "Take the elevator from floor " + loc2.getFloor() + " to floor " + loc3.getFloor() + ".\n";
                } else if (loc2.getNodeType() == Constants.NodeType.STAI && loc3.getNodeType() == Constants.NodeType.STAI) { // On and off the stairs
                    directions += "Take the stairs from " + loc2.getFloor() + " to floor " + loc3.getFloor() + ".\n";
                } else {
                    int x1 = loc1.getxCord();
                    int y1 = -1 * loc1.getyCord();

                    int x2 = loc2.getxCord();
                    int y2 = -1 * loc2.getyCord();

                    int x3 = loc3.getxCord();
                    int y3 = -1 * loc3.getyCord();

                    double a = calcDist(x1, y1, x2, y2); // 1 <-> 2
                    double b = calcDist(x2, y2, x3, y3); // 2 <-> 3
                    double c = calcDist(x1, y1, x3, y3); // 1 <-> 3

                    double angle = Math.toDegrees(Math.acos((Math.pow(a, 2.0) + Math.pow(b, 2.0) - Math.pow(c, 2.0)) / (2.0 * a * b)));
                    double vX = x1 - x2;
                    double vY = y1 - y2;
                    double uX = x3 - x2;
                    double uY = y3 - y2;
                    double cross = vX * uY - vY * uX;

                    totDist += a;
                    if (angle > STRAIGHT_ANGLE - TURN_SENSITIVITY && angle < STRAIGHT_ANGLE + TURN_SENSITIVITY) {
                        directions += "Turn ";
                        if (cross > 0) { // Right
                            directions += "right";
                        } else { // Left
                            directions += "left";
                        }
                        int displayDist = (int) (totDist * PIXEL_TO_METERS);
                        if (displayDist != 1) {
                            directions += " in " + displayDist + " meters.\n";
                        } else {
                            directions += " in " + displayDist + " meter.\n";
                        }
                        totDist = 0.0;
                    }
                }
            }

            // Rotate
            loc1 = loc2;
            loc2 = loc3;
        }
        return directions;
    }

    public static void printPath(AnchorPane[] panes, Map map, Location loc1, Location loc2) {
        for (AnchorPane pane : panes) {
            List<Node> lstNodes1 = new ArrayList<>();
            for (Node n : pane.getChildren()) {
                if (n instanceof Line) {
                    lstNodes1.add(n);
                }
            }
            for (Node n : lstNodes1) {
                pane.getChildren().remove(n);
            }
        }

        PathContext context = SettingsController.getAlgType();
        Stack<Location> path = context.findPath(loc1, loc2);
        String directions = context.txtDirections((Stack<Location>) path.clone());
        HashMap<String, Location> lstLocations = map.getAllLocations();
        Location prev = null;
        while (!path.isEmpty()) {
            Location curr = path.pop();
            if (prev != null) {
                Line line = new Line(MapDisplay.scaleX(prev.getxCord()), MapDisplay.scaleY(prev.getyCord()), MapDisplay.scaleX(curr.getxCord()), MapDisplay.scaleY(curr.getyCord()));
                line.setStroke(Color.BLACK);
                line.getStrokeDashArray().setAll(LINE_LENGTH, LINE_GAP);
                line.setStrokeWidth(LINE_WIDTH);
                line.setStrokeLineCap(StrokeLineCap.ROUND);
                line.setStrokeLineJoin(StrokeLineJoin.ROUND);
                final double maxOffset =
                        line.getStrokeDashArray().stream()
                                .reduce(
                                        0d,
                                        (a, b) -> a - b
                                );

                Timeline timeline = new Timeline(
                        new KeyFrame(
                                Duration.ZERO,
                                new KeyValue(
                                        line.strokeDashOffsetProperty(),
                                        0,
                                        Interpolator.LINEAR
                                )
                        ),
                        new KeyFrame(
                                Duration.seconds(3),
                                new KeyValue(
                                        line.strokeDashOffsetProperty(),
                                        maxOffset,
                                        Interpolator.LINEAR
                                )
                        )
                );
                timeline.setCycleCount(Timeline.INDEFINITE);
                timeline.play();
                if (curr.getFloor().equals("L2") && prev.getFloor().equals("L2")) {
                    panes[0].getChildren().add(1, line);
                } else if (curr.getFloor().equals("L1") && prev.getFloor().equals("L1")) {
                    panes[1].getChildren().add(1, line);
                } else if (curr.getFloor().equals("1") && prev.getFloor().equals("1")) {
                    panes[2].getChildren().add(1, line);
                } else if (curr.getFloor().equals("2") && prev.getFloor().equals("2")) {
                    panes[3].getChildren().add(1, line);
                } else {
                    panes[4].getChildren().add(1, line);
                }
            }
            prev = curr;
        }
    }

    public static String getDefLocation() {
        return defLocation;
    }

    public static void setDefLocation(String defLocation) {
        PathFinder.defLocation = defLocation;
        MapController.setTempStart(defLocation);
    }

    /**
     * Equation to calculate the distance between two points
     * @param x1 X-Location of point 1
     * @param y1 Y-Location of point 1
     * @param x2 X-Location of point 2
     * @param y2 Y-Location of point 2
     * @return The distance as a double
     */
    static double calcDist(int x1, int y1, int x2, int y2) {
        return Math.pow((Math.pow(x1 - x2, 2.0) + (Math.pow(y1 - y2, 2.0))), 0.5);
    }

    public abstract MapHelpers.Algorithm getAlg();

    protected final int floorToInt(String floor) {
        switch (floor) {
            case "L2":
                return 0;
            case "L1":
                return 1;
            case "1":
                return 2;
            case "2":
                return 3;
            default:
                return 4;
        }
    }
}
