package controllers;

import helpers.Constants;
import javafx.scene.input.MouseEvent;
import map.MapDisplay;
import map.PathFinder;
import java.net.URL;
import java.util.ResourceBundle;

public class UserMapController extends MapController {

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        MapDisplay.displayUser(panes);
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
