package controllers.transportation;

import com.jfoenix.controls.*;
import controllers.maps.MapController;
import database.LocationTable;
import database.TransportationTable;
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
import models.services.ServiceRequest;
import models.services.TransportationRequest;
import models.user.User;

import java.net.URL;
import java.sql.Timestamp;
import java.util.*;

public class TransportationRequestController  {

//   public JFXComboBox cmbStartLoc;
//   public JFXComboBox cmbEndLoc;
    public JFXTextField txtStartSearch;
    public JFXTextField txtEndSearch;


    public JFXTextField txtDetails;
   public JFXDatePicker datDate;
   public JFXTimePicker datTime;

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


    public JFXButton btnSendRequest;
    public JFXButton btnMarkDone;
    public JFXButton btnNavigate;
    public JFXButton btnClaim;
    public JFXButton btnDelete;
    public JFXTabPane tabMenu;
    public JFXTabPane floorMenu;

    ObservableList<TransportationRequest> transports = FXCollections.observableArrayList();

//    @Override
    public void initialize() {

//        super.initialize(location, resources);
//        MapDisplay.displayCust(this, panes, TextPane);
//        VisualRealtimeController.setPanMap(panFloor1);
//        initSanitation();
//        updateSanitation();

        SearchAPI txtStartSearchAPI = new SearchAPI(txtStartSearch);
        SearchAPI txtEndSearchAPI = new SearchAPI(txtEndSearch);

        txtStartSearchAPI.searchable();
        txtEndSearchAPI.searchable();

        initTransportation();
        updateTransportation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {// only enable btns if item selected
            btnDelete.setDisable(false);

        });




        //todo fix navigate btn disable

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

//        cmbStartLoc.valueProperty().addListener(((observable, oldValue, newValue) -> {
//           // btnSendRequest.setDisable(false);
//            updateRequestBTNs();
//        }));


    }

    private void initTransportation(){
        tblStartLoc.setCellValueFactory(new PropertyValueFactory<>("StartShortName"));
        tblEndLoc.setCellValueFactory(new PropertyValueFactory<>("EndShortName"));
        tblDetails.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
//        tblDate.setCellValueFactory(new PropertyValueFactory<>("DueDate"));
//        tblTime.setCellValueFactory(new PropertyValueFactory<>("DueTime"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        //System.out.println(transports.toString());
        tblData.setItems(transports);

    }

    private List updateTransportation() {
        List<TransportationRequest> lstReqs = TransportationTable.getTransportationRequests();
        if (lstReqs != null){
            transports.addAll(lstReqs);
    }
        return lstReqs;
    }

    public void navigateTo(){
        MapController mc = MapController.getCurrMapControl();
        Location start = mc.getMap().getLocation(MapController.getTempStart());
        Location end = tblData.getSelectionModel().getSelectedItem().getLocation();
        PathFinder.printPath(mc, start, end);
//        mc.getTabMenu().getSelectionModel().select(0);
    }

    public void tblClick(){
        try {
            TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
            if(selected.getDestination()!=null){
                updateClaimBtn();
                updateTableBTNs();
            }
        }catch (NullPointerException e){

        }
    }

    public void requestClick(){updateRequestBTNs();}

    public void updateRequestBTNs(){
        // TODO replace with search engine...
//        boolean startLocEmpty = (cmbStartLoc.getValue() == null);
//        boolean endLocEmpty = (cmbEndLoc.getValue() == null);
        boolean startLocEmpty=txtStartSearch.getText().equals("");
        boolean endLocEmpty=txtEndSearch.getText().equals("");

        boolean detailsEmpty = txtDetails.getText().equals("");
        boolean dateEmpty = (datDate.getValue() == null);
        boolean timeEmpty = (datTime.getValue() == null);

        btnSendRequest.setDisable(startLocEmpty || endLocEmpty || detailsEmpty || dateEmpty || timeEmpty);
    }

    public void updateTableBTNs(){

        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
        User servicer = selected.getServicer();
        boolean btnClaimEnabled = servicer == null || (servicer.equals(UserHelpers.getCurrentUser())&& selected.getStatus() == TransportationRequest.Status.INCOMPLETE);
        btnClaim.setDisable(!btnClaimEnabled);
        boolean btnMarkDoneEnabled = servicer != null && servicer.equals(UserHelpers.getCurrentUser());
        btnMarkDone.setDisable(!btnMarkDoneEnabled);

    }
    public void claimJob(){

        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();

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
        updateTableBTNs();
        tblData.refresh();

    }

    public void markDone(){
        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();

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

        updateTableBTNs();
        updateMarkDoneBtn();
    }

    private void updateBtns(){
        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if (selected != null) {
            User servicer = selected.getServicer();

            boolean btnClaimEnabled = (servicer == null || (servicer.equals(UserHelpers.getCurrentUser()) &&
                    (selected.getStatus() == TransportationRequest.Status.INCOMPLETE)));
            btnClaim.setDisable(!btnClaimEnabled);

            boolean btnMarkDoneEnabled = (servicer != null && servicer.equals(UserHelpers.getCurrentUser()));
            btnMarkDone.setDisable(!btnMarkDoneEnabled);
        }
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

    public void sendRequest(MouseEvent event) {
        event.consume();

        // Get request data from UI fields
        String description = txtDetails.getText();

        //get locations form search fields
        Location startLoc =LocationTable.getLocationByLongName(txtStartSearch.getText()).iterator().next();
        Location endLoc=LocationTable.getLocationByLongName(txtEndSearch.getText()).iterator().next();

        TransportationRequest request = new TransportationRequest(startLoc,endLoc, txtDetails.getText(),datDate.toString(),datTime.toString(), UserHelpers.getCurrentUser());

        TransportationTable.addTransportationRequest(request);


        updateTransportation();
        tblData.refresh();
    }

    public void cancelScr(MouseEvent event){
        event.consume();
        //ScreenController.deactivate();//todo figure out appropriate action after UI refactor
    }

    public void deleteTransportationRequest(MouseEvent mouseEvent) {
        TransportationRequest selected = tblData.getSelectionModel().getSelectedItem();
        TransportationTable.deleteTransportationRequest(selected);
        transports.setAll(updateTransportation());
        tblData.refresh();
    }

}


