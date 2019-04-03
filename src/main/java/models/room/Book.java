package models.room;

import models.user.User;

import java.util.Date;

public class Book {
    private int bookingID;
    private String roomID;
    private User user;
    private String startDate;
    private String endDate;

    public Book(int bookingID, String roomID, User user, String startDate, String endDate) {
        this.bookingID = bookingID;
        this.roomID = roomID;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
