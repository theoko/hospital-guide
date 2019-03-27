package map;

import database.Database;
import helpers.Constants;
import models.map.Edge;
import models.map.Location;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class CSVParser {

    public static void main(String[] args) {
        Database db = new Database();
        parse("/data/nodes.csv", "/data/edges.csv");
    }

    public static void parse(String pathNodes, String pathEdges) {
        HashMap<String, Location> lstLocations = parseNodes(strToPath(pathNodes));
        parseEdges(strToPath(pathEdges), lstLocations);
    }

    private static HashMap<String, Location> parseNodes(String pathNodes) {
        // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName

        HashMap<String, Location> lstLocations = new HashMap<>();

        // Declares a new file reader
        BufferedReader br = null;
        String line;
        String splitter = ","; // CSV default seperator

        try {
            br = new BufferedReader(new FileReader(pathNodes)); // Creates a new file reader
            br.readLine(); // Ignores first line
            // Loop until EOF
            while ((line = br.readLine()) != null) {
                // Split line
                String[] splitLine = line.split(splitter);
                String locID = splitLine[0];
                // Create a new map with the given fields
                Location newLoc = new Location(locID, Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]), splitLine[3], splitLine[4], Constants.NodeType.valueOf(splitLine[5]), splitLine[6], splitLine[7]);

                lstLocations.put(locID, newLoc);
                Database.addLocation(newLoc);

                System.out.println(locID + " parsed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally { // Close file reader
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lstLocations;
    }

    private static void parseEdges(String pathEdges, HashMap<String, Location> lstLocations) {
        // edgeID,startNode,endNode

        BufferedReader br = null;
        String line;
        String splitter = ",";

        try {
            br = new BufferedReader(new FileReader(pathEdges));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(splitter);
                String edgeID = splitLine[0];
                Location start = lstLocations.get(splitLine[1]);
                Location end = lstLocations.get(splitLine[2]);
                Edge newEdge = new Edge(edgeID, start, end);

                Database.addEdge(newEdge);

                System.out.println(edgeID + " parsed.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static String strToPath(String path) {
        return MapParser.class.getResource(path).getFile().replaceAll("%20", " ");
    }
}
