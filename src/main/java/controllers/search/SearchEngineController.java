package controllers.search;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.maps.*;
import database.LocationTable;
import helpers.UIHelpers;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import models.map.Location;
import models.search.SearchEngine;

import java.util.Iterator;
import java.util.Set;

public class SearchEngineController {

    public StackPane searchStackPane;

    public JFXButton btnDisplaySearch;
    public JFXTextField searchBox;
    public JFXButton btnDisplayBack;
    private static Object parentController;

    static final double ZOOM_SCALE = 2;

    public void initialize() {

        searchBox = new JFXTextField();
        searchBox.setMaxWidth(UIHelpers.getScreenWidth());
        searchBox.setPadding(new Insets(5));

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();

        autoCompletePopup.setSelectionHandler(event -> {
            searchBox.setText(event.getObject());

            // Check database for nodes
            Set<Location> locations = LocationTable.getLocationByLongName(event.getObject());

            if (locations.isEmpty()) {

                // Handle with search engine API

            } else {

                if (locations.size() == 1) {
                    // Focus on node
                    Iterator iterator = locations.iterator();

                    Location location = (Location) iterator.next();

                    focusOnNode(location);

                    goBack(null);

                } else {
                    // Display options if we match multiple nodes with same name
                    for(Location location : locations) {
                        System.out.println(location.getNodeID());
                    }
                }

            }

        });

        // Add event listener for search box
        searchBox.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                // Get results
                SearchEngine searchEngine = new SearchEngine(searchBox.getText());

                Set<String> results = searchEngine.getResults();

                if(results.size() > 0) {

                    autoCompletePopup.getSuggestions().clear();
                    for(String word : results) {
                        autoCompletePopup.getSuggestions().add(word);
                    }

                    autoCompletePopup.show(searchBox);

                } else {

                    autoCompletePopup.getSuggestions().clear();
                    autoCompletePopup.hide();

                }

            }
        });

        // Set visibility
        searchBox.setVisible(false);

        // Add search box to layout
        searchStackPane.getChildren().add(searchBox);

    }

    public static void setParentController(Object parentController) {
        SearchEngineController.parentController = parentController;
    }

    public static void focusOnNode(Location location) {

        switch (location.getFloor()) {
            case "3":
                switch (SearchEngineController.parentController.getClass().getName()) {

                    case "controllers.maps.UserMapController":
                        UserMapController userMapController = (UserMapController) SearchEngineController.parentController;
                        userMapController.btnFloor3_Click(null);

                        userMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.EmployeeMapController":
                        EmployeeMapController employeeMapController = (EmployeeMapController) SearchEngineController.parentController;
                        employeeMapController.btnFloor3_Click(null);

                        employeeMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.CustodianMapController":
                        CustodianMapController custodianMapController = (CustodianMapController) SearchEngineController.parentController;
                        custodianMapController.btnFloor3_Click(null);

                        custodianMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.AdminMapController":
                        AdminMapController adminMapController = (AdminMapController) SearchEngineController.parentController;
                        adminMapController.btnFloor3_Click(null);

                        adminMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                }
                break;

            case "2":
                switch (SearchEngineController.parentController.getClass().getName()) {

                    case "controllers.maps.UserMapController":
                        UserMapController userMapController = (UserMapController) SearchEngineController.parentController;
                        userMapController.btnFloor2_Click(null);

                        userMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.EmployeeMapController":
                        EmployeeMapController employeeMapController = (EmployeeMapController) SearchEngineController.parentController;
                        employeeMapController.btnFloor2_Click(null);

                        employeeMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.CustodianMapController":
                        CustodianMapController custodianMapController = (CustodianMapController) SearchEngineController.parentController;
                        custodianMapController.btnFloor2_Click(null);

                        custodianMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.AdminMapController":
                        AdminMapController adminMapController = (AdminMapController) SearchEngineController.parentController;
                        adminMapController.btnFloor2_Click(null);

                        adminMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                }
                break;

            case "1":
                switch (SearchEngineController.parentController.getClass().getName()) {

                    case "controllers.maps.UserMapController":
                        UserMapController userMapController = (UserMapController) SearchEngineController.parentController;
                        userMapController.btnFloor1_Click(null);

                        userMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.EmployeeMapController":
                        EmployeeMapController employeeMapController = (EmployeeMapController) SearchEngineController.parentController;
                        employeeMapController.btnFloor1_Click(null);

                        employeeMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.CustodianMapController":
                        CustodianMapController custodianMapController = (CustodianMapController) SearchEngineController.parentController;
                        custodianMapController.btnFloor1_Click(null);

                        custodianMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.AdminMapController":
                        AdminMapController adminMapController = (AdminMapController) SearchEngineController.parentController;
                        adminMapController.btnFloor1_Click(null);

                        adminMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                }
                break;

            case "G":
                switch (SearchEngineController.parentController.getClass().getName()) {

                    case "controllers.maps.UserMapController":
                        UserMapController userMapController = (UserMapController) SearchEngineController.parentController;
                        userMapController.btnFloorG_Click(null);

                        userMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.EmployeeMapController":
                        EmployeeMapController employeeMapController = (EmployeeMapController) SearchEngineController.parentController;
                        employeeMapController.btnFloorG_Click(null);

                        employeeMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.CustodianMapController":
                        CustodianMapController custodianMapController = (CustodianMapController) SearchEngineController.parentController;
                        custodianMapController.btnFloorG_Click(null);

                        custodianMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.AdminMapController":
                        AdminMapController adminMapController = (AdminMapController) SearchEngineController.parentController;
                        adminMapController.btnFloorG_Click(null);

                        adminMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                }
                break;

            case "L1":
                switch (SearchEngineController.parentController.getClass().getName()) {

                    case "controllers.maps.UserMapController":
                        UserMapController userMapController = (UserMapController) SearchEngineController.parentController;
                        userMapController.btnFloorL1_Click(null);

                        userMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.EmployeeMapController":
                        EmployeeMapController employeeMapController = (EmployeeMapController) SearchEngineController.parentController;
                        employeeMapController.btnFloorL1_Click(null);

                        employeeMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.CustodianMapController":
                        CustodianMapController custodianMapController = (CustodianMapController) SearchEngineController.parentController;
                        custodianMapController.btnFloorL1_Click(null);

                        custodianMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.AdminMapController":
                        AdminMapController adminMapController = (AdminMapController) SearchEngineController.parentController;
                        adminMapController.btnFloorL1_Click(null);

                        adminMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                }
                break;

            case "L2":
                switch (SearchEngineController.parentController.getClass().getName()) {

                    case "controllers.maps.UserMapController":
                        UserMapController userMapController = (UserMapController) SearchEngineController.parentController;
                        userMapController.btnFloorL2_Click(null);

                        userMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.EmployeeMapController":
                        EmployeeMapController employeeMapController = (EmployeeMapController) SearchEngineController.parentController;
                        employeeMapController.btnFloorL2_Click(null);

                        employeeMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.CustodianMapController":
                        CustodianMapController custodianMapController = (CustodianMapController) SearchEngineController.parentController;
                        custodianMapController.btnFloorL2_Click(null);

                        custodianMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                    case "controllers.maps.AdminMapController":
                        AdminMapController adminMapController = (AdminMapController) SearchEngineController.parentController;
                        adminMapController.btnFloorL2_Click(null);

                        adminMapController.getGesMap()
                                .animate(Duration.millis(1000))
                                .zoomTo(ZOOM_SCALE, new Point2D(location.getxCord(), location.getyCord()));
                        break;

                }
                break;

            default:
        }

    }

    double translation = -(UIHelpers.getScreenWidth() / 2.0) + 50.0;
    public void displaySearch(MouseEvent event) {

        // Display back button
        btnDisplaySearch.setVisible(false);
        searchBox.setVisible(true);
        btnDisplayBack.setVisible(true);

        btnDisplayBack.setPrefWidth(searchBox.getWidth());

        // Do animation
        TranslateTransition addWidth = new TranslateTransition(
                new Duration(350),
                searchStackPane
        );

        addWidth.setToX(translation);

        addWidth.play();

        TranslateTransition addWidthBtn = new TranslateTransition(
                new Duration(450),
                btnDisplayBack
        );

        addWidthBtn.setToY(50.0);

        addWidthBtn.play();

        searchBox.requestFocus();

    }

    public void goBack(MouseEvent event) {

        btnDisplaySearch.setVisible(true);

        // Do animation
        TranslateTransition addWidth = new TranslateTransition(
                new Duration(450),
                searchStackPane
        );

        addWidth.setByX(-translation);

        addWidth.play();

        // Hide search box
        btnDisplayBack.setVisible(false);
        searchBox.setVisible(false);

    }

}
