package models.map;

import database.Database;
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
    private boolean available;

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
        this.available = available;
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

    public void addSubPath(SubPath subPath) {
        lstSubPaths.add(subPath);
    }

    public ArrayList<SubPath> getSubPaths() {
        return lstSubPaths;
    }

    public boolean getAvailable(){ return available; }

    public void setAvailable(boolean isAvailable){
        this.available = isAvailable;
    }

    public String[] getStrings() {
        return new String[]{nodeID, Integer.toString(xCord), Integer.toString(yCord), floor, building,
            nodeType.toString(), longName, shortName};
    }

    public void setNodeType(Constants.NodeType nodeType) {
        this.nodeType = nodeType;
        Database.updateLocation(this);
    }

    public boolean deleteCurrNode() {
        return Database.deleteLocation(this);
    }
}
