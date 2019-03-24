package map;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class PathFinder {

    public static void main(String[] args) {
        // Create map
        Map map = MapParser.parse(PathFinder.class.getResource("/data/nodes.csv").getFile(), PathFinder.class.getResource("/data/edges.csv").getFile());
        Location start = map.getLocation("");
        Location end = map.getLocation("");
        List<Neighbor> path = findPath(map, start, end);
    }

    private static List<Neighbor> findPath(Map map, Location start, Location end) {
        List<Neighbor> path = new LinkedList<Neighbor>();

        PriorityQueue<Neighbor> inQueue = new PriorityQueue<Neighbor>();
        HashMap<String, Neighbor> used = new HashMap<String, Neighbor>();

        Neighbor sNeigh = new Neighbor("", start, 0.0);
        inQueue.add(sNeigh);

        while (!inQueue.isEmpty()) {
            Neighbor nNext = inQueue.poll();
            Location lNext = nNext.getLocation();

            if (lNext.getNodeID().equals(end.getNodeID())) {
                break;
            }
            used.put(lNext.getNodeID(), nNext);

            List<Neighbor> lstNeighbors = lNext.getNeighbors();
            for (Neighbor nCurr : lstNeighbors) {
                Location lCurr = nCurr.getLocation();
                if (!used.containsKey(lCurr.getNodeID())) {
                    inQueue.add(nCurr); //TODO: Adding distances
                }
            }

        }

        return path;
    }
}
