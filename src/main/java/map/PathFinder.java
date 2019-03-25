package map;

import java.util.*;

public class PathFinder {

    public static void main(String[] args) {
        // Create map
        Map map = MapParser.parse(PathFinder.class.getResource("/data/nodes.csv").getFile(), PathFinder.class.getResource("/data/edges.csv").getFile());
        Location start = map.getLocation("ADEPT00301");
        Location end = map.getLocation("DDEPT00402");

        long startTime = System.currentTimeMillis();
        List<Neighbor> path = findPath(map, start, end);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time taken: " + totalTime);
    }

    private static Stack<Neighbor> findPath(Map map, Location start, Location end) {
        Stack<Neighbor> path = new Stack<>();
        HashMap<String, Neighbor> parent = new HashMap<>();

        PriorityQueue<Neighbor> inQueue = new PriorityQueue<>();
        HashMap<String, Neighbor> used = new HashMap<>();

        Neighbor sNeigh = new Neighbor("", start, 0.0);
        inQueue.add(sNeigh);
        parent.put(start.getNodeID(), null);

        while (!inQueue.isEmpty()) {
            Neighbor nNext = inQueue.poll();
            Location lNext = nNext.getLocation();

            if (lNext.getNodeID().equals(end.getNodeID())) {
                System.out.println("Found node!");
                path = genPath(parent, nNext);
                printPath(path);
                break;
            }
            double currDist = nNext.getDist();
            used.put(lNext.getNodeID(), nNext);

            List<Neighbor> lstNeighbors = lNext.getNeighbors();
            for (Neighbor nCurr : lstNeighbors) {
                Location lCurr = nCurr.getLocation();
                if (!used.containsKey(lCurr.getNodeID())) {
                    double newDist = currDist + nCurr.getDist();
                    double heuristic = calcDist(lCurr.getxCord(), end.getxCord(), lCurr.getyCord(), end.getyCord());
                    nCurr.setDist(newDist + heuristic);
                    inQueue.add(nCurr); //TODO: Adding distances
                    parent.put(lCurr.getNodeID(), nNext);
                }
            }
        }
        return path;
    }

    private static Stack<Neighbor> genPath(HashMap<String, Neighbor> parent, Neighbor end) {
        Stack<Neighbor> path = new Stack<>();
        Neighbor prev = end;
        while (prev != null) {
            path.push(prev);
            prev = parent.get(prev.getLocation().getNodeID());
        }
        return path;
    }

    private static void printPath(Stack<Neighbor> path) {
        for (Neighbor curr : path) {
            System.out.println("Location: " + curr.getLocation().getNodeID());
        }
    }

    public static double calcDist(int x1, int y1, int x2, int y2) {
        return Math.pow((Math.pow(x1 - x2, 2.0) + (Math.pow(y1 - y2, 2.0))), 0.5);
    }
}
