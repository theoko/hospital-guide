package map;

import database.Database;
import helpers.Constants;
import models.map.Edge;
import models.map.Location;
import models.map.Map;
import models.map.SubPath;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MapParser {

    public static Map parse(String pathNodes, String pathEdges) {
        // Generates a hashmap of unlinked locations
        HashMap<String, Location> lstLocations = parseNodes(pathNodes);
        // Links the locations
        HashMap<String, Edge> lstEdges = parseEdges(pathEdges, lstLocations);
        Map map = new Map(lstLocations, lstEdges);
        return map;
    }

    private static HashMap<String, Location> parseNodes(String pathNodes) {
        // New hashmap of map nodes
        HashMap<String, Location> lstLocations = new HashMap<>();

        return lstLocations;
    }

    private static HashMap<String, Edge> parseEdges(String pathEdges, HashMap<String, Location> lstLocations) {
        HashMap<String, Edge> lstEdges = new HashMap<>();

        return lstEdges;
    }
}
