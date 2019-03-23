package map;

import java.util.HashMap;

public class Map {
    private HashMap<String, Location> lstLocations;
    private HashMap<String, Path> lstPaths;

    public Map(HashMap<String, Location> lstLocations, HashMap<String, Path> lstPaths) {
        this.lstLocations = lstLocations;
        this.lstPaths = lstPaths;
    }
}
