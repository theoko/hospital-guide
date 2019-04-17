package controllers.search;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import controllers.maps.MapController;
import database.LocationTable;
import helpers.UIHelpers;
import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

    static final double ZOOM_SCALE = 2.5;

    SearchEngine searchEngine;

    public SearchEngineController() {

    }

    public void initialize() {

        searchBox = new JFXTextField();
        searchBox.setMaxWidth(UIHelpers.getScreenWidth());
        searchBox.setPadding(new Insets(5));

        searchEngine = new SearchEngine();

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
                searchEngine.search(searchBox.getText());

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

        MapController mapController = (MapController) SearchEngineController.parentController;

        mapController.showFloor(location.getFloor());

        Circle nodeCircle = mapController.getMap().getLocation(location.getNodeID()).getNodeCircle();

        if(nodeCircle == null) {

            Point2D zoomPoint = new Point2D(location.getxCord(), location.getyCord());

            mapController.getGesMap()
                    .animate(Duration.millis(1000)).afterFinished(() -> {
                mapController.gesMap.animate(Duration.millis(500)).centreOn(zoomPoint);
            }).zoomTo(ZOOM_SCALE, zoomPoint);

        } else {

            Point2D zoomPoint = new Point2D(nodeCircle.getCenterX(), nodeCircle.getCenterY());

            mapController.getGesMap()
                    .animate(Duration.millis(1000)).afterFinished(() -> {
                mapController.gesMap.animate(Duration.millis(500)).centreOn(zoomPoint);
            }).zoomTo(ZOOM_SCALE, zoomPoint);
            // Fill
            nodeCircle.setFill(Color.ORANGE);


            KeyFrame[] keyFrames = new KeyFrame[] {
                    new KeyFrame(Duration.seconds(2),
                            new KeyValue(
                                    nodeCircle.radiusProperty(),
                                    nodeCircle.getRadius() + 10
                            )
                    ),
                    new KeyFrame(Duration.seconds(2),
                            new KeyValue(
                                    nodeCircle.radiusProperty(),
                                    nodeCircle.getRadius()
                            )
                    ),
                    new KeyFrame(Duration.seconds(2),
                            new KeyValue(
                                    nodeCircle.radiusProperty(),
                                    nodeCircle.getRadius() + 10
                            )
                    ),
                    new KeyFrame(Duration.seconds(2),
                            new KeyValue(
                                    nodeCircle.radiusProperty(),
                                    nodeCircle.getRadius()
                            )
                    )
            };

            Timeline[] timelines = new Timeline[keyFrames.length];

            SequentialTransition sequentialTransition = new SequentialTransition();
            for(int i=0; i<timelines.length; i++) {

                timelines[i] = new Timeline();
                timelines[i].getKeyFrames().add(keyFrames[i]);

                sequentialTransition.getChildren().add(timelines[i]);
            }

            sequentialTransition.play();


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
