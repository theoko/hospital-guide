package database;

import com.opencsv.CSVWriter;
import helpers.Constants;
import helpers.FileHelpers;
import map.MapParser;
import models.map.Edge;
import models.map.Location;
import models.map.Workspace;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class CSVParser {

    public static void main(String[] args) throws IOException {
        if(!Database.getDatabase().databaseExists()) {
            parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV(), FileHelpers.getWorkspacesCSV());
            System.out.println("Parsed");
        } else {
            System.out.println("Already here.");
        }


//        File data = new File("data");
//        data.mkdir();

//        if(FileHelpers.checkJar()) {
            export("eNodes.csv", "eEdges.csv");
            export("eWorkspaces.csv");

//        } else {
//            export("data/eNodes.csv", "data/eEdges.csv");
//        }
    }

    public static void parse(InputStream pathNodes, InputStream pathEdges, InputStream pathWorkspaces) {
        HashMap<String, Location> lstLocations = parseNodes(pathNodes);
        parseEdges(pathEdges, lstLocations);
        HashMap<String, Workspace> lstWorkspaces = parseWorkspaces(pathWorkspaces);
    }

    public static void export(String pathNodes, String pathEdges) throws IOException {

//        if(FileHelpers.checkJar()) {
            HashMap<String, Location> lstLocations = exportNodes(pathNodes);
            exportEdges(pathEdges, lstLocations);
//        } else {
//            HashMap<String, Location> lstLocations = exportNodes(strToPath(pathNodes));
//            exportEdges(strToPath(pathEdges), lstLocations);
//        }

    }

    public static void export(String pathWorkspaces) throws IOException {
        HashMap<String, Workspace> lstWorspaces = exportWorkspaces(pathWorkspaces);
    }

    private static HashMap<String, Location> parseNodes(InputStream pathNodes) {
        // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName

        HashMap<String, Location> lstLocations = new HashMap<>();

        // Declares a new file reader
        BufferedReader br = null;
        String line;
        String splitter = ","; // CSV default seperator

        try {
            br = new BufferedReader(new InputStreamReader(pathNodes)); // Creates a new file reader
            br.readLine(); // Ignores first line
            // Loop until EOF
            while ((line = br.readLine()) != null) {
                // Split line
                String[] splitLine = line.split(splitter);
                String locID = splitLine[0];
                // Create a new map with the given fields
                Location newLoc = new Location(locID, Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]), splitLine[3], splitLine[4], Constants.NodeType.valueOf(splitLine[5]), splitLine[6], splitLine[7]);

                lstLocations.put(locID, newLoc);
                LocationTable.addLocation(newLoc);
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

    private static void parseEdges(InputStream pathEdges, HashMap<String, Location> lstLocations) {
        // edgeID,startNode,endNode

        BufferedReader br = null;
        String line;
        String splitter = ",";

        try {
            br = new BufferedReader(new InputStreamReader(pathEdges));
            br.readLine();
            while ((line = br.readLine()) != null) {

                String[] splitLine = line.split(splitter);
                String edgeID = splitLine[0];
                Location start = lstLocations.get(splitLine[1]);
                Location end = lstLocations.get(splitLine[2]);
                Edge newEdge = new Edge(edgeID, start, end);

                if(start != null && end != null)
                    EdgeTable.addEdge(newEdge);
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

    private static HashMap<String, Workspace> parseWorkspaces(InputStream pathWorkspaces) {
        // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName

        HashMap<String, Workspace> lstWorkspaces = new HashMap<>();

        // Declares a new file reader
        BufferedReader br = null;
        String line;
        String splitter = ","; // CSV default seperator

        try {
            br = new BufferedReader(new InputStreamReader(pathWorkspaces)); // Creates a new file reader
            br.readLine(); // Ignores first line
            // Loop until EOF
            while ((line = br.readLine()) != null) {
                // Split line
                String[] splitLine = line.split(splitter);
                String wsID = splitLine[0];
                // Create a new map with the given fields
                Workspace newWS = new Workspace(wsID, Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]), Constants.NodeType.valueOf(splitLine[3]), splitLine[4]);

                lstWorkspaces.put(wsID, newWS);
                WorkspaceTable.addWorkspace(newWS);
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
        return lstWorkspaces;
    }

    private static HashMap<String, Location> exportNodes(String pathNodes) throws IOException {
        // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName

        HashMap<String, Location> lstLocations = new HashMap<>();

        File csvFile = new File(pathNodes);

        FileHelpers.recreateFile(csvFile);

        try {
            FileWriter oFile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(oFile);
            String[] header = {"nodeID", "xcoord", "ycoord", "floor", "building", "nodeType", "longName", "shortName"};
            writer.writeNext(header);

            lstLocations = LocationTable.getLocations();
            if (lstLocations != null) {
                for (Location loc : lstLocations.values()) {
                    String[] data = loc.getStringsLocation();
                    writer.writeNext(data);
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lstLocations;
    }

    private static void exportEdges(String pathEdges, HashMap<String, Location> lstLocations) throws IOException {
        // edgeID,startNode,endNode

        File csvFile = new File(pathEdges);

        FileHelpers.recreateFile(csvFile);

        try {
            FileWriter oFile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(oFile);
            String[] header = {"edgeID", "startNode", "endNode"};
            writer.writeNext(header);

            List<Edge> lstEdges = EdgeTable.getEdges(lstLocations);
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

    private static HashMap<String, Workspace> exportWorkspaces(String pathWorkspaces) throws IOException {
        // nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName

        HashMap<String, Workspace> lstWorkspaces = new HashMap<>();
        File csvFile = new File(pathWorkspaces);

        FileHelpers.recreateFile(csvFile);

        try {
            FileWriter oFile = new FileWriter(csvFile);
            CSVWriter writer = new CSVWriter(oFile);
            String[] header = {"nodeID", "xcoord", "ycoord", "nodeType", "Name"};
            writer.writeNext(header);

            lstWorkspaces = WorkspaceTable.getWorkspaces();
            if (lstWorkspaces != null) {
                for (Workspace ws : lstWorkspaces.values()) {
                    String[] data = ws.getStringsLocation();
                    writer.writeNext(data);
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lstWorkspaces;
    }

    private static String strToPath(String path) {
        return MapParser.class.getResource(path).getFile().replaceAll("%20", " ");
    }
}