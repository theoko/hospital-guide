package map;

import helpers.Constants;
import models.map.Location;
import models.map.SubPath;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public abstract class PathFinder {

    protected final double FLOOR_HEURISTIC = 100000;
    protected final double STRAIGHT_ANGLE = 90.0;
    protected final double TURN_SENSITIVITY = 45.0;
    protected final double PIXEL_TO_METERS = 0.1;

    protected Location start;
    protected Location end;

    public PathFinder(Location start, Location end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Finds a path from the start map to the end map using a*
     * @return A stack of locations that contains the path
     */
    public abstract Stack<Location> findPath();

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
