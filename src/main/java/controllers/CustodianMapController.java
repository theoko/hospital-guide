package controllers;

import com.jfoenix.controls.JFXButton;
import controllers.MapController;
import database.Database;
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
import map.MapDisplay;
import models.sanitation.SanitationRequest;

import javax.xml.crypto.Data;
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
    public JFXButton btnClaim;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    public void initialize() {
        toolTip();
        MapDisplay.displayEmployee(panMap, "Tower", "1");
        initSanitation();
        updateSanitation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnMarkDone.setDisable(false);
            btnClaim.setDisable(false);
        });

    }

    void toolTip() {
        btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
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
        List<SanitationRequest> lstReqs = Database.getSanitationRequests();
         spills.addAll(lstReqs);
    }

    public void markDone(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        Database.removeSanitationRequest(selected);
    }

    public void itemSelected(){

    }
}


