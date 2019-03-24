package controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.awt.event.ActionEvent;

public class MapController {

    @FXML
    ImageView floorOneMap;

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
        System.out.println("scroll: " + event.toString());

        // Handle onScroll event

    }

    public void floorOneMapZoomin(ActionEvent event) {
        System.out.println("kill me now");
    }
   // public void floorOneMapZoomin(ActionEvent event) {
  //      System.out.println("zoomin: " + event.toString());

        // Handle onclick event

   // }

    public void floorOneMapZoomout(ActionEvent event) {
        System.out.println("kill me again");
    }




}
