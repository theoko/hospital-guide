package controllers.search;

import com.jfoenix.controls.*;
import controllers.ScreenController;
import database.LocationTable;
import helpers.api.APIHelper;
import javafx.scene.input.MouseEvent;
import models.map.Location;
import models.search.SearchAPI;

public class TwoLocSearchPopupController {

    // Runs when send button is pressed
    private static Runnable onSendClick;

    public JFXTextField txtStartSearch;
    public JFXTextField txtEndSearch;
    public JFXButton btnSearch;

    public static void setOnSendClick(Runnable runnable) {
        onSendClick = runnable;
    }

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
        String startID = LocationTable.getLocationByLongName(txtStartSearch.getText()).iterator().next().getNodeID();
        String endID = LocationTable.getLocationByLongName(txtStartSearch.getText()).iterator().next().getNodeID();
        APIHelper.setStartLocID(startID);
        APIHelper.setEndLocID(endID);
        onSendClick.run();
        ScreenController.deactivate();
    }

    public void goBack(MouseEvent event) {
        event.consume();
        ScreenController.deactivate(); // TODO figure out appropriate action after UI refactor
    }
}


