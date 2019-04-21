package database;

import helpers.Constants;
import models.map.Location;
import models.services.SanitationRequest;
import models.services.SanitationRequest.Priority;
import models.services.ServiceRequest.Status;
import models.user.User;

import java.sql.*;
import java.sql.Types;
import java.util.*;
import java.util.Date;

public class SanitationTable {

    /**
     * Creates empty sanitation table
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
                    "CREATE TABLE " + Constants.SANITATION_TABLE + "(" +
                            "requestID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                            "nodeID VARCHAR(100) References " + Constants.LOCATION_TABLE + " (nodeID), " +
                            "priority VARCHAR(10), " +
                            "status VARCHAR(100), " +
                            "description VARCHAR(100), " +
                            "requesterID INT REFERENCES " + Constants.USERS_TABLE + "(userID), " +
                            "requestTime TIMESTAMP, " +
                            "servicerID INT REFERENCES " + Constants.USERS_TABLE + "(userID), " +
                            "claimedTime TIMESTAMP, " +
                            "completedTime TIMESTAMP, " +
                            "CONSTRAINT priority_enum CHECK (priority in ('LOW', 'MEDIUM', 'HIGH')), " +
                            "CONSTRAINT status_enum CHECK (status in ('INCOMPLETE', 'COMPLETE')))"
            );
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Seeds the database with sanitation requests from a CSV file.
     */
    public static void seed() {

        // Get locations, employees, and custodians from database.
        ArrayList<Location> locations = new ArrayList(LocationTable.getLocations().values());
        ArrayList<User> employees = UserTable.getEmployees();
        ArrayList<User> custodians = UserTable.getCustodians();

        // Generate random sanitation requests
        Random rand = new Random();
        final int numEntries = 30;
        for (int i = 0; i < numEntries; i++) {

            // Generate location (uniform)
            Location location = locations.get(rand.nextInt(locations.size()));

            // Generate priority (uniform)
            Priority priority;
            switch (rand.nextInt(3)) {
                case 0: priority = Priority.HIGH; break;
                case 1: priority = Priority.MEDIUM; break;
                default: priority = Priority.LOW; break;
            }

            // Generate requester
            User requester = employees.get(rand.nextInt(employees.size()));

            // Generate request time (uniform current time + ~12 hours)
            Timestamp requestTime = new Timestamp(
                    new Date().getTime() + rand.nextInt(43200000));

            // Generate description
            String description;
            switch (rand.nextInt(3)) {
                case 0: description = "Drink spill";
                case 1: description = "Vomit";
                default: description = "Radioactive waste";
            }

            // Add request to database
            SanitationRequest request = new SanitationRequest(
                    0, location, priority, Status.INCOMPLETE, description,
                    requester, requestTime,
                    null, null, null);

            // Mark 2/3 of requests as claimed
            int claimFlag = rand.nextInt(3);
            if (claimFlag > 0) {

                // Mark as claimed within 1 hour of request
                User servicer = custodians.get(rand.nextInt(custodians.size()));
                Timestamp claimedTime = new Timestamp(
                        requestTime.getTime() + rand.nextInt(3600000));
                request.setServicer(servicer);
                request.setClaimedTime(claimedTime);

                // Mark half of claimed requests as completed within 2 hours of claim
                if (claimFlag == 2) {
                    Timestamp completedTime = new Timestamp(
                            claimedTime.getTime() + rand.nextInt(7200000));
                    request.setCompletedTime(completedTime);
                    request.setStatus(Status.COMPLETE);
                }

                // Update request in database
                editSanitationRequest(request);
            }

            // Add request to database
            addSanitationRequest(request);
        }
    }

    /**
     * Adds sanitation request to the database.
     * @param request Sanitation request to add.
     * @return Boolean indicating success of add.
     * @brief Attempts to add NEW sanitation request to the database.
     * NOTE: Expects servicer and servicer timestamps to be NULL because it is a NEW request.
     */
    public static boolean addSanitationRequest(SanitationRequest request) {

        // Get data from request
        String nodeID = request.getLocation().getNodeID();
        String priority = request.getPriority().name();
        String status = request.getStatus().name();
        String description = request.getDescription();
        Integer requesterID = request.getRequesterID();
        Timestamp requestTime = request.getRequestTime();
        Integer servicerID = request.getServicerID();
        Timestamp claimedTime = request.getClaimedTime();
        Timestamp completedTime = request.getCompletedTime();
        
        try {
            // Attempt to add request to database
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(
                    "INSERT INTO " + Constants.SANITATION_TABLE +
                            " (nodeID, priority, status, description, requesterID, requestTime, servicerID, claimedTime, completedTime)" +
                            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, nodeID);
            statement.setString(2, priority);
            statement.setString(3, status);
            statement.setString(4, description);
            statement.setInt(5, requesterID);
            statement.setTimestamp(6, requestTime);
            if (servicerID == null) statement.setNull(7, Types.INTEGER);
            else statement.setInt(7, servicerID);
            if (claimedTime == null) statement.setNull(8, Types.TIMESTAMP);
            else statement.setTimestamp(8, claimedTime);
            if (completedTime == null) statement.setNull(9, Types.TIMESTAMP);
            else statement.setTimestamp(9, completedTime);
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
     * Deletes sanitation request with same ID as given request.
     * @return Boolean indicating if request was deleted.
     */
    public static boolean deleteSanitationRequest(SanitationRequest request) {
        int requestID = request.getRequestID();
        try {
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(
                    "DELETE FROM " + Constants.SANITATION_TABLE + " WHERE requestID=?");
            statement.setInt(1, requestID);
            return statement.execute();
        } catch (SQLException exception) {
            System.out.println("Failed to delete sanitation request with ID: " + requestID);
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * @brief Attempts to drop sanitation table.
     * @return Boolean indicating success of table drop.
     */
    public static boolean dropTable() {
        try {
            Statement statement = Database.getDatabase().getConnection().createStatement();
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
            Statement statement = Database.getDatabase().getConnection().createStatement();
            String query = "SELECT * FROM " + Constants.SANITATION_TABLE;
            ResultSet resultSet = statement.executeQuery(query);

            // Build request list from query
            ArrayList<SanitationRequest> sanitationRequests = new ArrayList<>();
            while (resultSet.next()) {

                // Build sanitation request fields from resultSet
                int sanitationID = resultSet.getInt("requestID");
                Location location = LocationTable.getLocationByID(
                        resultSet.getString("nodeID"));
                SanitationRequest.Priority priority =
                        SanitationRequest.Priority.valueOf(
                                resultSet.getString("priority"));
                SanitationRequest.Status status =
                        SanitationRequest.Status.valueOf(
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
                SanitationRequest sanitationRequest = new SanitationRequest(
                        sanitationID, location, priority, status, description,
                        requester, requestTime, servicer, claimedTime, completedTime);
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

    /**
     * Updates sanitation request with same ID as given request with:
     * Only changes status, servicerID, claimedTime, and completedTime.
     * @param request Updated sanitation request.
     */
    public static void editSanitationRequest(SanitationRequest request) {

        // Get updated data from request
        int requestID = request.getRequestID();
        String status = request.getStatus().name();
        User servicer = request.getServicer();
        Timestamp claimedTime = request.getClaimedTime();
        Timestamp completedTime = request.getCompletedTime();

        try {

            // Attempt to remove request from database
            PreparedStatement statement = Database.getDatabase().getConnection().prepareStatement(
                    "UPDATE " + Constants.SANITATION_TABLE +
                            " SET status=?, servicerID=?, claimedTime=?, completedTime=? WHERE requestID=?"
            );
            statement.setString(1, status);
            if (servicer == null) statement.setNull(2, Types.INTEGER);
            else statement.setInt(2, servicer.getUserID());
            statement.setTimestamp(3, claimedTime);
            statement.setTimestamp(4, completedTime);
            statement.setInt(5, requestID);
            statement.execute();

        } catch (SQLException exception) {

            // Print an exception message
            System.out.println("Sanitation Request Removal Exception:");
            exception.printStackTrace();
            System.out.println();

        }
    }
}
