package controllers.sanitation;

import com.jfoenix.controls.*;
import controllers.maps.MapController;
import database.LocationTable;
import database.SanitationTable;
import database.TransportationTable;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import map.PathFinder;
import models.map.Location;
import models.services.SanitationRequest;
import models.services.ServiceRequest;
import models.services.TransportationRequest;
import models.user.User;

import java.sql.Timestamp;
import java.util.List;

public class SanitationReportController {

    public JFXButton btnFilterRequests;
    public PieChart chartPie;

    ObservableList<SanitationRequest> requests = FXCollections.observableArrayList();

    //    @Override
    public void initialize() {

        List<SanitationRequest> unfilteredRequests =updateRequests();
        updatePieChart(unfilteredRequests);


    }

    private List updateRequests() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        if (lstReqs != null) {
            requests.addAll(lstReqs);
        }
        return lstReqs;
    }

    private void updatePieChart(List<SanitationRequest> requests) {

        int complete = 0;
        int notstarted = 0;
        int claimed = 0;
        for (SanitationRequest request : requests) {
            if (request.getStatus() == ServiceRequest.Status.COMPLETE) {
                complete++;
            } else if (request.getClaimedTime() != null) {
                claimed++;
            } else {
                notstarted++;

            }
        }
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Complete",complete),
                new PieChart.Data("Claimed",claimed),
                new PieChart.Data("Un-Claimed",notstarted)

                );

        chartPie.setData(pieChartData);

    }

    public void filterChange(){
        //todo enable disable filter btn appropriately. borrow from transportation request
    }

    public void cancelScr(){
        //todo implement or remove from fxml
    }

//    public  void tblClick(){
//        //todo implement or remove from fxml
//    }

    public void filterData(MouseEvent event) {
        event.consume();
        updateRequests();

        //todo implement filtering based on inputs (specific user date ect) filter observable

//
//        // Get request data from UI fields
//        String description = txtDetails.getText();
//
//        //get locations form search fields
//        Location startLoc =LocationTable.getLocationByLongName(txtStartSearch.getText()).iterator().next();
//        Location endLoc=LocationTable.getLocationByLongName(txtEndSearch.getText()).iterator().next();
//
//        TransportationRequest request = new TransportationRequest(startLoc,endLoc, txtDetails.getText(),datDate.toString(),datTime.toString(), UserHelpers.getCurrentUser());
//
//        TransportationTable.addTransportationRequest(request);
//
//
//        updateTransportation();
//        tblData.refresh();
    }


}


