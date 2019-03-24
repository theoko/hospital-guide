package controllers;

import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

public class MapController {

    @FXML
    ImageView floorOneMap;

    double sceneX, sceneY;
    double translateX, translateY;

    public void floorOneMapOnMousePressed(MouseEvent event) {

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((ImageView) (event.getSource())).getTranslateX();
        translateY = ((ImageView) (event.getSource())).getTranslateY();
    }

    public void floorOneMapOnMouseDragged(MouseEvent event) {

        // Handle onMouseDragged event
        double offsetX = event.getSceneX() - sceneX;
        double offsetY = event.getSceneY() - sceneY;
        double newTranslateX = translateX + offsetX;
        double newTranslateY = translateY + offsetY;

        ((ImageView) (event.getSource())).setTranslateX(newTranslateX);
        ((ImageView) (event.getSource())).setTranslateY(newTranslateY);
    }

    public void floorOneMapScroll(ScrollEvent event) {

        // Handle onScroll event
        double delta = -event.getDeltaY();

        Rectangle2D viewport = floorOneMap.getViewport();

//        double scale = MapHelpers.clamp(Math.pow(1.01, delta),
//
//                Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),
//
//                Math.max(UIHelpers.getScreenWidth() / viewport.getWidth(), UIHelpers.getScreenHeight() / viewport.getHeight())
//
//        );
//
//        Point2D mouse = MapHelpers.imageViewToImage(floorOneMap, new Point2D(e.getX(), e.getY()));
//
//        double newMinX = MapHelpers.clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale,
//                0, UIHelpers.getScreenWidth() - newWidth);
//        double newMinY = MapHelpers.clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale,
//                0, UIHelpers.getScreenHeight() - newHeight);
//
//        floorOneMap.setViewport(new Rectangle2D());
    }



}
