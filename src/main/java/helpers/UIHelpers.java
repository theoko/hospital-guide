package helpers;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Screen;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;
import models.map.Map;

import java.awt.Point;

public class UIHelpers {

    public static final double MIN_WIDTH = 400.0;
    public static final double MIN_PIXELS = 600.0;

    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

    public static void setAdminNodeClickEvent(Map map, AnchorPane[] panes, Location loc, Circle c) {
//        c.setOnMouseDragged(evt -> {
//            try {
//                evt.consume();
//                if(!AdminMapController.isEnableAddNode() && !AdminMapController.isEnableEditEdge()) {
//                    c.setCenterX(evt.getX());
//
//                    loc.setxCord((int)MapDisplay.revScaleX(evt.getX()));
//                    loc.setyCord((int)MapDisplay.revScaleY(evt.getY()));
//                    LocationTable.updateLocation(loc);
//                    c.setCenterY(evt.getY());
//                }
//            } catch (Exception e) {
//                throw new UnsupportedOperationException(e);
//            }
////        });
//        c.setOnMouseClicked(evt -> {
//            try {
//                evt.consume();
//                AdminMapController.locationSelectEvent(loc);
////                if(!AdminMapController.isEnableEditEdge())
////                    ScreenController.popUp(Constants.Routes.EDIT_LOCATION, loc, map, panes, c);
//            } catch (Exception e) {
//                throw new UnsupportedOperationException(e);
//            }
//        });
    }

    public static void mouseHover(JFXButton btn) {
        btn.setOnMouseEntered(event -> {
            ScreenController.sceneThing.setCursor(Cursor.HAND);
        });

        btn.setOnMouseExited(event -> {
            ScreenController.sceneThing.setCursor(Cursor.DEFAULT);
        });
    }

//    public static void loopPane(Pane p) {
//        for (Node n : p.getChildren()) {
//            if (n instanceof Pane) {
//                loopPane((Pane) n);
//            } else if (n instanceof JFXButton) {
//                mouseHover(n);
//            }
//        }
//    }

    public static void btnRaise(JFXButton btn) {
        btn.setOnMouseEntered(event -> {
            btn.setButtonType(JFXButton.ButtonType.RAISED);
        });

        btn.setOnMouseExited(event -> {
            btn.setButtonType(JFXButton.ButtonType.FLAT);
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
        double x = MapDisplay.scaleX(loc.getxCord());
        double y = MapDisplay.scaleY(loc.getyCord());
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
