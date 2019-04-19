package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class DepthSearch extends PathFinder {
    private Stack<SubPath> stack;

    @Override
    protected void setUp(Location start) {
        stack = new Stack<>();
        SubPath sStart = new SubPath("", start, 0.0);
        stack.push(sStart);
    }

    @Override
    protected boolean isEmpty() {
        return stack.isEmpty();
    }

    @Override
    protected SubPath getNext() {
        return stack.pop();
    }

    @Override
    protected void addNext(SubPath next) {
        stack.add(next);
    }

    @Override
    protected double getDist(SubPath loc1, SubPath loc2) {
        return 0;
    }

    @Override
    protected double getHeuristic(Location loc1, Location loc2) {
        return 0;
    }

    @Override
    public MapHelpers.Algorithm getAlg() {
        return MapHelpers.Algorithm.DFS;
    }
}
