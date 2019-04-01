package helpers;

import controllers.ScreenController;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import map.MapDisplay;
import models.map.Location;

import java.awt.*;

public class MapHelpers {

    public static double clamp(double value, double min, double max) {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    public static Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    public static Point mapPointToMapCoordinates(Point p) {
//        Point out = new Point(0, 0);
//        out.x = (int)((p.x + 0*MapDisplay.getxShift())*MapDisplay.getScale());
//        out.y = (int)((p.y + 0*MapDisplay.getyShift())*MapDisplay.getScale());
        return p;
    }
    public static Circle generateNode(Point target) {
        System.out.println(target.x + ", " + target.y);
        Circle circle = new Circle(target.x, target.y, MapDisplay.getLocRadius(), Color.WHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(MapDisplay.getLocWidth());
//        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                try {
//                    event.consume();
//                    ScreenController.popUp("info", loc);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        return circle;
    }

}
