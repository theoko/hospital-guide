package models.requests;

import models.sanitation.SanitationRequest;

public class GIFT {

    private int requestID;

    public enum Type {
        Flowers, Chocolate, Stuffed_Bear
    }

    private Type type;

    public enum Size {
        Small,Medium,Large
    }

    private Size size;
private String note;
private String sender;
private String recipient;



    public GIFT(int requestID, Type gift, Size size, String note,String recipient, String sender) {
        this.requestID = requestID;
        this.type = type;
        this.size = size;
        this.note = note;
        this.sender = sender;
        this.recipient = recipient;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
}