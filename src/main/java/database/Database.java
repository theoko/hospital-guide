package database;

import helpers.Constants;
import helpers.DatabaseHelpers;
import models.map.Edge;
import models.map.Location;
import models.user.Admin;
import models.user.Employee;
import models.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Database {

    static Connection connection;

//    SQLTemplates dialect;
//    Configuration configuration;
//    SQLQueryFactory sqlQueryFactory;

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
            dropTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createTables();

//        dialect = new DerbyTemplates();
//        configuration = new Configuration(dialect);
    }

    /**
     * Drops all database tables
     */
    public static void dropTables() {
        dropEdgeTable();
        dropLocationTable();
        dropAdminTable();
        dropCustodianTable();
        dropEmployeeTable();
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
                "(userID INT PRIMARY KEY," +
                " username VARCHAR(20), " +
                "password VARCHAR(32)," +
                " userType INT)";

        String employeeTable = "CREATE TABLE " + Constants.EMPLOYEE_TABLE +
                "(employeeID INT PRIMARY KEY," +
                " CONSTRAINT employeeID_fk FOREIGN KEY(employeeID) REFERENCES " + Constants.USERS_TABLE + "(userID))";

        String custodianTable = "CREATE TABLE " + Constants.CUSTODIAN_TABLE +
                "(employeeID INT REFERENCES " + Constants.EMPLOYEE_TABLE + "(employeeID))";

        String adminTable = "CREATE TABLE " + Constants.ADMIN_TABLE +
                "(adminID INT PRIMARY KEY," +
                " CONSTRAINT adminID_fk FOREIGN KEY(adminID) REFERENCES " + Constants.USERS_TABLE + "(userID))";

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

        try {

            boolean createUsersResult = statement.execute(usersTable);
            boolean createEmployeeResult = statement.execute(employeeTable);
            boolean createCustodianResult = statement.execute(custodianTable);
            boolean createAdminResult = statement.execute(adminTable);

            boolean createNodesResult = statement.execute(locationTable);
            boolean createEdgesTable = statement.execute(neighborTable);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public static boolean databaseExists() {

        boolean exists;

        if(Database.getLocations().isEmpty()) {
            exists = false;
        } else {
            exists = true;
        }


        return exists;

    }

    /**
     * Drop tables
     */
    private static boolean dropUsersTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.USERS_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.USERS_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropEmployeeTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.EMPLOYEE_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.EMPLOYEE_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropCustodianTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.CUSTODIAN_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.CUSTODIAN_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropAdminTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.ADMIN_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.ADMIN_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropLocationTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.NODES_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.NODES_TABLE + " cannot be dropped");

            return false;
        }
    }

    private static boolean dropEdgeTable(){
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
     * Generalized function for filtering tables
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

    public User getUserByID(int userID) {
        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.USERS_TABLE + " WHERE USERID=?"
            );

            statement.setInt(1, userID);

            ResultSet resultSet = statement.executeQuery();

            User user = new User(
                    resultSet.getInt("USERID"),
                    resultSet.getString("USERNAME"),
                    resultSet.getString("PASSWORD"),
                    resultSet.getInt("USERTYPE")
            );

            return user;

        } catch (SQLException e) {
            System.out.println("Cannot get user by ID!");

            return null;
        }
    }

    public Admin getAdminByID(int adminID) {
        return (Admin) getUserByID(adminID);
    }

    public Employee getEmployeeByID(int employeeID) {
        return (Employee) getUserByID(employeeID);
    }

    public static boolean addLocation(Location location) {

        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.NODES_TABLE + " (NODEID, XCOORD, YCOORD, FLOOR, BUILDING, NODETYPE, LONGNAME, SHORTNAME) " +
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

            return statement.execute();

        } catch (SQLException e) {
            System.out.println("Location " + location.getNodeID() + " cannot be added!");
            e.printStackTrace();

            return false;
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
     * Returns a list of users
     */
    public List<User> getUsers() {
        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.USERS_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<User> returnList = new ArrayList<>();

            while(resultSet.next()) {

                User user = new User(
                        resultSet.getInt("USERID"),
                        resultSet.getString("USERNAME"),
                        resultSet.getString("PASSWORD"),
                        resultSet.getInt("USERTYPE")
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
    public static HashMap<String, Location> getLocations() {
        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.NODES_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            HashMap<String, Location> returnList = new HashMap<>();

            while(resultSet.next()) {

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
            System.out.println("Failed to get users!");

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

            while(resultSet.next()) {
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
            System.out.println("Failed to get users!");
            return null;
        }
    }

    /**
     * Updates the location object specified on the database
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
     * @param deleteLocation
     * @return true if the location was deleted successfully, false otherwise
     */
    public static boolean deleteLocation(Location deleteLocation) {

        try {

            PreparedStatement statement1;
            PreparedStatement statement2;

            statement1 = connection.prepareStatement(
                    "DELETE FROM " + Constants.EDGES_TABLE +
                    " WHERE STARTNODEID=? OR ENDNODEID=?"
            );

            statement1.setString(1, deleteLocation.getNodeID());
            statement1.setString(2, deleteLocation.getNodeID());

            statement1.execute();

            statement2 = connection.prepareStatement(
                    "DELETE FROM " + Constants.NODES_TABLE +
                            " WHERE NODEID=?"
            );

            statement2.setString(1, deleteLocation.getNodeID());

            return statement2.execute();

        } catch (SQLException e) {
            System.out.println("Failed to update location: " + deleteLocation.getNodeID());
            e.printStackTrace();

            return false;
        }


    }

    /**
     * Updates the edge object specified on the database
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
    public List<Admin> getAdmins() throws Exception {

        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.ADMIN_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Admin> returnList = new ArrayList<>();

            while(resultSet.next()) {

                User user = getUserByID(resultSet.getInt("ADMINID"));

                if(user == null) {
                    throw new Exception("Users and admin tables not correctly linked!");
                }

                Admin admin = new Admin(
                        user.getUserID(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getUserType()
                );

                returnList.add(admin);

            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Failed to get admins!");

            return null;
        }
    }

    /**
     * Returns a list of employees
     */
    public List<Employee> getEmployees() throws Exception {
        try {

            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.EMPLOYEE_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Employee> returnList = new ArrayList<>();

            while(resultSet.next()) {

                User user = getUserByID(resultSet.getInt("EMPLOYEEID"));

                if(user == null) {
                    throw new Exception("Users and employee tables not correctly linked!");
                }

                Employee employee = new Employee(
                        user.getUserID(),
                        user.getUsername(),
                        user.getPassword(),
                        user.getUserType()
                );

                returnList.add(employee);

            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Failed to get employees!");

            return null;
        }
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
}
