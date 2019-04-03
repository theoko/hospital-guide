package models.room;

import java.util.Date;

public class Book {
    private int bookingID;
    private String roomID;
    private int userID;
    private String startDate;
    private String endDate;

    public Book(int bookingID, String roomID, int userID, String startDate, String endDate) {
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.userID = userID;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getBookingID() {
        return bookingID;
    }

    public void setBookingID(int bookingID) {
        this.bookingID = bookingID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
