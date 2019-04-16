package models.map;

import java.util.HashMap;

public class Map {
    private HashMap<String, Location> lstLocations;
    private HashMap<String, Edge> lstEdges;

    public Map(HashMap<String, Location> lstLocations, HashMap<String, Edge> lstEdges) {
        this.lstLocations = lstLocations;
        this.lstEdges = lstEdges;
    }

    public Location getLocation(String nodeID) {
        return lstLocations.get(nodeID);
    }

    public HashMap<String, Location> getAllLocations() {
        return lstLocations;
    }

    public Edge getEdge(String edgeID) {
        return lstEdges.get(edgeID);
    }

    public HashMap<String, Edge> getAllEdges() {
        return lstEdges;
    }

    public void addLocation(String locID, Location loc) {
        lstLocations.put(locID, loc);
    }
}
