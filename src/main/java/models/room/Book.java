package models.room;

import java.util.Date;

public class Book {
    private int bookingID;
    private String roomID;
    private int userID;
    private Date startDate;
    private Date endDate;

    public Book(int bookingID, String roomID, int userID, Date startDate, Date endDate) {
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
