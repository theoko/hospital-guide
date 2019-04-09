package controllers.requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PrescriptionController implements Initializable {

    /**
     * Class to encapsulate prescription requests
     */
    public class PrescriptionRequest {
        private String patient;
        private String location;
        private String prescription;
        private String amount;
        public PrescriptionRequest(String patient, String location, String prescription, String amount) {
            this.patient = patient;
            this.location = location;
            this.prescription = prescription;
            this.amount = amount;
        }
        public String getPatient() {
            return patient;
        }
        public String getLocation() {
            return location;
        }
        public String getPrescription() {
            return prescription;
        }
        public String getAmount() {
            return amount;
        }
    }

    @FXML
    JFXTextField patientTextField;

    @FXML
    JFXTextField locationTextField;

    @FXML
    JFXTextField prescriptionTextField;

    @FXML
    JFXTextField amountTextField;

    @FXML
    JFXButton submitButton;

    @FXML
    JFXButton backButton;

    @FXML
    TableView<PrescriptionRequest> tableView;

    @FXML
    TableColumn<PrescriptionRequest, String> patientColumn;

    @FXML
    TableColumn<PrescriptionRequest, String> locationColumn;

    @FXML
    TableColumn<PrescriptionRequest, String> prescriptionColumn;

    @FXML
    TableColumn<PrescriptionRequest, String> amountColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        submitButton.setDisable(true);
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("Patient"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        prescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Prescription"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("Amount"));
    }

    // Submit button callback
    public void onSubmitButtonPressed() {
        // Get data from text fields
        String patient = patientTextField.getText();
        String location = locationTextField.getText();
        String prescription = prescriptionTextField.getText();
        String amount = amountTextField.getText();

        // Clear text in text fields
        patientTextField.setText("");
        locationTextField.setText("");
        prescriptionTextField.setText("");
        amountTextField.setText("");

        // Add data to table view
        PrescriptionRequest request = new PrescriptionRequest(patient, location, prescription, amount);
        tableView.getItems().add(request);
        tableView.refresh();

        // Re-disable submit button
        submitButton.setDisable(true);
    }

    // Back button callback
    public void onBackButtonPressed() {
        ScreenController.deactivate();
    }

    // Updates submit button enable when text fields are updated
    public void onTextFieldKeyrelease() {
        boolean submitEnabled =
                patientTextField.getText().length() > 0 &&
                locationTextField.getText().length() > 0 &&
                prescriptionTextField.getText().length() > 0 &&
                amountTextField.getText().length() > 0;
        submitButton.setDisable(!submitEnabled);
    }
}
