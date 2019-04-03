package helpers;

import controllers.AdminMapController;
import controllers.ScreenController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;
import models.map.Map;

import java.awt.*;
import java.util.HashMap;

public class UIHelpers {

    public static final double MIN_WIDTH = 400.0;
    public static final double MIN_PIXELS = 600.0;

    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

    public static void setAdminNodeClickEvent(Circle c, Location loc) {
        c.setOnMouseClicked(evt -> {
            try {
                evt.consume();

                AdminMapController.locationSelectEvent(loc);
                if(!AdminMapController.isEnableEditEdge())
                    ScreenController.popUp("edit", loc);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
    }

    public static Line generateLineFromEdge(Edge e) {
        Location start = e.getStart();
        Location end = e.getEnd();
        Point startPoint = generateLocationCoordinates(start);
        Point endPoint = generateLocationCoordinates(end);
        Line line = new Line(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(MapDisplay.getEdgeWidth());
//        lstLines.put(edge.getEdgeID(), line);
        return line;
//        pane.getChildren().add(line);
    }
    public static Point generateLocationCoordinates(Location loc) {
        double x = (loc.getxCord() - MapDisplay.getxShift()) * MapDisplay.getScale();
        double y = (loc.getyCord() - MapDisplay.getyShift()) * MapDisplay.getScale();
        return new Point((int) x, (int) y);
    }
    public static Circle updateCircleForNodeType(Location loc) {
        Circle c = loc.getNodeCircle();
        if(loc.getNodeType() == Constants.NodeType.HALL) {
            c.setRadius(MapDisplay.getHallRadius());
            c.setFill(Color.GRAY);
        } else {
            c.setRadius(MapDisplay.getLocRadius());
            c.setFill(Color.WHITE);
        }


        return c;
    }

}
