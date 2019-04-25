package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import database.EdgeTable;
import database.LocationTable;
import helpers.Constants;
import helpers.DatabaseHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import map.MapDisplay;
import models.map.*;
import controllers.node.*;
import controllers.ScreenController;
import org.w3c.dom.NodeList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TableEditorController extends PopUpController implements Initializable {
    private static ObservableList<LocationTableEntry> nodeList = FXCollections.observableArrayList();
    private static ObservableList<EdgeTableEntry> edgeList = FXCollections.observableArrayList();
    public static MapController mapC;
    public static Map localMap;
    private LocationTableEntry selected_lte;

    @FXML
    TableView<LocationTableEntry> nodes;

    @FXML
    TableColumn<LocationTableEntry, String> type, ID, x, y, name, floor, building;

    @FXML
    JFXTextField nodeIDText, xText, yText, nameText, floorText, buildingText, typeText, warningText;

    @FXML
    JFXButton editNodeButton, addNodeButton, deleteNodeButton;

    @FXML
    TableView<EdgeTableEntry> edges;

    @FXML
    TableColumn<EdgeTableEntry, String> startColumn, endColumn;

    @FXML
    JFXTextField startText, endText;

    @FXML
    JFXButton addEdgeButton, deleteEdgeButton;

    public void initialize() {
        ID.setCellValueFactory(
                new PropertyValueFactory<>("ID")
        );
        x.setCellValueFactory(
                new PropertyValueFactory<>("x")
        );
        y.setCellValueFactory(
                new PropertyValueFactory<>("y")
        );
        name.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );
        floor.setCellValueFactory(
                new PropertyValueFactory<>("floor")
        );
        building.setCellValueFactory(
                new PropertyValueFactory<>("building")
        );
        type.setCellValueFactory(
                new PropertyValueFactory<>("type")
        );

        startColumn.setCellValueFactory(
                new PropertyValueFactory<>("nodeID1")
        );
        endColumn.setCellValueFactory(
                new PropertyValueFactory<>("nodeID2")
        );
        resetEdges();
        resetNodes();
        nodes.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                event.consume();
                updateEditableNode();
            }
        });
        warningText.setEditable(false);
        nodes.setItems(nodeList);
        edges.setItems(edgeList);
    }
    public void visuallyUpdateNodes() {
        List<Node> lstNodes = new ArrayList<>();

        for (Node n : mapC.panMap.getChildren()) {
            try {
                if (n instanceof Circle && n.getId() != null && n.getId().equals(loc.getNodeID())) {
                    lstNodes.add(n);
                }
                    if (n instanceof Line && n.getId().contains(loc.getNodeID())) {
                        lstNodes.add(n);
                    }
            } catch(Exception e) {}
        }
        for(Node n : lstNodes) {
            if(n instanceof Circle) {

                Location local = LocationTable.getLocations().get(n.getId());
                try {
                    mapC.panMap.getChildren().remove(n);
                } catch(Exception e){}
                loc = local;
                visualAddNode();
            } else if(n instanceof Line) {
                for(Edge e : EdgeTable.getEdges(LocationTable.getLocations())) {
                    try {
                        if (e.getEdgeID().equals(n.getId())) {
                            mapC.panMap.getChildren().remove(n);
                            visualAddEdge(e.getStart(), e.getEnd(), e);
                        }
                    } catch(Exception err) {}
                }
            }
        }
        if(lstNodes.size() == 0) visualAddNode();
    }
    public void clearNodeEditorText() {
        nodeIDText.clear();
        xText.clear();
        yText.clear();
        nameText.clear();
        floorText.clear();
        buildingText.clear();
        typeText.clear();
        editNodeButton.setDisable(true);
    }
    public void visualDeleteNode() {
        List<Node> lstNodes = new ArrayList<>();
//        loc = localMap.getLocation(locID);
//        resetEdges();
//        for(EdgeTableEntry e : edgeList) {
//            if(e.getNodeID1().equals(loc.getNodeID()) || e.getNodeID2().equals(loc.getNodeID())) {
//                EdgeTable.remo
//            }
//        }
        for (Node n : mapC.panMap.getChildren()) {
            try {
                if (n instanceof Circle && n.getId() != null && n.getId().equals(loc.getNodeID())) {
                    lstNodes.add(n);
                }
                for (SubPath sp : loc.getSubPaths()) {
                    if (n instanceof Line && n.getId().equals(sp.getEdgeID())) {
                        lstNodes.add(n);
                    }
                }
            } catch(Exception e) {}
        }
        mapC.panMap.getChildren().removeAll(lstNodes);
        LocationTable.deleteLocation(loc);
        ScreenController.deactivate();
    }
    public void visualDeleteNode(String locID) {
        loc = localMap.getLocation(locID);
        visualDeleteNode();

    }
    public void updateEditableNode() {
        LocationTableEntry lte = nodes.getSelectionModel().getSelectedItem();
        if(lte == null)  {
            clearNodeEditorText();
            return;
        }
        else {
            selected_lte = lte;
            nodeIDText.setText(lte.getID());
            xText.setText(""+lte.getX());
            yText.setText(""+lte.getY());
            nameText.setText(lte.getName());
            floorText.setText(lte.getFloor());
            buildingText.setText(lte.getBuilding());
            typeText.setText(lte.getType());
            editNodeButton.setDisable(false);
        }
    }
    public static void resetNodes() {
        nodeList.clear();
        for(Location loc : LocationTable.getLocations().values()) {
            nodeList.add(new LocationTableEntry(loc));
        }
    }
    public static void resetEdges() {
        edgeList.clear();
        for(Edge e : EdgeTable.getEdges(LocationTable.getLocations())) {
            edgeList.add(new EdgeTableEntry(e));
        }
    }
    @Override
    public void setLoc(Location loc) {
        //
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//
        initialize();
    }

    public void deleteEdge() {
        ObservableList<EdgeTableEntry> entriesToDelete = edges.getSelectionModel().getSelectedItems();
        for(EdgeTableEntry e : entriesToDelete) {
            visualDeleteEdge(e);
            EdgeTable.removeEdgeByID(e.toEdge());
            edgeList.remove(e);
        }
    }
    public void visualDeleteEdge(EdgeTableEntry e) {
        List<Node> lstNodes = new ArrayList<>();

        for (Node n : mapC.panMap.getChildren()) {
            try {
                if (n instanceof Line && n.getId() != null) {
                    if(n.getId().contains(e.getNodeID1()) && n.getId().contains(e.getNodeID2())) {
                        lstNodes.add(n);
                    }
                }
                for (SubPath sp : loc.getSubPaths()) {
                    if (n instanceof Line && n.getId().equals(sp.getEdgeID())) {
                        lstNodes.add(n);
                    }
                }
            } catch(Exception er) {}
        }
        mapC.panMap.getChildren().removeAll(lstNodes);
    }
    public void visualAddNode() {
        if(loc.getFloor().equals(mapC.getFloor())) {
            if(loc.getNodeType() != Constants.NodeType.HALL)
                mapC.panMap.getChildren().add(MapDisplay.createCircle
                        (mapC, loc, MapDisplay.NodeStyle.REGULAR, 1, Constants.Routes.EDIT_LOCATION, true));
            else
                mapC.panMap.getChildren().add(0, MapDisplay.createCircle(
                        mapC, loc, MapDisplay.NodeStyle.POINT, 1, Constants.Routes.EDIT_LOCATION, true));
        }
    }
    public void visualAddEdge(Location start, Location end, Edge e) {
        if(start.getFloor().equals(end.getFloor())) {
            Line l = MapDisplay.creatLine(mapC, start, end, e);
            mapC.panMap.getChildren().add(0, l);
            e.setLine(l);
        }

    }
    public void addEdge() {
        String nodeID1 = startText.getText();
        String nodeID2 = endText.getText();
        Location nodeStart = LocationTable.getLocationByID(nodeID1);
        Location nodeEnd = LocationTable.getLocationByID(nodeID2);

        if(nodeStart == null) {
            startText.setText("Node doesn't exist!");

        }
        if(nodeEnd == null) {
            endText.setText("Node doesn't exist!");
        }
        if(nodeStart != null && nodeEnd != null){
            Edge e = new Edge(nodeID1 + "_" + nodeID2, nodeStart, nodeEnd);
            EdgeTable.addEdge(e);
            startText.setText("");
            endText.setText("");
            visualAddEdge(nodeStart, nodeEnd, e);
            resetEdges();

        }
    }
    public void deleteNode() {
        ObservableList<LocationTableEntry> entriesToDelete = nodes.getSelectionModel().getSelectedItems();
        for(LocationTableEntry loc : entriesToDelete) {

            visualDeleteNode(loc.getID());
            LocationTable.deleteLocation(loc.toLocation());
            nodeList.remove(loc);

        }
        resetEvent();
        clearNodeEditorText();
        warningText.clear();

    }
    public void editNode() {
        Location loc = LocationTable.getLocationByID(selected_lte.getID());
        try {
            loc.setShortName(nameText.getText());
            loc.setLongName(nameText.getText());
            if(xText.getText().equals("")) throw new Exception();
            if(yText.getText().equals("")) throw new Exception();

            loc.setyCord(Integer.parseInt(yText.getText()));
            loc.setxCord(Integer.parseInt(xText.getText()));
            loc.setBuilding(buildingText.getText());
            String floor = floorText.getText();
            if(!DatabaseHelpers.isValidFloor(floor)) {
                floorText.setText("Invalid floor!");
                throw new Exception();
            }
            loc.setFloor(floorText.getText());
            loc.setNodeType(DatabaseHelpers.stringToEnum(typeText.getText()));
            if(!nodeIDText.getText().equals(selected_lte.getID())) {
                nodeIDText.setText("ID cannot be changed on edit!");
                throw new Exception();
            }
            LocationTable.updateLocation(loc);
            this.loc = loc;
            visuallyUpdateNodes();
//            if(!LocationTable.updateLocation(loc)) throw new Exception();
            resetNodes();
            warningText.clear();

        } catch(Exception e) {
            warningText.setText("Operation failed!");
        }
        if(loc == null) return;
//        nodeIDText.setText(lte.getID());
//        xText.setText(""+lte.getX());
//        yText.setText(""+lte.getY());
//        nameText.setText(lte.getName());
//        floorText.setText(lte.getFloor());
//        buildingText.setText(lte.getBuilding());
    }
    public void addNode() {
        Location loc = new Location(null, 0, 0, null, null, null, null, null);
        try {
            loc.setNodeID(nodeIDText.getText());
            loc.setShortName(nameText.getText());
            loc.setLongName(nameText.getText());
            if(xText.getText().equals("")) throw new Exception();
            if(yText.getText().equals("")) throw new Exception();

            loc.setyCord(Integer.parseInt(yText.getText()));
            loc.setxCord(Integer.parseInt(xText.getText()));
            loc.setBuilding(buildingText.getText());
            String floor = floorText.getText();
            if(!DatabaseHelpers.isValidFloor(floor)) {
                floorText.setText("Invalid floor!");
                throw new Exception();
            }
            loc.setFloor(floorText.getText());
            loc.setNodeType(DatabaseHelpers.stringToEnum(typeText.getText()));

            if(!LocationTable.addLocation(loc)) throw new Exception();
            this.loc = loc;
            visualAddNode();
            resetNodes();

            warningText.clear();
            clearNodeEditorText();
        } catch(Exception e) {
            warningText.setText("Operation failed!");
        }
    }
    public void returnToMenu() throws Exception {
        ScreenController.deactivate();
        ScreenController.activate(Constants.Routes.ADMIN_MAP);
    }
    public void resetEvent() {
        resetNodes();
        resetEdges();
    }
    public void resetEvent(MouseEvent mouseEvent) {
        resetEvent();
    }

}

