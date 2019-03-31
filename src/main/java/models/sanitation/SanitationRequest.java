/**
 * @brief Class for implementing sanitation requests.
 */

package models.sanitation;
import models.map.Location;

public class SanitationRequest implements Comparable<SanitationRequest> {

    /**
     * Enumeration for request priority.
     */
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    /**
     * Class fields.
     */
    private Location location;  // Location of the spill
    private Priority priority;  // Priority of the request
    private String description; // Textual description of request

    /**
     * @brief Constructs new sanitation request.
     * @param location Location of the spill
     * @param priority Priority of the request
     * @param description Textual description of request
     */
    public SanitationRequest(Location location, Priority priority, String description) {
        this.location = location;
        this.priority = priority;
        this.description = description;
    }

    /**
     * Converts priority to integer
     */
    private Integer priorityToInt() {
        switch(priority) {
            case HIGH: return 1;
            case MEDIUM: return 2;
            case LOW: return 3;
            default: return 0;
        }
    }

    /**
     * @brief Compares sanitation requests based on priority.
     */
    public int compareTo(SanitationRequest request) {
        return priorityToInt().compareTo(request.priorityToInt());
    }

    /**
     * @brief Converts sanitation request to string.
     */
    public String toString() {
        String string = "Request: " +
                "Location: " + location.getNodeID() + ", " +
                "Priority: " + priority.name() + ", " +
                "Description: " + description;
        return string;
    }

    /**
     * Attribute getters
     */
    public Location getLocation() {
        return location;
    }
    public Priority getPriority() { return priority; }
    public String getDescription() {
        return description;
    }
}