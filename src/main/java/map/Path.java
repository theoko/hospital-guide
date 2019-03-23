package map;

public class Path {

    private String edgeID;
    private Location startNode;
    private Location endNode;

    public Path(String edgeID, Location startNode, Location endNode) {
        this.edgeID = edgeID;
        this.startNode = startNode;
        this.endNode = endNode;
    }

    public String getEdgeID() {
        return edgeID;
    }

    public Location getStartNode() {
        return startNode;
    }

    public Location getEndNode() {
        return endNode;
    }
}
