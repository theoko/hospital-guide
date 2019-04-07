package models.map;

public class SubPath implements Comparable<SubPath> {

    private String edgeID;
    private Location node;
    private double dist;
    private double heuristic;
    private double weight;

    public SubPath(String edgeID, Location node, double dist) {
        this.edgeID = edgeID;
        this.node = node;
        this.dist = dist;
        this.heuristic = 0.0;
        weight = dist + heuristic;
    }

    public SubPath(String edgeID, Location node, double dist, double heuristic) {
        this.edgeID = edgeID;
        this.node = node;
        this.dist = dist;
        this.heuristic = heuristic;
        weight = dist + heuristic;
    }

    public String getEdgeID() {
        return edgeID;
    }

    public Location getLocation() {
        return node;
    }

    public double getDist() {
        return dist;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public int compareTo(SubPath n) {
        if (weight - n.getWeight() > 0) { // Positive: this > n
            return 1;
        } else if (weight - n.getWeight() == 0.0) { // Zero: this == n
            return 0;
        } else { // Negative: this < n
            return -1;
        }
    }
}
