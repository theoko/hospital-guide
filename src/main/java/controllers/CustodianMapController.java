package controllers;

import com.jfoenix.controls.JFXButton;
import controllers.MapController;
import database.Database;
import database.SanitationTable;
import helpers.Constants;
import helpers.SpillModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import models.sanitation.SanitationRequest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustodianMapController extends MapController {

    public JFXButton btnSettings;
    public TableView<SanitationRequest> tblData;
    public TableColumn<SanitationRequest,String> tblLocation;
    public TableColumn<SanitationRequest,String> tblPriority;
    public TableColumn<SanitationRequest,String> tblStatus;
    public TableColumn<SanitationRequest,String> tblUser;
    public TableColumn<SanitationRequest,String> tblDescription;

    public JFXButton btnMarkDone;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        MapDisplay.displayCust(panes);
        initSanitation();
        updateSanitation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnMarkDone.setDisable(false);
            updateBtn();
        });
    }

    private void initSanitation(){
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        tblData.setItems(spills);
    }

    private void updateSanitation() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        spills.addAll(lstReqs);
    }

    public void markDone(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if (selected.getUser().equals("")) {
            selected.setUser("user_temp");
        } else {
            selected.setUser("");
        }
        if (selected.getStatusObj() == SanitationRequest.Status.COMPLETE) {
            selected.setStatus(SanitationRequest.Status.INCOMPLETE);
        } else {
            selected.setStatus(SanitationRequest.Status.COMPLETE);
        }
        SanitationTable.editSanitationRequest(selected);
        tblData.refresh();
        updateBtn();
    }

    private void updateBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatusObj() == SanitationRequest.Status.COMPLETE) {
            btnMarkDone.setText("Mark Incomplete");
        } else {
            btnMarkDone.setText("Mark Complete");
        }
    }
}


