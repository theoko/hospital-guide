package controllers.requests;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.input.MouseEvent;

public class RequestsController {

    public JFXButton btnIT;
    public JFXButton btnInterpreter;
    public JFXButton btnPerscription;
    public JFXButton btnInternalTrans;
    public JFXButton btnGiftStore;
    public JFXButton btnFlourist;
    public JFXButton btnSecurity;
    public JFXButton btnVisualAudio;
    public JFXButton btnExternalTrans;
    public JFXButton btnPatientInfo;

    public void btnIT_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.IT);
    }

    public void btnInterpreter_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.INTERPRETER);
    }

    public void btnPrescription(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.PERSCRIPTION);
    }

    public void btnInternalTrans(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.INTERNAL_TRANS);
    }

    public void btnGiftStore_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.GIFT_STORE);
    }

    public void btnFlourist_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.FLOURIST);
    }

    public void btnSecurity_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.SECURITY);
    }

    public void btnAudioVisual_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.VISUAL_AUDIO);
    }

    public void btnExternalTrans(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.EXTERNAL_TRANS);
    }

    public void btnPatientInfo_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.activate(Constants.Routes.PATIENT_INFO);
    }
}
