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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CustodianMapController extends MapController {

    public JFXButton btnSettings;
    public TableView<SpillModel> tblData;
    public TableColumn<SpillModel,String> tblLocation;
    public  TableColumn<SpillModel,String> tblPriority;
    public TableColumn<SpillModel,String> tblStatus;

    ObservableList<SpillModel> spillModels = FXCollections.observableArrayList();

    public void initialize() {
        toolTip();
        MapDisplay.displayUser(panMap, "Tower", "1");
        initSanitation();
        updateSanitation();
        seedTable();
    }

    void toolTip() {
        btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }

    private void initSanitation(){
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("Loc"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblData.setItems(spillModels);
    }

    private void updateSanitation() {
        List<SanitationRequest> lstReqs = Database.getSanitationRequests();
        for (SanitationRequest req : lstReqs) {
            SpillModel sm = new SpillModel(req.getLocation().getLongName(), req.getPriority().name(), "INCOMPLETE");
            spillModels.add(sm);
        }
    }
    //TODO: Delete this
    private void seedTable() {
        spillModels.add(
                new SpillModel("Loc1", "1", "INCOMPLETE")
        );
        spillModels.add(
                new SpillModel("Loc2", "2", "INCOMPLETE")
        );
        spillModels.add(
                new SpillModel("Loc3", "3", "INCOMPLETE")
        );
    }
}


