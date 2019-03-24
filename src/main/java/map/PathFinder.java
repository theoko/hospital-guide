package map;

public class PathFinder {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        Map map = MapParser.parse(PathFinder.class.getResource("/data/nodes.csv").getFile(), PathFinder.class.getResource("/data/edges.csv").getFile());
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time taken: " + totalTime);
    }
}
