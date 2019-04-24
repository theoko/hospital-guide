package map;

import com.jfoenix.controls.JFXButton;
import controllers.maps.MapController;
import controllers.settings.SettingsController;
import google.FirebaseAPI;
import helpers.Constants;
import helpers.MapHelpers;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import models.map.DirectionStep;
import models.map.Location;
import models.map.Map;
import models.map.SubPath;

import java.util.*;

public abstract class PathFinder {

    protected static final double FLOOR_HEURISTIC = 100000;
    protected static final double STRAIGHT_ANGLE = 90.0;
    protected static final double TURN_SENSITIVITY = 45.0;
    protected static final double PIXEL_TO_METERS = 0.08;
    private static double LINE_WIDTH = 3.5;
    private static double LINE_LENGTH = 5.0;
    private static double LINE_GAP = 10.0;
    private static double SPEED = 1.4;
    private static double FLOOR_TIME = 30;

    public static String defLocation;

    public PathFinder() {
    }

    /**
     * Finds a path from the start map to the end map using a*
     *
     * @return A stack of locations that contains the path
     */
    public Stack<Location> findPath(Location start, Location end) {
        Stack<Location> path = new Stack<>();
        setUp(start);
        HashMap<String, SubPath> used = new HashMap<>();

        while (!isEmpty()) {
            SubPath sNext = getNext();
            Location lNext = sNext.getLocation();
            System.out.println("sNext: " + sNext);
            System.out.println("lNext: " + lNext.getNodeID());
            if (used.containsKey(lNext.getNodeID())) {
                continue;
            }
            if (lNext.getNodeID().equals(end.getNodeID())) {
                path = genPath(sNext);
                break;
            }
            used.put(lNext.getNodeID(), sNext);

            List<SubPath> lstSubPaths = lNext.getSubPaths();
            for (SubPath nCurr : lstSubPaths) {
                Location lCurr = nCurr.getLocation();
                if (!used.containsKey(lCurr.getNodeID())) {
                    SubPath newNeigh = new SubPath(
                            nCurr.getEdgeID(),
                            lCurr,
                            getDist(sNext, nCurr),
                            getHeuristic(lCurr, end));
                    addNext(newNeigh);
                    newNeigh.setParent(sNext);
                }
            }
        }
        return path;
    }

    public static void printByType(MapController mc, Map map, Constants.NodeType nodeType) {
        printByType(mc, map, nodeType, nodeType);
    }

    public static void printByType(MapController mc, Map map, Constants.NodeType nodeType1, Constants.NodeType nodeType2) {
        Location kiosk = map.getLocation(MapController.getTempStart());
        Location closest = findByType(kiosk, nodeType1, nodeType2);
        printPath(mc, kiosk, closest);
    }

    private static Location findByType(Location start, Constants.NodeType nodeType1, Constants.NodeType nodeType2) {
        PriorityQueue<SubPath> queue = new PriorityQueue<>();
        SubPath sStart = new SubPath("", start, 0.0);
        queue.add(sStart);
        HashMap<String, SubPath> used = new HashMap<>();

        while (!queue.isEmpty()) {
            SubPath currSub = queue.poll();
            Location currLoc = currSub.getLocation();
            if (used.containsKey(currLoc.getNodeID())) {
                continue;
            }
            if (currLoc.getNodeType().equals(nodeType1) || currLoc.getNodeType().equals(nodeType2)) {
                return currLoc;
            }
            used.put(currLoc.getNodeID(), currSub);

            List<SubPath> lstSubs = currLoc.getSubPaths();
            for (SubPath nxtSub : lstSubs) {
                Location nxtLoc = nxtSub.getLocation();
                if (!used.containsKey(nxtLoc.getNodeID())) {
                    SubPath nxtCpy = new SubPath(
                            nxtSub.getEdgeID(),
                            nxtLoc,
                            Math.abs(floorToInt(start.getFloor()) - floorToInt(nxtLoc.getFloor())),
                            0.0
                    );
                    nxtCpy.setParent(currSub);
                    queue.add(nxtCpy);
                }
            }
        }
        return null;
    }

    public static List<Location> findNearestLocations(Location start) {
        PriorityQueue<SubPath> queue = new PriorityQueue<>();
        PriorityQueue<Location> qPopper = new PriorityQueue<>();
        HashMap<String, SubPath> lstUsed = new HashMap<>();

        SubPath sStart = new SubPath("", start, 0.0);
        queue.add(sStart);

        while (!queue.isEmpty()) {
            SubPath currSub = queue.poll();
            Location currLoc = currSub.getLocation();
            if (lstUsed.containsKey(currLoc.getNodeID())) {
                continue;
            }
            lstUsed.put(currLoc.getNodeID(), currSub);

            double currDist = currSub.getDist();
            List<SubPath> lstSubs = currLoc.getSubPaths();
            for (SubPath nxtSub : lstSubs) {
                Location nxtLoc = nxtSub.getLocation();
                if (!lstUsed.containsKey(nxtLoc.getNodeID())) {
                    SubPath nxtCpy = new SubPath(
                            nxtSub.getEdgeID(),
                            nxtLoc,
                            currDist + nxtSub.getDist(),
                            0.0);
                    nxtCpy.setParent(currSub);
                    queue.add(nxtCpy);
                    qPopper.add(nxtLoc);
                }
            }
        }

        List<Location> lstNearest = new ArrayList<>();
        while (!qPopper.isEmpty()) {
            lstNearest.add(qPopper.poll());
        }
        return lstNearest;
    }

    protected abstract void setUp(Location start);

    protected abstract boolean isEmpty();

    protected abstract SubPath getNext();

    protected abstract void addNext(SubPath next);

    protected abstract double getDist(SubPath loc1, SubPath loc2);

    protected abstract double getHeuristic(Location loc1, Location loc2);

    /**
     * Generates a path from the given parent map and end map
     *
     * @param end The end map
     * @return A stack of locations containing the path
     */
    protected final Stack<Location> genPath(SubPath end) {
        // Create an empty stack of locations
        Stack<Location> path = new Stack<>();
        // Start at the last node (end)
        SubPath prev = end;
        // Loop thru until the start is reached (parent == null)
        while (prev != null) {
            path.push(prev.getLocation());
            // prev := prev -> parent
            prev = prev.getParent();
        }
        return path;
    }

    public static final String txtDirections(Stack<Location> path) {
        Stack<DirectionStep> directionStepStack = makeDirectionStack(path);

        StringBuilder result = new StringBuilder();
        for (DirectionStep step : directionStepStack) {
            result.append(step.getDiections() + "\n");
        }
//        while (!directionStepStack.isEmpty()) {
//            result.append(directionStepStack.pop().getDiections()+"\n");
//        }
        return result.toString();
    }

    public static final Stack<DirectionStep> makeDirectionStack(Stack<Location> path) {
        String directions = "";
        Location start = null;

        Location loc1 = null;
        Location loc2 = null;
        Location loc3 = null;

        double distance = 0.0;
        double totDist = 0.0;


        Stack<DirectionStep> directionStepStack = new Stack<>();

        String currentDirections = "";

        while (!path.isEmpty()) {
            loc3 = path.pop();

            String currentFloor = loc3.getFloor();

            if (loc2 == null) {
                start = loc3;
            }
            if (loc1 != null && loc2 != null) {
                String tempStr = "";

                String directionUpDown="";
                if(floorToInt(loc2.getFloor())>floorToInt(loc3.getFloor())){
                    directionUpDown = "down";
                }else{
                    directionUpDown="up";
                }


                if (loc2.getNodeType() == Constants.NodeType.ELEV && loc3.getNodeType() == Constants.NodeType.ELEV) { // On and off the elevator
                    tempStr += "Take the elevator "+directionUpDown+" from FL " + loc2.getFloor() + " to FL " + loc3.getFloor() + "\n";
                    currentDirections = tempStr;
                } else if (loc2.getNodeType() == Constants.NodeType.STAI && loc3.getNodeType() == Constants.NodeType.STAI) { // On and off the stairs
                    tempStr += "Take the stairs "+directionUpDown +" from FL " + loc2.getFloor() + " to FL " + loc3.getFloor() + "\n";
                    currentDirections = tempStr;
                } else {
                    int x1 = loc1.getxCord();
                    int y1 = -1 * loc1.getyCord();

                    int x2 = loc2.getxCord();
                    int y2 = -1 * loc2.getyCord();

                    int x3 = loc3.getxCord();
                    int y3 = -1 * loc3.getyCord();

                    double a = calcDist(x1, y1, x2, y2); // 1 <-> 2
                    distance += a;
                    double b = calcDist(x2, y2, x3, y3); // 2 <-> 3
                    double c = calcDist(x1, y1, x3, y3); // 1 <-> 3

                    double angle = Math.toDegrees(Math.acos((Math.pow(a, 2.0) + Math.pow(b, 2.0) - Math.pow(c, 2.0)) / (2.0 * a * b)));
                    double vX = x1 - x2;
                    double vY = y1 - y2;
                    double uX = x3 - x2;
                    double uY = y3 - y2;
                    double cross = vX * uY - vY * uX;

                    totDist += a;
                    if (angle > STRAIGHT_ANGLE - TURN_SENSITIVITY && angle < STRAIGHT_ANGLE + TURN_SENSITIVITY) {
                        tempStr += "Turn ";
                        if (cross > 0) { // Right
                            tempStr += "right";
                        } else { // Left
                            tempStr += "left";
                        }
                        int displayDist = (int) (totDist * PIXEL_TO_METERS);
                        if (displayDist != 1) {
                            tempStr += " in " + displayDist + " meters\n";
                        } else {
                            tempStr += " in " + displayDist + " meter\n";
                        }
                        directions += tempStr;
                        currentDirections = tempStr;

                        totDist = 0.0;
                    }
                }
            }

            // Rotate
            loc1 = loc2;
            loc2 = loc3;

            if (!directionStepStack.empty()) {
                if (!currentDirections.equals(directionStepStack.get(directionStepStack.size() - 1).getDiections())) {
                    //extract data from string and put in setp stack
                    DirectionStep step = new DirectionStep();
                    step.setFloor(currentFloor);
                    //String currentDirections = directions.substring(0, directions.indexOf("\n"));
                    step.setDiections(currentDirections);
                    // directions.replaceFirst(currentDirections,"");//delete it from the string
                    directionStepStack.add(step);
                }
            } else {
                //extract data from string and put in setp stack
                DirectionStep step = new DirectionStep();
                step.setFloor(currentFloor);
                //String currentDirections = directions.substring(0, directions.indexOf("\n"));
                step.setDiections(currentDirections);
                // directions.replaceFirst(currentDirections,"");//delete it from the string
                directionStepStack.add(step);
            }
        }
        Location end = loc3;
        int numFloors;
        if (start != null && end != null) {
            numFloors = Math.abs(floorToInt(start.getFloor()) - floorToInt(end.getFloor()));
        } else {
            numFloors = 0;
        }

        distance = distance * PIXEL_TO_METERS;
        directions += "Distance: " + (int) distance + " meters\n";
        int time = (int) (distance / SPEED + numFloors * FLOOR_TIME);
        int minutes = time / 60;
        int seconds = time - minutes * 60;
        directions += "Time: ";
        if (minutes != 0) {
            directions += minutes;
            if (minutes != 1) {
                directions += " minutes";
            } else {
                directions += " minute";
            }
        }
        if (seconds != 0) {
            if (minutes != 0) {
                directions += " and ";
            }
            if (seconds != 1) {
                directions += seconds;
                directions += " seconds";
            } else {
                directions += " second";
            }
        }

        //add extras into new data format
        String[] arrDirections = directions.split("\n");
        for (String extras : arrDirections) {
            DirectionStep step = new DirectionStep();
            step.setFloor("");
            step.setDiections(extras);
            directionStepStack.add(step);
        }


        return directionStepStack;
    }

    public static void printPath(MapController mc, Location start, Location end) {
        if (end.getNodeCircle() != null) {
            end.getNodeCircle().setFill(MapDisplay.nodeEnd);
        }
        mc.clearTransit();
        mc.clearMap();
        PathContext context = SettingsController.getAlgType();
        Stack<Location> path = context.findPath(start, end);
        Stack<Location> path1 = (Stack<Location>) path.clone();
        MapController.currentRoute = (Stack<Location>) path.clone();
        String directions = context.txtDirections((Stack<Location>) path.clone());
        FirebaseAPI.addDirections(start, end, directions);
        MapController.currentDirections = directions;
        addDirections(mc.txtPane, makeDirectionStack((Stack<Location>) path.clone()));
        HashMap<String, Location> lstLocations = mc.getMap().getAllLocations();

        Path line = null;
        String currFloor = "";
        Location curr;
        while (!path.isEmpty()) {
            curr = path.pop();
            if (line == null) {
                line = new Path();
                line.getElements().add(new MoveTo(curr.getxCord(), curr.getyCord()));
                currFloor = curr.getFloor();
            } else if (!curr.getFloor().equals(currFloor)) {
                animateLine(line);
                mc.addLine(line, currFloor);
                mc.panMap.getChildren().add(0, line);

                PathElement pe = line.getElements().get(line.getElements().size() - 1);
                line = new Path();
                LineTo lt;
                MoveTo mt;
                if (pe instanceof LineTo) {
                    lt = (LineTo) pe;
                    line.getElements().add(new MoveTo(lt.getX(), lt.getY()));
                } else {
                    mt = (MoveTo) pe;
                    line.getElements().add(new MoveTo(mt.getX(), mt.getY()));
                }
                line.getElements().add(new LineTo(curr.getxCord(), curr.getyCord()));
                animateLine(line);
                mc.panMap.getChildren().add(0, line);

                line = new Path();
                line.getElements().add(new MoveTo(curr.getxCord(), curr.getyCord()));
                currFloor = curr.getFloor();
            } else {
                line.getElements().add(new LineTo(curr.getxCord(), curr.getyCord()));
            }
        }
        animateLine(line);
        mc.addLine(line, currFloor);

        mc.displayPath(line);
        mc.displayLocations(path1);
    }

    private static void animateLine(Path line) {
        line.setStroke(Color.BLACK);
        line.setOpacity(MapDisplay.opac);
        line.getStrokeDashArray().setAll(LINE_LENGTH, LINE_GAP);
        line.setStrokeWidth(LINE_WIDTH);
        line.setStrokeLineCap(StrokeLineCap.ROUND);
        line.setStrokeLineJoin(StrokeLineJoin.ROUND);
        final double maxOffset =
                line.getStrokeDashArray().stream()
                        .reduce(
                                0d,
                                (a, b) -> a - b
                        );

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                0,
                                Interpolator.LINEAR
                        )
                ),
                new KeyFrame(
                        Duration.seconds(3),
                        new KeyValue(
                                line.strokeDashOffsetProperty(),
                                maxOffset,
                                Interpolator.LINEAR
                        )
                )
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static String getDefLocation() {
        return defLocation;
    }

    public static void setDefLocation(String defLocation) {
        PathFinder.defLocation = defLocation;
        MapController.setTempStart(defLocation);
    }

    /**
     * Equation to calculate the distance between two points
     *
     * @param x1 X-Location of point 1
     * @param y1 Y-Location of point 1
     * @param x2 X-Location of point 2
     * @param y2 Y-Location of point 2
     * @return The distance as a double
     */
    static double calcDist(int x1, int y1, int x2, int y2) {
        return Math.pow((Math.pow(x1 - x2, 2.0) + (Math.pow(y1 - y2, 2.0))), 0.5);
    }

    public abstract MapHelpers.Algorithm getAlg();

    public static int floorToInt(String floor) {
        switch (floor) {
            case "L2":
                return 0;
            case "L1":
                return 1;
            case "G":
                return 2;
            case "1":
                return 3;
            case "2":
                return 4;
            case "3":
                return 5;
            default:
                return 6;
        }
    }

    private static void addDirections(ScrollPane txtPane, Stack<DirectionStep> directionSteps) {
        VBox vbox = new VBox();

        vbox.setPadding(new Insets(10, 10, 10, 15));
        vbox.setStyle("-fx-background-radius: 20;");
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        //String[] arrDirections = directions.split("\n");todo delete once working

        String lastFloor = "";

        VBox content = new VBox();
        VBox rootContent = new VBox();
        rootContent.setPrefWidth(375);
        content.setPrefWidth(375);

        TitledPane currentPane = new TitledPane();
        boolean first = true;
        int i = 0;
        for (DirectionStep step : directionSteps) {//loop through steps in directions

            String floor = step.getFloor();

            final TreeItem<HBox> parentNode = new TreeItem<>();


            if ((!floor.equals(lastFloor) || first)) {// Is a new floor
                //    root.getChildren().addAll(parentNode);
                first = false;
                HBox component = buildPaneChildren(step);
                content.getChildren().add(component);

                if (currentPane != null) {
                    currentPane.setContent(content);
                }
                content = new VBox();
                content.setPrefWidth(350);

                currentPane = buildTitledPane(step);
                if (currentPane != null) {
                    lastFloor = floor;
                    rootContent.getChildren().add(currentPane);
                } else {
                    Label lbl = new Label();

                    HBox result = new HBox();
                    String direction = step.getDiections();
                    lbl.setText(direction);
                    lbl.setFont(new Font(18));
                    lbl.setTextFill(Color.WHITE);
                    lbl.setPrefWidth(330);
                    lbl.setPrefHeight(40);
                    lbl.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
                    lbl.setAlignment(Pos.CENTER);
                    lbl.setPadding(new Insets(25, 4, 25, 5));
                    if (lbl.getText().contains("Distance") || lbl.getText().contains("Time")) {
                        lbl.setStyle("-fx-font-size: 20px;" + "-fx-font-weight: BOLD;" + "-fx-background-color: green;" + "-fx-background-radius: 30;");
                        lbl.setTextFill(Color.BLACK);
                        HBox other = new HBox();
                        other.getChildren().add(lbl);
                        rootContent.getChildren().add(other);
                    }

                }

            } else {// is a new step on the same floor

                HBox component = buildPaneChildren(step);
                AnchorPane componentAnchor = new AnchorPane();
                componentAnchor.getChildren().add(component);
                content.getChildren().add(componentAnchor);

            }
        }
        AnchorPane rootContentAnchor=new AnchorPane();
        rootContentAnchor.getChildren().add(rootContent);
        txtPane.setContent(rootContentAnchor);
        txtPane.setVisible(true);
    }

    private static TitledPane buildTitledPane(DirectionStep step) {
        TitledPane titledPane = new TitledPane();
        Label lbl = new Label();
        String floor = step.getFloor();

        lbl.setText(floor);
        lbl.setTextFill(Color.WHITE);
        lbl.setPrefWidth(330);
        lbl.setPrefHeight(40);
        lbl.setStyle("-fx-background-color: purple;" + "-fx-background-radius: 30;");
        lbl.setAlignment(Pos.CENTER);
        lbl.setPadding(new Insets(5, 4, 4, 5));
        HBox floorBox = new HBox();
        floorBox.getChildren().add(lbl);
        //    root.setValue(floorBox);
        //      root.setExpanded(true);
        String lable;
        if (step.getFloor().equals("")) {
            return null;
        } else {
            lable = "Floor: ";
        }
        titledPane.setText(lable + floor);
        return titledPane;
    }

    private static HBox buildPaneChildren(DirectionStep step) {
        Label lbl = new Label();

        HBox result = new HBox();
        result.setPadding(new Insets(10,0,10,0));
        String direction = step.getDiections();
        lbl.setText(direction);
        lbl.setFont(new Font(18));
        lbl.setTextFill(Color.WHITE);
        lbl.setPrefWidth(270);
        lbl.setPrefHeight(20);
        lbl.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        lbl.setAlignment(Pos.CENTER);
        lbl.setPadding(new Insets(25, 4, 25, 5));

        final TreeItem<HBox> childNode = new TreeItem<>();


        if (lbl.getText().contains("left")) {
            HBox left = new HBox();
            ImageView imgLeft = new ImageView();
            imgLeft.setImage(new Image("images/Icons/left.png"));
            imgLeft.setFitHeight(80);
            imgLeft.setFitWidth(80);
            imgLeft.setPreserveRatio(true);
            imgLeft.setPickOnBounds(true);
            imgLeft.setStyle("-fx-background-color: green;");
            AnchorPane leftPane = new AnchorPane();
            leftPane.getChildren().add(imgLeft);
            leftPane.setPrefWidth(40);
            leftPane.setPrefHeight(40);
            leftPane.setStyle("-fx-background-color: green;" + "-fx-background-radius: 20;");
            left.getChildren().add(leftPane);
            left.setSpacing(0);
            left.setAlignment(Pos.CENTER);
            left.getChildren().add(lbl);
            result.getChildren().add(left);

        } else if (lbl.getText().contains("right")) {
            HBox right = new HBox();
            ImageView imgRight = new ImageView();
            imgRight.setImage(new Image("images/Icons/right.png"));
            imgRight.setFitHeight(80);
            imgRight.setFitWidth(80);
            imgRight.setPreserveRatio(true);
            imgRight.setPickOnBounds(true);
            imgRight.setStyle("-fx-background-color: green;");
            AnchorPane rightPane = new AnchorPane();
            imgRight.toFront();
            rightPane.getChildren().add(imgRight);
            rightPane.setPrefWidth(40);
            rightPane.setPrefHeight(40);
            rightPane.setStyle("-fx-background-color: green;" + "-fx-background-radius: 20;");
            right.getChildren().add(rightPane);
            right.setSpacing(0);
            right.setAlignment(Pos.CENTER);
            right.getChildren().add(lbl);
            result.getChildren().add(right);

        }else if(lbl.getText().contains("down")){
            HBox down = new HBox();
            ImageView imgDown = new ImageView();
            imgDown.setImage(new Image("images/Icons/down.png"));
            imgDown.setFitHeight(80);
            imgDown.setFitWidth(80);
            imgDown.setPreserveRatio(true);
            imgDown.setPickOnBounds(true);
            imgDown.setStyle("-fx-background-color: green;");
            AnchorPane downPane = new AnchorPane();
            imgDown.toFront();
            downPane.getChildren().add(imgDown);
            downPane.setPrefWidth(40);
            downPane.setPrefHeight(40);
            downPane.setStyle("-fx-background-color: green;" + "-fx-background-radius: 20;");
            down.getChildren().add(downPane);
            down.setSpacing(0);
            down.setAlignment(Pos.CENTER);
            down.getChildren().add(lbl);
            result.getChildren().add(down);

        } else if(lbl.getText().contains("up")){

            HBox up = new HBox();
            ImageView imgUp = new ImageView();
            imgUp.setImage(new Image("images/Icons/up.png"));
            imgUp.setFitHeight(80);
            imgUp.setFitWidth(80);
            imgUp.setPreserveRatio(true);
            imgUp.setPickOnBounds(true);
            imgUp.setStyle("-fx-background-color: green;");
            AnchorPane upPane = new AnchorPane();
            imgUp.toFront();
            upPane.getChildren().add(imgUp);
            upPane.setPrefWidth(40);
            upPane.setPrefHeight(40);
            upPane.setStyle("-fx-background-color: green;" + "-fx-background-radius: 20;");
            up.getChildren().add(upPane);
            up.setSpacing(0);
            up.setAlignment(Pos.CENTER);
            up.getChildren().add(lbl);
            result.getChildren().add(up);

    } else {
            HBox other = new HBox();
            other.getChildren().add(lbl);
            result.getChildren().add(other);

        }

        return result;

    }

}
