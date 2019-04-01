package controllers;

import com.jfoenix.controls.JFXButton;
import database.Database;
import helpers.Constants;
import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;

import java.awt.*;
import java.util.ArrayList;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;

    private static boolean enableAddNode = false;
    private static boolean enableEditEdge = false;

    private String selectedFloor = "1", selectedBuilding = "Tower";

    private static Location selectedLocation;

    public static void selectLocation(Location loc) {
        selectedLocation = loc;
    }
    public static void deselectLocation() {
        selectedLocation = null;
    }
    public void enableEdgeEditor() {
        enableEditEdge = !enableEditEdge;
    }
    public void enableNodeCreation() {
        enableAddNode = !enableAddNode;
    }
    public void initialize() {
        toolTip();
        MapDisplay.displayAdmin(panMap, "Tower", "1");
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
        UIHelpers.setAdminNodeClickEvent(circ, loc);
        panMap.getChildren().add(circ);


    }


    @Override
    public final void logOut(MouseEvent event) throws Exception {
        enableAddNode = false;
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate("welcome");
    }


    @Override
    public void floorOneMapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "1";
        try {
            if (enableAddNode /* && !enableEditEdge*/)
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
