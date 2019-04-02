package models.map;

import javafx.scene.shape.Line;

public class Edge {
    private String edgeID;
    private Location start;
    private Location end;
    private Line line;

    public Edge(String edgeID, Location start, Location end) {
        this.edgeID = edgeID;
        this.start = start;
        this.end = end;
    }

    public String getEdgeID() {
        return edgeID;
    }

    public Location getStart() {
        return start;
    }

    public Location getEnd() {
        return end;
    }

    public String[] getStrings() {
        return new String[]{edgeID, start.getNodeID(), end.getNodeID()};
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public void setEdgeID(String edgeID) {
        this.edgeID = edgeID;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setEnd(Location end) {
        this.end = end;
    }
}
