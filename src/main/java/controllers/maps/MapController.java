package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import images.ImageFactory;
import javafx.animation.Interpolator;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.map.Map;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.util.*;

public abstract class MapController implements Initializable {
    private final double MAX_ZOOM = 2.0;
    private final double MIN_ZOOM = 0.01;
    private final double ZOOM_BUFFER = 0.5;
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
    public AnchorPane tilDirections;
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
    protected static String tempStart;
    private static MapController currMapControl;
    public static Stack<Location> currentRoute;
    public static String currentDirections;
    protected Map map;

    public MapController() {
        floor = "3";
        lstLineTransits = new LinkedList<>();
        transitIt = 0;
        currMapControl = this;
        currentDirections = null;
        currentRoute = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gesMap.setVBarEnabled(false);
        gesMap.setHBarEnabled(false);
        gesMap.setMaxScale(MAX_ZOOM);
        gesMap.setMinScale(MIN_ZOOM);
        gesMap.setFitMode(GesturePane.FitMode.COVER);

        updateButtons();
        Image img = ImageFactory.getImage("3");
        imgMap.setImage(img);
        zoomOut();
    }

    public void zoomOut() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                gesMap.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        t.setDaemon(true);
        t.start();
    }

    protected void initDirections() {
        txtPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        txtPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        tilDirections.expandedProperty().addListener(((observable, oldValue, newValue) -> {
//            if (newValue) {
//                tilDirections.setPrefHeight(500.0);
//                txtPane.setPrefHeight(500.0);
//            } else {
//                tilDirections.setPrefHeight(0.0);
//                txtPane.setPrefHeight(0.0);
//            }
//            gesMap.requestFocus();
//        }));
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

    protected void showFloorHelper(String newFloor) {
        floor = newFloor;
        imgMap.setImage(ImageFactory.getImage(floor));
        updateLines();
        updateButtons();
        displayHint();
    }

    public abstract void showFloor(String newFloor);

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
            if (ltFloor.equals(floor)) {
                ltLine.setOpacity(1);
            } else {
                ltLine.setOpacity(MapDisplay.opac);
            }
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
        String startFloor = lstLineTransits.get(transitIt++).getFloor();
        showFloor(startFloor);
    };

    private void displayHint() {
        if (lstLineTransits.size() > 0 && lstLineTransits.size() >= transitIt) {
            String lstFloor = lstLineTransits.get(transitIt - 1).getFloor();
            if (lstFloor.equals(floor)) {
                clearArrow();
                Path line = lstLineTransits.get(transitIt - 1).getLine();
                panToLine(line);
                if (lstLineTransits.size() > transitIt) {
                    String nxtFloor = lstLineTransits.get(transitIt++).getFloor();
                    displayArrow(nxtFloor);
                } else {
                    transitIt++;
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
        gesMap.reset();
        Bounds lineBounds = line.getBoundsInLocal();
        double startX = lineBounds.getMinX();
        double startY = lineBounds.getMinY();
        double endX = lineBounds.getMaxX();
        double endY = lineBounds.getMaxY();
        if (endX >= 0.0) {
            Point2D middle = new Point2D((startX + endX) / 2, (startY + endY) / 2);

            double lineWidth = lineBounds.getWidth();
            double lineHeight = lineBounds.getHeight();
            Bounds gesView = gesMap.getTargetViewport();
            double gesWidth = gesView.getWidth();
            double gesHeight = gesView.getHeight();
            double zoomWidth = (gesWidth - lineWidth) / gesWidth;
            double zoomHeight = (gesHeight - lineHeight) / gesHeight;
            double zoom = zoomWidth < zoomHeight ? zoomWidth : zoomHeight;
            if (zoom > 0) {
                zoom *= 1 - ZOOM_BUFFER;
            } else {
                zoom *= 1 + ZOOM_BUFFER;
            }
            gesMap.animate(Duration.millis(ANIMATION_TIME)).afterFinished(() -> {
                gesMap.animate(Duration.millis(ANIMATION_TIME)).centreOn(middle);
            }).zoomBy(zoom, middle);
        } else {
            MoveTo mt = ((MoveTo) line.getElements().get(0));
            Point2D pnt = new Point2D(mt.getX(), mt.getY());
            gesMap.animate(Duration.millis(ANIMATION_TIME)).afterFinished(() -> {
                gesMap.animate(Duration.millis(ANIMATION_TIME)).centreOn(pnt);
            }).zoomBy(2, pnt);
        }
    }
}
