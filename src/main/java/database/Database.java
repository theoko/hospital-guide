package database;

import helpers.Constants;

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

        String usersTable = "CREATE TABLE Users (userID INT, username VARCHAR(20), password VARCHAR(32), userType INT)";

        try {
//            ResultSet resultSet = statement.executeQuery(usersTable);
            boolean result = statement.execute(usersTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Database db = new Database();
        db.createTables();

    }
}
