package database;

import helpers.Constants;
import models.map.Location;
import models.map.Neighbor;
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

    public Database() {

        try {
            DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:derby:" + Constants.DB_NAME + ";create=true");
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        dialect = new DerbyTemplates();
//        configuration = new Configuration(dialect);
//        sqlQueryFactory = new SQLQueryFactory(configuration, dataSource);
    }

    /**
     * Creates the basic database tables
     */
    private void createTables() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dropEdgesTable();
        dropNodesTable();
        dropAdminTable();
        dropCustodianTable();
        dropEmployeeTable();
        dropUsersTable();

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

        String nodesTable = "CREATE TABLE " + Constants.NODES_TABLE +
                "(nodeID VARCHAR(100) PRIMARY KEY," +
                "xCoord INT," +
                "yCoord INT," +
                "floor VARCHAR(100)," +
                "building VARCHAR(100)," +
                "nodeType VARCHAR(100)," +
                "longName VARCHAR(100)," +
                "shortName VARCHAR(100))";

        String edgesTable = "CREATE TABLE " + Constants.EDGES_TABLE +
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

            boolean createNodesResult = statement.execute(nodesTable);
            boolean createEdgesTable = statement.execute(edgesTable);

            if(!createUsersResult) {
                System.out.println("Failed to create users table");
            }

            if(!createEmployeeResult) {
                System.out.println("Failed to create employee table");
            }

            if(!createCustodianResult) {
                System.out.println("Failed to create custodian table");
            }

            if(!createAdminResult) {
                System.out.println("Failed to create admin table");
            }

            if(!createNodesResult) {
                System.out.println("Failed to create nodes table");
            }

            if(!createEdgesTable) {
                System.out.println("Failed to create edges table");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    /**
     * Drop tables
     */
    private boolean dropUsersTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.USERS_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.USERS_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropEmployeeTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.EMPLOYEE_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.EMPLOYEE_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropCustodianTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.CUSTODIAN_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.CUSTODIAN_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropAdminTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.ADMIN_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.ADMIN_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropNodesTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.NODES_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.NODES_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropEdgesTable(){
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.EDGES_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.EDGES_TABLE + " cannot be dropped");

            return false;
        }
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

    public static boolean addNode(Location location) {

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
            statement.setString(6, location.getNodeType().toString());
            statement.setString(7, location.getLongName());
            statement.setString(8, location.getShortName());

            statement.execute();

            return true;

        } catch (SQLException e) {
            System.out.println("Location cannot be added!");
            e.printStackTrace();

            return false;
        }

    }

    public static boolean addEdge(Neighbor neighbor) {

        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "INSERT INTO " + Constants.EDGES_TABLE + " (EDGEID, STARTNODEID, ENDNODEID) " +
                            "VALUES (?, ?, ?)"
            );

            statement.setString(1, neighbor.getEdgeID());
            statement.setString(2, neighbor.getLocation().getNodeID());
            statement.setString(3, String.valueOf(neighbor.getDist()));

            statement.execute();

            return true;

        } catch (SQLException e) {
            System.out.println("Neighbor cannot be added!");

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
     * Returns a list of admins
     */
    public List<Admin> getAdmins() throws Exception {
        return new ArrayList<>();
    }

    /**
     * Returns a list of employees
     */
    public List<Employee> getEmployees() throws Exception {
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        Database db = new Database();

//        db.createTables();

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
