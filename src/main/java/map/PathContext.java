package map;

import models.map.Location;

import java.util.Stack;

public class PathContext {

    private PathFinder pathFinder;

    public PathContext(PathFinder pathFinder) {
        this.pathFinder = pathFinder;
    }

    public Stack<Location> findPath() {
        return pathFinder.findPath();
    }

    public String txtDirections(Stack<Location> path) {
        return pathFinder.txtDirections(path);
    }
}
