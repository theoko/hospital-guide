package models.requests;

import models.user.User;

public class Florist {

    private String RecipientName;

    public enum FlowerType {
        ROSE, TULIP, HYDRANGEA, OTHER
    }

    private FlowerType flowerType;

    private String Message;
    private String Time;
    private String DeliveryDate;
    private String user;

    public Florist(String recipientName, FlowerType flowerType, String message, String time, String date, String user) {
        this.RecipientName = recipientName;
        this.flowerType = flowerType;
        this.Message = message;
        this.Time = time;
        this.DeliveryDate = date;
        this.user = user;
    }

    public String getRecipientName() {
        return RecipientName;
    }

    public void setRecipientName(String recipientName) {
        RecipientName = recipientName;
    }

    public FlowerType getFlowerType() {
        return flowerType;
    }

    public void setFlowerType(FlowerType flowerType) {
        this.flowerType = flowerType;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDate(String date) {
        DeliveryDate = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}