package map;

import models.map.Location;
import models.map.SubPath;

import java.util.*;

public class BreadthSearch extends PathFinder {

    public BreadthSearch(Location start, Location end) {
        super(start, end);
    }

    @Override
    public Stack<Location> findPath() {
        Stack<Location> path = new Stack<>();

        Queue<SubPath> queue = new LinkedList<>();
        HashMap<String, SubPath> parent = new HashMap<>();
        HashMap<String, SubPath> used = new HashMap<>();

        SubPath sStart = new SubPath("", start, 0.0);
        queue.add(sStart);
        parent.put(start.getNodeID(), null);

        while (!queue.isEmpty()) {
            SubPath sNext = queue.poll();
            Location lNext = sNext.getLocation();
            if (used.containsKey(lNext.getNodeID())) {
                continue;
            }

            if (lNext.getNodeID().equals(end.getNodeID())) {
                path = genPath(parent, sNext);
                break;
            }

            used.put(lNext.getNodeID(), sNext);

            List<SubPath> lstNeighbors = lNext.getSubPaths();
            for (SubPath nCurr : lstNeighbors) {
                Location lCurr = nCurr.getLocation();
                if (!used.containsKey(lCurr.getNodeID())) {
                    queue.add(nCurr);
                    parent.putIfAbsent(lCurr.getNodeID(), sNext);
                }
            }
        }
        return path;
    }
}
