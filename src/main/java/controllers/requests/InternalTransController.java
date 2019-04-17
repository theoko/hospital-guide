package controllers.requests;

import com.jfoenix.controls.JFXComboBox;
import database.LocationTable;
import helpers.UIHelpers;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import models.map.Location;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

public class InternalTransController {

    public AnchorPane internalTransPane;

    String prevSelectedStartLocation;
    String prevSelectedEndLocation;

    String selectedStartLocation;
    String selectedEndLocation;

    public void initialize() {

        internalTransPane.setPrefHeight(655);
        internalTransPane.setPrefWidth(1200);

        VBox vBox = new VBox(15);
        vBox.setAlignment(Pos.CENTER);

        AnchorPane.setTopAnchor(vBox,0.0);
        AnchorPane.setBottomAnchor(vBox,0.0);
        AnchorPane.setLeftAnchor(vBox,0.0);
        AnchorPane.setRightAnchor(vBox,0.0);


        Label startLocation = new Label("Start location");
        Label endLocation = new Label("End location");

        setLabelProperties(startLocation);
        setLabelProperties(endLocation);

        JFXComboBox startComboBox = new JFXComboBox();
        startComboBox.setPrefWidth(600);

        JFXComboBox endComboBox = new JFXComboBox();
        endComboBox.setPrefWidth(600);

        HashMap<String, Location> locations = LocationTable.getLocations();

        startComboBox.setPromptText("Select starting location");
        endComboBox.setPromptText("Select ending location");

        LinkedList<String> locationNames = new LinkedList<>();
        if(locations != null) {
            Iterator it = locations.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();

                Location location = (Location) pair.getValue();

                locationNames.add(location.getLongName());

                it.remove();
            }
        }

        for(String name : locationNames) {
            startComboBox.getItems().add(name);
            endComboBox.getItems().add(name);
        }

        vBox.getChildren().add(startLocation);
        vBox.getChildren().add(startComboBox);
        vBox.getChildren().add(endLocation);
        vBox.getChildren().add(endComboBox);

        internalTransPane.getChildren().add(vBox);

        startComboBox.valueProperty().addListener((observable, oldValue, newValue) ->  {
//            Label newValueLabel = (Label) newValue;
            prevSelectedStartLocation = oldValue.toString();
            selectedStartLocation = newValue.toString();

            endComboBox.getItems().add(prevSelectedStartLocation);
            endComboBox.getItems().remove(selectedStartLocation);

            System.out.println(selectedStartLocation);
        });

        endComboBox.valueProperty().addListener((observable, oldValue, newValue) ->  {
//            Label newValueLabel = (Label) newValue;
            prevSelectedEndLocation = oldValue.toString();
            selectedEndLocation = newValue.toString();

            startComboBox.getItems().add(prevSelectedEndLocation);
            startComboBox.getItems().remove(selectedEndLocation);

            System.out.println(selectedEndLocation);
        });
    }

    private void setLabelProperties(Label label) {
        label.setPrefHeight(40.0);
        label.setPrefWidth(600);

        label.setAlignment(Pos.CENTER);
        label.setContentDisplay(ContentDisplay.CENTER);
        label.setStyle("-fx-background-color: #022D5A;");
        label.setTextFill(Color.WHITE);
    }

}
