package controllers.search;

import com.jfoenix.controls.JFXAutoCompletePopup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import helpers.UIHelpers;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import models.search.SearchEngine;

import java.util.Set;

public class SearchEngineController {

    public StackPane searchStackPane;

    public JFXButton btnDisplaySearch;
    public JFXTextField searchBox;
    public JFXButton btnDisplayBack;

    public void initialize() {
        searchBox = new JFXTextField();
        searchBox.setMaxWidth(UIHelpers.getScreenWidth());
        searchBox.setPadding(new Insets(5));

        JFXAutoCompletePopup<String> autoCompletePopup = new JFXAutoCompletePopup<>();

        autoCompletePopup.setSelectionHandler(event -> {
            searchBox.setText(event.getObject());

            // Focus on node

            // Display options if we match multiple nodes with same name

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
