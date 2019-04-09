package map;

import helpers.MapHelpers;
import models.map.Location;

import java.util.Stack;

public class PathContext {

    private PathFinder pathFinder;

    public PathContext(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public Stack<Location> findPath(Location start, Location end) {
        return pathFinder.findPath(start, end);
    }

    public String txtDirections(Stack<Location> path) {
        return pathFinder.txtDirections(path);
    }

    public MapHelpers.Algorithm getAlg() {
        return pathFinder.getAlg();
    }
}
