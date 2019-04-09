package models.requests;

public class VisualAudio {
    private String type;
    private String location;
    private String description;
    private String user;

    public VisualAudio(String type, String location, String description, String user) {
        this.type = type;
        this.location = location;
        this.description = description;
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getUser() {
        return user;
    }
}
