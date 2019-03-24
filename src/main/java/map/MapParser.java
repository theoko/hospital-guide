package map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MapParser {

    public static Map parse(String pathNodes, String pathEdges) {
        HashMap<String, Location> lstLocations = parseNodes(pathNodes);
        HashMap<String, Path> lstPaths = parseEdges(pathEdges, lstLocations);
        Map map = new Map(lstLocations, lstPaths);
        return map;
    }

    /**
     * Parses the nodes from a data file
     * @param pathNodes File path for node data file
     */
    private static HashMap<String, Location> parseNodes(String pathNodes) {
        // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName

        HashMap<String, Location> lstLocations = new HashMap<String, Location>();

        BufferedReader br = null;
        String line;
        String splitter = ",";

        try {
            br = new BufferedReader(new FileReader(pathNodes));
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] splitLine = line.split(splitter);
                String locID = splitLine[0];
                Location newLoc = new Location(locID, Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]), splitLine[3], splitLine[4], splitLine[5], splitLine[6], splitLine[7]);
                lstLocations.put(locID, newLoc);
                System.out.println(locID + " parsed.");
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
        return lstLocations;
    }

    /**
     * Parses the edges from a data file and connects nodes
     * @param pathEdges File path for edge data file
     * @param lstLocations List of nodes/locations
     * @return
     */
    private static HashMap<String, Path> parseEdges(String pathEdges, HashMap<String, Location> lstLocations) {
        // edgeID,startNode,endNode

        HashMap<String, Path> lstPath = new HashMap<String, Path>();

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

                Path newPath = new Path(pathID, start, end);
                lstPath.put(pathID, newPath);
                System.out.println(pathID + " parsed.");

                start.addNeighbor(end);
                end.addNeighbor(start);
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
        return lstPath;
    }
}
