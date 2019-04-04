package database;

import helpers.Constants;
import models.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserTable {
    private static Connection connection;

    private static void createUserTable() {
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

        try {
            statement.execute(usersTable);
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates user based off of database
     */
    public static boolean createUser(User user) {

        try {

            User checkUser = getUserByUsername(user.getUsername());

            // We need to create the user
            if (checkUser == null) {

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

    public static boolean dropUsersTable() {
        try {
            Statement statement;

            statement = connection.createStatement();

            return statement.execute("DROP TABLE " + Constants.USERS_TABLE);

        } catch (SQLException e) {
            System.out.println("Table " + Constants.USERS_TABLE + " cannot be dropped");

            return false;
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
}
