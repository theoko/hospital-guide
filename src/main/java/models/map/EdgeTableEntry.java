package models.map;

import database.LocationTable;

public class EdgeTableEntry {
    private String nodeID1, nodeID2;

    public EdgeTableEntry(Edge e) {
        this.nodeID1 = e.getStart().getNodeID();
        this.nodeID2 = e.getEnd().getNodeID();
    }

    public String getNodeID1() {
        return nodeID1;
    }

    public void setNodeID1(String nodeID1) {
        this.nodeID1 = nodeID1;
    }

    public String getNodeID2() {
        return nodeID2;
    }

    public void setNodeID2(String nodeID2) {
        this.nodeID2 = nodeID2;
    }
    public Edge toEdge() {
        return new Edge(this.nodeID1+"_"+this.nodeID2, LocationTable.getLocationByID(this.nodeID1)
                , LocationTable.getLocationByID(this.nodeID2));
    }
}