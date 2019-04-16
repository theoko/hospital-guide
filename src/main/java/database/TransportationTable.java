package database;

import helpers.Constants;
import models.map.Location;
import models.services.TransportationRequest;
import models.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class TransportationTable {

    /**
     * Creates empty transportation table
     */
    public static void createTable() {

        // Create statement
        Statement statement = null;
        try {
            statement = Database.getDatabase().getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Attempt to create table
        try {
            statement.execute(
                    "CREATE TABLE " + Constants.TRANSPORTATION_TABLE + "(" +
                            "requestID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                            "locationID VARCHAR(100) References " + Constants.LOCATION_TABLE + " (nodeID), " +
                            "destinationID VARCHAR(100) References " + Constants.LOCATION_TABLE + "(nodeID), " +
                            "status VARCHAR(100), " +
                            "description VARCHAR(100), " +
                            "requesterID INT REFERENCES " + Constants.USERS_TABLE + "(userID), " +
                            "requestTime TIMESTAMP, " +
                            "servicerID INT REFERENCES " + Constants.USERS_TABLE + "(userID), " +
                            "claimedTime TIMESTAMP, " +
                            "completedTime TIMESTAMP)"
            );
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request Transportation request to add.
     * @return Boolean indicating success of add.
     * @brief Attempts to add NEW transportation request to the database.
     * NOTE: Expects servicer and servicer timestamps to be NULL because it is a NEW request.
     */
    public static boolean addTransportationRequest(TransportationRequest request) {

        // Get data from request
        String locationID = request.getLocation().getNodeID();
        String destinationID = request.getDestination().getNodeID();
        String status = request.getStatus().name();
        String description = request.getDescription();
        int requesterID = request.getRequester().getUserID();
        Timestamp requestTime = request.getRequestTime();
        User requester = request.getRequester();

        try {
            // Attempt to add request to database
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(
                    "INSERT INTO " + Constants.TRANSPORTATION_TABLE +
                            " (locationID, destinationID, status, description, requesterID, requestTime)" +
                            " VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, locationID);
            statement.setString(2, destinationID);
            statement.setString(3, status);
            statement.setString(4, description);
            statement.setInt(5, requesterID);
            statement.setTimestamp(6, requestTime);
            return statement.execute();

        } catch (SQLException exception) {

            // Print an exception message
            System.out.println("Transportation Request (" + description + ") could not be added.");
            exception.printStackTrace();
            System.out.println();
            return false;
        }
    }

    /**
     * Deletes transportation request with same ID as given request.
     * @return Boolean indicating if request was deleted.
     */
    public static boolean deleteTransportationRequest(TransportationRequest request) {
        int requestID = request.getRequestID();
        try {
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(
                    "DELETE FROM " + Constants.TRANSPORTATION_TABLE + " WHERE requestID=?");
            statement.setInt(1, requestID);
            return statement.execute();
        } catch (SQLException exception) {
            System.out.println("Failed to delete transportation request with ID: " + requestID);
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Attempts to drop transportation table.
     * @return Boolean indicating success of table drop.
     */
    public static boolean dropTable() {
        try {
            Statement statement = Database.getDatabase().getConnection().createStatement();
            return statement.execute("DROP TABLE " + Constants.TRANSPORTATION_TABLE);
        } catch (SQLException exception) {
            System.out.println("Table " + Constants.TRANSPORTATION_TABLE + " cannot be dropped.");
            return false;
        }
    }

    /**
     * @brief Returns list of transportation requests from the database.
     */
    public static ArrayList<TransportationRequest> getTransportationRequests() {
        try {
            // Execute query to get all sanitation requests
            Statement statement = Database.getDatabase().getConnection().createStatement();
            String query = "SELECT * FROM " + Constants.TRANSPORTATION_TABLE;
            ResultSet resultSet = statement.executeQuery(query);

            // Build request list from query
            ArrayList<TransportationRequest> transportationRequests = new ArrayList<>();
            while (resultSet.next()) {

                // Build sanitation request fields from resultSet
                int requestID = resultSet.getInt("requestID");
                Location location = LocationTable.getLocationByID(
                        resultSet.getString("locationID"));
                Location destination = LocationTable.getLocationByID(
                        resultSet.getString("destinationID"));
                TransportationRequest.Status status =
                        TransportationRequest.Status.valueOf(
                                resultSet.getString("status"));
                String description = resultSet.getString("description");
                User requester = UserTable.getUserByID(
                        resultSet.getInt("requesterID"));
                Timestamp requestTime = resultSet.getTimestamp("requestTime");
                User servicer = UserTable.getUserByID(
                        resultSet.getInt("servicerID"));
                Timestamp claimedTime = resultSet.getTimestamp("claimedTime");
                Timestamp completedTime = resultSet.getTimestamp("completedTime");

                // Create and add sanitation request to list
                TransportationRequest transportationRequest = new TransportationRequest(
                        requestID, location, destination, status, description,
                        requester, requestTime, servicer, claimedTime, completedTime);
                transportationRequests.add(transportationRequest);
            }

            // Sort the list by priority
            Collections.sort(transportationRequests);

            // Return the list
            return transportationRequests;

        } catch (SQLException exception) {
            System.out.println("Failed to get transportation requests.");
            exception.printStackTrace();
            System.out.println();
            return null;
        }
    }

    /**
     * Updates tramsportation request with same ID as given request with:
     * Only changes status, servicerID, claimedTime, and completedTime.
     * @param request Updated transportation request
     */
    public static void editTransportationRequest(TransportationRequest request) {

        // Get updated data from request
        int requestID = request.getRequestID();
        String status = request.getStatus().name();
        User servicer = request.getServicer();
        Timestamp claimedTime = request.getClaimedTime();
        Timestamp completedTime = request.getCompletedTime();

        try {

            // Attempt to remove request from database
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(
                    "UPDATE " + Constants.TRANSPORTATION_TABLE +
                            " SET status=?, servicerID=?, claimedTime=?, completedTime=? WHERE requestID=?"
            );
            statement.setString(1, status);
            if (servicer == null) statement.setNull(2, java.sql.Types.INTEGER);
            else statement.setInt(2, servicer.getUserID());
            statement.setTimestamp(3, claimedTime);
            statement.setTimestamp(4, completedTime);
            statement.setInt(5, requestID);
            statement.execute();

        } catch (SQLException exception) {

            // Print an exception message
            System.out.println("Transportation Request Removal Exception:");
            exception.printStackTrace();
            System.out.println();

        }
    }
}
