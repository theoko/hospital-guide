package controllers;

import com.jfoenix.controls.JFXButton;
import database.Database;
import helpers.Constants;
import helpers.MapHelpers;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import map.MapDisplay;
import models.map.Location;

import java.awt.*;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;

    private boolean enableAddNode = false;
    private String selectedFloor = "1", selectedBuilding = "Tower";

    public void enableMapEditor() {
        enableAddNode = !enableAddNode;
    }
    public void initialize() {
        toolTip();
        MapDisplay.displayAdmin(panMap, "Tower", "1");
        MapDisplay.displayUser(panMap1, "Tower", "1");
    }

    void toolTip() {
        btnDownload.setTooltip(new Tooltip(Constants.DOWNLOAD_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }

    public void clickDownload(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate("download");
    }
    public void addNode(MouseEvent event) throws Exception {
        Point selectedPoint = new Point((int)event.getX(), (int)event.getY());
        Point nodeCoordinate = MapHelpers.mapPointToMapCoordinates(selectedPoint);
        Circle circ = MapHelpers.generateNode(nodeCoordinate);
//        Point nodeDisplayCoordinate = MapHelpers.mapPointToMapCoordinates()
        Location loc = new Location(null, nodeCoordinate.x, nodeCoordinate.y,
                this.selectedFloor, this.selectedBuilding, Constants.NodeType.HALL,
                "RECENT_ADDITION", "RECENT_ADDITION");

        ScreenController.popUp("edit", loc);
//        String locID = Database.generateUniqueNodeID(loc);
//        loc.setNodeID(locID);
//        loc.addCurrNode();
        circ.setOnMouseClicked(evt -> {
            try {
                evt.consume();
                ScreenController.popUp("edit", loc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        panMap.getChildren().add(circ);


    }

    @Override
    public void floorOneMapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "1";
        try {
            if (enableAddNode)
                addNode(event);
        } catch(Exception e) {

        }

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }

}
