package controllers;

import com.jfoenix.controls.JFXButton;
import database.SanitationTable;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.sanitation.SanitationRequest;
import models.user.User;

import java.net.URL;
import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;

public class SanitationTableController implements Initializable {

    public TableView<SanitationRequest> tblData;
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

    ObservableList<SanitationRequest> spills = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblData.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            btnNavigate.setDisable(false);
        });

        initSanitation();
        updateSanitation();
    }

    private void initSanitation(){
        tblLocation.setCellValueFactory(new PropertyValueFactory<>("LocationShortName"));
        tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));
        tblStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        tblDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tblRequester.setCellValueFactory(new PropertyValueFactory<>("RequesterUserName"));
        tblClaimTime.setCellValueFactory(new PropertyValueFactory<>("ClaimedTime"));
        tblServiceTime.setCellValueFactory(new PropertyValueFactory<>("CompletedTime"));
        tblServicer.setCellValueFactory(new PropertyValueFactory<>("ServicerUserName"));
        tblData.setItems(spills);
    }

    private void updateSanitation() {
        List<SanitationRequest> lstReqs = SanitationTable.getSanitationRequests();
        if (lstReqs != null) {
            spills.addAll(lstReqs);
        }
    }

    public void tblClick(MouseEvent mouseEvent) {
        updateClaimBtn();
        updateBtns();
    }

    private void updateBtns(){
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();
        User servicer = selected.getServicer();

        boolean btnClaimEnabled = (servicer == null || (servicer.equals(UserHelpers.getCurrentUser()) &&
                (selected.getStatus() == SanitationRequest.Status.INCOMPLETE)));
        btnClaim.setDisable(!btnClaimEnabled);

        boolean btnMarkDoneEnabled = (servicer != null && servicer.equals(UserHelpers.getCurrentUser()));
        btnMarkDone.setDisable(!btnMarkDoneEnabled);
    }

    public void claimJob(MouseEvent mouseEvent) {
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();

        if (selected.getServicer() != null) {
            selected.setServicer(null);
            selected.setClaimedTime(null);
        } else {
            selected.setServicer(UserHelpers.getCurrentUser());
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            selected.setClaimedTime(timestamp);
        }

        SanitationTable.editSanitationRequest(selected);
        tblData.refresh();
        updateClaimBtn();
        updateBtns();
    }

    private void updateClaimBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getServicer() != null) {
            btnClaim.setText("Un-Claim");
        } else {
            btnClaim.setText("Claim");
        }
    }

    public void markDone(MouseEvent mouseEvent) {
        SanitationRequest selected = tblData.getSelectionModel().getSelectedItem();

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
        updateBtns();
        updateMarkDoneBtn();
    }

    private void updateMarkDoneBtn() {
        if (tblData.getSelectionModel().getSelectedItem().getStatus() == SanitationRequest.Status.COMPLETE) {
            btnMarkDone.setText("Mark Incomplete");
        } else {
            btnMarkDone.setText("Mark Complete");
        }
    }

    public void navigateTo(MouseEvent mouseEvent) {
        //        Location start = map.getLocation(MapController1.getTempStart());
//        Location end = tblData.getSelectionModel().getSelectedItem().getLocation();
//        PathFinder.printPath(panes, TextPane, map, start, end);
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
//        tabMenu.getSelectionModel().select(0);
//        floorMenu.getSelectionModel().select(floorIndex);
    }
}
