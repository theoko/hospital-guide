package database;

import helpers.Constants;
import models.map.Location;

import java.sql.*;
import java.util.*;


public class Database {

    private static String newPrefixChar = "X";
    private static Connection connection;

    static {

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.gc();

        connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:derby:" + Constants.DB_NAME + ";create=true");
//            dropTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(!Database.databaseExists()) {
            createTables();
        }
    }

    /**
     * Drops all database tables
     */
    public static void dropTables() {
        SanitationTable.dropSanitationTable();
        BookTable.dropBookTable();
        RoomTable.dropRoomTable();
        EdgeTable.dropEdgeTable();
        LocationTable.dropLocationTable();
        UserTable.dropUsersTable();
    }

    /**
     * Creates the basic database tables
     */
    private static void createTables() {
        UserTable.createTable();
        LocationTable.createtable();
        EdgeTable.createTable();
        RoomTable.createTable();
        BookTable.createTable();
        SanitationTable.createTable();

    }

    public static Connection getConnection() {
        return connection;
    }

    public static void setConnection(Connection connection) {
        Database.connection = connection;
    }

    /**
     * Checks if the database exists locally
     * @return true if the database exists, false otherwise
     */
    public static boolean databaseExists() {

        boolean exists;

        HashMap<String, Location> locations = LocationTable.getLocations();

        if(locations != null) {
            if(locations.isEmpty()) {
                exists = false;
            } else {
                exists = true;
            }
        } else {
            exists = false;
        }


        return exists;
    }

    public static String generateUniqueNodeID(Location c) {

        String id = newPrefixChar + c.getNodeType().toString() + "000" +
                c.getDBFormattedFloor();
        while(LocationTable.getLocations().containsKey(id)) {
            String numericalIDStr = id.substring(id.length() - 5, id.length() - 2);
            int numericalIDVal = Integer.parseInt(numericalIDStr);
            numericalIDVal++;
            numericalIDStr = String.format("%03d", numericalIDVal);
            id = newPrefixChar + c.getNodeType().toString() + numericalIDStr +
                    c.getDBFormattedFloor();
        }
        return id;
    }
}
