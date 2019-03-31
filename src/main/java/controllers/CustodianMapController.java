package controllers;

import com.jfoenix.controls.JFXButton;
import controllers.MapController;
import helpers.Constants;
import helpers.SpillModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import map.MapDisplay;

public class CustodianMapController extends MapController{

    public JFXButton btnSettings;
    @FXML
    private TableView<SpillModel> tblData;
    @FXML
    private TableColumn<SpillModel,String> tblLocation;
    @FXML
    private  TableColumn<SpillModel,String> tblPriority;
    @FXML
    private TableColumn<SpillModel,String> tblStatus;

    public void initialize() {
        toolTip();
        MapDisplay.displayUser(panMap, "Tower", "1");
        updateSanitation();
    }

    void toolTip() {
        btnSettings.setTooltip(new Tooltip(Constants.SETTINGS_BUTTON_TOOLTIP));
        btnReturn.setTooltip(new Tooltip(Constants.LOGOUT_BUTTON_TOOLTIP));
    }


    public void updateSanitation(){

        //tblLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        ObservableList<SpillModel> spillModels = FXCollections.observableArrayList();
        spillModels.add(new SpillModel("Hallway 3B", "HIGH"));
        tblData.setItems(spillModels);


    }
}


