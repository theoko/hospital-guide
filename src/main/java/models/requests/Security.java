package models.requests;



public class Security {
    public enum ThreatLevel {
        LOW, MEDIUM, HIGH, MIDNIGHT
    }
    private ThreatLevel threatLevel;
    private String description;

    public Security(ThreatLevel threatLevel, String description) {
        this.threatLevel = threatLevel;
        this.description = description;
    }

    public String getThreatLevel() {
        return threatLevel.name();
    }

    public String getDescription() {
        return description;
    }
}
