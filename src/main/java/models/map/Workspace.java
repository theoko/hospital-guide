package models.map;

import controllers.AdminMapController;
import controllers.VisualRealtimeController;
import database.Database;
import database.LocationTable;
import database.WorkspaceTable;
import helpers.Constants;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Workspace {

    private String nodeID;
    private Circle nodeCircle;
    private int xCord;
    private int yCord;
    private Constants.NodeType nodeType;
    private String longName;
    private boolean available;

    public Workspace(String nodeID, int xCord, int yCord, Constants.NodeType nodeType, String longName) {
        this.nodeID = nodeID;
        this.xCord = xCord;
        this.yCord = yCord;
        this.nodeType = nodeType;
        this.longName = longName;
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
    public Constants.NodeType getNodeType() {
        return nodeType;
    }

    public String getLongName() {
        return longName;
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

    public boolean getAvailable(){ return available; }

    public void setAvailable(boolean isAvailable){
        this.available = isAvailable;
    }

    public String[] getStringsLocation() {
        return new String[]{nodeID, Integer.toString(xCord), Integer.toString(yCord),
                nodeType.toString(), longName};
    }

    public void setNodeType(Constants.NodeType nodeType) {
        this.nodeType = nodeType;
        WorkspaceTable.updateWorkspace(this);
    }

    public boolean deleteCurrNode() {
        VisualRealtimeController.removeCircle(this);
        return WorkspaceTable.deleteWorkspace(this);
    }

    public Circle getNodeCircle() {
        return nodeCircle;
    }

    public void setNodeCircle(Circle nodeCircle) {
        this.nodeCircle = nodeCircle;
    }
}
