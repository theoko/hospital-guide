package models.map;

import helpers.Constants;

import java.util.ArrayList;

public class Location {

    private String nodeID;
    private int xCord;
    private int yCord;
    private String floor;
    private String building;
    private Constants.NodeType nodeType;
    private String longName;
    private String shortName;
    private ArrayList<SubPath> lstSubPaths;

    public Location(String nodeID, int xCord, int yCord, String floor, String building, Constants.NodeType nodeType, String longName, String shortName) {
        this.nodeID = nodeID;
        this.xCord = xCord;
        this.yCord = yCord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.lstSubPaths = new ArrayList<>();
    }

    public String getNodeID() {
        return nodeID;
    }

    public int getxCord() {
        return xCord;
    }

    public int getyCord() {
        return yCord;
    }

    public String getFloor() {
        return floor;
    }

    public String getBuilding() {
        return building;
    }

    public Constants.NodeType getNodeType() {
        return nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void addNeighbor(SubPath subPath) {
        lstSubPaths.add(subPath);
    }

    public ArrayList<SubPath> getNeighbors() {
        return lstSubPaths;
    }

}
