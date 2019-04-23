package controllers.search;

import com.jfoenix.controls.*;
import controllers.ScreenController;
import database.LocationTable;
import helpers.api.APIHelper;
import javafx.scene.input.MouseEvent;
import models.map.Location;
import models.search.SearchAPI;

public class TwoLocSearchPopupController {

    public JFXTextField txtStartSearch;
    public JFXTextField txtEndSearch;
    public JFXButton btnSearch;

    public void initialize() {
        SearchAPI txtStartSearchAPI = new SearchAPI(txtStartSearch);
        SearchAPI txtEndSearchAPI = new SearchAPI(txtEndSearch);
        txtStartSearchAPI.searchable();
        txtEndSearchAPI.searchable();
    }

    public void updateRequestBTNs() {
        boolean startLocEmpty=txtStartSearch.getText().equals("");
        boolean endLocEmpty=txtEndSearch.getText().equals("");
        btnSearch.setDisable(startLocEmpty || endLocEmpty );
    }

    public void sendRequest(MouseEvent event) {
        APIHelper.setStartLocID(txtStartSearch.getText());
        APIHelper.setEndLocID(txtEndSearch.getText());
        ScreenController.deactivate();
    }

    public void goBack(MouseEvent event) {
        event.consume();
        ScreenController.deactivate(); // TODO figure out appropriate action after UI refactor
    }
}


