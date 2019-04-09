package controllers.requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import controllers.ScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.requests.IT;
import java.net.URL;
import java.util.ResourceBundle;

public class ExternalTransController implements Initializable {
    public JFXComboBox cmbTransType;
    public JFXTimePicker time;
    public JFXTextField name;
    public JFXButton btnSendRequest;
    public JFXButton btnCancel;
    public TableView<ExternalTrans> tbl;
    public TableColumn<IT, String> tblExternalReqID;
    public TableColumn<IT, String> tblName;
    public TableColumn<IT, String> tblType;
    public TableColumn<IT, String> tblTime;

    ObservableList<ExternalTrans> extReqs = FXCollections.observableArrayList();
    private int reqID;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.textProperty().addListener(((observable, oldValue, newValue) -> {
            checkForm();
        }));
        time.valueProperty().addListener(((observable, oldValue, newValue) -> {
            checkForm();
        }));
        cmbTransType.valueProperty().addListener(((observable, oldValue, newValue) -> {
            checkForm();
        }));

        tblExternalReqID.setCellValueFactory(new PropertyValueFactory<>("ExternalReqID"));
        tblName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tblType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        tblTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
        tbl.setItems(extReqs);
    }

    public void sendReq(javafx.scene.input.MouseEvent event){
        event.consume();

        String tblName = name.getText();
        String transType = (String) cmbTransType.getValue();
        ExternalTrans.TransType type;
        switch (transType) {
            case "Cab":
                type = ExternalTrans.TransType.CAB;
                break;
            case "Uber":
                type = ExternalTrans.TransType.UBER;
                break;
            case "Limo":
                type = ExternalTrans.TransType.LIMO;
                break;
            default:
                type = ExternalTrans.TransType.PBUS;
                break;
        }
        ExternalTrans request = new ExternalTrans(reqID++, tblName, type, time.getValue().toString());
        extReqs.add(request);
        tbl.refresh();

        name.setText("");
        cmbTransType.setValue(null);
        time.setValue(null);
    }

    public void cancelScreen(javafx.scene.input.MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

    private void checkForm() {
        boolean bolName = !name.getText().equals("");
        boolean bolTime = time.getValue() != null;
        boolean bolType = cmbTransType.getValue() != null;
        btnSendRequest.setDisable(!(bolName && bolTime && bolType));
    }
}
