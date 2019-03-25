package map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class MapParser {

    public static Map parse(String pathNodes, String pathEdges) {
        HashMap<String, Location> lstLocations = parseNodes(pathNodes);
        parseEdges(pathEdges, lstLocations);
        Map map = new Map(lstLocations);
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
                Location newLoc = new Location(locID, Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]), splitLine[3], splitLine[4], createNode(splitLine[5]), splitLine[6], splitLine[7]);
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

                double dist = PathFinder.calcDist(start.getxCord(), start.getyCord(), end.getxCord(), end.getyCord());
                System.out.println("Start Location: (" + start.getxCord() + ", " + start.getyCord() + ")");
                System.out.println("End Location: (" + end.getxCord() + ", " + end.getyCord() + ")");
                System.out.println("Distance: " + dist);

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

    //TODO: Complete switch statement
    private static NodeType createNode(String nodeType) {
        switch (nodeType) {
            case "BATH":
                return NodeType.BATH;
            default:
                return NodeType.STAI;
        }
    }
}
