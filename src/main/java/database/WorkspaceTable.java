package database;

import helpers.Constants;
import helpers.DatabaseHelpers;
import models.map.Location;
import models.map.Workspace;
import models.room.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class WorkspaceTable {

    private static void WorkspaceTable() {}

    public static void createtable(){
        Statement statement = null;
        try {
            statement = Database.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String workspaceTable = "CREATE TABLE " + Constants.WORKSPACE_TABLE +
                "(nodeID VARCHAR(100) PRIMARY KEY," +
                "xCoord INT," +
                "yCoord INT," +
                "nodeType VARCHAR(100)," +
                "longName VARCHAR(100))";
        try {
            statement.execute(workspaceTable);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static boolean dropWorkspaceTable() {
        try {
            Statement statement;

            statement = Database.getConnection().createStatement();

            return statement.execute("DROP TABLE " + Constants.WORKSPACE_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.WORKSPACE_TABLE + " cannot be dropped");

            return false;
        }
    }

    /**
     * @param id Location ID.
     * @brief Returns location from database corresponding to given ID.
     */
    public static Workspace getWorkspaceByID(String id) {
        try {
            // Execute query
            String stmtString = "SELECT * FROM " + Constants.WORKSPACE_TABLE + " WHERE NODEID=?";
            PreparedStatement statement = Database.getConnection().prepareStatement(stmtString);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            // Process and return result
            if (resultSet.next()) {
                Workspace workspace = new Workspace(
                        resultSet.getString("NODEID"),
                        resultSet.getInt("XCOORD"),
                        resultSet.getInt("YCOORD"),
                        Constants.NodeType.valueOf(resultSet.getString("NODETYPE")),
                        resultSet.getString("LONGNAME")
                );
                return workspace;
            }
            return null;

        } catch (SQLException exception) {
            System.out.println("Cannot get location by ID: " + id);
            exception.printStackTrace();
            System.out.println();
            return null;
        }
    }

    public static boolean addWorkspace(Workspace workspace) {

        try {

            PreparedStatement statement;

            statement = Database.getConnection().prepareStatement(
                    "INSERT INTO " + Constants.WORKSPACE_TABLE + " (NODEID, XCOORD, YCOORD, NODETYPE, LONGNAME)" +
                            " VALUES (?, ?, ?, ?, ?)"
            );

            statement.setString(1, workspace.getNodeID());
            statement.setInt(2, workspace.getxCord());
            statement.setInt(3, workspace.getyCord());
            statement.setString(4, String.valueOf(DatabaseHelpers.enumToString(workspace.getNodeType())));
            statement.setString(5, workspace.getLongName());

            statement.execute();

         /*   if (Objects.equals(DatabaseHelpers.enumToString(workspace.getNodeType()), Constants.NodeType.CONF.name())) {

                // Populate conference room table
                RoomTable.addRoom(new Room(
                        workspace.getNodeID(),
                        5
                ));
            }*/

            return true;

        } catch (SQLException e) {
            System.out.println("Workspace " + workspace.getNodeID() + " cannot be added!");
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
    public static HashMap<String, Workspace> getWorkspaces() {
        try {

            Statement statement;

            statement = Database.getConnection().createStatement();

            String query = "SELECT * FROM " + Constants.WORKSPACE_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            HashMap<String, Workspace> returnList = new HashMap<>();

            while (resultSet.next()) {

                String nodeID = resultSet.getString("NODEID");
                Workspace node = new Workspace(
                        nodeID,
                        resultSet.getInt("XCOORD"),
                        resultSet.getInt("YCOORD"),
                        DatabaseHelpers.stringToEnum(resultSet.getString("NODETYPE")),
                        resultSet.getString("LONGNAME")
                );
                returnList.put(nodeID, node);
            }
            return returnList;
        } catch (SQLException e) {
            System.out.println("Failed to get workspaces!");

            return null;
        }
    }

    /**
     * Updates the location object specified on the database
     *
     * @param updatedWorkspace
     * @return true if the location was updated successfully, false otherwise
     */
    public static boolean updateWorkspace(Workspace updatedWorkspace) {

        try {
            PreparedStatement statement;

            statement = Database.getConnection().prepareStatement(
                    "UPDATE " + Constants.WORKSPACE_TABLE +
                            " SET XCOORD=?, YCOORD=?, NODETYPE=?, LONGNAME=?" +
                            " WHERE NODEID=?"
            );

            statement.setInt(1, updatedWorkspace.getxCord());
            statement.setInt(2, updatedWorkspace.getyCord());
            statement.setString(3, String.valueOf(DatabaseHelpers.enumToString(updatedWorkspace.getNodeType())));
            statement.setString(4, updatedWorkspace.getLongName());
            statement.setString(5, updatedWorkspace.getNodeID());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("Failed to update workspace: " + updatedWorkspace.getNodeID());
            e.printStackTrace();

            return false;
        }
    }

    /**
     * Deletes the location object specified on the database
     *
     * @param deleteWorkspace
     * @return true if the location was deleted successfully, false otherwise
     */
    public static boolean deleteWorkspace(Workspace deleteWorkspace) {

        try {
            // Add location to deleted locations table

            PreparedStatement statement3;
            statement3 = Database.getConnection().prepareStatement(
                    "DELETE FROM " + Constants.WORKSPACE_TABLE +
                            " WHERE NODEID=?"
            );

            statement3.setString(1, deleteWorkspace.getNodeID());

            return statement3.execute();

        } catch (SQLException e) {
            System.out.println("Failed to delete location: " + deleteWorkspace.getNodeID());
            e.printStackTrace();

            return false;
        }
    }
}
