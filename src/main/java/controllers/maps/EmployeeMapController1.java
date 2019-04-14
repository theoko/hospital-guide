package controllers.maps;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMapController1 extends MapController1 {
    public JFXButton btnSettings;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
       // MapDisplay.displayEmployee(panes, TextPane);
    }
}
