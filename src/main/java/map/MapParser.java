package map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MapParser {

    /**
     * Parses a graph from the two csv files
     * @param pathNodes File path for the nodes
     * @param pathEdges File path for the edges
     * @return A map of all the nodes linked together
     */
    public static Map parse(String pathNodes, String pathEdges) {
        // Generates a hashmap of unlinked locations
        HashMap<String, Location> lstLocations = parseNodes(strToPath(pathNodes));
        // Links the locations
        parseEdges(strToPath(pathEdges), lstLocations);
        Map map = new Map(lstLocations);
        return map;
    }

    /**
     * Parses the nodes from the data file
     * @param pathNodes File path for nodes data file
     * @return A hashmap of unlinked locations
     */
    private static HashMap<String, Location> parseNodes(String pathNodes) {
        // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName

        // New hashmap of location nodes
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
                // Create a new location with the given fields
                Location newLoc = new Location(locID, Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]), splitLine[3], splitLine[4], createNode(splitLine[5]), splitLine[6], splitLine[7]);
                // Add the location into the map
                lstLocations.put(locID, newLoc);
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

    /**
     * Parses the edges from the data file
     * @param pathEdges File path for the edges data file
     * @param lstLocations List of locations to link together
     */
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
                String pathID = splitLine[0];
                Location start = lstLocations.get(splitLine[1]);
                Location end = lstLocations.get(splitLine[2]);
                System.out.println(pathID + " parsed.");

                // Calculates the distance between two locations
                double dist = PathFinder.calcDist(start.getxCord(), start.getyCord(), end.getxCord(), end.getyCord());
                System.out.println("Start Location: (" + start.getxCord() + ", " + start.getyCord() + ")");
                System.out.println("End Location: (" + end.getxCord() + ", " + end.getyCord() + ")");
                System.out.println("Distance: " + dist);

                // Creates 2 new neighbors that are linked respectively
                Neighbor nEnd = new Neighbor(pathID, end, dist);
                start.addNeighbor(nEnd);
                Neighbor nStart = new Neighbor(pathID, start, dist);
                end.addNeighbor(nStart);
                System.out.println(start.getNodeID() + " and " + end.getNodeID() + " connected.");
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

    /**
     * Creates a NodeType enum from a string
     * @param nodeType String of the type of node
     * @return A NodeType enum of the given type
     */
    private static NodeType createNode(String nodeType) {
        switch (nodeType) {
            case "BATH":
                return NodeType.BATH;
            case "CONF":
                return NodeType.CONF;
            case "DEPT":
                return NodeType.DEPT;
            case "ELEV":
                return NodeType.ELEV;
            case "EXIT":
                return NodeType.EXIT;
            case "HALL":
                return NodeType.HALL;
            case "INFO":
                return NodeType.INFO;
            case "LABS":
                return NodeType.LABS;
            case "REST":
                return NodeType.REST;
            case "RETL":
                return NodeType.RETL;
            case "SERV":
                return NodeType.SERV;
            default:
                return NodeType.STAI;
        }
    }
}
