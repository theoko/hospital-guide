package map;

import models.map.Location;
import models.map.SubPath;

import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

public class AStar extends PathFinder {

    public AStar(Location start, Location end) {
        super(start, end);
    }

    @Override
    public Stack<Location> findPath() {
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
}
