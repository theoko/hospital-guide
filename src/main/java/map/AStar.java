package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.*;

public class AStar extends PathFinder {
    private PriorityQueue<SubPath> inQueue;

    @Override
    protected void setUp(Location start) {
        inQueue = new PriorityQueue<>();
        SubPath sNeigh = new SubPath("", start, 0.0);
        inQueue.add(sNeigh);
    }

    @Override
    protected boolean isEmpty() {
        return inQueue.isEmpty();
    }

    @Override
    protected SubPath getNext() {
        return inQueue.poll();
    }

    @Override
    protected void addNext(SubPath next) {
        inQueue.add(next);
    }

    @Override
    protected double getDist(SubPath loc1, SubPath loc2) {
        return loc1.getDist() + loc2.getDist();
    }

    @Override
    protected double getHeuristic(Location loc1, Location loc2) {
        double heuristic = calcDist(loc1.getxCord(), loc1.getyCord(), loc2.getxCord(), loc2.getyCord());
        heuristic += FLOOR_HEURISTIC * Math.abs(floorToInt(loc1.getFloor()) - floorToInt(loc2.getFloor()));
        return heuristic;
    }

    /*@Override
    public Stack<Location> findPath(Location start, Location end) {
        // Create a new stack to hold the path from start to end
        Stack<Location> path = new Stack<>();

        // Create priority queue and hashmaps
        PriorityQueue<SubPath> inQueue = new PriorityQueue<>();
        HashMap<String, SubPath> used = new HashMap<>();

        // Initialize values
        SubPath sNeigh = new SubPath("", start, 0.0);
        inQueue.add(sNeigh);

        // Loop while queue isn't empty or end map is found
        while (!inQueue.isEmpty()) {
            // Poll next neighbor off the queue and get its map
            SubPath sNext = inQueue.poll();
            Location lNext = sNext.getLocation();
            if (used.containsKey(lNext.getNodeID())) {
                continue;
            }

            // Check to see if map is our end map
            if (lNext.getNodeID().equals(end.getNodeID())) {
                // Generate path from parent map and end node
                path = genPath(sNext);
                break;
            }

            // Get the node's value and add it to the used map
            double currDist = sNext.getDist();
            used.put(lNext.getNodeID(), sNext);

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
                    newNeigh.setParent(sNext);
                }
            }
        }
        return path;
    }*/

    @Override
    public MapHelpers.Algorithm getAlg() {
        return MapHelpers.Algorithm.ASTAR;
    }
}
