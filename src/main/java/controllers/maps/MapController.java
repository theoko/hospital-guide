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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.map.Map;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
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
    public ImageView imgArrow3;
    public ImageView imgArrow2;
    public ImageView imgArrow1;
    public ImageView imgArrowG;
    public ImageView imgArrowL1;
    public ImageView imgArrowL2;

    protected String floor;
    protected List<List<Path>> lstLines;
    protected List<String> lstTransitions;
    protected Point2D center;
    protected static String tempStart;
    protected Map map;

    public MapController() {
        lstLines = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lstLines.add(new ArrayList<>());
        }
        floor = "3";
        lstTransitions = new LinkedList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gesMap.setVBarEnabled(false);
        gesMap.setHBarEnabled(false);

        updateButtons();
        Image img = ImageFactory.getImage("3");
        imgMap.setImage(img);
        center = new Point2D(img.getWidth() / 2, img.getHeight() / 2);

        gesMap.zoomTo(0.001, center);
//        gesMap.animate(Duration.millis(1000)).zoomTo(.001, center);
    }

    protected void initDirections() {
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
    }

    public abstract void btnReturn_Click(MouseEvent mouseEvent) throws Exception;

    public abstract void btnFloor3_Click(MouseEvent mouseEvent);
    public abstract void btnFloor2_Click(MouseEvent mouseEvent);
    public abstract void btnFloor1_Click(MouseEvent mouseEvent);
    public abstract void btnFloorG_Click(MouseEvent mouseEvent);
    public abstract void btnFloorL1_Click(MouseEvent mouseEvent);
    public abstract void btnFloorL2_Click(MouseEvent mouseEvent);

    public boolean isAdmin() {
        return false;
    }

    protected void showFloor3() {
        imgMap.setImage(ImageFactory.getImage("3"));
        floor = "3";
        upDateLines();
        updateButtons();
    }

    protected void showFloor2() {
        imgMap.setImage(ImageFactory.getImage("2"));
        floor = "2";
        upDateLines();
        updateButtons();
    }

    protected void showFloor1() {
        imgMap.setImage(ImageFactory.getImage("1"));
        floor = "1";
        upDateLines();
        updateButtons();
    }

    protected void showFloorG() {
        imgMap.setImage(ImageFactory.getImage("G"));
        floor = "G";
        upDateLines();
        updateButtons();
    }

    protected void showFloorL1() {
        imgMap.setImage(ImageFactory.getImage("L1"));
        floor = "L1";
        upDateLines();
        updateButtons();
    }

    protected void showFloorL2() {
        imgMap.setImage(ImageFactory.getImage("L2"));
        floor = "L2";
        upDateLines();
        updateButtons();
    }

    public String getFloor() {
        return floor;
    }

    public void addLine(Path line, String floor) {
        lstLines.get(PathFinder.floorToInt(floor)).add(line);
        lstTransitions.add(floor);
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

    public void clearPath(Location end) {
        lstLines = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lstLines.add(new ArrayList<>());
        }
        lstTransitions = new LinkedList<>();

        List<Node> lstNodes = new ArrayList<>();
        for (Node n : panMap.getChildren()) {
            if (n instanceof Path) {
                lstNodes.add(n);
            } else if (n instanceof Circle) {
                Circle circle = (Circle) n;
                if (circle.getFill().equals(MapDisplay.nodeEnd)) {
                    circle.setFill(MapDisplay.nodeFill);
                }
                if (end != null && circle.getId().equals(end.getNodeID())) {
                    circle.setFill(MapDisplay.nodeEnd);
                }
            }
        }
        for (Node n : lstNodes) {
            panMap.getChildren().remove(n);
        }
    }

    public void displayPath(Path line) {
        panMap.getChildren().add(0, line);
        String startFloor = lstTransitions.remove(0);
        switch (startFloor) {
            case "3":
                showFloor3();
                break;
            case "2":
                showFloor2();
                break;
            case "1":
                showFloor1();
                break;
            case "G":
                showFloorG();
                break;
            case "L1":
                showFloorL1();
                break;
            default:
                showFloorL2();
        }
    };

    private void displayHint() {
        clearArrow();
        if (lstTransitions.size() > 0) {
            String nxtFloor = lstTransitions.remove(0);
            displayArrow(nxtFloor);
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
        displayHint();
    }

    private void clearArrow() {
        imgArrow3.setImage(null);
        imgArrow2.setImage(null);
        imgArrow1.setImage(null);
        imgArrowG.setImage(null);
        imgArrowL1.setImage(null);
        imgArrowL2.setImage(null);
    }

    private void displayArrow(String nxtFloor) {
        switch (nxtFloor) {
            case "3":
                imgArrow3.setImage(ImageFactory.getImage("arrow"));
                break;
            case "2":
                imgArrow2.setImage(ImageFactory.getImage("arrow"));
                break;
            case "1":
                imgArrow1.setImage(ImageFactory.getImage("arrow"));
                break;
            case "G":
                imgArrowG.setImage(ImageFactory.getImage("arrow"));
                break;
            case "L1":
                imgArrowL1.setImage(ImageFactory.getImage("arrow"));
                break;
            default:
                imgArrowL2.setImage(ImageFactory.getImage("arrow"));
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
