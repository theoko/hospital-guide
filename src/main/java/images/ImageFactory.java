package images;

import javafx.scene.image.Image;
import java.util.concurrent.*;

public class ImageFactory {
    private static ConcurrentMap<String, Image> imgMap =  new ConcurrentHashMap<>();

    public static void load() {
            Thread t = new Thread(() -> {
                Image fl3 = new Image("/images/Original/03_thethirdfloor.png");
                imgMap.put("3", fl3);
                Image fl2 = new Image("/images/Original/02_thesecondfloor.png");
                imgMap.put("2", fl2);
                Image fl1 = new Image("/images/Original/01_thefirstfloor.png");
                imgMap.put("1", fl1);
                Image flG = new Image("/images/Original/00_thegroundfloor.png");
                imgMap.put("G", flG);
                Image flL1 = new Image("/images/Original/00_thelowerlevel1.png");
                imgMap.put("L1", flL1);
                Image flL2 = new Image("/images/Original/00_thelowerlevel2.png");
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
