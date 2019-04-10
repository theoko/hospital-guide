package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import database.Database;
import database.LocationTable;
import database.SanitationTable;
import helpers.Constants;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.sanitation.SanitationRequest;
import models.user.User;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.net.URL;

import static controllers.VisualRealtimeController.panMap;

public class CustodianMapController extends MapController {

    public JFXButton btnSettings;
    public TableView<SanitationRequest> tblData;
    public TableColumn<SanitationRequest,String> tblRequestID;
    public TableColumn<SanitationRequest,String> tblLocation;
    public TableColumn<SanitationRequest,String> tblPriority;
    public TableColumn<SanitationRequest,String> tblStatus;
    public TableColumn<SanitationRequest,String> tblDescription;
    public TableColumn<SanitationRequest,String> tblRequester;
    public TableColumn<SanitationRequest,String> tblClaimTime;
    public TableColumn<SanitationRequest,String> tblServicer;
    public TableColumn<SanitationRequest,String> tblServiceTime;


    public JFXButton btnMarkDone;
    public JFXButton btnNavigate;
    public JFXButton btnClaim;
    public JFXTabPane tabMenu;
    public JFXTabPane floorMenu;

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        MapDisplay.displayCust(this, panes);
        VisualRealtimeController.setPanMap(panFloor1);
        initSanitation();
        updateSanitation();

        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnNavigate.setDisable(false);

        });
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        if(selected!=null)
        if(selected.getServicer()==null||selected.getServicer().equals(UserHelpers.getCurrentUser())) {//only enable claiming if unclaimed
            btnClaim.setDisable(false);
        }else{
            btnClaim.setDisable(true);
        }

    }

    private void initSanitation(){
//        tblRequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("LocationShortName"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        System.out.println(spills.toString());
        tblData.setItems(spills);
    }

    private void updateSanitation() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        if(lstReqs!=null)
         spills.addAll(lstReqs);
    }

    public void navigateTo(){
        Location start = map.getLocation(PathFinder.getDefLocation());
        Location end = tblData.getSelectionModel().getSelectedItem().getLocation();
        PathFinder.printPath(panes, map, start, end);

        String floor = start.getFloor();
        int floorIndex;
        switch (floor) {
            case "1":
                floorIndex = 3;
                break;
            case "2":
                floorIndex = 1;
                break;
            case "3":
                floorIndex = 0;
                break;
            case "L1":
                floorIndex = 3;
                break;
            default:
                floorIndex = 4;
                break;
        }

        tabMenu.getSelectionModel().select(0);
        floorMenu.getSelectionModel().select(floorIndex);
    }

    public void tblClick(){
        updateClaimBtn();
        updateAllBTNS();
    }

    public void updateAllBTNS(){

        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        User servicer = selected.getServicer();
        boolean btnClaimEnabled = servicer == null || (servicer.equals(UserHelpers.getCurrentUser())&& selected.getStatus() == SanitationRequest.Status.INCOMPLETE);
        btnClaim.setDisable(!btnClaimEnabled);
        boolean btnMarkDoneEnabled = servicer != null && servicer.equals(UserHelpers.getCurrentUser());
        btnMarkDone.setDisable(!btnMarkDoneEnabled);

    }
    public void claimJob(){

        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();

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

        SanitationTable.editSanitationRequest(selected);

        updateClaimBtn();

        updateAllBTNS();

        tblData.refresh();

    }

    public void markDone(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
//        if (selected.getUser().equals("")) {
//            selected.setUser("user_temp");
//        } else {
//            selected.setUser("");
//        }
        if (selected.getStatus() == SanitationRequest.Status.COMPLETE) {
            selected.setStatus(SanitationRequest.Status.INCOMPLETE);
            selected.setCompletedTime(null);
        } else {
            selected.setStatus(SanitationRequest.Status.COMPLETE);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            selected.setCompletedTime(timestamp);
        }
        SanitationTable.editSanitationRequest(selected);
        tblData.refresh();

        updateAllBTNS();

        updateMarkDoneBtn();
    }

    private void updateMarkDoneBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatus() == SanitationRequest.Status.COMPLETE) {
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
}


