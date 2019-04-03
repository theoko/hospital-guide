package database;

import helpers.Constants;
import helpers.DatabaseHelpers;
import helpers.MapHelpers;
import models.map.Edge;
import models.map.Location;
import models.room.Book;
import models.room.Room;
import models.sanitation.SanitationRequest;
import models.user.User;

import java.sql.*;
import java.util.*;


public class Database {

    private static String newPrefixChar = "X";
    static Connection connection;

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

//        dialect = new DerbyTemplates();
//        configuration = new Configuration(dialect);
    }

    /**
     * Drops all database tables
     */
    public static void dropTables() {
        dropDeletedEdgesTable();
        dropDeletedLocationTable();
        dropSanitationTable();
        dropBookTable();
        dropRoomTable();
        dropEdgeTable();
        dropLocationTable();
        dropUsersTable();
    }

    /**
     * Creates the basic database tables
     */
    private static void createTables() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String usersTable = "CREATE TABLE " + Constants.USERS_TABLE +
                "(userID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                " username VARCHAR(32), " +
                " password VARCHAR(32)," +
                " userType VARCHAR(32))";


        String locationTable = "CREATE TABLE " + Constants.NODES_TABLE +
                "(nodeID VARCHAR(100) PRIMARY KEY," +
                "xCoord INT," +
                "yCoord INT," +
                "floor VARCHAR(100)," +
                "building VARCHAR(100)," +
                "nodeType VARCHAR(100)," +
                "longName VARCHAR(100)," +
                "shortName VARCHAR(100))";

        String neighborTable = "CREATE TABLE " + Constants.EDGES_TABLE +
                "(edgeID VARCHAR(100) PRIMARY KEY," +
                "startNodeID VARCHAR(100)," +
                "endNodeID VARCHAR(100)," +
                "CONSTRAINT startNodeID_fk FOREIGN KEY(startNodeID) REFERENCES " + Constants.NODES_TABLE + "(nodeID)," +
                "CONSTRAINT endNodeID_fk FOREIGN KEY(endNodeID) REFERENCES " + Constants.NODES_TABLE + "(nodeID))";

        String roomTable = "CREATE TABLE " + Constants.ROOM_TABLE +
                "(nodeID VARCHAR(100)," +
                "capacity INT," +
                "CONSTRAINT nodeIDRoom_fk FOREIGN KEY(nodeID) REFERENCES " + Constants.NODES_TABLE + "(nodeID)" +
                ")";

        String bookTable = "CREATE TABLE " + Constants.BOOK_TABLE + "(" +
                "bookingID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                "nodeID VARCHAR(100)," +
                "userID INT," +
                "startDate TIMESTAMP," +
                "endDate TIMESTAMP," +
                "CONSTRAINT roomID2_fk FOREIGN KEY(nodeID) REFERENCES " + Constants.NODES_TABLE + "(nodeID)," +
                "CONSTRAINT userID2_fk FOREIGN KEY(userID) REFERENCES " + Constants.USERS_TABLE + "(userID))";

        String deletedLocationsTable = "CREATE TABLE " + Constants.DELETED_LOCATION_TABLE + "(" +
                "nodeID VARCHAR(100) PRIMARY KEY," +
                "xCoord INT," +
                "yCoord INT," +
                "floor VARCHAR(100)," +
                "building VARCHAR(100)," +
                "nodeType VARCHAR(100)," +
                "longName VARCHAR(100)," +
                "shortName VARCHAR(100))";

        String deletedEdgesTable = "CREATE TABLE " + Constants.DELETED_EDGES_TABLE +
                "(edgeID VARCHAR(100) PRIMARY KEY," +
                "startNodeID VARCHAR(100)," +
                "endNodeID VARCHAR(100)," +
                "CONSTRAINT startNodeIDdel_fk FOREIGN KEY(startNodeID) REFERENCES " + Constants.DELETED_LOCATION_TABLE + "(nodeID)," +
                "CONSTRAINT endNodeIDdel_fk FOREIGN KEY(endNodeID) REFERENCES " + Constants.DELETED_LOCATION_TABLE + "(nodeID))";

        String sanitationTable = "CREATE TABLE " + Constants.SANITATION_TABLE + "(" +
                "requestID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                "nodeID VARCHAR(100) References " + Constants.NODES_TABLE + " (nodeID), " +
                "priority VARCHAR(10), " +
                "description VARCHAR(100), " +
                "status VARCHAR(100), " +
                "userID VARCHAR(100), " +
                "CONSTRAINT nodeIDClean_fk FOREIGN KEY(nodeID) REFERENCES " + Constants.NODES_TABLE + "(nodeID)," +
                "CONSTRAINT priority_enum CHECK (priority in ('LOW', 'MEDIUM', 'HIGH')), " +
                "CONSTRAINT status_enum CHECK (status in ('INCOMPLETE', 'COMPLETE')))";

        try {

            statement.execute(usersTable);
            statement.execute(locationTable);
            statement.execute(neighborTable);

            statement.execute(roomTable);
            statement.execute(bookTable);
            statement.execute(sanitationTable);
            statement.execute(deletedLocationsTable);
            statement.execute(deletedEdgesTable);

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }

    }

    /*
     * checks if location is available
    *
    public static boolean checkAvailabilityTime(Date startDate, Date endDate){
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.BOOK_TABLE +
                            "WHERE startDate=? AND endDate=? " +


            );


            statement.execute(usersTable);
            statement.execute(employeeTable);
            statement.execute(custodianTable);
            statement.execute(adminTable);
            statement.execute(locationTable);
            statement.execute(neighborTable);
            statement.execute(createSanitationTable);
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }*/

    public static Room getRoomByID(String roomID) {
        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.ROOM_TABLE + " WHERE NODEID=?"
            );

            statement.setString(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                Room room = new Room(
                        resultSet.getString("NODEID"),
                        resultSet.getInt("CAPACITY")
                );

                return room;
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Cannot get room by ID!");
            e.printStackTrace();

            return null;
        }
    }

    public static boolean addRoom(Room room) {
        try {
            PreparedStatement statement;
            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.ROOM_TABLE + " (NODEID, CAPACITY) " +
                            "VALUES (?, ?)"
            );

            statement.setString(1, room.getRoomID());
            statement.setInt(2, room.getCapacity());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("Room " + room.getRoomID() + " cannot be added!");
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
    public static boolean checkAvailabilityLocation(Room room, String startTime, String endTime) {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(
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
    public static List<Room> checkAvailabilityTime(String startTime, String endTime) {

        PreparedStatement statement1;
        ArrayList<Room> roomsAvailable = new ArrayList<>();

        try {

            String unavailableRooms = "SELECT nodeID FROM " + Constants.BOOK_TABLE +
                    " WHERE ? <=  ENDDATE" +
                    " AND ? >= STARTDATE" +
                    " OR ? >=  STARTDATE" +
                    " AND ? <= ENDDATE";

            statement1 = connection.prepareStatement(
                    "SELECT nodeID FROM " + Constants.ROOM_TABLE +
                            " EXCEPT (" + unavailableRooms + ")"
            );

            statement1.setTimestamp(1, Timestamp.valueOf(endTime));
            statement1.setTimestamp(2, Timestamp.valueOf(endTime));
            statement1.setTimestamp(3, Timestamp.valueOf(startTime));
            statement1.setTimestamp(4, Timestamp.valueOf(startTime));

            ResultSet resultSet = statement1.executeQuery();

            while (resultSet.next()) {
                Room room = getRoomByID(resultSet.getString("NODEID"));

                roomsAvailable.add(room);
            }

            return roomsAvailable;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Create a booking for a room
     * @param book
     */
    public static boolean createBooking(Book book) {

        try {

            PreparedStatement statement;
            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.BOOK_TABLE + " (NODEID, USERID, STARTDATE, ENDDATE) " +
                            "VALUES (?, ?, ?, ?)"
            );

            statement.setString(1, book.getRoomID());
            statement.setInt(2, getUserByUsername(book.getUser().getUsername()).getUserID());
            statement.setTimestamp(3, Timestamp.valueOf(book.getStartDate()));
            statement.setTimestamp(4, Timestamp.valueOf(book.getEndDate()));

            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }

    /**
     * Creates user based off of database
     */
    public static boolean createUser(User user) {

        try {

            User checkUser = Database.getUserByUsername(user.getUsername());

            // We need to create the user
            if(checkUser == null) {

                PreparedStatement statement;
                statement = connection.prepareStatement(
                        "INSERT INTO " + Constants.USERS_TABLE + " (USERNAME, PASSWORD, USERTYPE) " +
                                "VALUES (?, ?, ?)"
                );

                statement.setString(1, user.getUsername());
                statement.setString(2, user.getPassword());
                statement.setString(3, user.getUserType().name());
                return statement.execute();

            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }


    /**
     * Drop tables
     */
    private static boolean dropDeletedEdgesTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.DELETED_EDGES_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.DELETED_EDGES_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropDeletedLocationTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.DELETED_LOCATION_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.DELETED_LOCATION_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropBookTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.BOOK_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.BOOK_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropRoomTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.ROOM_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.ROOM_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropUsersTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.USERS_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.USERS_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropLocationTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.NODES_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.NODES_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropEdgeTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.EDGES_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.EDGES_TABLE + " cannot be dropped");

            return false;
        }
    }

    /**
     * @return Boolean indicating success of table drop.
     * @brief Attempts to drop sanitation table.
     */
    private static boolean dropSanitationTable() {
        try {
            Statement statement = connection.createStatement();
            return statement.execute("DROP TABLE " + Constants.SANITATION_TABLE);
        } catch (SQLException exception) {
            System.out.println("Table " + Constants.SANITATION_TABLE + " cannot be dropped.");
            return false;
        }
    }

    /**
     * Checks if the database exists locally
     * @return true if the database exists, false otherwise
     */
    public static boolean databaseExists() {

        boolean exists;

        HashMap<String, Location> locations = Database.getLocations();

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

    /**
     * Generalized function for filtering tables
     *
     * @return a list of objects
     */
    public List<Object> filterTable(HashMap<String, ArrayList<String>> builder) {

//        // Start with select
//        String query = "SELECT ";
//
//        ArrayList<String> projection = builder.get(Constants.DB_PROJECTION);
//        ArrayList<String> relation = builder.get(Constants.DB_RELATION);
//        ArrayList<String> condition = builder.get(Constants.DB_CONDITION);
//
//        for(int i=0; i<projection.size(); i++) {
//            query += projection.get(i);
//
//            if(i < projection.size() - 1)
//                query += ", ";
//        }
//
//        query += " FROM ";
//
//        for(int i=0; i<relation.size(); i++) {
//            query += relation.get(i);
//            query += " " + Character.toUpperCase(relation.get(i).charAt(0));
//
//            if(i < relation.size() - 1)
//                query += ", ";
//        }
//
//        System.out.println(query);


        return new ArrayList<>();
    }

    public static Book getBookByRoomID(String roomID) {
        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.BOOK_TABLE + " WHERE ROOMID=?"
            );

            statement.setString(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {

                Book book = new Book(
                        resultSet.getInt("BOOKINGID"),
                        resultSet.getString("ROOMID"),
                        getUserByID(resultSet.getInt("USERID")),
                        resultSet.getString("STARTDATE"),
                        resultSet.getString("ENDDATES")
                );

                return book;

            }

            return null;

        } catch (SQLException e) {
            System.out.println("Cannot get room by ID!");

            return null;
        }
    }

    public static List<Book> getBookingsForUser(User user) {
        try {

            User userByUsername = getUserByUsername(user.getUsername());

            if(userByUsername == null) {
                return null;
            }

            int userID = userByUsername.getUserID();

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.BOOK_TABLE + " WHERE USERID=?"
            );

            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();

            List<Book> bookings = new ArrayList<>();

            while(resultSet.next()) {

                Book book = new Book(
                        resultSet.getInt("BOOKINGID"),
                        resultSet.getString("NODEID"),
                        getUserByID(resultSet.getInt("USERID")),
                        resultSet.getString("STARTDATE"),
                        resultSet.getString("ENDDATE")
                );

                bookings.add(book);

            }

            return bookings;

        } catch (SQLException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static User getUserByID(int userID) {
        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.USERS_TABLE + " WHERE USERID=?"
            );

            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                User user = new User(
                        resultSet.getInt("USERID"),
                        resultSet.getString("USERNAME"),
                        resultSet.getString("PASSWORD"),
                        Constants.Auth.valueOf(resultSet.getString("USERTYPE"))
                );

                return user;
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Cannot get user by ID!");

            return null;
        }
    }

    public static User getUserByUsername(String username) {
        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.USERS_TABLE + " WHERE USERNAME=?"
            );

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {
                User user = new User(
                        resultSet.getInt("USERID"),
                        resultSet.getString("USERNAME"),
                        resultSet.getString("PASSWORD"),
                        Constants.Auth.valueOf(resultSet.getString("USERTYPE"))
                );

                return user;
            }

            return null;

        } catch (SQLException e) {
            System.out.println("Cannot get user by username!");

            return null;
        }
    }

    public User getAdminByID(int adminID) {
        return getUserByID(adminID);
    }

    public User getEmployeeByID(int employeeID) {
        return getUserByID(employeeID);
    }

    public static boolean addLocation(Location location) {

        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.NODES_TABLE + " (NODEID, XCOORD, YCOORD, FLOOR, BUILDING, NODETYPE, LONGNAME, SHORTNAME ) " +
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
                addRoom(new Room(
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

    public static boolean addDeleteLocation(Location location) {

        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.DELETED_LOCATION_TABLE + " (NODEID, XCOORD, YCOORD, FLOOR, BUILDING, NODETYPE, LONGNAME, SHORTNAME) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
            );

            statement.setString(1, location.getNodeID());
            statement.setInt(2, location.getxCord());
            statement.setInt(3, location.getyCord());
            statement.setString(4, location.getFloor());
            statement.setString(5, location.getBuilding());
            statement.setString(6, location.getNodeType().name());
            statement.setString(7, location.getLongName());
            statement.setString(8, location.getShortName());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("Location " + location.getNodeID() + " cannot be added!");
            e.printStackTrace();

            return false;
        }

    }

    public static boolean addDeleteEdge(Edge edge) {

        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.DELETED_EDGES_TABLE + " (EDGEID, STARTNODEID, ENDNODEID) " +
                            "VALUES (?, ?, ?)"
            );

            statement.setString(1, edge.getEdgeID());
            statement.setString(2, edge.getStart().getNodeID());
            statement.setString(3, edge.getEnd().getNodeID());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("SubPath cannot be added to deleted edges table!");

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
            String stmtString = "SELECT * FROM " + Constants.NODES_TABLE + " WHERE NODEID=?";
            PreparedStatement statement = connection.prepareStatement(stmtString);
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

    public static ArrayList<Room> getRooms() {
        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.ROOM_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Room> returnList = new ArrayList<>();

            while (resultSet.next()) {

                Room room = new Room(
                        resultSet.getString("ROOMID"),
                        resultSet.getInt("CAPACITY")
                );

                returnList.add(room);

            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Failed to get users!");

            return null;
        }
    }

    public static boolean addEdge(Edge edge) {

        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.EDGES_TABLE + " (EDGEID, STARTNODEID, ENDNODEID) " +
                            "VALUES (?, ?, ?)"
            );

            statement.setString(1, edge.getEdgeID());
            statement.setString(2, edge.getStart().getNodeID());
            statement.setString(3, edge.getEnd().getNodeID());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("SubPath cannot be added!");

            return false;
        }

    }

    /**
     * getBookings
     */
    public List<Book> getBookings() {
        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.BOOK_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Book> returnList = new ArrayList<>();

            while (resultSet.next()) {

                Book user = new Book(
                        resultSet.getInt("BOOKINGID"),
                        resultSet.getString("ROOMID"),
                        getUserByID(resultSet.getInt("USERID")),
                        resultSet.getString("STARTDATE"),
                        resultSet.getString("ENDDATE")
                );

                returnList.add(user);

            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Failed to get users!");

            return null;
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
            PreparedStatement statement = connection.prepareStatement(
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
     * @brief Returns list of sanitation requests from the database.
     */
    public static ArrayList<SanitationRequest> getSanitationRequests() {
        try {
            // Execute query to get all sanitation requests
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM " + Constants.SANITATION_TABLE;
            ResultSet resultSet = statement.executeQuery(query);

            // Build request list from query
            ArrayList<SanitationRequest> sanitationRequests = new ArrayList<>();
            while (resultSet.next()) {

                // Build sanitation request fields from resultSet
                int sanitationID = resultSet.getInt("REQUESTID");
                Location location = getLocationByID(
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
            PreparedStatement statement = connection.prepareStatement(
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

    /**
     * Returns a list of users
     */
    public List<User> getUsers() {
        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.USERS_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<User> returnList = new ArrayList<>();

            while (resultSet.next()) {

                User user = new User(
                        resultSet.getInt("USERID"),
                        resultSet.getString("USERNAME"),
                        resultSet.getString("PASSWORD"),
                        Constants.Auth.valueOf(resultSet.getString("USERTYPE"))
                );

                returnList.add(user);

            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Failed to get users!");

            return null;
        }
    }

    /**
     * Returns a list of nodes
     */
    public static HashMap<String, Location> getDeletedLocations() {
        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.DELETED_LOCATION_TABLE;

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
            System.out.println("Failed to get deleted locations!");

            return null;
        }
    }

    /**
     * Returns a list of nodes
     */
    public static HashMap<String, Location> getLocations() {
        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.NODES_TABLE;

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

    public static List<Edge> getEdges(HashMap<String, Location> lstLocations) {
        try {
            Statement statement;
            statement = connection.createStatement();
            String query = "SELECT * FROM " + Constants.EDGES_TABLE;
            ResultSet resultSet = statement.executeQuery(query);
            List<Edge> returnList = new ArrayList<>();

            while (resultSet.next()) {
                String edgeID = resultSet.getString("EDGEID");
                Edge edge = new Edge(
                        edgeID,
                        lstLocations.get(resultSet.getString("STARTNODEID")),
                        lstLocations.get(resultSet.getString("ENDNODEID"))
                );

                returnList.add(edge);
            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Failed to get edges!");
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

            statement = connection.prepareStatement(
                    "UPDATE " + Constants.NODES_TABLE +
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
                statement1 = connection.prepareStatement(
                    "DELETE FROM " + Constants.ROOM_TABLE +
                    " WHERE NODEID=?"
                );

                statement1.setString(1, deleteLocation.getNodeID());

                statement1.execute();

            }

            PreparedStatement statement2;
            PreparedStatement statement3;

            statement2 = connection.prepareStatement(
                    "DELETE FROM " + Constants.EDGES_TABLE +
                            " WHERE STARTNODEID=? OR ENDNODEID=?"
            );

            statement2.setString(1, deleteLocation.getNodeID());
            statement2.setString(2, deleteLocation.getNodeID());

            statement2.execute();

            // Add location to deleted locations table

            statement3 = connection.prepareStatement(
                    "DELETE FROM " + Constants.NODES_TABLE +
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

    /**
     * Updates the edge object specified on the database
     *
     * @param updatedEdge
     * @return true if the edge was updated successfully, false otherwise
     */
    public static boolean updateEdge(Edge updatedEdge) {

        try {
            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "UPDATE " + Constants.EDGES_TABLE +
                            " SET STARTNODEID=?, ENDNODEID=?" +
                            " WHERE EDGEID=?"
            );

            statement.setString(1, updatedEdge.getStart().getNodeID());
            statement.setString(2, updatedEdge.getEnd().getNodeID());
            statement.setString(3, updatedEdge.getEdgeID());

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("Failed to update edge: " + updatedEdge.getEdgeID());
            e.printStackTrace();

            return false;
        }

    }

    /**
     * Returns a list of admins
     */
    public List<User> getAdmins() throws Exception {

        return new ArrayList<>();
    }

    /**
     * Returns a list of employees
     */
    public List<User> getEmployees() throws Exception {
        return new ArrayList<>();
    }

    public static void main(String[] args) {

//        Database db;
//
//        if(Database.connection == null) {
//            System.out.println("our connection is fucked");
//        }
//
//        File dbDirectory = new File(Constants.DB_NAME.replaceAll("%20", " "));
//
//        if(dbDirectory.exists()) {
//            System.out.println("Exists!");
//
//            db = new Database();
//
//        } else {
//            System.out.println("Need to create the database!");
//
//            db = new Database();
//
//            db.createTables();
//        }

//        HashMap<String, ArrayList<String>> builder = new HashMap<>();
//
//        ArrayList<String> proj = new ArrayList<>();
//        proj.add("USERID");
//        proj.add("USERNAME");
//
//        ArrayList<String> relation = new ArrayList<>();
//
//        builder.put(Constants.DB_PROJECTION, proj);
//
//        db.filterTable(builder);

    }
    public static String addNewLocation(Location loc) {
                String locID = Database.generateUniqueNodeID(loc);
        loc.setNodeID(locID);
        loc.addCurrNode();
        return locID;
    }

    public static String generateUniqueNodeID(Location c) {

        String id = newPrefixChar + c.getNodeType().toString() + "000" +
                c.getDBFormattedFloor();
        while(getLocations().containsKey(id)) {
            String numericalIDStr = id.substring(id.length() - 5, id.length() - 2);
            int numericalIDVal = Integer.parseInt(numericalIDStr);
            numericalIDVal++;
            numericalIDStr = String.format("%03d", numericalIDVal);
            id = newPrefixChar + c.getNodeType().toString() + numericalIDStr +
                    c.getDBFormattedFloor();
        }
        return id;

    }
    public static boolean edgeExists(Edge e) {
        return hasEdgeByID(MapHelpers.generateEdgeID(e, Constants.START_FIRST))
                || hasEdgeByID(MapHelpers.generateEdgeID(e, Constants.END_FIRST));
    }
    public static void removeEdgeByID(Edge e) {
        if(edgeExists(e)) {
            String EdgeID = hasEdgeByID(MapHelpers.generateEdgeID(e, Constants.START_FIRST)) ?
                    MapHelpers.generateEdgeID(e, Constants.START_FIRST)
                    : MapHelpers.generateEdgeID(e, Constants.END_FIRST);
//            Edge e = getEdges(getLocations()).get(EdgeID);
//            VisualRealtimeController.removeLine();
            //removeEdge(EdgeID);
        }
    }
    public static boolean toggleEdge(Edge e) {
        boolean edgeExists = edgeExists(e);

        if(e.getEdgeID() == null) {
            e.setEdgeID(MapHelpers.generateEdgeID(e, Constants.START_FIRST));
        }

        if(edgeExists) {
            removeEdgeByID(e);
            return Constants.DESELECTED;
        }
        else {
            addEdge(e);
            return Constants.SELECTED;
        }
    }
    public static boolean hasEdgeByID(String id) {
        List<Edge> edges = getEdges(getLocations());
        for(Edge e : edges) {
            if(e.getEdgeID().equals(id)) return true;
        }
        return false;
    }
}
