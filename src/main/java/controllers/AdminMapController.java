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

public class AdminMapController extends MapController {
    public JFXButton btnDownload;
    public JFXButton btnBooking;

    private static boolean enableAddNode = false;
    private static boolean enableEditEdge = false;

    private String selectedFloor = "1", selectedBuilding = "Shapiro";

    private static Location selectedLocation; // Location that is being modified or created

    public static void locationSelectEvent(Location loc) {
        if(enableEditEdge) {
            if (selectedLocation != loc) {
                selectLocation(loc);

            }
            else {
                deselectLocation();
                VisualRealtimeController.visuallyDeselectCircle(loc);
            }
        }
    }

    public static boolean isEnableAddNode() {
        return enableAddNode;
    }

    public static boolean isEnableEditEdge() {
        return enableEditEdge;
    }

    public static void selectLocation(Location loc) {
        if(selectedLocation != null) {
            Edge edge = MapHelpers.generateEdge(selectedLocation, loc);
            boolean edgeToggle = Database.toggleEdge(edge);
            if(edgeToggle == Constants.SELECTED) {
                Line line = UIHelpers.generateLineFromEdge(edge);
                edge.setLine(line);
                VisualRealtimeController.addLine(line);
            } else {
                VisualRealtimeController.removeLine(edge.getLine());
            }
        }
        else {
            selectedLocation = loc;
            VisualRealtimeController.visuallySelectCircle(loc);
        }
    }

    public static void deselectLocation() {
        selectedLocation = null;
    }

    public void enableEdgeEditor() {
        try {
            VisualRealtimeController.visuallyDeselectCircle(selectedLocation);
        } catch(Exception e) {
            // Circle is null
        }

        selectedLocation = null;

        enableEditEdge = !enableEditEdge;
    }
    public void enableNodeCreation() {
        enableAddNode = !enableAddNode;
    }

    public void initialize() {
        // Set tooltip
        toolTip();

        MapDisplay.displayAdmin(panMap, "1");
        VisualRealtimeController.setPanMap(panMap);
        selectedLocation = null;
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

    public void displayBooking(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate("book-room");
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
        loc.setNodeCircle(circ);
        panMap.getChildren().add(circ);
    }
//    public static void removeCircle(Circle c) {
//        panMap.getChildren().remove(c);
//
//    }


    @Override
    public final void logOut(MouseEvent event) throws Exception {
        enableAddNode = false;
        enableEditEdge = false;
        event.consume();
        ScreenController.logOut(btnReturn);
        ScreenController.activate("welcome");
    }
    @Override
    public void floorOneMapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "1";
        try {
            if (enableAddNode && !enableEditEdge)
                addNode(event);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }
}
