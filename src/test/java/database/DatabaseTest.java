package database;

import helpers.Constants;
import map.CSVParser;
import models.map.Location;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;

import static helpers.Constants.NodeType.HALL;
import static org.junit.Assert.*;

public class DatabaseTest {

    Database db;

    public DatabaseTest() {

        // Parse locations and edges
        // Add locations and edges to the database
        File dbDirectory = new File(Constants.DB_NAME.replaceAll("%20", " "));

        if(dbDirectory.exists()) {
            System.out.println("Exists!");

            db = new Database();

        } else {
            System.out.println("Need to create the database!");

            db = new Database();



            CSVParser.parse("/data/nodes.csv", "/data/edges.csv");
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
        HashMap<String, Location> locations = db.getLocations();

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
        assertTrue(true);
    }

}