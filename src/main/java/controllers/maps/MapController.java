package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import helpers.Constants;
import helpers.UIHelpers;
import google.FirebaseAPI;
import helpers.UserHelpers;
import images.ImageFactory;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.map.Map;
import net.kurobako.gesturefx.GesturePane;

import java.net.URL;
import java.sql.Time;
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
    public Pane panMap;
    public ScrollPane txtPane;
    public JFXTabPane tabMenu;
    public AnchorPane panRoot;
    //public JFXButton btn;
    public VBox vbxButtons;

    protected String floor;
    protected List<LineTuple> lstLineTransits;
    protected static String tempStart;
    private static MapController currMapControl;
    public static Stack<Location> currentRoute;
    public static String currentDirections;
    protected Map map;
    private List<Timeline> lstTls;

    public MapController() {
        floor = "1";
        lstLineTransits = new LinkedList<>();
        lstTls = new ArrayList<>();
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

        addFloorBtns();
        Image img = ImageFactory.getImage(floor);
        imgMap.setImage(img);
        addDoc();
        UIHelpers.addHover(panRoot);
        //UIHelpers.btnRaise(btn);

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

    public boolean isAdmin() {
        return false;
    }

    protected void showFloorHelper(String newFloor) {
        floor = newFloor;
        imgMap.setImage(ImageFactory.getImage(floor));
        updateLines();
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

    private void addFloorBtns() {
        final double HBX_SPACING = 10.0;
        final double VBX_SPACING = 25.0;

        vbxButtons.getChildren().clear();
        vbxButtons.setSpacing(VBX_SPACING);

        String[] strFloors = new String[] {"4", "3", "2", "1", "G", "L1", "L2"};
        for (String strFloor : strFloors) {
            HBox hbx = new HBox();
            hbx.setAlignment(Pos.CENTER_LEFT);
            hbx.setSpacing(HBX_SPACING);

            JFXButton btn = new JFXButton();
            btn.setText(strFloor);
            btn.getStyleClass().add("jfx-button");
            btn.getStyleClass().add("animated-option-button");
            btn.setOnMouseClicked((e) -> {
                showFloor(strFloor);
                updateButtons(btn);
            });
            if (strFloor.equals(floor)) {
                styleButton(btn, "highlight", "unhighlight");
            }

            hbx.getChildren().add(btn);
            vbxButtons.getChildren().add(hbx);
        }
    }

    private void addBreadCrumbs() {
        final double HBX_SPACING = 10.0;
        final double VBX_SPACING = 1.0;
        final double VBX_HEIGHT = 50.0;

        vbxButtons.getChildren().clear();
        vbxButtons.setSpacing(VBX_SPACING);

        for (LineTuple lt : lstLineTransits) {
            String strFloor = lt.floor;

            HBox hbx = new HBox();
            hbx.setAlignment(Pos.CENTER);
            hbx.setSpacing(HBX_SPACING);

            JFXButton btn = new JFXButton();
            btn.setText(strFloor);
            btn.getStyleClass().add("jfx-button");
            btn.getStyleClass().add("animated-option-button");
            btn.setOnMouseClicked((e) -> {
                showFloorHelper(strFloor);
                displayHint(hbx);
                updateBreadButtons(btn);
            });

            hbx.getChildren().add(btn);
            vbxButtons.getChildren().add(hbx);

            VBox vbxLoad = new VBox();
            vbxLoad.setSpacing(VBX_SPACING);
            vbxLoad.setPrefHeight(VBX_HEIGHT);

            final int NUM_DOTS = 3;
            final double DOT_RADIUS = 5.0;
            for (int i = 0; i < NUM_DOTS; i++) {
                HBox hbxDot = new HBox();
                hbxDot.setPrefHeight(15.0);
                hbxDot.setAlignment(Pos.CENTER_LEFT);
                hbxDot.setPadding(new Insets(0, 0, 0, 20));
                Circle dot = new Circle(DOT_RADIUS);
                dot.setFill(Color.BLACK);
                hbxDot.getChildren().add(dot);
                vbxLoad.getChildren().add(hbxDot);
            }
            vbxButtons.getChildren().add(vbxLoad);
        }

        HBox hbxCancel = new HBox();
        hbxCancel.setSpacing(HBX_SPACING);
        hbxCancel.setAlignment(Pos.CENTER);
        JFXButton btnCancel = new JFXButton();

        btnCancel.setText("X");
        btnCancel.getStyleClass().add("jfx-cancel-button");
        btnCancel.getStyleClass().add("animated-option-button");
        btnCancel.setOnMouseClicked((e) -> {
            addFloorBtns();
            clearMap();
            showFloor(floor);
        });

        hbxCancel.getChildren().add(btnCancel);
        vbxButtons.getChildren().add(hbxCancel);
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

    public void clearTransit() {
        lstLineTransits = new LinkedList<>();
    }

    public void clearMap() {
        panMap.getChildren().clear();
    }

    public void displayPath(Path line) {
        panMap.getChildren().add(0, line);
        addBreadCrumbs();
        updateLines();
    }

    public void displayLocations(Stack<Location> path) {
        String lstFloor = "";
        Location lstLoc = null;
        while (!path.isEmpty()) {
            Location curLoc = path.pop();
            String curFloor = curLoc.getFloor();
            if (path.size() == 0) {
                Circle circle = MapDisplay.createCircle(this, curLoc, MapDisplay.NodeStyle.REGULAR, 1, Constants.Routes.USER_INFO, false);
                panMap.getChildren().add(circle);
            } else if (!curFloor.equals(lstFloor) ) {
                if (lstLoc != null) {
                    Circle circle1 = MapDisplay.createCircle(this, lstLoc, MapDisplay.NodeStyle.REGULAR, 1, Constants.Routes.USER_INFO, false);
                    panMap.getChildren().add(circle1);
                }
                Circle circle2 = MapDisplay.createCircle(this, curLoc, MapDisplay.NodeStyle.REGULAR, 1, Constants.Routes.USER_INFO, false);
                panMap.getChildren().add(circle2);
            }
            lstFloor = curFloor;
            lstLoc = curLoc;
        }
    }

    private void displayHint(HBox btnHbox) {
        if (lstLineTransits.size() > 0) {
            ObservableList<Node> lstNodes = vbxButtons.getChildren();
            int i = 0;
            for (Node n : vbxButtons.getChildren()) {
                if (n instanceof HBox) {
                    if (n.equals(btnHbox)) {
                        panner(lstLineTransits.get(i).getLine());
                        break;
                    }
                    i++;
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

    private void updateBreadButtons(JFXButton btn) {
        deAnimate();

        ObservableList<Node> children = vbxButtons.getChildren();
        for (int i = 0; i < children.size() - 1; i += 2) {
            HBox nHbx = (HBox) children.get(i);
            VBox nVbx = (VBox) children.get(i + 1);
            JFXButton nBtn = (JFXButton) nHbx.getChildren().get(0);
            if (nBtn.equals(btn)) {
                styleButton(nBtn, "highlight", "unhighlight");
                animateVbox(nVbx);
            } else {
                styleButton(nBtn, "unhighlight", "highlight");
                resetVbox(nVbx);
            }
        }
    }

    private void updateButtons(JFXButton btn) {
        ObservableList<Node> children = vbxButtons.getChildren();
        for (int i = 0; i < children.size(); i++) {
            HBox nHbx = (HBox) children.get(i);
            JFXButton nBtn = (JFXButton) nHbx.getChildren().get(0);
            if (nBtn.equals(btn)) {
                styleButton(nBtn, "highlight", "unhighlight");
            } else {
                styleButton(nBtn, "unhighlight", "highlight");
            }
        }
    }

    private void styleButton(JFXButton btn, String add, String remove) {
        btn.getStyleClass().remove(remove);
        if (!btn.getStyleClass().contains(add)) {
            btn.getStyleClass().add(add);
        }
    }

    private void deAnimate() {
        for (Timeline tl : lstTls) {
            tl.stop();
        }
        lstTls = new ArrayList<>();
    }

    private void resetVbox(VBox vbx) {
        for (Node n : vbx.getChildren()) {
            HBox hbx = (HBox) n;
            Circle c = (Circle) hbx.getChildren().get(0);
            c.setRadius(5.0);
        }
    }

    private void animateVbox(VBox vbx) {
        resetVbox(vbx);

        final double PULSE_TIME = 1000.0;
        ObservableList<Node> children = vbx.getChildren();
        for (int i = 0; i < children.size(); i++) {
            HBox hbx = (HBox) children.get(i);
            Circle c = (Circle) hbx.getChildren().get(0);
            Timeline tl = new Timeline();
            tl.setCycleCount(Timeline.INDEFINITE);
            tl.setAutoReverse(true);
            tl.setDelay(Duration.millis(PULSE_TIME / children.size() * i));

            KeyValue kv = new KeyValue(c.radiusProperty(), 7.5);
            Duration d = Duration.millis(PULSE_TIME);

            KeyFrame kf = new KeyFrame(d, kv);
            tl.getKeyFrames().add(kf);

            tl.play();
            lstTls.add(tl);
        }
    }

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
