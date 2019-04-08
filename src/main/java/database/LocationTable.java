package database;

import helpers.Constants;
import helpers.DatabaseHelpers;
import models.map.Edge;
import models.map.Location;
import models.room.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class LocationTable {

    private static void LocationTable() {}

    public static void createtable(){
        Statement statement = null;
        try {
            statement = Database.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String locationTable = "CREATE TABLE " + Constants.LOCATION_TABLE +
                "(nodeID VARCHAR(100) PRIMARY KEY," +
                "xCoord INT," +
                "yCoord INT," +
                "floor VARCHAR(100)," +
                "building VARCHAR(100)," +
                "nodeType VARCHAR(100)," +
                "longName VARCHAR(100)," +
                "shortName VARCHAR(100))";
        try {
            statement.execute(locationTable);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static boolean dropLocationTable() {
        try {
            Statement statement;

            statement = Database.getConnection().createStatement();

            return statement.execute("DROP TABLE " + Constants.LOCATION_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.LOCATION_TABLE + " cannot be dropped");

            return false;
        }
    }

    /**
     * @param id Location ID.
     * @brief Returns location from database corresponding to given ID.
     */
    public static Location getLocationByID(String id) {
        try {
            // Execute query
            String stmtString = "SELECT * FROM " + Constants.LOCATION_TABLE + " WHERE NODEID=?";
            PreparedStatement statement = Database.getConnection().prepareStatement(stmtString);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Process and return result
            if (resultSet.next()) {
                Location location = new Location(
                        resultSet.getString("NODEID"),
                        resultSet.getInt("XCOORD"),
                        resultSet.getInt("YCOORD"),
                        resultSet.getString("FLOOR"),
                        resultSet.getString("BUILDING"),
                        Constants.NodeType.valueOf(resultSet.getString("NODETYPE")),
                        resultSet.getString("LONGNAME"),
                        resultSet.getString("SHORTNAME")
                );
                return location;
            }
            return null;

        } catch (SQLException exception) {
            System.out.println("Cannot get location by ID: " + id);
            exception.printStackTrace();
            System.out.println();
            return null;
        }
    }

    public static boolean addLocation(Location location) {

        try {

            PreparedStatement statement;

            statement = Database.getConnection().prepareStatement(
                    "INSERT INTO " + Constants.LOCATION_TABLE + " (NODEID, XCOORD, YCOORD, FLOOR, BUILDING, NODETYPE, LONGNAME, SHORTNAME ) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            statement.setString(1, location.getNodeID());
            statement.setInt(2, location.getxCord());
            statement.setInt(3, location.getyCord());
            statement.setString(4, location.getFloor());
            statement.setString(5, location.getBuilding());
            statement.setString(6, String.valueOf(DatabaseHelpers.enumToString(location.getNodeType())));
            statement.setString(7, location.getLongName());
            statement.setString(8, location.getShortName());

            statement.execute();

            if (Objects.equals(DatabaseHelpers.enumToString(location.getNodeType()), Constants.NodeType.CONF.name())) {

                // Populate conference room table
                RoomTable.addRoom(new Room(
                        location.getNodeID(),
                        5
                ));
            }

            return true;

        } catch (SQLException e) {
            System.out.println("Location " + location.getNodeID() + " cannot be added!");
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Checks availability for the room specified based on start and end date
     *
     * @param room
     * @param startTime
     * @param endTime
     * @return true if the room is available, false otherwise
     */
    public static boolean checkAvailabilityByLocation(Room room, String startTime, String endTime) {
        PreparedStatement statement;

        try {
            statement = Database.getConnection().prepareStatement(
                    "SELECT * FROM " + Constants.BOOK_TABLE +
                            " WHERE nodeID=? AND ? <=  ENDDATE" +
                            " AND ? >= STARTDATE" +
                            " OR ? >=  STARTDATE" +
                            " AND ? <= ENDDATE"
            );

            statement.setString(1, room.getRoomID());
            statement.setString(2, endTime);
            statement.setString(3, endTime);
            statement.setString(4, startTime);
            statement.setString(5, startTime);

            // Should return 0 rows
            ResultSet resultSet = statement.executeQuery();

            return resultSet.getFetchSize() == 0;

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Checks if location is available
     */
    public static List<Room> checkAvailabilityByTime(String startTime, String endTime) {

        PreparedStatement statement1;
        ArrayList<Room> roomsAvailable = new ArrayList<>();

        try {

            String unavailableRooms = "SELECT nodeID FROM " + Constants.BOOK_TABLE +
                    " WHERE ? <=  ENDDATE" +
                    " AND ? >= STARTDATE" +
                    " OR ? >=  STARTDATE" +
                    " AND ? <= ENDDATE";

            statement1 = Database.getConnection().prepareStatement(
                    "SELECT nodeID FROM " + Constants.ROOM_TABLE +
                            " EXCEPT (" + unavailableRooms + ")"
            );

            statement1.setTimestamp(1, Timestamp.valueOf(endTime));
            statement1.setTimestamp(2, Timestamp.valueOf(endTime));
            statement1.setTimestamp(3, Timestamp.valueOf(startTime));
            statement1.setTimestamp(4, Timestamp.valueOf(startTime));

            ResultSet resultSet = statement1.executeQuery();

            while (resultSet.next()) {
                Room room = RoomTable.getRoomByID(resultSet.getString("NODEID"));

                roomsAvailable.add(room);
            }

            return roomsAvailable;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of nodes
     */
    public static HashMap<String, Location> getLocations() {
        try {

            Statement statement;

            statement = Database.getConnection().createStatement();

            String query = "SELECT * FROM " + Constants.LOCATION_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            HashMap<String, Location> returnList = new HashMap<>();

            while (resultSet.next()) {

                String nodeID = resultSet.getString("NODEID");
                Location node = new Location(
                        nodeID,
                        resultSet.getInt("XCOORD"),
                        resultSet.getInt("YCOORD"),
                        resultSet.getString("FLOOR"),
                        resultSet.getString("BUILDING"),
                        DatabaseHelpers.stringToEnum(resultSet.getString("NODETYPE")),
                        resultSet.getString("LONGNAME"),
                        resultSet.getString("SHORTNAME")
                );
                returnList.put(nodeID, node);
            }
            return returnList;
        } catch (SQLException e) {
            System.out.println("Failed to get locations!");

            return null;
        }
    }

    /**
     * Updates the location object specified on the database
     *
     * @param updatedLocation
     * @return true if the location was updated successfully, false otherwise
     */
    public static boolean updateLocation(Location updatedLocation) {

        try {
            PreparedStatement statement;

            statement = Database.getConnection().prepareStatement(
                    "UPDATE " + Constants.LOCATION_TABLE +
                            " SET XCOORD=?, YCOORD=?, FLOOR=?, BUILDING=?, NODETYPE=?, LONGNAME=?, SHORTNAME=?" +
                            " WHERE NODEID=?"
            );

            statement.setInt(1, updatedLocation.getxCord());
            statement.setInt(2, updatedLocation.getyCord());
            statement.setString(3, updatedLocation.getFloor());
            statement.setString(4, updatedLocation.getBuilding());
            statement.setString(5, String.valueOf(DatabaseHelpers.enumToString(updatedLocation.getNodeType())));
            statement.setString(6, updatedLocation.getLongName());
            statement.setString(7, updatedLocation.getShortName());
            statement.setString(8, updatedLocation.getNodeID());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("Failed to update location: " + updatedLocation.getNodeID());
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Deletes the location object specified on the database
     *
     * @param deleteLocation
     * @return true if the location was deleted successfully, false otherwise
     */
    public static boolean deleteLocation(Location deleteLocation) {

        try {

            // We need to check if a ROOM is to be removed
            // In that case, the room should first be removed from the rooms table
            // since in the constraints defined
            if(deleteLocation.getNodeType() == Constants.NodeType.CONF) {

                System.out.println("Conference room with ID: " + deleteLocation.getNodeID());

                PreparedStatement statement1;
                statement1 = Database.getConnection().prepareStatement(
                        "DELETE FROM " + Constants.ROOM_TABLE +
                                " WHERE NODEID=?"
                );

                statement1.setString(1, deleteLocation.getNodeID());
                statement1.execute();
            }

            PreparedStatement statement2;
            PreparedStatement statement3;

            statement2 = Database.getConnection().prepareStatement(
                    "DELETE FROM " + Constants.EDGES_TABLE +
                            " WHERE STARTNODEID=? OR ENDNODEID=?"
            );

            statement2.setString(1, deleteLocation.getNodeID());
            statement2.setString(2, deleteLocation.getNodeID());

            statement2.execute();

            // Add location to deleted locations table

            statement3 = Database.getConnection().prepareStatement(
                    "DELETE FROM " + Constants.LOCATION_TABLE +
                            " WHERE NODEID=?"
            );

            statement3.setString(1, deleteLocation.getNodeID());

            return statement3.execute();

        } catch (SQLException e) {
            System.out.println("Failed to delete location: " + deleteLocation.getNodeID());
            e.printStackTrace();

            return false;
        }
    }

    public static String uniqueID(Location loc) {
        String locID = generateUniqueNodeID(loc);
        loc.setNodeID(locID);
        LocationTable.addLocation(loc);
        return locID;
    }

    public static String generateUniqueNodeID(Location c) {

        String id = Database.getNewPrefixChar() + c.getNodeType().toString() + "000" +
                c.getDBFormattedFloor();
        while(LocationTable.getLocations().containsKey(id)) {
            String numericalIDStr = id.substring(id.length() - 5, id.length() - 2);
            int numericalIDVal = Integer.parseInt(numericalIDStr);
            numericalIDVal++;
            numericalIDStr = String.format("%03d", numericalIDVal);
            id = Database.getNewPrefixChar() + c.getNodeType().toString() + numericalIDStr +
                    c.getDBFormattedFloor();
        }
        return id;
    }

    public static ArrayList<Edge> getEdgesByID(String locationID) {
        try {

            PreparedStatement statement;

            statement = Database.getConnection().prepareStatement(
                    "SELECT * FROM " + Constants.EDGES_TABLE + " WHERE STARTNODEID=? OR ENDNODEID=?"
            );

            statement.setString(1, locationID);
            statement.setString(2, locationID);

            ResultSet resultSet = statement.executeQuery();

            ArrayList<Edge> returnList = new ArrayList<>();

            while(resultSet.next()) {
                Edge edge = new Edge(
                        resultSet.getString("EDGEID"),
                        getLocationByID(resultSet.getString("STARTNODEID")),
                        getLocationByID(resultSet.getString("ENDNODEID"))
                );

                returnList.add(edge);
            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Cannot get edges of location by locationID!");

            return null;
        }
    }
}
