package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTabPane;
import helpers.Constants;
import helpers.UIHelpers;
import images.ImageFactory;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    private final double MIN_ZOOM = 0.1;
    private final double ZOOM_PADDING_W = 500.0;
    private final double ZOOM_PADDING_H = 0;
    private final double ZOOM_OUT = 50.0;
    private final double ZOOM_SPEED = 0.5;
    private final double PAN_SPEED = 0.5;
    private final double PARTIAL_ZOOM = 0.3;
    private final double ZOOM_RESET = 0.5;
    private final double RESET_SPEED = 1000;

    public GesturePane gesMap;
    public ImageView imgMap;
    public VBox vboxDock;
    public Pane panMap;
    public ScrollPane txtPane;
    public JFXTabPane tabMenu;
    public AnchorPane panRoot;
    //public JFXButton btn;
    public VBox vbxButtons;

    protected JFXComboBox start, end;

    protected String floor;
    protected List<LineTuple> lstLineTransits;
    protected static String tempStart;
    private static MapController currMapControl;
    public static Stack<Location> currentRoute;
    public static String currentDirections;
    protected Map map;
    private HashMap<String, Location> longNames;
    private List<Timeline> lstTls;
    private boolean doesPan;
    private List<JFXButton> lstBreadBtns;
    private List<HBox> lstBreadHbxs;
    private HashMap<String, Boolean> lstTransits;

    public MapController() {
        floor = "1";
        lstLineTransits = new LinkedList<>();
        lstTls = new ArrayList<>();
        currMapControl = this;
        currentDirections = null;
        currentRoute = null;
        doesPan = true;
        lstBreadBtns = new ArrayList<>();
        lstBreadHbxs = new ArrayList<>();
        lstTransits = new HashMap<>();
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
        start = new JFXComboBox();
        end = new JFXComboBox();
        addDoc();
        start.valueProperty().addListener(((observable, oldValue, newValue) -> {
            lstLocs();
        }));
        end.valueProperty().addListener(((observable, oldValue, newValue) -> {
            lstLocs();
        }));

        for (Node n1 : vboxDock.getChildren()) {
            JFXNodesList nl1 = (JFXNodesList) n1;
            JFXButton btn = (JFXButton) nl1.getChildren().get(0);
            btn.setOnMouseClicked(event -> {
                for (Node n2 : vboxDock.getChildren()) {
                    if (!n1.equals(n2)) {
                        JFXNodesList nl2 = (JFXNodesList) n2;
                        nl2.animateList(false);
                    }
                }
            });
        }

        zoomOut();
    }

    public void zoomOut() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(1000);
                gesMap.centreOn(gesMap.targetPointAtViewportCentre());
                gesMap.animate(Duration.millis(RESET_SPEED)).zoomTo(ZOOM_RESET, gesMap.targetPointAtViewportCentre());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        t.setDaemon(true);
        t.start();
    }

    private void lstLocs() {
        if (start.getValue() != null && end.getValue() != null) {
            String strStart = (String) start.getValue();
            String strEnd = (String) end.getValue();
            Location locStart = longNames.get(strStart);
            Location locEnd = longNames.get(strEnd);
            PathFinder.printPath(this, locStart, locEnd);
        }
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

    abstract public void associateUserWithDirections(Location start, Location end);

    public abstract void btnReturn_Click(MouseEvent mouseEvent) throws Exception;

    public boolean isAdmin() {
        return false;
    }

    protected void showFloorHelper(String newFloor) {
        floor = newFloor;
        imgMap.setImage(ImageFactory.getImage(floor));
        updateLines();
        updateLocations();
    }

    public abstract void showFloor(String newFloor);

    public String getFloor() {
        return floor;
    }

    private class LineTuple {
        private Path line;
        private String floor;

        LineTuple(Path line, String floor) {
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

            UIHelpers.mouseHover(btn);
           // btn.setTooltip(new Tooltip(""));

            hbx.getChildren().add(btn);
            vbxButtons.getChildren().add(hbx);
        }
    }

    private void addBreadCrumbs() {
        final double HBX_SPACING = 10.0;
        final double VBX_SPACING = 1.0;
        final double VBX_HEIGHT = 56.0;

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
                showBreadCrumb(hbx, btn);
            });


            UIHelpers.mouseHover(btn);

            lstBreadHbxs.add(hbx);
            lstBreadBtns.add(btn);

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
                dot.setFill(Color.NAVY);
                dot.setStroke(Color.GOLD);
                dot.setStrokeWidth(0);
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
            cancelBreadCrumbs();
        });

        UIHelpers.mouseHover(btnCancel);


        hbxCancel.getChildren().add(btnCancel);
        vbxButtons.getChildren().add(hbxCancel);
    }

    private void showBreadCrumb(HBox hbx, JFXButton btn) {
        showFloorHelper(btn.getText());
        displayHint(hbx);
        updateBreadButtons(btn);
    }

    private void cancelBreadCrumbs() {
        addFloorBtns();
        clearMap();
        showFloor(floor);
        lstBreadHbxs = new ArrayList<>();
        lstBreadBtns = new ArrayList<>();
        gesMap.animate(Duration.millis(RESET_SPEED)).zoomTo(ZOOM_RESET, gesMap.targetPointAtViewportCentre());
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
        showBreadCrumb(lstBreadHbxs.get(0), lstBreadBtns.get(0));
    }

    public void displayLocations(Stack<Location> path) {
        Location lstLoc = path.pop();
        String lstFloor = lstLoc.getFloor();
        Circle start = MapDisplay.createCircle(this, lstLoc, MapDisplay.NodeStyle.START, 1, Constants.Routes.USER_INFO, false);
        start.setOnMouseClicked(event -> {
            cancelBreadCrumbs();
        });

        int i = 0;
        panMap.getChildren().add(start);
        while (!path.isEmpty()) {
            Location curLoc = path.pop();
            String curFloor = curLoc.getFloor();
            if (path.size() == 0) {
                Circle end = MapDisplay.createCircle(this, curLoc, MapDisplay.NodeStyle.END, 1, Constants.Routes.USER_INFO, false);
                end.setOnMouseClicked(event -> {
                    cancelBreadCrumbs();
                });
                panMap.getChildren().add(end);
            } else if (!curFloor.equals(lstFloor) ) {
                lstTransits.put(lstLoc.getNodeID(), upOrDown(lstFloor, curFloor));
                Circle circle1 = MapDisplay.createCircle(this, lstLoc, MapDisplay.NodeStyle.REGULAR, 1, Constants.Routes.USER_INFO, false);
                final int index = i;
                circle1.setOnMouseClicked(event -> {
                    showBreadCrumb(lstBreadHbxs.get(index + 1), lstBreadBtns.get(index + 1));
                });
                panMap.getChildren().add(circle1);

                lstTransits.put(curLoc.getNodeID(), upOrDown(curFloor, lstFloor));
                Circle circle2 = MapDisplay.createCircle(this, curLoc, MapDisplay.NodeStyle.REGULAR, 1, Constants.Routes.USER_INFO, false);
                circle2.setOnMouseClicked(event -> {
                    showBreadCrumb(lstBreadHbxs.get(index), lstBreadBtns.get(index));
                });
                panMap.getChildren().add(circle2);

                i++;
            }
            lstFloor = curFloor;
            lstLoc = curLoc;
        }
        updateLocations();
    }

    private Boolean upOrDown(String flrStart, String flrEnd) {
        int intStart = PathFinder.floorToInt(flrStart);
        int intEnd = PathFinder.floorToInt(flrEnd);
        return intStart - intEnd < 0;
    }

    private void updateLocations() {
        clearArrows();
        List<Node> lstArrows = new ArrayList<>();
        for (Node n : panMap.getChildren()) {
            if (n instanceof Circle) {
                Circle circ = (Circle) n;
                if (circ.getId() != null) {
                    Location loc = map.getLocation(circ.getId());
                    if (loc.getFloor().equals(floor)) {
                        circ.setOpacity(1);
                        if (loc.getNodeType().equals(Constants.NodeType.ELEV) || loc.getNodeType().equals(Constants.NodeType.STAI)) {
                            if (lstTransits.containsKey(loc.getNodeID())) {
                                lstArrows.add(addArrow(loc, lstTransits.get(loc.getNodeID())));
                            }
                        }
                    } else {
                        circ.setOpacity(MapDisplay.opac);
                    }
                }
            }
        }
        for (Node arrow : lstArrows) {
            panMap.getChildren().add(arrow);
        }
    }

    private Node addArrow(Location loc, boolean isUp) {
        final double fitWidth = 50.0;
        final double xOffSet = 10.0;
        final double yOffSet = 25.0;

        ImageView imgArrow = new ImageView();
        if (isUp) {
            imgArrow.setImage(ImageFactory.getImage("arrowUp"));
        } else {
            imgArrow.setImage(ImageFactory.getImage("arrowDown"));
        }
        imgArrow.setPreserveRatio(true);
        imgArrow.setFitWidth(fitWidth);
        imgArrow.setX(loc.getxCord() + xOffSet);
        imgArrow.setY(loc.getyCord() - yOffSet);
        return imgArrow;
    }

    private void clearArrows() {
        List<Node> lstNodes = new ArrayList<>();
        for (Node n : panMap.getChildren()) {
            if (n instanceof ImageView) {
                lstNodes.add(n);
            }
        }

        for (Node n : lstNodes) {
            panMap.getChildren().remove(n);
        }
    }

    private void displayHint(HBox btnHbox) {
        if (lstLineTransits.size() > 0) {
            ObservableList<Node> lstNodes = vbxButtons.getChildren();
            int i = 0;
            for (Node n : vbxButtons.getChildren()) {
                if (n instanceof HBox) {
                    if (n.equals(btnHbox)) {
                        if (doesPan) {
                            panner(lstLineTransits.get(i).getLine());
                        }
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
        longNames = new HashMap<>();
        for (Location loc : map.getAllLocations().values()) {
            if (!loc.getNodeType().equals(Constants.NodeType.HALL)) {
                longNames.put(loc.getLongName(), loc);
            }
        }
        popCombo(start);
        popCombo(end);
    }

    private void popCombo(JFXComboBox cmbBox) {
        for (Location loc : longNames.values()) {
            cmbBox.getItems().add(loc.getLongName());
        }
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
            c.setStrokeWidth(0.0);
        }
    }

    private void animateVbox(VBox vbx) {
        resetVbox(vbx);

        final double PULSE_TIME = 1000.0;
        ObservableList<Node> children = vbx.getChildren();
        for (int i = 0; i < children.size(); i++) {
            HBox hbx = (HBox) children.get(i);
            Circle c = (Circle) hbx.getChildren().get(0);
            c.setStrokeWidth(2.0);
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
                    double panSpeed1 = calcDist(beforeCenter, lineCenter) / PAN_SPEED;
                    gesMap.animate(Duration.millis(panSpeed1)).afterFinished(() -> {
                        // Zoom in towards line
                        Bounds afterView = gesMap.getTargetViewport();
                        Point2D afterCenter = getCenter(afterView);
                        double zoomIn = calcZoom(afterView, lineView, true);
                        double zoomInSpeed = Math.abs(zoomIn / ZOOM_SPEED);
                        gesMap.animate(Duration.seconds(zoomInSpeed)).afterFinished(() -> {
                            // Center the line
                            double panSpeed2 = calcDist(afterCenter, lineCenter) / PAN_SPEED;
                            gesMap.animate(Duration.millis(panSpeed2)).centreOn(lineCenter);
                        }).zoomBy(zoomIn, afterCenter);
                    }).centreOn(lineCenter);
                }).zoomBy(zoomOut, beforeCenter);
            } else { // Small -> big
                // Zoom out a partial amount
                double zoomOut = calcZoom(beforeView, lineView, true) * PARTIAL_ZOOM;
                gesMap.animate(Duration.seconds(Math.abs(zoomOut / ZOOM_SPEED))).afterFinished(() -> {
                    // Pan as close to center of line as possible
                    gesMap.animate(Duration.millis(calcDist(beforeCenter, lineCenter) / PAN_SPEED)).afterFinished(() -> {
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
            double zoom = MAX_ZOOM;
            double zoomBy = Math.abs((zoom - gesMap.getCurrentScale()) / zoom);
            gesMap.animate(Duration.seconds(zoomBy / ZOOM_SPEED)).afterFinished(() -> {
                gesMap.animate(Duration.millis(calcDist(beforeCenter, pnt) / PAN_SPEED)).centreOn(pnt);
            }).zoomTo(zoom, pnt);
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
