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
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

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

    public TableColumn<SanitationRequest,String> tblRequestTime;


    ObservableList<SanitationRequest> requests = FXCollections.observableArrayList();

    ObservableList<SimpleEmployee> chartEmployees = FXCollections.observableArrayList();
    ObservableList<SimpleEmployee> chartCustodians = FXCollections.observableArrayList();

    ArrayList<String> filteredUsersGlobal = new ArrayList<String>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
       // super.initialize(location, resources);

        initTables();
        List<SanitationRequest> unfilteredRequests = updateRequests();

        SanitationAnalyzer fullAnalyzer=new SanitationAnalyzer();
        filteredUsersGlobal.addAll(fullAnalyzer.getEmployees());

        if (unfilteredRequests != null) {
            updatePieChart(unfilteredRequests);
            updateSummaryStats();
            requests.addAll(unfilteredRequests);
        }


        initSanitation();
        updateSanitation();

forceSimpleEmployeeUpdate(fullAnalyzer);

    }
    public void forceSimpleEmployeeUpdate(SanitationAnalyzer fullAnalyzer){
        ArrayList<String> employees = fullAnalyzer.getEmployees();
        ArrayList<String> custodians = fullAnalyzer.getCustodians();

        for (String employee : employees) {

            int requestedCount = fullAnalyzer.getEmployeeRequestCount().get(employee);
            SimpleEmployee emp = new SimpleEmployee(employee);
            emp.setRequests(requestedCount);
            chartEmployees.add(emp);

        }

        chartCustodians.clear();
        // Custodian Statistics
        for (String custodian : custodians) {
            int claimedCount = fullAnalyzer.getCustodianClaimedCount().get(custodian);
            int completedCount = fullAnalyzer.getCustodianCompletedCount().get(custodian);
            SimpleEmployee emp = new SimpleEmployee(custodian);
            emp.setClaimed(claimedCount);
            emp.setCompleted(completedCount);
            chartCustodians.add(emp);
        }
    }


    public void tblClick() {

        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if (selected != null)


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
        //if (lstReqs != null) {
        //    requests.addAll(lstReqs);
        //}
        return lstReqs;
    }

    private void updateSummaryStats() {
        updateSummaryStats(new SanitationAnalyzer());
    }

    private void updateSummaryStats(SanitationAnalyzer analyzer) {
        //SanitationAnalyzer analyzer = new SanitationAnalyzer();

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

        chartEmployees.clear();
        // Employee Statistics
        for (String employee : employees) {
            if(filteredUsersGlobal.contains(employee)) {
                int requestedCount = analyzer.getEmployeeRequestCount().get(employee);
                SimpleEmployee emp = new SimpleEmployee(employee);
                emp.setRequests(requestedCount);
                chartEmployees.add(emp);
            }
        }

        chartCustodians.clear();
        // Custodian Statistics
        for (String custodian : custodians) {
            if(filteredUsersGlobal.contains(custodian)) {
                int claimedCount = analyzer.getCustodianClaimedCount().get(custodian);
                int completedCount = analyzer.getCustodianCompletedCount().get(custodian);
                SimpleEmployee emp = new SimpleEmployee(custodian);
                emp.setClaimed(claimedCount);
                emp.setCompleted(completedCount);
                chartCustodians.add(emp);
            }
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



    private void initSanitation(){
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("LocationShortName"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
        tblRequestTime.setCellValueFactory(new PropertyValueFactory<>("RequestTime"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        tblData.setItems(requests);
    }

    protected List updateSanitation() {
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
                new PieChart.Data("Complete", complete),
                new PieChart.Data("Claimed", claimed),
                new PieChart.Data("Un-Claimed", notstarted)

        );

        chartPie.setData(pieChartData);

    }

    public void filterChange() {

        boolean hasNames = !txtUserNames.getText().equals("");
        boolean hasStartDate = (datStartDate.getValue() == null);
        boolean hasEndDate = (datStartDate.getValue() == null);

        //btnFilter.setDisable(hasNames||hasStartDate||hasEndDate);//old only search

        boolean startAfterEnd= false;

        if (datStartDate.getValue() != null&&datEndDate.getValue() != null) {
            Date startDate = Date.from(datStartDate.getValue().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant());
            Timestamp startTime = new Timestamp(startDate.getTime());


            Date endDate = Date.from(datEndDate.getValue().atStartOfDay()
                    .atZone(ZoneId.systemDefault()).toInstant());
            Timestamp endTime = new Timestamp(endDate.getTime());

            if(startTime.after(endTime)){
                startAfterEnd=true;
            }

        }


        btnFilter.setDisable(startAfterEnd);//new clear search
    }

    public void cancelScr() {
        //todo implement or remove from fxml
    }

//    public  void tblClick(){
//        //todo implement or remove from fxml
//    }

    public void btnClear(){
        datEndDate.getEditor().clear();
        datStartDate.getEditor().clear();
        txtUserNames.clear();

        filterData();
        filterData();
    }

    public void filterData() {
//        event.consume();
//        updateRequests();

        List<SanitationRequest> fullList = updateRequests();

        if (txtUserNames.getText().equals("") && datStartDate.getValue() == null && datStartDate.getValue() == null) {//clear search
            updateSanitation();
            SanitationAnalyzer analyzer = new SanitationAnalyzer();
            updateSummaryStats(analyzer);
            updatePieChart(fullList);
            requests.clear();
            requests.addAll(fullList);
            updateSummaryStats(analyzer);

            forceSimpleEmployeeUpdate(analyzer);


            tblCustodian.refresh();
            tblEmployee.refresh();


        } else {

            String users = txtUserNames.getText() + ",";
            List<String> userList = Arrays.asList(users.split("\\s*,\\s*"));
            ArrayList<String> userListTrimmed = new ArrayList<String>();
            for (int i = 0; i < userList.size(); i++) {
                String newString = userList.get(i);
                userListTrimmed.add(newString);
            }

            //List<SanitationRequest> filteredList = new List;

            List<SanitationRequest> filteredList = new ArrayList<>();


            Timestamp earliestTime = fullList.get(0).getRequestTime();
            Timestamp latestTime = fullList.get(0).getRequestTime();

            for (SanitationRequest request : fullList
            ) {
                Timestamp currentTime = request.getRequestTime();
                if (currentTime.before(earliestTime)) {
                    earliestTime = currentTime;
                }
                if (currentTime.after(latestTime)) {
                    latestTime = currentTime;
                }

            }

            if (datStartDate.getValue() != null) {
                Date date = Date.from(datStartDate.getValue().atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant());
                Timestamp timeStamp = new Timestamp(date.getTime());
                earliestTime = timeStamp;
            }

            if (datEndDate.getValue() != null) {
                Date date = Date.from(datEndDate.getValue().atStartOfDay()
                        .atZone(ZoneId.systemDefault()).toInstant());
                Timestamp timeStamp = new Timestamp(date.getTime());
                latestTime = timeStamp;
            }


            for (SanitationRequest req : fullList) {//lop through all requests
                if (req.getRequestTime().after(earliestTime) && req.getRequestTime().before(latestTime)) {//if a request is between the min and max date
                    if (txtUserNames.getText().equals("")) {//if no user is specified
                        filteredList.add(req);//add the request to the result
                        userListTrimmed.add(req.getRequesterUserName());
                        userListTrimmed.add(req.getServicerUserName());

                    } else {//a user or several are specified
                        for (String name : userList) {//loop through all specified users
                            if (name.equals(req.getRequesterUserName()) || name.equals(req.getServicerUserName())) {//if the request has a user name we are looking for
                                filteredList.add(req);//add the request to the result
                            }
                        }
                    }
                }
            }


            ArrayList<SanitationRequest> finalList = new ArrayList<SanitationRequest>(filteredList);

            SanitationAnalyzer analyzer = new SanitationAnalyzer(finalList);//call with filtered list arg
            updateSummaryStats(analyzer);
            updatePieChart(finalList);
            requests.clear();
            requests.addAll(filteredList);
            filteredUsersGlobal.clear();
            filteredUsersGlobal.addAll(userListTrimmed);
           // forceSimpleEmployeeUpdate(analyzer);

        }

        List<SanitationRequest> unfilteredRequests = updateRequests();
        List<SanitationRequest> filteredRequests;


        tblEmployee.refresh();
        tblCustodian.refresh();
        tblData.refresh();

    }


}
