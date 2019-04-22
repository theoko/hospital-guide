package controllers.sanitation;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import database.SanitationTable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.analysis.SanitationAnalyzer;
import models.services.SanitationRequest;
import models.services.ServiceRequest;
import models.services.SimpleEmployee;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class AdminSanitationController extends SanitationController {
    public JFXButton btnDelete;
    public TableView<SanitationRequest> tblData;


    public PieChart chartPie;
    public LineChart chartLine;

    public Text textSummaryStatistics;
    public Text textRequestToClaimTime;
    public Text textClaimToCompleteTime;

    public JFXTextField txtUserNames;
    public JFXDatePicker datStartDate;
    public JFXDatePicker datEndDate;

    public JFXButton btnFilter;

    public TableView<SimpleEmployee> tblEmployee;
    public TableColumn<SimpleEmployee, String> tblEmployeeEmployee;
    public TableColumn<SimpleEmployee, String> tblEmployeeRequests;

    public TableView<SimpleEmployee> tblCustodian;
    public TableColumn<SimpleEmployee, String> tblCustodianEmployee;
    public TableColumn<SimpleEmployee, String> tblCustodianClaimed;
    public TableColumn<SimpleEmployee, String> tblCustodianCompleted;


    ObservableList<SanitationRequest> requests = FXCollections.observableArrayList();

    ObservableList<SimpleEmployee> chartEmployees = FXCollections.observableArrayList();
    ObservableList<SimpleEmployee> chartCustodians = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        initTables();
        List<SanitationRequest> unfilteredRequests = updateRequests();
        updatePieChart(unfilteredRequests);
        updateSummaryStats(unfilteredRequests);
    }



    public void tblClick(){

        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if(selected!=null)
            btnDelete.setDisable(false);
    }

    public void deleteSanitationRequest(MouseEvent mouseEvent) {
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        super.deleteSanitationRequest(selected);
    }
//    private List updateEmployees(List<SimpleEmployee> lstReqs) {
//        if (lstReqs != null){
//            chartEmployees.addAll(lstReqs);
//        }
//        return lstReqs;
//    }
//    private List updateCustodians( List<SimpleEmployee> lstReqs) {
//       ;
//        if (lstReqs != null){
//            chartCustodians.addAll(lstReqs);
//        }
//        return lstReqs;
//    }

    private void initTables() {
        tblEmployeeEmployee.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tblEmployeeRequests.setCellValueFactory(new PropertyValueFactory<>("Requests"));
        tblCustodianEmployee.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tblCustodianClaimed.setCellValueFactory(new PropertyValueFactory<>("Claimed"));
        tblCustodianCompleted.setCellValueFactory(new PropertyValueFactory<>("Completed"));
        tblEmployee.setItems(chartEmployees);
        tblCustodian.setItems(chartCustodians);

    }

    private List updateRequests() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        if (lstReqs != null) {
            requests.addAll(lstReqs);
        }
        return lstReqs;
    }

    private void updateSummaryStats(List<SanitationRequest> requests) {
        SanitationAnalyzer analyzer = new SanitationAnalyzer();

        StringBuilder summaryStats = new StringBuilder();
        summaryStats.append("Requests: " + analyzer.getNumRequests().toString() + "      Incomplete: " + analyzer.getPercentIncomplete().toString() + "%      Claimed: " + analyzer.getPercentClaimed().toString() + "%      Completed: " + analyzer.getNumCompletedRequests().toString());
        textSummaryStatistics.setText(summaryStats.toString());

        StringBuilder requestToClaimTime = new StringBuilder();
        requestToClaimTime.append("Mean: " + analyzer.getMeanClaimedTime().toString() + "      Minimum: " + analyzer.getMinClaimedTime().toString() + "      Maximum: " + analyzer.getMaxClaimedTime().toString());
        textRequestToClaimTime.setText(requestToClaimTime.toString());

        StringBuilder claimToCompleteTime = new StringBuilder();
        claimToCompleteTime.append("Mean: " + analyzer.getMeanCompletedTime().toString() + "      Minimum: " + analyzer.getMinCompletedTime().toString() + "      Maximum: " + analyzer.getMaxCompletedTime().toString());
        textClaimToCompleteTime.setText(claimToCompleteTime.toString());

        ArrayList<String> employees = analyzer.getEmployees();
        ArrayList<String> custodians = analyzer.getCustodians();

        // Employee Statistics
        for (String employee : employees) {
            int requestedCount = analyzer.getEmployeeRequestCount().get(employee);
            SimpleEmployee emp = new SimpleEmployee(employee);
            emp.setRequests(requestedCount);
            chartEmployees.add(emp);
        }

        // Custodian Statistics
        for (String custodian : custodians) {
            int claimedCount = analyzer.getCustodianClaimedCount().get(custodian);
            int completedCount = analyzer.getCustodianCompletedCount().get(custodian);
            SimpleEmployee emp = new SimpleEmployee(custodian);
            emp.setClaimed(claimedCount);
            emp.setCompleted(completedCount);
            chartCustodians.add(emp);
        }
        tblEmployee.refresh();
        tblCustodian.refresh();
        //analyzer.getEmployeeRequestCount()

    }

    private void updateLineChart(List<SanitationRequest> requests) {

        ObservableList<XYChart.Series<Date, Number>> completedLine = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Date, Number>> claimedLine = FXCollections.observableArrayList();
        ObservableList<XYChart.Series<Date, Number>> requestedLine = FXCollections.observableArrayList();


        for (SanitationRequest request : requests) {
            if (request.getStatus() == ServiceRequest.Status.COMPLETE) {

                // completedLine.add(new XYChart.Series<Date, Number>(new GregorianCalendar(2012, 11, 15).getTime(), 2);


                // completedLine.add(new XYChart.Data<Date,Number>(request.getCompletedTime(),1));
                // claimedLine.add(new LineChart.Data(request.getClaimedTime(),1));
                // requestedLine.add(new LineChart.Data(request.getRequestTime(),1));
            } else if (request.getClaimedTime() != null) {
                // claimedLine.add(new LineChart.Data(request.getClaimedTime(),1));
                // requestedLine.add(new LineChart.Data(request.getRequestTime(),1));
            } else {
                // requestedLine.add(new LineChart.Data(request.getRequestTime(),1));
            }
        }
        chartLine.setData(completedLine);
        chartLine.setData(claimedLine);
        chartLine.setData(requestedLine);


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
                new PieChart.Data("Complete", complete),
                new PieChart.Data("Claimed", claimed),
                new PieChart.Data("Un-Claimed", notstarted)

        );

        chartPie.setData(pieChartData);

    }

    public void filterChange() {

        boolean hasNames = !txtUserNames.getText().equals("");
        boolean hasStartDate =(datStartDate.getValue() == null);
        boolean hasEndDate=(datStartDate.getValue() == null);

        btnFilter.setDisable(hasNames||hasStartDate||hasEndDate);
    }

    public void cancelScr() {
        //todo implement or remove from fxml
    }

//    public  void tblClick(){
//        //todo implement or remove from fxml
//    }

    public void filterData(MouseEvent event) {
        event.consume();
        updateRequests();

        //todo implement filtering based on inputs (specific user date ect) filter observable

        List<SanitationRequest> unfilteredRequests = updateRequests();
        List<SanitationRequest> filteredRequests;


//        for(SanitationRequest request:unfilteredRequests){
//            if(request.getRequestTime()>)
//
//        }
//        for(SanitationRequest request:unfilteredRequests){
//            if(request.getRequestTime()>)
//
//        }



        // updatePieChart(filteredRequests);

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
