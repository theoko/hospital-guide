package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import helpers.Constants;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.layout.FlowPane;
import javafx.util.Callback;
import map.MapDisplay;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMapController extends MapController /*implements Initializable*/{
    public JFXButton btnSettings;

    @FXML
    private FlowPane main;

 /*   @FXML
    private JFXTreeTableView<Room> treeView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        JFXTreeTableColumn<Room, String> roomN = new JFXTreeTableColumn<>("Room Name");
        roomN.setPrefWidth(150);
        roomN.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().roomName;
            }
        });


        JFXTreeTableColumn<Room, String> roomIDs = new JFXTreeTableColumn<>("Room ID");
        roomIDs.setPrefWidth(150);
        roomIDs.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().roomID;
            }
        });

        JFXTreeTableColumn<Room, String> roomT = new JFXTreeTableColumn<>("Room Type");
        roomT.setPrefWidth(150);
        roomT.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Room, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Room, String> param) {
                return param.getValue().getValue().roomType;
            }
        });

        ObservableList<Room> rooms = FXCollections.observableArrayList();
        rooms.add(new Room("Yeet", "YT420", "Get Lit Room"));
        rooms.add(new Room("Nope", "Aahhh", "KMN Room"));

        final TreeItem<Room> root = new RecursiveTreeItem<Room>(rooms, RecursiveTreeObject::getChildren);
        treeView.getColumns().setAll(roomN, roomIDs, roomT);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

    }

    class Room extends RecursiveTreeObject<Room> {
        StringProperty roomName;
        StringProperty roomID;
        StringProperty roomType;

        public Room(String roomName, String roomID, String roomType) {
            this.roomName = new SimpleStringProperty(roomName);
            this.roomID = new SimpleStringProperty(roomID);
            this.roomType = new SimpleStringProperty(roomType);
        }
    }*/

      public void initialize() {
          toolTip();
           MapDisplay.displayUser(panMap, "Tower", "1");
    }

    void toolTip() {
        btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }
}
