package controllers.requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import models.requests.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientInfoController {

    public JFXComboBox cmbPriority;
    public JFXTextField txtName;
    public JFXTextField txtDOB;
    public JFXTextField txtDesc;
    public JFXButton btnSendRequest;
    public JFXButton btnCancel;

    public String HIGH;
    public String LOW;
    public String MEDIUM;

    public TableView<Patient> tblPatient;

    public TableColumn<Patient, String> tblName;
    public TableColumn<Patient, String> tblDOB;
    public TableColumn<Patient, String> tblDesc;
    public TableColumn<Patient, String> tblPriority;

    ObservableList<Patient> patientReqs = FXCollections.observableArrayList();

    public void initialize() {
        initPatient();
    }


    List<Patient> PatientDetails = new ArrayList<>();


    private void initPatient() {
            tblName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            tblDOB.setCellValueFactory(new PropertyValueFactory<>("Dob"));
            tblDesc.setCellValueFactory(new PropertyValueFactory<>("Desc"));
            tblPriority.setCellValueFactory(new PropertyValueFactory<>("Priority"));

            tblPatient.setItems(patientReqs);
            if(PatientDetails != null) {
                patientReqs.clear();
                patientReqs.addAll(PatientDetails);
                tblPatient.refresh();
            }
        }



        public void sendRequest(MouseEvent event) {
            event.consume();
            String name = txtName.getText();
            String desc = txtDesc.getText();
            String dob = txtDOB.getText();
            String priority = (String) cmbPriority.getValue();



            Patient req = new Patient(name, dob, desc, Patient.Priority.valueOf(priority));
            PatientDetails.add(req);
            patientReqs.clear();
            patientReqs.addAll(PatientDetails);

            txtName.setText("");
            txtDesc.setText("");
            txtDOB.setText("");
            //cmbPriority.setValue();
            tblPatient.refresh();

        }



        public void completeReq(MouseEvent event) {
            event.consume();
            event.consume();
            Patient selected = tblPatient.getSelectionModel().getSelectedItem();
            System.out.println(selected.toString());
            PatientDetails.remove(selected);
            patientReqs.clear();
            patientReqs.addAll(PatientDetails);
            tblPatient.refresh();
        }

        public void cancelSrc(MouseEvent event) {
            event.consume();
            ScreenController.deactivate();

        }

    public JFXTextField getTxtDOB() {
        return txtDOB;
    }

    public JFXTextField getTxtName() {
        return txtName;
    }

    public JFXTextField getTxtDesc() {
        return txtDesc;
    }
}
