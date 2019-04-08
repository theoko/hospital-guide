package map;

import database.Database;
import database.EdgeTable;
import database.LocationTable;
import models.map.Edge;
import models.map.Location;
import models.map.Map;
import models.map.SubPath;
import java.util.HashMap;
import java.util.List;

public class MapParser {

    /**
     * Generates a hashmap of unlinked locations
     * @return models.map.Map
     */
    public static Map parse() {
        // Generates a hashmap of unlinked locations
        HashMap<String, Location> lstLocations = LocationTable.getLocations();
        // Links the locations
        HashMap<String, Edge> lstEdges = parseEdges(lstLocations);
        Map map = new Map(lstLocations, lstEdges);
        return map;
    }

    /**
     * Returns a hashmap of edges
     * @param lstLocations
     * @return
     */
    private static HashMap<String, Edge> parseEdges(HashMap<String, Location> lstLocations) {
        HashMap<String, Edge> mapEdges = new HashMap<>();
        List<Edge> lstEdges = EdgeTable.getEdges(lstLocations);
        for (Edge edge : lstEdges) {
            String edgeID = edge.getEdgeID();
            Location start = lstLocations.get(edge.getStart().getNodeID());
            Location end = lstLocations.get(edge.getEnd().getNodeID());
            double dist = PathFinder.calcDist(start.getxCord(), start.getyCord(), end.getxCord(), end.getyCord());

            start.addSubPath(new SubPath(edgeID, end, dist));
            end.addSubPath(new SubPath(edgeID, start, dist));

            mapEdges.put(edge.getEdgeID(), edge);
        }
        return mapEdges;
    }
}
