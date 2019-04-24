/**
 * @brief Class for implementing internal transportation requests.
 */

package models.services;

import models.map.Location;
import models.user.User;

import java.sql.Timestamp;
import java.util.Date;

public class SimpleEmployee {

    String name;   // Destination of transportation
    int requests;
    int claimed;
    int completed;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
    }

    public int getClaimed() {
        return claimed;
    }

    public void setClaimed(int claimed) {
        this.claimed = claimed;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public SimpleEmployee(
            String name

    ) {
        this.name = name;
    }


}
