package models.map;

import controllers.AdminMapController;
import controllers.VisualRealtimeController;
import database.Database;
import database.LocationTable;
import helpers.Constants;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Location {

    private String nodeID;
    private Circle nodeCircle;
    private int xCord;
    private int yCord;
    private String floor;
    private String building;
    private Constants.NodeType nodeType;
    private String longName;
    private String shortName;
    private ArrayList<SubPath> lstSubPaths;
    private Location parent;
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
        this.parent = null;
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

    public String getDBFormattedFloor() {
        if(floor.length() == 1) return "0" + floor;
        else return floor;
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

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public void setxCord(int xCord) {
        this.xCord = xCord;
    }

    public void setyCord(int yCord) {
        this.yCord = yCord;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void addSubPath(SubPath subPath) {
        lstSubPaths.add(subPath);
    }

    public ArrayList<SubPath> getSubPaths() {
        return lstSubPaths;
    }

    public Location getParent() {
        return parent;
    }

    public void setParent(Location parent) {
        this.parent = parent;
    }

    public boolean getAvailable(){ return available; }

    public void setAvailable(boolean isAvailable){
        this.available = isAvailable;
    }

    public String[] getStringsLocation() {
        return new String[]{nodeID, Integer.toString(xCord), Integer.toString(yCord), floor, building,
            nodeType.toString(), longName, shortName};
    }

    public void setNodeType(Constants.NodeType nodeType) {
        this.nodeType = nodeType;
        LocationTable.updateLocation(this);
    }

    public boolean deleteCurrNode() {
        VisualRealtimeController.removeCircle(this);
        return LocationTable.deleteLocation(this);
    }

    public Circle getNodeCircle() {
        return nodeCircle;
    }

    public void setNodeCircle(Circle nodeCircle) {
        this.nodeCircle = nodeCircle;
    }

}
