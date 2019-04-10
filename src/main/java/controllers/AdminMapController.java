package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import database.Database;
import database.EdgeTable;
import database.SanitationTable;
import helpers.Constants;
import helpers.MapHelpers;
import helpers.UIHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.MapDisplay;
import models.map.Edge;
import models.map.Location;
import models.sanitation.SanitationRequest;

import models.search.SearchEngine;

import java.awt.*;
import java.util.List;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMapController extends MapController {
    public JFXButton btnDownload;
    public JFXButton btnBooking;
    @FXML
    private JFXToggleButton enableEdge, enableNode;


    public TableView<SanitationRequest> tblData;
    public TableColumn<SanitationRequest,String> tblLocation;
    public TableColumn<SanitationRequest,String> tblPriority;
    public TableColumn<SanitationRequest,String> tblStatus;
    public TableColumn<SanitationRequest,String> tblDescription;
    public TableColumn<SanitationRequest,String> tblRequester;
    public TableColumn<SanitationRequest,String> tblClaimTime;
    public TableColumn<SanitationRequest,String> tblServicer;
    public TableColumn<SanitationRequest,String> tblServiceTime;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    public JFXButton btnDelete;



    private static boolean enableAddNode = false;
    private static boolean enableEditEdge = false;

    public AnchorPane outerTabAnchor;
    public JFXTextField searchBox;

    public static String selectedFloor = "1", selectedBuilding = "Shapiro";

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
            boolean edgeToggle = EdgeTable.toggleEdge(edge);
            if(edgeToggle == Constants.SELECTED) {
                if(edge.getStart().getFloor().equals(edge.getEnd().getFloor())) {
                    Line line = UIHelpers.generateLineFromEdge(edge);
                    edge.setLine(line);
                    VisualRealtimeController.addLine(line);

                    VisualRealtimeController.pushCircleToFront(edge.getStart());

                    VisualRealtimeController.pushCircleToFront(edge.getEnd());
                    VisualRealtimeController.visuallySelectCircle(edge.getEnd());
                } else {
                    VisualRealtimeController.visuallySelectCircle(edge.getEnd());
                }
            } else if(edgeToggle == Constants.DESELECTED){
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
        if(enableAddNode) {
            enableNode.setSelected(false);
            enableNodeCreation();
        }
        VisualRealtimeController.visuallyDeselectAll();
        selectedLocation = null;

        enableEditEdge = !enableEditEdge;
    }
    public void enableNodeCreation() {
        if(enableEditEdge) {
            enableEdge.setSelected(false);
            enableEdgeEditor();
        }
        enableAddNode = !enableAddNode;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        // Set tooltip

        initSanitation();
        updateSanitation();

        MapDisplay.displayAdmin(panes);
        configVisualRealtimeController();
        VisualRealtimeController.setPanMap(panFloor1);
        selectedLocation = null;

        // Initialize search box
        searchBox = new JFXTextField();
        searchBox.setMaxWidth(UIHelpers.getScreenWidth());
        searchBox.setPadding(new Insets(5));

        // Add search box to layout
        outerTabAnchor.getChildren().add(searchBox);

        // Add event listener for search box
        searchBox.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                SearchEngine searchEngine = new SearchEngine(searchBox.getText());

            }
        });

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {// only enable btns if item selected
            btnDelete.setDisable(false);

        });

    }

    public void clickDownload(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.DOWNLOAD);
    }

    public void displayBooking(MouseEvent event) throws Exception {
        event.consume();
        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.BOOKING_WINDOW);
    }
    public void addNode(MouseEvent event) throws Exception {
        Point selectedPoint = new Point((int)event.getX(), (int)event.getY());
        Point nodeCoordinate = MapHelpers.mapPointToMapCoordinates(selectedPoint);
        Circle circ = MapHelpers.generateNode(nodeCoordinate);
//        Point nodeDisplayCoordinate = MapHelpers.mapPointToMapCoordinates()
        Location loc = new Location(null, nodeCoordinate.x, nodeCoordinate.y,
                this.selectedFloor, this.selectedBuilding, Constants.NodeType.HALL,
                "RECENT_ADDITION", "RECENT_ADDITION");

        ScreenController.popUp(Constants.Routes.EDIT_LOCATION, loc);
//        String locID = Database.generateUniqueNodeID(loc);
//        loc.setNodeID(locID);
//        loc.addCurrNode();
        UIHelpers.setAdminNodeClickEvent(map, panes, loc, circ);
        loc.setNodeCircle(circ);
        AnchorPane addToPane = determinePanMapFromFloor(loc.getFloor());
        addToPane.getChildren().add(circ);
    }
//    public static void removeCircle(Circle c) {
//        panMap.getChildren().remove(c);
//
//    }

    public void deleteSanitationRequest(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        SanitationTable.deleteSanitationRequest(selected);
        updateSanitation();
        tblData.refresh();
    }


    @Override
    public void logOut(MouseEvent event) {
        enableAddNode = false;
        enableEditEdge = false;
        event.consume();
        ScreenController.logOut(btnReturn);
        try {
            ScreenController.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void floorL2MapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "L2";
        VisualRealtimeController.setPanMap(panFloorL2);
        mapOnMousePressed(event);
    }
    public void floorL1MapOnMousePressed(MouseEvent event)  {
        this.selectedFloor = "L1";
        VisualRealtimeController.setPanMap(panFloorL1);
        mapOnMousePressed(event);
    }
    public void floorThreeMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setPanMap(panFloor3);
        this.selectedFloor = "3";
        mapOnMousePressed(event);
    }
    public void floorTwoMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setPanMap(panFloor2);
        this.selectedFloor = "2";
        mapOnMousePressed(event);
    }
    @Override
    public void floorOneMapOnMousePressed(MouseEvent event)  {
        VisualRealtimeController.setPanMap(panFloor1);
        this.selectedFloor = "1";
        mapOnMousePressed(event);
    }
    public void mapOnMousePressed(MouseEvent event)  {
//        this.selectedFloor = "1";
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
    public void configVisualRealtimeController() {
        panFloor1.setId("1");
        panFloor2.setId("2");
        panFloor3.setId("3");
        panFloorL1.setId("L1");
        panFloorL2.setId("L2");
        VisualRealtimeController.addPanMap(panFloor1);
        VisualRealtimeController.addPanMap(panFloor2);
        VisualRealtimeController.addPanMap(panFloor3);
        VisualRealtimeController.addPanMap(panFloorL1);
        VisualRealtimeController.addPanMap(panFloorL2);

    }

    private void initSanitation(){
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("LocationShortName"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        //tblUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        System.out.println(spills.toString());
        tblData.setItems(spills);
    }

    private void updateSanitation() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        if(lstReqs != null) {
            spills.setAll(lstReqs);
        }
    }
}

