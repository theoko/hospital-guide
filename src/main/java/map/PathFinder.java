package map;

public class PathFinder {

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // TODO: Fix path names for CSV files
        Map map = MapParser.parse("C:\\Users\\Miandoli8\\Documents\\College\\Soft Eng\\App\\out\\production\\resources\\csv\\nodes.csv", "C:\\Users\\Miandoli8\\Documents\\College\\Soft Eng\\App\\out\\production\\resources\\csv\\edges.csv");

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time taken: " + totalTime);
    }
}
