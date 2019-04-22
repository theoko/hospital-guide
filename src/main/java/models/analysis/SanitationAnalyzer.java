/**
 * Class for analyzing data from the sanitation request database.
 */

package models.analysis;

import database.SanitationTable;
import database.UserTable;
import models.services.SanitationRequest;
import models.user.User;

import java.util.ArrayList;
import java.util.HashMap;

public class SanitationAnalyzer
{
    /**
     * Requests loaded from database
     */
    private ArrayList<SanitationRequest> requests;

    /**
     * Summary statistics
     */
    private Integer numRequests;            // Total number of requests
    private Integer numIncompleteRequests;  // Number of incomplete requests
    private Integer numClaimedRequests;     // Number of claimed requests
    private Integer numCompletedRequests;   // Number of completed requests
    private Integer percentIncomplete;      // Percentage of requests incomplete
    private Integer percentClaimed;         // Percentage of requests claimed
    private Integer percentCompleted;       // Percentage of requests completed
    private Integer meanClaimedTime;        // Mean request to claim time (minutes)
    private Integer minClaimedTime;         // Min request to claim time (minutes)
    private Integer maxClaimedTime;         // Max request to claim time (minutes)
    private Integer meanCompletedTime;      // Mean claim to complete time (minutes)
    private Integer minCompletedTime;       // Min claim to complete time (minutes)
    private Integer maxCompletedTime;       // Max claim to complete time (minutes)

    /**
     * Employee statistics
     */
    private ArrayList<String> employees = new ArrayList<>();
    private ArrayList<String> custodians = new ArrayList<>();
    private HashMap<String, Integer> employeeRequestCount = new HashMap<>();
    private HashMap<String, Integer> custodianClaimedCount = new HashMap<>();
    private HashMap<String, Integer> custodianCompletedCount = new HashMap<>();

    /**
     * Constructor which analyzes all sanitation requests from the database.
     */
    public SanitationAnalyzer() {
        this(SanitationTable.getSanitationRequests());
    }

    /**
     * Constructor which analyzes list of sanitation requests.
     */
    public SanitationAnalyzer(ArrayList<SanitationRequest> requests)
    {
        // Load requests from database
        this.requests = requests;

        // Calculate summary statistics
        numRequests = requests.size();
        numIncompleteRequests = 0;
        numClaimedRequests = 0;
        numCompletedRequests = 0;
        for (SanitationRequest request : requests)
        {
            if (request.isCompleted()) numCompletedRequests++;
            if (request.isClaimed()) numClaimedRequests++;
            else numIncompleteRequests++;
        }
        percentIncomplete = (int)((double)numIncompleteRequests / (double)numRequests * 100.0);
        percentClaimed = (int)((double)numClaimedRequests / (double)numRequests * 100.0);
        percentCompleted = (int)((double)numCompletedRequests / (double)numRequests * 100.0);

        // Calculate claimed time statistics
        meanClaimedTime = 0;
        minClaimedTime = Integer.MAX_VALUE;
        maxClaimedTime = Integer.MIN_VALUE;
        for (SanitationRequest request : requests)
        {
            Integer claimedTime = request.getMinutesRequestedToClaimed();
            if (claimedTime != null)
            {
                meanClaimedTime += claimedTime;
                if (claimedTime < minClaimedTime) minClaimedTime = claimedTime;
                if (claimedTime > maxClaimedTime) maxClaimedTime = claimedTime;
            }
        }
        if (numClaimedRequests == 0)
        {
            minClaimedTime = 0;
            maxClaimedTime = 0;
        }
        else
        {
            meanClaimedTime /= numClaimedRequests;
        }

        // Calculate completed time statistics
        meanCompletedTime = 0;
        minCompletedTime = Integer.MAX_VALUE;
        maxCompletedTime = Integer.MIN_VALUE;
        for (SanitationRequest request : requests)
        {
            Integer completedTime = request.getMinutesClaimedToCompleted();
            if (completedTime != null)
            {
                meanCompletedTime += completedTime;
                if (completedTime < minCompletedTime) minCompletedTime = completedTime;
                if (completedTime > maxCompletedTime) maxCompletedTime = completedTime;
            }
        }
        if (meanCompletedTime == 0)
        {
            minCompletedTime = 0;
            maxCompletedTime = 0;
        }
        else
        {
            meanCompletedTime /= numCompletedRequests;
        }

        // Calculate employee statistics
        for (User employee : UserTable.getEmployees())
        {
            employees.add(employee.getUsername());
        }
        for (String employee : employees)
        {
            employeeRequestCount.put(employee, 0);
        }
        for (SanitationRequest request : requests)
        {
            String employee = request.getRequesterUserName();
            Integer newRequestCount = employeeRequestCount.get(employee) + 1;
            employeeRequestCount.put(employee, newRequestCount);
        }

        // Calculate custodian statistics
        for (User custodian : UserTable.getCustodians())
        {
            custodians.add(custodian.getUsername());
        }
        for (String custodian : custodians)
        {
            custodianClaimedCount.put(custodian, 0);
            custodianCompletedCount.put(custodian, 0);
        }
        for (SanitationRequest request : requests)
        {
            if (request.isClaimed())
            {
                String custodian = request.getServicerUserName();
                Integer newClaimedCount = custodianClaimedCount.get(custodian) + 1;
                custodianClaimedCount.put(custodian, newClaimedCount);
                if (request.isCompleted())
                {
                    Integer newCompletedCount = custodianCompletedCount.get(custodian) + 1;
                    custodianCompletedCount.put(custodian, newCompletedCount);
                }
            }
        }

        // TODO remove
        print();
    }

    /**
     * Prints summary statistics to the console.
     */
    public void print()
    {
        // Title
        System.out.println("\nSanitation Analyzer\n");

        // Summary Statistics
        System.out.println("Summary Statistics");
        System.out.println("------------------");
        System.out.println("Requests:\t" + numRequests);
        System.out.println("Incomplete:\t" + percentIncomplete + "%");
        System.out.println("Claimed:\t" + percentClaimed + "%");
        System.out.println("Completed:\t" + percentCompleted);
        System.out.println();

        // Claimed Time Statistics
        System.out.println("Request to Claim Time (min)");
        System.out.println("---------------------------");
        System.out.println("Mean:\t" + meanClaimedTime);
        System.out.println("Min:\t" + minClaimedTime);
        System.out.println("Max:\t" + maxClaimedTime);
        System.out.println();

        // Completed Time Statistics
        System.out.println("Claim to Complete Time (min)");
        System.out.println("----------------------------");
        System.out.println("Mean:\t" + meanCompletedTime);
        System.out.println("Min:\t" + minCompletedTime);
        System.out.println("Max:\t" + maxCompletedTime);
        System.out.println();

        // Employee Statistics
        System.out.println("Employee\tRequests");
        System.out.println("--------------------");
        for (String employee : employees)
        {
            Integer requestCount = employeeRequestCount.get(employee);
            System.out.println(employee + "\t" + requestCount);
        }
        System.out.println();

        // Custodian Statistics
        System.out.println("Custodian\tClaimed\tCompleted");
        System.out.println("-----------------------------");
        for (String custodian : custodians)
        {
            Integer claimedCount = custodianClaimedCount.get(custodian);
            Integer completedCount = custodianCompletedCount.get(custodian);
            System.out.println(custodian + "\t" + claimedCount + "\t\t" + completedCount);
        }
        System.out.println();
    }

    /**
     * Attribute getters
     */
    public ArrayList<SanitationRequest> getRequests() {
        return requests;
    }
    public Integer getNumRequests() {
        return numRequests;
    }
    public Integer getNumIncompleteRequests() {
        return numIncompleteRequests;
    }
    public Integer getNumClaimedRequests() {
        return numClaimedRequests;
    }
    public Integer getNumCompletedRequests() {
        return numCompletedRequests;
    }
    public Integer getPercentIncomplete() {
        return percentIncomplete;
    }
    public Integer getPercentClaimed() {
        return percentClaimed;
    }
    public Integer getPercentCompleted() {
        return percentCompleted;
    }
    public Integer getMeanClaimedTime() {
        return meanClaimedTime;
    }
    public Integer getMinClaimedTime() {
        return minClaimedTime;
    }
    public Integer getMaxClaimedTime() {
        return maxClaimedTime;
    }
    public Integer getMeanCompletedTime() {
        return meanCompletedTime;
    }
    public Integer getMinCompletedTime() {
        return minCompletedTime;
    }
    public Integer getMaxCompletedTime() {
        return maxCompletedTime;
    }
    public HashMap<String, Integer> getEmployeeRequestCount() {
        return employeeRequestCount;
    }
    public HashMap<String, Integer> getCustodianClaimedCount() {
        return custodianClaimedCount;
    }
    public HashMap<String, Integer> getCustodianCompletedCount() {
        return custodianCompletedCount;
    }

    public ArrayList<String> getEmployees(){
        return employees;
    }
    public ArrayList<String> getCustodians(){
        return custodians;
    }
}