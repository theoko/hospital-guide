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
    public static void setUserNodeClickEvent(Circle c, Location loc, HashMap<String, Line> lstLines, Map map) {
        c.setOnMouseClicked(event -> {
            try {
                event.consume();
                ScreenController.popUp("info", loc, map, lstLines);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public static void setAdminNodeClickEvent(Circle c, Location loc) {

        c.setOnMouseClicked(evt -> {
            try {
                evt.consume();
//                AdminMapController.locationForEdgeSelected(loc);
                ScreenController.popUp("edit", loc);
            } catch (Exception e) {
                throw new UnsupportedOperationException(e);
            }
        });
    }
    public static Line generateLineFromEdge(Edge e) {
        Location start = e.getStart();
        Location end = e.getEnd();
        Line line = new Line(start.getxCord(), start.getyCord(), end.getxCord(), end.getyCord());
        line.setStroke(Color.BLACK);
        line.setStrokeWidth(MapDisplay.getEdgeWidth());
//        lstLines.put(edge.getEdgeID(), line);
        return line;
//        pane.getChildren().add(line);
    }

}
