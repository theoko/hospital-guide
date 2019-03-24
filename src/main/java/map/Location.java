package map;

import java.util.ArrayList;

public class Location {

    private String nodeID;
    private int xCord;
    private int yCord;
    private String floor;
    private String building;
    private String nodeType;
    private String longName;
    private String shortName;
    private ArrayList<Location> lstNeighbors;

    public Location(String nodeID, int xCord, int yCord, String floor, String building, String nodeType, String longName, String shortName) {
        this.nodeID = nodeID;
        this.xCord = xCord;
        this.yCord = yCord;
        this.floor = floor;
        this.building = building;
        this.nodeType = nodeType;
        this.longName = longName;
        this.shortName = shortName;
        this.lstNeighbors = new ArrayList<Location>();
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

    public String getNodeType() {
        return nodeType;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public void addNeighbor(Location neighbor) {
        lstNeighbors.add(neighbor);
    }

    public ArrayList<Location> getNeighbors() {
        return lstNeighbors;
    }
}
