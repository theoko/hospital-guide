package controllers.AutoLogout;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.input.MouseEvent;

public class AutoLogoutController {

    public JFXTextField txtTime;
    public JFXButton btnSubmit;
    public double time = 5;

    public AutoLogoutController() {
        this.time = time;
    }

    public double getTime(){
        return Double.parseDouble(txtTime.getText());
    }

    public void btnSubmit_Clicked(MouseEvent mouseEvent) {
        mouseEvent.consume();
        time = getTime();
        try {
            ScreenController.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
