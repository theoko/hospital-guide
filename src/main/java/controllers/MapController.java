package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import sun.plugin.javascript.navig.Anchor;

import java.awt.event.ActionEvent;
import java.util.Stack;

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
        System.out.println("scroll: " + event.toString());

        // Handle onScroll event

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
