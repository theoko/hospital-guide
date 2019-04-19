package models.map;

import helpers.Constants;

public class LocationTableEntry {
    private String ID, floor, building, name;
    private Constants.NodeType type;
    private int x, y;

    public LocationTableEntry(Location loc) {
        this.ID = loc.getNodeID();
        this.floor = loc.getFloor();
        this.building = loc.getBuilding();
        this.name = loc.getShortName();
        this.x = loc.getxCord();
        this.y = loc.getyCord();
        this.type = loc.getNodeType();
    }
    public Location toLocation() {
        return new Location(ID, x, y, floor, building, type, name, name);
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFloor() {
        return floor;
    }

    public String getType() {
        return type.toString();
    }

    public void setType(Constants.NodeType type) {
        this.type = type;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}