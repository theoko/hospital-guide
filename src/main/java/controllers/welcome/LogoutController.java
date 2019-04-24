package controllers.welcome;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import helpers.Constants;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;

public abstract class LogoutController implements Initializable {

    public JFXTextField txtTime;
    public JFXButton btnSubmit;
    public double time = 1;

    public LogoutController() {
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