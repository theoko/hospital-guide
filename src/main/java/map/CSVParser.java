package map;

import com.opencsv.CSVWriter;
import database.Database;
import helpers.Constants;
import models.map.Edge;
import models.map.Location;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class CSVParser {

    public static void main(String[] args) {
        Database db = new Database();
        parse("/data/nodes.csv", "/data/edges.csv");
        export("/data/eNodes.csv", "/data/eEdges.csv");
    }

    public static void parse(String pathNodes, String pathEdges) {
        HashMap<String, Location> lstLocations = parseNodes(strToPath(pathNodes));
        parseEdges(strToPath(pathEdges), lstLocations);
    }

    public static void export(String pathNodes, String pathEdges) {
        HashMap<String, Location> lstLocations = exportNodes(strToPath(pathNodes));
        exportEdges(strToPath(pathEdges), lstLocations);
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

    private static HashMap<String, Location> exportNodes(String pathNodes) {
        // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName

        HashMap<String, Location> lstLocations = new HashMap<>();
        File csvFile = new File(pathNodes);
//        try {
//            //csvFile.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            FileWriter oFile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(oFile);
            String[] header = {"nodeID", "xcoord", "ycoord", "floor", "building", "nodeType", "longName", "shortName"};
            writer.writeNext(header);

            lstLocations = Database.getLocations();
            if (lstLocations != null) {
                for (Location loc : lstLocations.values()) {
                    String[] data = loc.getStrings();
                    writer.writeNext(data);
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lstLocations;
    }

    private static void exportEdges(String pathEdges, HashMap<String, Location> lstLocations) {
        File csvFile = new File(pathEdges);
        try {
            FileWriter oFile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(oFile);
            String[] header = {""};
            writer.writeNext(header);

            List<Edge> lstEdges = Database.getEdges(lstLocations);
            if (lstEdges != null) {
                for (Edge edge : lstEdges) {
                    String[] data = edge.getStrings();
                    writer.writeNext(data);
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String strToPath(String path) {
        return MapParser.class.getResource(path).getFile().replaceAll("%20", " ");
    }
}
