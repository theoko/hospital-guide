package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.*;

public class BreadthSearch extends PathFinder {
    private Queue<SubPath> queue;

    @Override
    protected void setUp(Location start) {
        Queue<SubPath> queue = new LinkedList<>();
        SubPath sStart = new SubPath("", start, 0.0);
        queue.add(sStart);
    }

    @Override
    protected boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    protected SubPath getNext() {
        return queue.poll();
    }

    @Override
    protected void addNext(SubPath next) {
        queue.add(next);
    }

    @Override
    protected double getDist(SubPath loc1, SubPath loc2) {
        return 0;
    }

    @Override
    protected double getHeuristic(Location loc1, Location loc2) {
        return 0;
    }

    /*@Override
    public Stack<Location> findPath(Location start, Location end) {
        Stack<Location> path = new Stack<>();

        Queue<SubPath> queue = new LinkedList<>();
        HashMap<String, SubPath> used = new HashMap<>();

        SubPath sStart = new SubPath("", start, 0.0);
        queue.add(sStart);

        while (!queue.isEmpty()) {
            SubPath sNext = queue.poll();
            Location lNext = sNext.getLocation();
            if (used.containsKey(lNext.getNodeID())) {
                continue;
            }

            if (lNext.getNodeID().equals(end.getNodeID())) {
                path = genPath(sNext);
                break;
            }

            used.put(lNext.getNodeID(), sNext);

            List<SubPath> lstNeighbors = lNext.getSubPaths();
            for (SubPath nCurr : lstNeighbors) {
                Location lCurr = nCurr.getLocation();
                if (!used.containsKey(lCurr.getNodeID())) {
                    nCurr.setParent(sNext);
                    queue.add(nCurr);
                }
            }
        }
        return path;
    }*/

    @Override
    public MapHelpers.Algorithm getAlg() {
        return MapHelpers.Algorithm.BFS;
    }
}
