package controllers.employee;

import com.jfoenix.controls.JFXButton;
import controllers.node.InfoController;
import controllers.ScreenController;
import helpers.Constants;
import javafx.scene.input.MouseEvent;

public class EmployeeInfoController extends InfoController {
    public JFXButton btnRequest;

    public void btnReportSpill_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
        try {
            ScreenController.popUp(Constants.Routes.SANITATION_REQUEST , loc);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }
}
