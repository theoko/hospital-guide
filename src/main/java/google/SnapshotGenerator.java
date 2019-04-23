package google;

import controllers.maps.MapController;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import models.map.Location;
import net.kurobako.gesturefx.GesturePane;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class SnapshotGenerator {
    private GesturePane panMap;
    private MapController mc;
    private int x_min = 0, x_max = 0, y_min = 0, y_max = 0;
    public static final int PADDING = 50;
    public SnapshotGenerator(MapController mc) {
        this.mc = mc;
        this.panMap = mc.gesMap;
    }
    public int[] determineBoundaries(ArrayList<Location> locations) {
        if(locations.size() == 0) return null;
        int xmin, xmax, ymin, ymax;
        xmin = locations.get(0).getxCord();
        xmax = xmin;
        ymin = locations.get(0).getyCord();
        ymax = ymin;
        for(Location l : locations) {
            int curx = l.getxCord(), cury = l.getyCord();
            xmin = curx < xmin ? curx : xmin;
            xmax = curx > xmax ? curx : xmax;
            ymin = cury < ymin ? cury : ymin;
            ymax = cury > ymax ? cury : ymax;
        }
        return new int[] {0, 1000, 0, 1000};
//        return new int[]{xmin - PADDING, xmax + PADDING, ymin - PADDING, ymax + PADDING};
    }



    public ArrayList<File> generateImages(Stack<Location> route) {
//        WritableImage image = panMap.snapshot(new SnapshotParameters(), null);
        ArrayList<File> imageLocations = new ArrayList<File>();

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
            while(i > 0) allLocations.remove(--i);
            int[] boundsForFloor = determineBoundaries(localLocationsForBoundaries);
            localLocationsForBoundaries.clear();
            WritableImage wi = generateFloorImage(floor, boundsForFloor);
            File localFile = (writeToRandomFile(wi));
            resizeImage(localFile);
            imageLocations.add(localFile);
        }
        return imageLocations;
    }

    public void resizeImage(File f) {
        try {

            BufferedImage image = ImageIO.read(f);

            BufferedImage resized = resize(image, 750, 750);

            ImageIO.write(resized, "png", f);

//            FirebaseAPI.uploadDirectionsImage(f.getPath());

        } catch(Exception e) {

        }
    }
    private static BufferedImage resize(BufferedImage img, int height, int width) throws Exception {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    public  WritableImage generateSnapshot(int[] boundaries, Location loc) {
        return generateSnapshot(boundaries, loc.getFloor());
    }
    public  WritableImage generateSnapshot(int[] boundaries, String s) {
        //setSelectedPane(s);
        SnapshotParameters shp = new SnapshotParameters();
        Rectangle2D viewPort = new Rectangle2D(boundaries[0], boundaries[2], boundaries[1]-boundaries[0],
                boundaries[3]- boundaries[2]);
        shp.setViewport(viewPort);
//        double scale = 256 / viewPort.getWidth();
//        shp.setTransform(Transform.scale(scale, scale, panMap.getWidth() / 2, panMap.getHeight() / 2));
        WritableImage snap = panMap.snapshot(shp, null);

        return snap;
    }



    public File writeToRandomFile(WritableImage wi) {
        int cur = 0;
        File file = new File(cur + ".png");
        while(file.exists()) file = new File( (++cur) + ".png");
        return writeToFile(wi, file.getPath());
    }
    public File writeToFile(WritableImage wi, String s) {
        File f = new File(s);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wi, null), "png", f);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return f;
    }
    public WritableImage generateFloorImage(String floor, int[] boundaries) {
        mc.showFloor(floor);
        return generateSnapshot(boundaries, floor);
    }
}
