package models.requests;

import javafx.beans.property.SimpleStringProperty;

public class InterpreterRequest {
        private final String loc, lang;
        private String complete = "INCOMPLETE";

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public InterpreterRequest(String s, String r) {
            this.loc = s;
            this.lang = r;
        }

    public String getLoc() {
        return loc;
    }

    public String getLang() {
        return lang;
    }
}
