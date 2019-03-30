package map;

import controllers.ScreenController;
import helpers.Constants;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import models.map.Edge;
import models.map.Location;
import models.map.Map;

import java.util.HashMap;

public class MapDisplay {

    private final static double defRadius = 15.0;
    private final static double defWidth = 2.5;
    private final static double xShift = 1095.0;
    private final static double yShift = 565.0;
    private final static double scale = 1.6;

    public static void display(Map map, AnchorPane pane, String building, String floor) {
        HashMap<String, Edge> lstEdges = map.getAllEdges();
        for (Edge edge : lstEdges.values()) {
            Location start = edge.getStart();
            Location end = edge.getEnd();
            if (start.getBuilding().equals(building) && start.getFloor().equals(floor) &&
                    end.getBuilding().equals(building) && end.getFloor().equals(floor)) {
                double x1 = (double) (start.getxCord() - xShift)*scale;
                double x2 = (double) (end.getxCord() - xShift)*scale;
                double y1 = (double) (start.getyCord() - yShift)*scale;
                double y2 = (double) (end.getyCord() - yShift)*scale;
                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(defWidth);
                pane.getChildren().add(line);
            }
        }

        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (Location loc : lstLocations.values()) {
            if (loc.getBuilding().equals(building) && loc.getFloor().equals(floor) && loc.getNodeType() != Constants.NodeType.HALL) {
                double xLoc = (double) (loc.getxCord() - xShift)*scale;
                double yLoc = (double) (loc.getyCord() - yShift)*scale;
                Color color = Color.WHITE;
                Circle circle = new Circle(xLoc, yLoc, defRadius, color);
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(defWidth / 3.0);
                circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        try {
                            event.consume();
                            ScreenController.popUp("info", loc);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                pane.getChildren().add(circle);
            }
        }
    }

    private static Color nodeColor(Location loc) {
        switch (loc.getNodeType()) {
            case BATH:
                return Color.GRAY;
            case CONF:
                return Color.GRAY;
            case DEPT:
                return Color.GRAY;
            case ELEV:
                return Color.YELLOW;
            case EXIT:
                return Color.GRAY;
            case HALL:
                return Color.RED;
            case INFO:
                return Color.GRAY;
            case LABS:
                return Color.GRAY;
            case REST:
                return Color.BLUE;
            case RETL:
                return Color.WHITE;
            case SERV:
                return Color.GRAY;
            default:
                return Color.YELLOW;
        }
    }
}
