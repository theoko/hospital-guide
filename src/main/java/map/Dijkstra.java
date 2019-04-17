package map;

import helpers.MapHelpers;
import models.map.Location;
import models.map.SubPath;

import java.util.PriorityQueue;

public class Dijkstra extends PathFinder {
    private PriorityQueue<SubPath> queue;

    @Override
    protected void setUp(Location start) {
        queue = new PriorityQueue<>();
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
        return loc1.getDist() + loc2.getDist();
    }

    @Override
    protected double getHeuristic(Location loc1, Location loc2) {
        return 0;
    }

    @Override
    public MapHelpers.Algorithm getAlg() {
        return MapHelpers.Algorithm.DIJKSTRA;
    }
}
