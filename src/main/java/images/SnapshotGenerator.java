package images;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import models.map.Location;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Stack;


public class SnapshotGenerator {
    private AnchorPane panMap;
    private int x_min = 0, x_max = 0, y_min = 0, y_max = 0;
    public SnapshotGenerator() {}
    public int[] determineBoundaries(ArrayList<Location> locations) {
        return new int[]{0, 1000, 0, 1000};
    }
    public void generateImage(Stack<Location> route) {
//        WritableImage image = panMap.snapshot(new SnapshotParameters(), null);

        ArrayList<WritableImage> images = new ArrayList<WritableImage>();

        ArrayList<String> floorQueue = new ArrayList<String>();
        ArrayList<Location> localLocationsForBoundaries = new ArrayList<Location>();
        ArrayList<Location> allLocations = new ArrayList<Location>();
        String curFloor = "";
        for(Location loc : route) {
            if(!curFloor.equals(loc.getFloor())) {
                curFloor = loc.getFloor();
                floorQueue.add(curFloor);
            }
            allLocations.add(loc);
        }

        for(String floor : floorQueue) {
            int i = 0;
            for(i = 0; i < allLocations.size() && floor.equals(allLocations.get(i).getFloor()); i++) {
                localLocationsForBoundaries.add(allLocations.get(i));
            }
            while(i > 0) allLocations.remove(i--);
            int[] boundsForFloor = determineBoundaries(localLocationsForBoundaries);
            localLocationsForBoundaries.clear();
            generateFloorImage(floor, boundsForFloor);
        }

    }
    public void generateFloorImage(String floor, int[] boundaries) {

    }
}
