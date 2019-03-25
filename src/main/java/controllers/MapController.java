package controllers;

import com.jfoenix.controls.JFXButton;
import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sun.plugin.javascript.navig.Anchor;

import java.awt.event.ActionEvent;
import java.util.Stack;

import static helpers.UIHelpers.MIN_PIXELS;

public class MapController {

    @FXML
    private AnchorPane Map;

    @FXML
    JFXButton zoomIn;

    @FXML
    JFXButton zoomOut;

    double sceneX, sceneY;
    double translateX, translateY;

    public void floorOneMapOnMousePressed(MouseEvent event) {

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }

    public void floorOneMapOnMouseDragged(MouseEvent event) {

        // Handle onMouseDragged event
        double offsetX = event.getSceneX() - sceneX;
        double offsetY = event.getSceneY() - sceneY;
        double newTranslateX = translateX + offsetX;
        double newTranslateY = translateY + offsetY;

        ((AnchorPane) event.getSource()).setTranslateX(newTranslateX);
        ((AnchorPane) event.getSource()).setTranslateY(newTranslateY);
    }

    public void floorOneMapScroll(ScrollEvent event) {

        // Handle onScroll event
        double delta = -event.getDeltaY();

//        Rectangle2D viewport = floorOneMap.getViewport();


        double scale = MapHelpers.clamp(Math.pow(1.01, delta),

                Math.min(MIN_PIXELS / floorOneMap.getViewport().getWidth(), MIN_PIXELS / floorOneMap.getViewport().getHeight()),

                Math.max(UIHelpers.getScreenWidth() / floorOneMap.getViewport().getWidth(), UIHelpers.getScreenHeight() / floorOneMap.getViewport().getHeight())

        );

        Point2D mouse = MapHelpers.imageViewToImage(floorOneMap, new Point2D(event.getX(), event.getY()));

        double newWidth = floorOneMap.getViewport().getWidth() * scale;
        double newHeight = floorOneMap.getViewport().getHeight() * scale;

        double newMinX = MapHelpers.clamp(mouse.getX() - (mouse.getX() - floorOneMap.getViewport().getMinX()) * scale,
                0, UIHelpers.getScreenWidth() - newWidth);
        double newMinY = MapHelpers.clamp(mouse.getY() - (mouse.getY() - floorOneMap.getViewport().getMinY()) * scale,
                0, UIHelpers.getScreenHeight() - newHeight);

        floorOneMap.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
    }



    public void floorOneMapZoomIn(MouseEvent event) {
        System.out.println("scroll: " + event.toString());


   /*     zoomIn.addEventHandler(event.MOUSE_CLICKED, Event -> {
                Map.fireEvent(Event.copyFor(Map, Map));
            Event.consume();
        });*/
    }

    public void floorOneMapZoomOut(MouseEvent event) {
        System.out.println("scroll: " + event.toString());
    }
}
