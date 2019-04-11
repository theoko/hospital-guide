package controllers;

import com.jfoenix.controls.JFXButton;
import images.ImageFactory;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import map.MapDisplay;
import map.PathFinder;
import net.kurobako.gesturefx.GesturePane;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MapAllController implements Initializable {
    public GesturePane gesMap;
    public ImageView imgMap;
    public JFXButton btnFloor3;
    public JFXButton btnFloor2;
    public JFXButton btnFloor1;
    public JFXButton btnFloorG;
    public JFXButton btnFloorL1;
    public JFXButton btnFloorL2;
    public JFXButton btnReturn;
    public Pane panMap;
    public ScrollPane txtPane;
    public TitledPane tilDirections;

    private Point2D center;
    private String floor;
    private List<List<Path>> lstLines;

    public MapAllController() {
        lstLines = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lstLines.add(new ArrayList<>());
        }
        floor = "3";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gesMap.setVBarEnabled(false);
        gesMap.setHBarEnabled(false);
        txtPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        txtPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tilDirections.expandedProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue) {
                tilDirections.setPrefHeight(500.0);
                txtPane.setPrefHeight(500.0);
            } else {
                tilDirections.setPrefHeight(0.0);
                txtPane.setPrefHeight(0.0);
            }
            gesMap.requestFocus();
        }));

        Image img = ImageFactory.getImage("3");
        imgMap.setImage(img);
        center = new Point2D(img.getWidth() / 2, img.getHeight() / 2);
        MapDisplay.displayUser(this);
        gesMap.zoomTo(3, center);
        gesMap.animate(Duration.millis(1000)).zoomTo(.001, center);
    }

    public void btnReturn_Click(MouseEvent mouseEvent) {

    }

    public void btnFloor3_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("3"));
        floor = "3";
        MapDisplay.displayUser(this);
        upDateLines();
    }

    public void btnFloor2_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("2"));
        floor = "2";
        MapDisplay.displayUser(this);
        upDateLines();
    }

    public void btnFloor1_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("1"));
        floor = "1";
        MapDisplay.displayUser(this);
        upDateLines();
    }

    public void btnFloorG_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("G"));
        floor = "G";
        MapDisplay.displayUser(this);
        upDateLines();
    }

    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("L1"));
        floor = "L1";
        MapDisplay.displayUser(this);
        upDateLines();
    }

    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("L2"));
        floor = "L2";
        MapDisplay.displayUser(this);
        upDateLines();
    }

    public String getFloor() {
        return floor;
    }

    public void addLine(Path line, int index) {
        lstLines.get(index).add(line);
        upDateLines();
    }

    private void upDateLines() {
        for (int i = 0; i < lstLines.size(); i++) {
            boolean isCurr = i == PathFinder.floorToInt(floor);
            for (Path line : lstLines.get(i)) {
                line.setStroke(PathFinder.colorLine(isCurr));
            }
        }
    }
}
