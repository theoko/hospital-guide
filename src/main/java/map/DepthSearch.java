package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class DepthSearch extends PathFinder {

    @Override
    public Stack<Location> findPath(Location start, Location end) {
        Stack<Location> path = new Stack<>();
        Stack<SubPath> stack = new Stack<>();
        HashMap<String, SubPath> used = new HashMap<>();

        SubPath sStart = new SubPath("", start, 0.0);
        stack.push(sStart);

        while (!stack.isEmpty()) {
            SubPath sNext = stack.pop();
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
                    stack.push(nCurr);
                }
            }
        }
        return path;
    }

    @Override
    public MapHelpers.Algorithm getAlg() {
        return MapHelpers.Algorithm.DFS;
    }
}
