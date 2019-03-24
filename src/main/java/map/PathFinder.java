package map;

public class PathFinder {

    public static void main(String[] args) {
        // TODO: Fix path names for CSV files
        Map map = MapParser.parse(PathFinder.class.getResource("/data/csv/nodes.csv").toString(), PathFinder.class.getResource("/data/csv/edges.csv").toString());
    }
}
