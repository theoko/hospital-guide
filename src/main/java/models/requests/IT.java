package models.requests;

import models.user.User;

public class IT {

    private int requestID;

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    private Priority priority;

    public enum Type {
        SOFTWARE, HARDWARE, NETWORK, OTHER
    }

    private Type type;
    private String Desc;

    public enum Building {
        BTM, FRAN45, TOWER, FRAN15, SHAPIRO
    }

    private Building build;

    public enum Room {
        CONFA, CLASSA, WZA, CONFB, CLASSB, WZB, CONFC, CLASSC, WZC,
    }

    private Room room;

    public enum reqTime {
        ASAP, LATER
    }

    private reqTime rTime;
    private String Time;
    private String user;


    public IT(int requestID, Priority priority, Type type, String desc, Building build, Room room, String time, String user) {
        this.requestID = requestID;
        this.priority = priority;
        this.type = type;
        this.Desc = desc;
        this.build = build;
        this.room = room;
        this.Time = time;
        this.user = user;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public Building getBuild() {
        return build;
    }

    public void setBuild(Building build) {
        this.build = build;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public reqTime getrTime() {
        return rTime;
    }

    public void setrTime(reqTime rTime) {
        this.rTime = rTime;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}