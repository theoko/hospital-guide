package helpers;

public class SpillModel {
    private String loc;
    private String priority;
    private String status;

    public String getLoc() {
        return loc;
    }

    public String getPriority() {
        return priority;
    }

    public String getStatus() {
        return status;
    }

    public SpillModel(String loc, String priority, String status){
        this.loc = loc;
        this.priority = priority;
        this.status = status;
    }
}

