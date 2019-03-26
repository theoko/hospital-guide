package database;

import helpers.Constants;

import javax.lang.model.type.NullType;
import javax.swing.text.StyledEditorKit;
import java.sql.*;

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

    private void createTables() {
        Statement statement = null;
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        dropUsersTable();
        dropEmployeeTable();
        dropCustodianTable();
        dropAdminTable();
        dropNodesTable();
        dropEdgesTable();

        String usersTable = "CREATE TABLE " + Constants.USERS_TABLE +
                "(userID INT CONSTRAINT usersTable_pk PRIMARY KEY," +
                " username VARCHAR(20), " +
                "password VARCHAR(32)," +
                " userType INT)";

        String employeeTable = "CREATE TABLE " + Constants.EMPLOYEE_TABLE +
                "(employeeID INT," +
                " userID INT REFERENCES USERS(userID))";

        String custodianTable = "CREATE TABLE " + Constants.CUSTODIAN_TABLE +
                "(employeeID INT REFERENCES EMPLOYEE(employeeID))";

        String adminTable = "CREATE TABLE " + Constants.EMPLOYEE_TABLE +
                "(FOREIGN KEY (userID) REFERENCES EMPLOYEE(employeeID))";

        String nodesTable = "CREATE TABLE " + Constants.NODES_TABLE +
                "(nodeID VARCHAR(30) PRIMARY KEY," +
                " xCoord INT," +
                " yCoord INT," +
                " floor INT," +
                " building VARCHAR(30)," +
                " nodeType VARCHAR(30)," +
                " longName VARCHAR(30)," +
                " shortName VARCHAR(30))";

        String edgesTable = "CREATE TABLE " + Constants.EDGES_TABLE +
                "(edgeID VARCHAR(30)," +
                " FOREIGN KEY (startNodeID) REFERENCES NODES(userID)," +
                " FOREIGN KEY (endNodeID) REFERENCES NODES(userID))";

        try {

            // TODO: check if table exists
            boolean createUsersResult = statement.execute(usersTable);
            boolean createEmployeeResult = statement.execute(employeeTable);
            boolean createCustodianResult = statement.execute(custodianTable);
            boolean createAdminResult = statement.execute(adminTable);

            boolean createNodesResult = statement.execute(nodesTable);
            boolean createEdgesTable = statement.execute(edgesTable);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    private boolean dropUsersTable(){
        try {
            Statement statement = null;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.USERS_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.USERS_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropEmployeeTable(){
        try {
            Statement statement = null;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.EMPLOYEE_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.EMPLOYEE_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropCustodianTable(){
        try {
            Statement statement = null;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.CUSTODIAN_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.CUSTODIAN_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropAdminTable(){
        try {
            Statement statement = null;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.ADMIN_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.ADMIN_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropNodesTable(){
        try {
            Statement statement = null;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.NODES_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.NODES_TABLE + " cannot be dropped");

            return false;
        }
    }

    private boolean dropEdgesTable(){
        try {
            Statement statement = null;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.EDGES_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.EDGES_TABLE + " cannot be dropped");

            return false;
        }
    }

    public static void main(String[] args) {
        Database db = new Database();
        db.createTables();

    }
}
