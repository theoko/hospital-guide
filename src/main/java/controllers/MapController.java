package controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class MapController {

    @FXML
    static ImageView floorOneMap;

    static EventHandler<MouseEvent> floorOneMapOnMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            System.out.println("test");
        }
    };

    static EventHandler<MouseEvent> floorOneMapOnMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            System.out.println("test2");
        }
    };

}
