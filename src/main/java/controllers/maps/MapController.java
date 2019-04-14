package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import images.ImageFactory;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private final double MAX_ZOOM = 2.0;
    private final double MIN_ZOOM = 0.35;
    private final double ANIMATION_TIME = 1000;

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
    public JFXTabPane tabMenu;

    protected String floor;
    protected List<LineTuple> lstLineTransits;
    private int transitIt;
    protected Point2D center;
    protected static String tempStart;
    private static MapController currMapControl;
    protected Map map;

    public MapController() {
        floor = "3";
        lstLineTransits = new LinkedList<>();
        transitIt = 0;
        currMapControl = this;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gesMap.setVBarEnabled(false);
        gesMap.setHBarEnabled(false);
        gesMap.setMaxScale(MAX_ZOOM);
        gesMap.setMinScale(MIN_ZOOM);

        updateButtons();
        Image img = ImageFactory.getImage("3");
        imgMap.setImage(img);
        gesMap.viewportBoundProperty().addListener(((observable, oldValue, newValue) -> {
            center = new Point2D(img.getWidth() / 2, img.getHeight() / 2);
            gesMap.centreOn(center);
            gesMap.zoomTo(MAX_ZOOM, center);
            gesMap.animate(Duration.millis(ANIMATION_TIME)).zoomTo(MIN_ZOOM, center);
        }));
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

    public static MapController getCurrMapControl() {
        return currMapControl;
    }

    public JFXTabPane getTabMenu() {
        return tabMenu;
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
        updateLines();
        updateButtons();
        displayHint();
    }

    protected void showFloor2() {
        imgMap.setImage(ImageFactory.getImage("2"));
        floor = "2";
        updateLines();
        updateButtons();
        displayHint();
    }

    protected void showFloor1() {
        imgMap.setImage(ImageFactory.getImage("1"));
        floor = "1";
        updateLines();
        updateButtons();
        displayHint();
    }

    protected void showFloorG() {
        imgMap.setImage(ImageFactory.getImage("G"));
        floor = "G";
        updateLines();
        updateButtons();
        displayHint();
    }

    protected void showFloorL1() {
        imgMap.setImage(ImageFactory.getImage("L1"));
        floor = "L1";
        updateLines();
        updateButtons();
        displayHint();
    }

    protected void showFloorL2() {
        imgMap.setImage(ImageFactory.getImage("L2"));
        floor = "L2";
        updateLines();
        updateButtons();
        displayHint();
    }

    public String getFloor() {
        return floor;
    }

    private class LineTuple {
        private Path line;
        private String floor;

        public LineTuple(Path line, String floor) {
            this.line = line;
            this.floor = floor;
        }

        public Path getLine() {
            return line;
        }

        public String getFloor() {
            return floor;
        }

    }

    public GesturePane getGesMap() {
        return gesMap;
    }

    public void addLine(Path line, String floor) {
        lstLineTransits.add(new LineTuple(line, floor));
        updateLines();
    }

    private void updateLines() {
        for (LineTuple lt : lstLineTransits) {
            Path ltLine = lt.getLine();
            String ltFloor = lt.getFloor();
            ltLine.setStroke(PathFinder.colorLine(ltFloor.equals(floor)));
        }
    }

    public void clearPath(Location end) {
        lstLineTransits = new LinkedList<>();
        transitIt = 0;

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
        String startFloor = lstLineTransits.get(transitIt).getFloor();
        Path startLine = lstLineTransits.get(transitIt++).getLine();
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
        if (lstLineTransits.size() > 0) {
            String lstFloor = lstLineTransits.get(transitIt - 1).getFloor();
            if (lstFloor.equals(floor)) {
                clearArrow();
                Path line = lstLineTransits.get(transitIt - 1).getLine();
                panToLine(line);
                if (lstLineTransits.size() > transitIt) {
                    String nxtFloor = lstLineTransits.get(transitIt++).getFloor();
                    displayArrow(nxtFloor);
                }
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

    public Map getMap() {
        return map;
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

    private void panToLine(Path line) {
        Bounds lineBounds = line.getBoundsInParent();
        double startX = lineBounds.getMinX();
        double startY = lineBounds.getMinY();
        double endX = lineBounds.getMaxX();
        double endY = lineBounds.getMaxY();
        Point2D center = new Point2D((startX + endX) / 2, (startY + endY) / 2);

        double width = endX - startX;
        double height = endY - startY;
        double zWidth = widthZoom(width);
        double zHeight = heightZoom(height);
        double zoom = (zWidth > zHeight) ? zHeight : zWidth;

        gesMap.zoomTo(zoom, center);
        gesMap.animate(Duration.millis(ANIMATION_TIME)).centreOn(center);
    }

    private double widthZoom(double width) {
        return (width * -0.001534) + 2.837;
    }

    private double heightZoom(double height) {
        return (height * -0.002259) + 2.546;
    }
}
