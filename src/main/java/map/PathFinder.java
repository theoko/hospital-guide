package map;

import helpers.Constants;
import models.map.Location;
import models.map.SubPath;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class PathFinder {

    private static double FLOOR_HEURISTIC = 100000;
    private static double STRAIGHT_ANGLE = 90.0;
    private static double TURN_SENSITIVITY = 45.0;
    private static double PIXEL_TO_METERS = 0.1;

    /**
     * Finds a path from the start map to the end map using a*
     * @param start The start map
     * @param end The end map
     * @return A stack of locations that contains the path
     */
    public static Stack<Location> findPath(Location start, Location end) {
        // Create a new stack to hold the path from start to end
        Stack<Location> path = new Stack<>();

        // Create priority queue and hashmaps
        PriorityQueue<SubPath> inQueue = new PriorityQueue<>();
        HashMap<String, SubPath> parent = new HashMap<>();
        HashMap<String, SubPath> used = new HashMap<>();

        // Initialize values
        SubPath sNeigh = new SubPath("", start, 0.0);
        inQueue.add(sNeigh);
        parent.put(start.getNodeID(), null);

        // Loop while queue isn't empty or end map is found
        while (!inQueue.isEmpty()) {
            // Poll next neighbor off the queue and get its map
            SubPath nNext = inQueue.poll();
            Location lNext = nNext.getLocation();
            if (used.containsKey(lNext.getNodeID())) {
                continue;
            }

            // Check to see if map is our end map
            if (lNext.getNodeID().equals(end.getNodeID())) {
                // Generate path from parent map and end node
                path = genPath(parent, nNext);
                break;
            }

            // Get the node's value and add it to the used map
            double currDist = nNext.getDist();
            used.put(lNext.getNodeID(), nNext);

            // Gets the node's neighbors and loop thru them all
            List<SubPath> lstSubPaths = lNext.getSubPaths();
            for (SubPath nCurr : lstSubPaths) {
                // Get the real map from the neighbor
                Location lCurr = nCurr.getLocation();
                // Check duplicate
                if (!used.containsKey(lCurr.getNodeID())) {
                    // Add the node's value to the current value
                    double newDist = currDist + nCurr.getDist();
                    // Calculate the heuristic based on distance to end map
                    //TODO: Incentive to not get off the elevator
                    double heuristic = calcDist(lCurr.getxCord(), lCurr.getyCord(), end.getxCord(), end.getyCord());
                    heuristic += FLOOR_HEURISTIC * Math.abs(floorToInt(lCurr.getFloor()) - floorToInt(end.getFloor()));
                    // Create a new neighbor with updated distance value
                    SubPath newNeigh = new SubPath(nCurr.getEdgeID(), nCurr.getLocation(), newDist, heuristic);
                    // Add the new neighbor into the queue and add its parent into the parent map
                    inQueue.add(newNeigh);
                    parent.putIfAbsent(lCurr.getNodeID(), nNext);
                }
            }
        }
        return path;
    }

    /**
     * Generates a path from the given parent map and end map
     * @param parent Hashmap of each node's parent
     * @param end The end map
     * @return A stack of locations containing the path
     */
    private static Stack<Location> genPath(HashMap<String, SubPath> parent, SubPath end) {
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

    public static String txtDirections(Stack<Location> path) {
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
                    System.out.println(loc2.getShortName());
                    System.out.println(a);
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
    public static double calcDist(int x1, int y1, int x2, int y2) {
        return Math.pow((Math.pow(x1 - x2, 2.0) + (Math.pow(y1 - y2, 2.0))), 0.5);
    }

    private static int floorToInt(String floor) {
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
