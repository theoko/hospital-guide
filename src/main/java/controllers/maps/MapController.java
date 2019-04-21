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
    private final double ZOOM_PADDING_W = 500.0;
    private final double ZOOM_PADDING_H = 0;
    private final double ANIMATION_TIME = 1000;
    private final double ZOOM_OUT = 50.0;
    private final double ZOOM_SPEED = 0.35;
    private final double PAN_SPEED = 0.25;
    private final double PARTIAL_ZOOM = 0.3;

    public GesturePane gesMap;
    public ImageView imgMap;
    public JFXButton btnFloor4;
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
        floor = "1";
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
        Image img = ImageFactory.getImage(floor);
        imgMap.setImage(img);
        addDoc();

        zoomOut();
    }

    public void zoomOut() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                gesMap.reset();
//                gesMap.setMinScale(gesMap.getCurrentScale());
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
    }

    public static MapController getCurrMapControl() {
        return currMapControl;
    }

    public JFXTabPane getTabMenu() {
        return tabMenu;
    }

    protected abstract void addDoc();

    public abstract void btnReturn_Click(MouseEvent mouseEvent) throws Exception;

    public void btnFloor4_Click(MouseEvent mouseEvent) {
        showFloor("4");
    }

    public void btnFloor3_Click(MouseEvent mouseEvent) {
        showFloor("3");
    }

    public void btnFloor2_Click(MouseEvent mouseEvent) {
        showFloor("2");
    }

    public void btnFloor1_Click(MouseEvent mouseEvent) {
        showFloor("1");
    }

    public void btnFloorG_Click(MouseEvent mouseEvent) {
        showFloor("G");
    }

    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        showFloor("L1");
    }

    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        showFloor("L2");
    }

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
//                clearArrow();
                Path line = lstLineTransits.get(transitIt - 1).getLine();
                panner(line);
                if (lstLineTransits.size() > transitIt) {
                    String nxtFloor = lstLineTransits.get(transitIt++).getFloor();
//                    displayArrow(nxtFloor);
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
        styleButton(btnFloor4, false);
        styleButton(btnFloor3, false);
        styleButton(btnFloor2, false);
        styleButton(btnFloor1, false);
        styleButton(btnFloorG, false);
        styleButton(btnFloorL1, false);
        styleButton(btnFloorL2, false);
        switch (floor) {
            case "4":
                styleButton(btnFloor4, true);
                break;
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

    /*private void clearArrow() {
        imgArrow3.setImage(null);
        imgArrow2.setImage(null);
        imgArrow1.setImage(null);
        imgArrowG.setImage(null);
        imgArrowL1.setImage(null);
        imgArrowL2.setImage(null);
    }*/

    /*private void displayArrow(String nxtFloor) {
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
    }*/

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

    /*private void panToLine(Path line) {
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
            }).zoomBy(2 * MAX_ZOOM, pnt);
        }
    }*/

    private void panner(Path line) {
        Bounds beforeView = gesMap.getTargetViewport();
        Point2D beforeCenter = getCenter(beforeView);
        if (line.getElements().size() > 1) { // Actual path
            // Big -> small
            Bounds lineView = line.getBoundsInLocal();
            Point2D lineCenter = new Point2D((lineView.getMinX() + lineView.getMaxX()) / 2,
                    (lineView.getMinY() + lineView.getMaxY()) / 2);
            double mockZoom = calcZoom(beforeView, lineView, false);
            if (mockZoom > 0) { // Big -> small
                double zoomOut = -(beforeView.getHeight() + ZOOM_OUT) / beforeView.getHeight() + 1.0;
                // Zoom out a bit
                gesMap.animate(Duration.seconds(Math.abs(zoomOut / ZOOM_SPEED))).afterFinished(() -> {
                    // Pan as close to center of line as possible
                    gesMap.animate(Duration.millis(calcDist(beforeCenter, lineCenter) / PAN_SPEED)).afterFinished(() -> {
                        // Zoom in towards line
                        Bounds afterView = gesMap.getTargetViewport();
                        Point2D afterCenter = getCenter(afterView);
                        double zoomIn = calcZoom(afterView, lineView, true);
                        gesMap.animate(Duration.seconds(Math.abs(zoomIn / ZOOM_SPEED))).afterFinished(() -> {
                            // Center the line
                            gesMap.animate(Duration.millis(calcDist(afterCenter, lineCenter) / PAN_SPEED)).centreOn(lineCenter);
                        }).zoomBy(zoomIn, afterCenter);
                    }).centreOn(lineCenter);
                }).zoomBy(zoomOut, beforeCenter);
            } else { // Small -> big
                // Zoom out a partial amount
                double zoomOut = calcZoom(beforeView, lineView, true) * PARTIAL_ZOOM;
                gesMap.animate(Duration.seconds(Math.abs(zoomOut / ZOOM_SPEED))).afterFinished(() -> {
                    // Pan as close to center of line as possible
                    gesMap.animate(Duration.millis(calcDist(beforeCenter, lineCenter))).afterFinished(() -> {
                        // Finish out the zoom
                        Bounds afterView = gesMap.getTargetViewport();
                        double zoomOuter = calcZoom(afterView, lineView, true);
                        gesMap.animate(Duration.seconds(Math.abs(zoomOuter / ZOOM_SPEED))).zoomBy(zoomOuter, lineCenter);
                    }).centreOn(lineCenter);
                }).zoomBy(zoomOut, beforeCenter);

            }
        } else { // Point
            MoveTo mt = ((MoveTo) line.getElements().get(0));
            Point2D pnt = new Point2D(mt.getX(), mt.getY());
            double zoom = 2 * MAX_ZOOM;
            gesMap.animate(Duration.seconds(zoom / ZOOM_SPEED)).afterFinished(() -> {
                gesMap.animate(Duration.millis(calcDist(beforeCenter, pnt))).centreOn(pnt);
            }).zoomBy(zoom, pnt);
        }
    }

    private Point2D getCenter(Bounds bounds) {
        return new Point2D((bounds.getMinX() + bounds.getMaxX()) / 2,
                (bounds.getMinY() + bounds.getMaxY()) / 2);
    }

    private double calcDist(Point2D start, Point2D end) {
        return Math.pow(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2), 0.5);
    }

    private double calcZoom(Bounds start, Bounds end, boolean hasPadding) {
        double endWidth = end.getWidth();
        double endHeight = end.getHeight();
        if (hasPadding) {
            endWidth += ZOOM_PADDING_W;
            endHeight += ZOOM_PADDING_H;
        }
        double zoomWidth = (start.getWidth() - endWidth) / start.getWidth();
        double zoomHeight = (start.getHeight() - endHeight) / start.getHeight();

        return zoomWidth < zoomHeight ? zoomWidth : zoomHeight;
    }
}
