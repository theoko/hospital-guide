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
import models.map.Edge;
import models.map.Location;

import java.awt.*;

public class MapHelpers {

    public enum Algorithm {
        ASTAR, DFS, BFS
    }

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
        return new Point((int) MapDisplay.revScaleX(p.x), (int) MapDisplay.revScaleY(p.y));
    }

    public static Circle generateNode(Point target) {
        Circle circle = new Circle(MapDisplay.scaleX(target.x), MapDisplay.scaleY(target.y),
                MapDisplay.getLocRadius(), Color.WHITE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(MapDisplay.getLocWidth());
//        circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                try {
//                    event.consume();
//                    ScreenController.popUp(Constants.Routes.USER_INFO, loc);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        return circle;
    }

    public static Edge generateEdge(Location loc1, Location loc2) {
        Edge e = new Edge(null, loc1, loc2);
        e.setEdgeID(MapHelpers.generateEdgeID(e, Constants.START_FIRST));
        return e;
    }

    public static String generateEdgeID(Edge edge, boolean startFirst) {
        String nodeID1 = edge.getStart().getNodeID();
        String nodeID2 = edge.getEnd().getNodeID();
        return startFirst ? nodeID1 + "_" + nodeID2
                : nodeID2 + "_" + nodeID1;
    }
}
