package database;

import helpers.Constants;
import models.map.Location;
import models.room.Book;

import java.sql.*;
import java.util.*;


public class Database {

    private static String newPrefixChar = "X";
    private static Connection connection;

    private Database(){}

    private static Database makeDatabase;

    private static class DatabaseHelper{
        private static final Database makeDatabase = new Database();
    }

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

        if(!Database.getDatabase().databaseExists()) {
            createTables();
        }
    }

    public static Database getDatabase(){
        return DatabaseHelper.makeDatabase;
    }

    /**
     * Drops all database tables
     */
    public static void dropTables() {
        SanitationTable.dropTable();
        BookLocationTable.dropTable();
        RoomTable.dropTable();
        EdgeTable.dropTable();
        LocationTable.dropTable();
        UserTable.dropTable();
    }

    /**
     * Creates the basic database tables
     */
    private static void createTables() {
        UserTable.createTable();
        LocationTable.createtable();
        EdgeTable.createTable();
        RoomTable.createTable();
        BookLocationTable.createTable();
        SanitationTable.createTable();
        TransportationTable.createTable();
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        Database.connection = connection;
    }

    /**
     * Checks if the database exists locally
     * @return true if the database exists, false otherwise
     */
    public boolean databaseExists() {

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

    public String getNewPrefixChar() {
        return newPrefixChar;
    }

    public static void setNewPrefixChar(String newPrefixChar) {
        Database.newPrefixChar = newPrefixChar;
    }
}
