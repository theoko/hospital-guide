package database;

import helpers.Constants;
import models.map.Location;
import models.sanitation.SanitationRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;

public class SanitationTable {

    private static void SanitationTable() {}

    public static void createTable(){
        Statement statement = null;
        try {
            statement = Database.getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sanitationTable = "CREATE TABLE " + Constants.SANITATION_TABLE + "(" +
                "requestID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                "nodeID VARCHAR(100) References " + Constants.LOCATION_TABLE + " (nodeID), " +
                "priority VARCHAR(10), " +
                "description VARCHAR(100), " +
                "status VARCHAR(100), " +
                "userID VARCHAR(100), " +
                "CONSTRAINT nodeIDClean_fk FOREIGN KEY(nodeID) REFERENCES " + Constants.LOCATION_TABLE + "(nodeID)," +
                "CONSTRAINT priority_enum CHECK (priority in ('LOW', 'MEDIUM', 'HIGH')), " +
                "CONSTRAINT status_enum CHECK (status in ('INCOMPLETE', 'COMPLETE')))";
        try {
            statement.execute(sanitationTable);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param request Sanitation request to add.
     * @return Boolean indicating success of add.
     * @brief Attempts to add sanitation request to the database.
     */
    public static boolean addSanitationRequest(SanitationRequest request) {
        // Get data from request
        Location location = request.getLocationObj();
        SanitationRequest.Priority priority = request.getPriorityObj();
        String description = request.getDescription();
        SanitationRequest.Status status = request.getStatusObj();
        String userID = request.getUser();

        try {
            // Attempt to add request to database
            PreparedStatement statement = Database.getConnection().prepareStatement(
                    "INSERT INTO " + Constants.SANITATION_TABLE +
                            " (NODEID, PRIORITY, DESCRIPTION, STATUS, USERID)" +
                            " VALUES (?, ?, ?, ?, ?)"
            );
            statement.setString(1, location.getNodeID());
            statement.setString(2, priority.name());
            statement.setString(3, description);
            statement.setString(4, status.name());
            statement.setString(5, userID);
            return statement.execute();
        } catch (SQLException exception) {
            // Print an exception message
            System.out.println("Sanitation Request (" + description + ") could not be added.");
            exception.printStackTrace();
            System.out.println();
            return false;
        }
    }

    /**
     * @return Boolean indicating success of table drop.
     * @brief Attempts to drop sanitation table.
     */
    public static boolean dropSanitationTable() {
        try {
            Statement statement = Database.getConnection().createStatement();
            return statement.execute("DROP TABLE " + Constants.SANITATION_TABLE);
        } catch (SQLException exception) {
            System.out.println("Table " + Constants.SANITATION_TABLE + " cannot be dropped.");
            return false;
        }
    }

    /**
     * @brief Returns list of sanitation requests from the database.
     */
    public static ArrayList<SanitationRequest> getSanitationRequests() {
        try {
            // Execute query to get all sanitation requests
            Statement statement = Database.getConnection().createStatement();
            String query = "SELECT * FROM " + Constants.SANITATION_TABLE;
            ResultSet resultSet = statement.executeQuery(query);

            // Build request list from query
            ArrayList<SanitationRequest> sanitationRequests = new ArrayList<>();
            while (resultSet.next()) {

                // Build sanitation request fields from resultSet
                int sanitationID = resultSet.getInt("REQUESTID");
                Location location = LocationTable.getLocationByID(
                        resultSet.getString("NODEID"));
                SanitationRequest.Priority priority =
                        SanitationRequest.Priority.valueOf(
                                resultSet.getString("PRIORITY"));
                String description = resultSet.getString("DESCRIPTION");
                SanitationRequest.Status status = SanitationRequest.Status.valueOf(resultSet.getString("STATUS"));
                String userID = resultSet.getString("USERID");

                // Create and add sanitation request to list
                SanitationRequest sanitationRequest = new SanitationRequest(
                        sanitationID, location, priority, description, status, userID);
                sanitationRequests.add(sanitationRequest);
            }

            // Sort the list by priority
            Collections.sort(sanitationRequests);

            // Return the list
            return sanitationRequests;

        } catch (SQLException exception) {
            System.out.println("Failed to get sanitation requests.");
            exception.printStackTrace();
            System.out.println();
            return null;
        }
    }

    public static void editSanitationRequest(SanitationRequest request) {
        SanitationRequest.Status status = request.getStatusObj();
        String userID = request.getUser();
        int requestID = request.getRequestID();
        try {
            // Attempt to remove request from database
            PreparedStatement statement = Database.getConnection().prepareStatement(
                    "UPDATE " + Constants.SANITATION_TABLE + " SET STATUS=?, USERID=? WHERE REQUESTID=?"
            );
            statement.setString(1, status.name());
            statement.setString(2, userID);
            statement.setInt(3, requestID);
            statement.execute();
        } catch (SQLException exception) {
            // Print an exception message
            System.out.println("Sanitation Request Removal Exception:");
            exception.printStackTrace();
            System.out.println();
        }
    }
}
