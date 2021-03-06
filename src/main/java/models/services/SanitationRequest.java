/**
 * @brief Class for implementing sanitation requests.
 */

package models.services;
import models.map.Location;
import models.user.User;
import java.util.Date;
import java.sql.Timestamp;

public class SanitationRequest extends ServiceRequest implements Comparable<SanitationRequest> {

    /**
     * Enumerations
     */
    public enum Priority {
        LOW, MEDIUM, HIGH
    }
    Priority priority;

    /**
     * @brief Constructs new sanitation request to add to database.
     * @param location Location of the spill
     * @param priority Priority of the request
     * @param description Textual description of request
     * @param requester Employee who made request
     */
    public SanitationRequest(Location location, Priority priority, String description, User requester)
    {
        super(
                0, location, Status.INCOMPLETE, description,
                requester, new Timestamp(new Date().getTime()),
                null, null, null
        );
        this.priority = priority;
    }

    /**
     * @brief Constructs sanitation request with all fields.
     * @param requestID Request ID (generated by database)
     * @param location Location of the spill
     * @param priority Priority of the request
     * @param status Status of request
     * @param description Textual description of request
     * @param requester Employee who made request
     * @param requestTime Time request was made
     * @param servicer Custodian who fulfilled the request
     * @param claimedTime Time service was claimed by a custodian
     * @param completedTime Time service was marked completed
     */
    public SanitationRequest(
            int requestID,
            Location location,
            Priority priority,
            Status status,
            String description,
            User requester,
            Timestamp requestTime,
            User servicer,
            Timestamp claimedTime,
            Timestamp completedTime) {
        super(
                requestID, location, status, description,
                requester, requestTime,
                servicer, claimedTime, completedTime
        );
        this.priority = priority;
    }

    /**
     * Attribute getters
     */
    public Priority getPriority() {
        return priority;
    }

    /**
     * Converts status to comparable integer.
     */
    private Integer statusToInt() {
        switch(status) {
            case INCOMPLETE: return 1;
            case COMPLETE: return 2;
            default: return 0;
        }
    }

    /**
     * Converts claimed status to comparable integer.
     */
    private Integer claimedToInt() {
        if (servicer == null) return 1;
        else return 2;
    }

    /**
     * Converts priority to comparable integer.
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
     * @brief Compares sanitation requests based on the following field order:
     * - Incomplete before complete
     * - Unclaimed before claimed
     * - Highest priority first
     * - Earliest request time first
     */
    public int compareTo(SanitationRequest request) {

        // Compare by status (incomplete before complete)
        Integer cmp = statusToInt().compareTo(request.statusToInt());
        if (cmp != 0) return cmp;

        // Compare by claimed status (unclaimed before claimed)
        cmp = claimedToInt().compareTo(request.claimedToInt());
        if (cmp != 0) return cmp;

        // Compare by priority (highest first)
        cmp = priorityToInt().compareTo(request.priorityToInt());
        if (cmp != 0) return cmp;

        // Compare by request time (earliest first)
        return requestTime.compareTo(request.requestTime);
    }
}