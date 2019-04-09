package models.requests;

import models.user.User;


public class Patient {

    //private int requestID;

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    private Priority priority;

    public String desc;
    public String name;
    public String dob;

    public Patient(String name, String dob, String desc, Priority priority) {
        this.name = name;
        this.dob = dob;
        this.desc = desc;
        this.priority = priority;
    }


    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
}
