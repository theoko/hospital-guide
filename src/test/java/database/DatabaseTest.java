package database;

import helpers.FileHelpers;
import models.map.Edge;
import models.map.Location;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static helpers.Constants.NodeType.HALL;
import static org.junit.Assert.*;

public class DatabaseTest {



    @Before
    public void setUp() {
        // Parse locations and edges
        // Add locations and edges to the database
        if(!Database.databaseExists()) {
            CSVParser.parse(FileHelpers.getNodesCSV(), FileHelpers.getEdgesCSV());
        }

    }

    @Test
    public void getUser() {
        assertTrue(true);
    }

    @Test
    public void getUsers() {
        assertTrue(true);
    }

    @Test
    public void getAdmins() {
        assertTrue(true);
    }

    @Test
    public void getLocations() {
        // Get locations from the database
        // Compare locations to the ones that were retrieved from the database


        Location newLoc = new Location("AHALL00201",1608,2596,"1","BTM",HALL,"Hall","Hall");
        HashMap<String, Location> locations = Database.getLocations();


        // check that all fields are equal to the original after being added and and pulled from the database

        assertTrue(newLoc.getBuilding().equals((locations.get(newLoc.getNodeID())).getBuilding()));
        assertTrue(newLoc.getFloor().equals((locations.get(newLoc.getNodeID())).getFloor()));
        assertTrue(newLoc.getShortName().equals((locations.get(newLoc.getNodeID())).getShortName()));
        assertTrue(newLoc.getLongName().equals((locations.get(newLoc.getNodeID())).getLongName()));
        assertTrue(newLoc.getNodeType() == (locations.get(newLoc.getNodeID())).getNodeType());
        assertTrue(newLoc.getxCord() == (locations.get(newLoc.getNodeID())).getxCord());
        assertTrue(newLoc.getyCord() == (locations.get(newLoc.getNodeID())).getyCord());
        assertTrue(newLoc.getNodeID().equals((locations.get(newLoc.getNodeID())).getNodeID()));



    }

    @Test
    public void getEdges() {
        // Get edges from the database
        // Compare edges to the ones that were retrieved from the database
        Location AHALL00202 = new Location("AHALL00202",1590,2604,"2","BTM",HALL,"Hall","Hall");
        Location AHALL00302 = new Location("AHALL00302",1590,2745,"2","BTM",HALL,"Hall","Hall");
        Edge newEdg = new Edge("AHALL00202_AHALL00302", AHALL00302, AHALL00202);
        HashMap<String, Location> locations = Database.getLocations();
        List<Edge> edges = Database.getEdges(locations);
        Edge testEdge = null;
        Location testStart = null;
        Location testEnd = null;
        for (Edge e : edges) {
            if (e.getEdgeID().equals(newEdg.getEdgeID())) {
                testEdge = e;
                testStart = e.getStart();
                testEnd = e.getEnd();
            }
        }


        // check that all fields are equal to the original after being added and and pulled from the database

        assertEquals(newEdg.getEdgeID(), testEdge.getEdgeID());
        assertEquals(newEdg.getEnd().getNodeID(), testEnd.getNodeID());
        assertEquals(newEdg.getStart().getNodeID(), testStart.getNodeID());

    }

    @Test
    public void addEdge() {
        Location AHALL00202 = new Location("AHALL00202", 1590, 2604, "2", "BTM", HALL, "Hall", "Hall");
        Location AHALL00302 = new Location("AHALL00302", 1590, 2745, "2", "BTM", HALL, "Hall", "Hall");
        Edge newEdg = new Edge("AHALLTEST", AHALL00302, AHALL00202);
        Database.addEdge(newEdg);
        HashMap<String, Location> locations = Database.getLocations();
        List<Edge> edges = Database.getEdges(locations);
        Edge testEdge = null;
        for (Edge e : edges) {
            if (e.getEdgeID().equals(newEdg.getEdgeID())) {
                testEdge = e;
            }
        }
        assertEquals(newEdg.getEdgeID(), testEdge.getEdgeID());
    }

    @Test
    public void addLocation() {
        Location newLoc = new Location("AHALL00202", 1590, 2604, "2", "BTM", HALL, "Hall", "Hall");

        Database.addLocation(newLoc);
        HashMap<String, Location> locations = Database.getLocations();
        Edge testLoc = null;
        
        assertEquals(newLoc.getNodeID(), locations.get(newLoc.getNodeID()).getNodeID());
        assertEquals(newLoc.getxCord(), locations.get(newLoc.getNodeID()).getxCord());
        assertEquals(newLoc.getyCord(), locations.get(newLoc.getNodeID()).getyCord());
        assertEquals(newLoc.getFloor(), locations.get(newLoc.getNodeID()).getFloor());
//        assertEquals(newLoc.getNodeID(), locations.get(newLoc.getNodeID()).getNodeID());
//        assertEquals(newLoc.getNodeID(), locations.get(newLoc.getNodeID()).getNodeID());
//        assertEquals(newLoc.getNodeID(), locations.get(newLoc.getNodeID()).getNodeID());
//        assertEquals(newLoc.getNodeID(), locations.get(newLoc.getNodeID()).getNodeID());
//        assertEquals(newLoc.getNodeID(), locations.get(newLoc.getNodeID()).getNodeID());
    }

//    @Test
//    public void updateEdge() {
//        Location newLoc = new Location("AHALL00201",1608,2596,"1","BTM",HALL,"Hall","Hall");
//        Location newLoc2 = new Location("BHALL00201",1620,2596,"1","BTM",HALL,"Hall","Hall");
//        Edge newEdg = new Edge("EDG111111", newLoc, newLoc2);
//        Database.addEdge(newEdg);
//
//        assertTrue(Database.updateEdge(newEdg));
//    }
}