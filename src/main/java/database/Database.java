package database;

import helpers.Constants;
import models.user.Admin;
import models.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    Connection connection;

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
                "(nodeID VARCHAR(30) PRIMARY KEY," +
                "xCoord INT," +
                "yCoord INT," +
                "floor INT," +
                "building VARCHAR(30)," +
                "nodeType VARCHAR(30)," +
                "longName VARCHAR(30)," +
                "shortName VARCHAR(30))";

        String edgesTable = "CREATE TABLE " + Constants.EDGES_TABLE +
                "(edgeID VARCHAR(30) PRIMARY KEY," +
                "startNodeID VARCHAR(30)," +
                "endNodeID VARCHAR(30)," +
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
        try {

            PreparedStatement statement;

            statement = connection.prepareStatement(
                    "SELECT * FROM " + Constants.USERS_TABLE + " WHERE USERID=?"
            );

            statement.setInt(1, adminID);

            ResultSet resultSet = statement.executeQuery();

            Admin admin = new Admin(
                    resultSet.getInt("USERID"),
                    resultSet.getString("USERNAME"),
                    resultSet.getString("PASSWORD"),
                    resultSet.getInt("USERTYPE")
            );

            return admin;

        } catch (SQLException e) {
            System.out.println("Cannot get admin by ID!");

            return null;
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
     * Returns a list of admins
     */
    public List<Admin> getAdmins() throws Exception {
        try {
            Statement statement;

            statement = connection.createStatement();

            String query = "SELECT * FROM " + Constants.ADMIN_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            List<Admin> returnList = new ArrayList<>();

            while(resultSet.next()) {

                int adminID = resultSet.getInt("ADMINID");

                // Get admin
                Admin adminReturned = getAdminByID(adminID);

                if(adminReturned == null) {
                    throw new Exception("Admin table is not correctly linked with the user table");
                }

                Admin admin = new Admin(
                        resultSet.getInt("ADMINID"),
                        adminReturned.getUsername(),
                        adminReturned.getPassword(),
                        adminReturned.getUserType()
                );

                returnList.add(admin);

            }

            return returnList;

        } catch (SQLException e) {
            System.out.println("Failed to get admins!");

            return null;
        }
    }

    public static void main(String[] args) {
        Database db = new Database();
        db.createTables();

    }
}
