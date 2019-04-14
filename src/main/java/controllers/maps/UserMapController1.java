package controllers.maps;

import controllers.ScreenController;
import controllers.maps.MapController1;
import helpers.Constants;
import javafx.scene.input.MouseEvent;
import map.PathFinder;
import java.net.URL;
import java.util.ResourceBundle;

public class UserMapController1 extends MapController1 {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        //MapDisplay.displayUser(panes, TextPane);
    }

    @Override
    public void logOut(MouseEvent event) {
        event.consume();
        tempStart = PathFinder.getDefLocation();
        ScreenController.logOut(btnReturn);
        try {
            ScreenController.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
