package controllers.maps;

import com.jfoenix.controls.JFXButton;
import controllers.ScreenController;
import helpers.Constants;
import images.ImageFactory;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import map.PathFinder;
import models.map.Map;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public abstract class MapController implements Initializable {
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

    protected String floor;
    protected List<List<Path>> lstLines;
    protected Point2D center;
    protected static String tempStart;
    protected Map map;

    public MapController() {
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

        updateButtons();
        Image img = ImageFactory.getImage("3");
        imgMap.setImage(img);
        center = new Point2D(img.getWidth() / 2, img.getHeight() / 2);

//        gesMap.zoomTo(3, center);
//        gesMap.animate(Duration.millis(1000)).zoomTo(.001, center);
    }

    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnReturn);
    }

    public void btnFloor3_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("3"));
        floor = "3";
        upDateLines();
        updateButtons();
    }

    public void btnFloor2_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("2"));
        floor = "2";
        upDateLines();
        updateButtons();
    }

    public void btnFloor1_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("1"));
        floor = "1";
        upDateLines();
        updateButtons();
    }

    public void btnFloorG_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("G"));
        floor = "G";
        upDateLines();
        updateButtons();
    }

    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("L1"));
        floor = "L1";
        upDateLines();
        updateButtons();
    }

    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        imgMap.setImage(ImageFactory.getImage("L2"));
        floor = "L2";
        upDateLines();
        updateButtons();
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

    public static void setTempStart(String tempStart) {
        MapController.tempStart = tempStart;
    }

    public static String getTempStart() {
        return tempStart;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    private void updateButtons() {
        styleButton(btnFloor3, false);
        styleButton(btnFloor2, false);
        styleButton(btnFloor1, false);
        styleButton(btnFloorG, false);
        styleButton(btnFloorL1, false);
        styleButton(btnFloorL2, false);
        switch (floor) {
            case "3":
                styleButton(btnFloor3, true);
                break;
            case "2":
                styleButton(btnFloor2, true);
                break;
            case "1":
                styleButton(btnFloor1, true);
                break;
            case "G":
                styleButton(btnFloorG, true);
                break;
            case "L1":
                styleButton(btnFloorL1, true);
                break;
            default:
                styleButton(btnFloorL2, true);
                break;
        }
    }

    private void styleButton(JFXButton btn, boolean highlight) {
        btn.getStyleClass().clear();
        btn.getStyleClass().add("jfx-button");
        btn.getStyleClass().add("animated-option-button");
        if (highlight) {
            btn.getStyleClass().add("highlight");
        } else {
            btn.getStyleClass().add("unhighlight");
        }
    }
}
