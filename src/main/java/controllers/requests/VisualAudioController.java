package controllers.requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import database.Database;
import helpers.DatabaseHelpers;
import helpers.UIHelpers;
import helpers.UserHelpers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.requests.VisualAudio;

import java.net.URL;
import java.util.ResourceBundle;

public class VisualAudioController implements Initializable {
    public JFXRadioButton radVisual;
    public JFXRadioButton radAudio;
    public JFXTextField txtLocation;
    public JFXTextField txtDescription;
    public JFXButton btnSubmit;
    public JFXButton btnCancel;
    public TableView<VisualAudio> tblRequests;
    public TableColumn<VisualAudio, String> colType;
    public TableColumn<VisualAudio, String> colLocation;
    public TableColumn<VisualAudio, String> colDescription;
    public TableColumn<VisualAudio, String> colUser;

    private ToggleGroup grpType;
    private ObservableList<VisualAudio> lstRequests;

    public VisualAudioController() {
        lstRequests = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        grpType = new ToggleGroup();
        radVisual.setToggleGroup(grpType);
        radAudio.setToggleGroup(grpType);

        colType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("Location"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("User"));
        tblRequests.setItems(lstRequests);

        grpType.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            checkForm();
        }));
        txtLocation.textProperty().addListener(((observable, oldValue, newValue) -> {
            checkForm();
        }));
        txtDescription.textProperty().addListener(((observable, oldValue, newValue) -> {
            checkForm();
        }));
    }

    public void btnSubmit_Click(MouseEvent mouseEvent) {
        mouseEvent.consume();
        String type;
        if (radAudio.isSelected()) {
            type = "Audio";
        } else {
            type = "Visual";
        }
        String location = txtLocation.getText();
        String description = txtDescription.getText();
        String user = UserHelpers.getCurrentUser().getUsername();
        VisualAudio va = new VisualAudio(type, location, description, user);
        lstRequests.add(va);
        tblRequests.refresh();

        txtLocation.setText("");
        txtDescription.setText("");
        radAudio.setSelected(false);
        radVisual.setSelected(false);
    }

    public void btnCancel_Click(MouseEvent mouseEvent) {
        mouseEvent.consume();
        ScreenController.deactivate();
    }

    private void checkForm() {
        boolean bolLocation = !txtLocation.getText().equals("");
        boolean bolDescription = !txtDescription.getText().equals("");
        boolean bolType = grpType.getSelectedToggle() != null;
        btnSubmit.setDisable(!(bolLocation && bolDescription && bolType));
    }
}
