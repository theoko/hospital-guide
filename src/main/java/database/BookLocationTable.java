package database;

import helpers.Constants;
import models.room.Book;
import models.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookLocationTable {

    private static void BookLocationTable() { }

    public static void createTable(){
        Statement statement1 = null;
        try {
            statement1 = Database.getDatabase().getConnection().createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String bookLocationTable = "CREATE TABLE " + Constants.BOOK_LOCATION_TABLE + "(" +
                "bookingID INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)," +
                "nodeID VARCHAR(100)," +
                "userID INT," +
                "startDate TIMESTAMP," +
                "endDate TIMESTAMP," +
                "CONSTRAINT roomID2_fk FOREIGN KEY(nodeID) REFERENCES " + Constants.LOCATION_TABLE+ "(nodeID)," +
                "CONSTRAINT userID2_fk FOREIGN KEY(userID) REFERENCES " + Constants.USERS_TABLE + "(userID))";

        try {
            statement1.execute(bookLocationTable);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Create a booking for a room
     * @param book
     */
    public static boolean createBooking(Book book) {

        try {

            PreparedStatement statement;
            statement = Database.getDatabase().getConnection().prepareStatement(
                    "INSERT INTO " + Constants.BOOK_LOCATION_TABLE + " (NODEID, USERID, STARTDATE, ENDDATE) " +
                            "VALUES (?, ?, ?, ?)"
            );

            statement.setString(1, book.getRoomID());
            statement.setInt(2, UserTable.getUserByUsername(book.getUser().getUsername()).getUserID());
            statement.setTimestamp(3, Timestamp.valueOf(book.getStartDate()));
            statement.setTimestamp(4, Timestamp.valueOf(book.getEndDate()));

            return statement.execute();

        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }

    }

    public static boolean dropTable() {
        try {
            Statement statement;

            statement = Database.getDatabase().getConnection().createStatement();

            return statement.execute("DROP TABLE " + Constants.BOOK_LOCATION_TABLE);
        } catch (SQLException e) {
            System.out.println("Table " + Constants.BOOK_LOCATION_TABLE + " cannot be dropped");

            return false;
        }
    }

    public static Book getBookByRoomID(String roomID) {
        try {
            PreparedStatement statement;
            statement = Database.getDatabase().getConnection().prepareStatement(
                    "SELECT * FROM " + Constants.BOOK_LOCATION_TABLE + " WHERE ROOMID=?"
            );
            statement.setString(1, roomID);

            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()) {

                Book book = new Book(
                        resultSet.getInt("BOOKINGID"),
                        resultSet.getString("ROOMID"),
                        UserTable.getUserByID(resultSet.getInt("USERID")),
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

            User userByUsername = UserTable.getUserByUsername(user.getUsername());
            if(userByUsername == null) {
                return null;
            }

            int userID = userByUsername.getUserID();

            PreparedStatement statement;

            statement = Database.getDatabase().getConnection().prepareStatement(
                    "SELECT * FROM " + Constants.BOOK_LOCATION_TABLE + " WHERE USERID=?"
            );

            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();
            List<Book> bookings = new ArrayList<>();

            while(resultSet.next()) {

                Book book = new Book(
                        resultSet.getInt("BOOKINGID"),
                        resultSet.getString("NODEID"),
                        UserTable.getUserByID(resultSet.getInt("USERID")),
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

    /**
     * getBookings
     */
    public List<Book> getBookings() {
        try {
            Statement statement;

            statement = Database.getDatabase().getConnection().createStatement();

            String query = "SELECT * FROM " + Constants.BOOK_LOCATION_TABLE;

            ResultSet resultSet = statement.executeQuery(query);

            ArrayList<Book> returnList = new ArrayList<>();

            while (resultSet.next()) {

                Book user = new Book(
                        resultSet.getInt("BOOKINGID"),
                        resultSet.getString("ROOMID"),
                        UserTable.getUserByID(resultSet.getInt("USERID")),
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
}
