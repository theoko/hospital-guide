package images;

import javafx.scene.image.Image;
import java.util.concurrent.*;

public class ImageFactory {
    private static ConcurrentMap<String, Image> imgMap =  new ConcurrentHashMap<>();

    public static void load() {
            Thread t = new Thread(() -> {
                Image fl4 = new Image("/images/maps/floor4.png");
                imgMap.put("4", fl4);
                Image fl3 = new Image("/images/maps/floor3.png");
                imgMap.put("3", fl3);
                Image fl2 = new Image("/images/maps/floor2.png");
                imgMap.put("2", fl2);
                Image fl1 = new Image("/images/maps/floor1.png");
                imgMap.put("1", fl1);
                Image flG = new Image("/images/maps/floorG.png");
                imgMap.put("G", flG);
                Image flL1 = new Image("/images/maps/floorL1.png");
                imgMap.put("L1", flL1);
                Image flL2 = new Image("/images/maps/floorL2.png");
                imgMap.put("L2", flL2);
                Image arrow = new Image("/images/Arrow.gif");
                imgMap.put("arrow", arrow);
            });
            t.setDaemon(true);
            t.start();
    }

    public static Image getImage(String imgID) {
        return imgMap.get(imgID);
    }
}
