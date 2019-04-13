package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import database.TransportationTable;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.services.TransportationRequest;
import models.user.User;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class TransportationRequestController  {

    public JFXButton btnSettings;
    public TableView<TransportationRequest> tblData;
    //public TableColumn<SanitationRequest,String> tblRequestID;
    public TableColumn<TransportationRequest,String> tblStartLoc;
    public TableColumn<TransportationRequest,String> tblEndLoc;
    public TableColumn<TransportationRequest,String> tblDetails;
    public TableColumn<TransportationRequest,String> tblTime;
    public TableColumn<TransportationRequest,String> tblDate;
    public TableColumn<TransportationRequest,String> tblRequester;
    public TableColumn<TransportationRequest,String> tblClaimTime;
    public TableColumn<TransportationRequest,String> tblServicer;
    public TableColumn<TransportationRequest,String> tblServiceTime;


    public JFXButton btnMarkDone;
    public JFXButton btnNavigate;
    public JFXButton btnClaim;
    public JFXTabPane tabMenu;
    public JFXTabPane floorMenu;

    ObservableList<TransportationRequest> transports = FXCollections.observableArrayList();

//    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        super.initialize(location, resources);
//        MapDisplay.displayCust(this, panes, TextPane);
//        VisualRealtimeController.setPanMap(panFloor1);
//        initSanitation();
//        updateSanitation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnNavigate.setDisable(false);

        });
        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if(selected!=null)
        if(selected.getServicer()==null||selected.getServicer().equals(UserHelpers.getCurrentUser())) {//only enable claiming if unclaimed
            btnClaim.setDisable(false);
        }else{
            btnClaim.setDisable(true);
        }

    }

    private void initSanitation(){
//        tblRequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
        tblStartLoc.setCellValueFactory(new PropertyValueFactory<>("StartLocation"));
        tblEndLoc.setCellValueFactory(new PropertyValueFactory<>("EndLocation"));
        //tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        //tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDetails.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        System.out.println(transports.toString());
        tblData.setItems(transports);
    }

    private void updateSanitation() {
        List<TransportationRequest> lstReqs = TransportationTable.getTransportationRequests();
        if(lstReqs!=null)
         transports.addAll(lstReqs);
    }

    public void navigateTo(){
//        Location start = map.getLocation(MapController.getTempStart());
//        Location end = tblData.getSelectionModel().getSelectedItem().getLocation();
//
//        PathFinder.printPath(panes, TextPane, map, start, end);
//
//        String floor = start.getFloor();
//        int floorIndex;
//        switch (floor) {
//            case "1":
//                floorIndex = 3;
//                break;
//            case "2":
//                floorIndex = 1;
//                break;
//            case "3":
//                floorIndex = 0;
//                break;
//            case "L1":
//                floorIndex = 3;
//                break;
//            default:
//                floorIndex = 4;
//                break;
//        }
//
//        tabMenu.getSelectionModel().select(0);
//        floorMenu.getSelectionModel().select(floorIndex);
    }

    public void tblClick(){
        updateClaimBtn();
        updateAllBTNS();
    }

    public void updateAllBTNS(){

        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
        User servicer = selected.getServicer();
        boolean btnClaimEnabled = servicer == null || (servicer.equals(UserHelpers.getCurrentUser())&& selected.getStatus() == TransportationRequest.Status.INCOMPLETE);
        btnClaim.setDisable(!btnClaimEnabled);
        boolean btnMarkDoneEnabled = servicer != null && servicer.equals(UserHelpers.getCurrentUser());
        btnMarkDone.setDisable(!btnMarkDoneEnabled);

    }
    public void claimJob(){

        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();

//        if (selected.getServicer() != null) {
//        }else{
//            selected.setServicer(null);
//        }


        if (selected.getServicer() != null) {
           //if(selected.getServicerUserName().equals(UserHelpers.getCurrentUser())){
               selected.setServicer(null);
               selected.setClaimedTime(null);
           //}

        } else {
            selected.setServicer(UserHelpers.getCurrentUser());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            selected.setClaimedTime(timestamp);
        }

        TransportationTable.editTransportationRequest(selected);

        updateClaimBtn();

        updateAllBTNS();

        tblData.refresh();

    }

    public void markDone(){
        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
//        if (selected.getUser().equals("")) {
//            selected.setUser("user_temp");
//        } else {
//            selected.setUser("");
//        }
        if (selected.getStatus() == TransportationRequest.Status.COMPLETE) {
            selected.setStatus(TransportationRequest.Status.INCOMPLETE);
            selected.setCompletedTime(null);
        } else {
            selected.setStatus(TransportationRequest.Status.COMPLETE);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            selected.setCompletedTime(timestamp);
        }
        TransportationTable.editTransportationRequest(selected);
        tblData.refresh();

        updateAllBTNS();

        updateMarkDoneBtn();
    }

    private void updateMarkDoneBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatus() == TransportationRequest.Status.COMPLETE) {
            btnMarkDone.setText("Mark Incomplete");
        } else {
            btnMarkDone.setText("Mark Complete");
        }
    }

    private void updateClaimBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getServicer()!=null) {
            btnClaim.setText("Un-Claim");
        } else {
            btnClaim.setText("Claim");
        }
    }

    public void sendRequest(){

    }

    public void cancelScr(){

    }
}


