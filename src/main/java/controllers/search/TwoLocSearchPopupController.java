package controllers.search;

import com.jfoenix.controls.*;
import controllers.ScreenController;
import controllers.maps.MapController;
import database.LocationTable;
import database.TransportationTable;
import helpers.APIHelper;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import map.PathFinder;
import models.map.Location;
import models.search.SearchAPI;
import models.services.TransportationRequest;
import models.user.User;

import java.sql.Timestamp;
import java.util.List;

public class TwoLocSearchPopupController {

//   public JFXComboBox cmbStartLoc;
//   public JFXComboBox cmbEndLoc;
    public JFXTextField txtStartSearch;
    public JFXTextField txtEndSearch;
    public JFXButton btnSearch;


//    @Override
    public void initialize() {
        SearchAPI txtStartSearchAPI = new SearchAPI(txtStartSearch);
        SearchAPI txtEndSearchAPI = new SearchAPI(txtEndSearch);

        txtStartSearchAPI.searchable();
        txtEndSearchAPI.searchable();
    }





    public void updateRequestBTNs(){

        boolean startLocEmpty=txtStartSearch.getText().equals("");
        boolean endLocEmpty=txtEndSearch.getText().equals("");



        btnSearch.setDisable(startLocEmpty || endLocEmpty );
    }
    public Location getEndLocation(){
        Location endLoc=LocationTable.getLocationByLongName(txtEndSearch.getText()).iterator().next();
        return endLoc;
    }
    public Location getStartLocation(){
        Location startLoc =LocationTable.getLocationByLongName(txtStartSearch.getText()).iterator().next();
        return startLoc;
    }

    public void sendRequest(MouseEvent event) {
        APIHelper.setStartLocID(txtStartSearch.getText());
        APIHelper.setEndLocID(txtEndSearch.getText());
        ScreenController.deactivate();
    }

    public void goBack(MouseEvent event){
        event.consume();
        ScreenController.deactivate();//todo figure out appropriate action after UI refactor
    }


}


