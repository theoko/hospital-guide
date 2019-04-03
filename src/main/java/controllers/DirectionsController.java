package controllers;


import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import map.MapDisplay;
import map.PathFinder;
import models.map.Location;
import models.map.SubPath;
import javafx.animation.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class DirectionsController extends PopUpController implements Initializable {
    public Label lblStart;
    public Label lblEnd;
    public JFXButton btnCancel;
    public JFXButton btnGo;

    private Location loc2;

    private final double lineWidth = 3.5;
    private final double lineLength = 5.0;
    private final double lineGap = 10.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setLoc(Location loc) {
        this.loc = loc;
        lblEnd.setText(loc.getLongName());
    }

    public void setLoc2(Location loc2) {
        this.loc2 = loc2;
        lblStart.setText(loc2.getLongName());
    }

    public void btnCancel_OnClick(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

    public void btnGo_OnClick(MouseEvent event) {
        event.consume();

        ObservableList<Node> lstNodes =  pane.getChildren();
        List<Node> lstLines = new ArrayList<>();
        for (Node n : lstNodes) {
            if (n instanceof Line) {
                lstLines.add(n);
            }
        }
        for (Node l : lstLines) {
            lstNodes.remove(l);
        }

        Stack<SubPath> path = PathFinder.findPath(map, loc, loc2);
        HashMap<String, Location> lstLocations = map.getAllLocations();
        for (SubPath sub : path) {
            String id = sub.getEdgeID();
            if (!id.equals("")) {
                String locID1 = id.substring(0, id.indexOf("_"));
                String locID2 = id.substring(id.indexOf("_") + 1);
                String endLoc = sub.getLocation().getNodeID();
                String start;
                String end;
                if (locID1.equals(endLoc)) {
                    start = locID2;
                    end = locID1;
                } else {
                    start = locID1;
                    end = locID2;
                }
                Location loc1 = lstLocations.get(start);
                Location loc2 = lstLocations.get(end);
                if (loc1.getFloor().equals("1") && loc2.getFloor().equals("1")) {
                    Line line = new Line(MapDisplay.scaleX(loc1.getxCord()), MapDisplay.scaleY(loc1.getyCord()), MapDisplay.scaleX(loc2.getxCord()), MapDisplay.scaleY(loc2.getyCord()));
                    line.setStroke(Color.BLACK);

                    line.getStrokeDashArray().setAll(lineLength, lineGap);
                    line.setStrokeWidth(lineWidth);
                    line.setStrokeLineCap(StrokeLineCap.ROUND);
                    line.setStrokeLineJoin(StrokeLineJoin.ROUND);

                    final double maxOffset =
                            line.getStrokeDashArray().stream()
                                    .reduce(
                                            0d,
                                            (a, b) -> a + b
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
                                    Duration.seconds(2),
                                    new KeyValue(
                                            line.strokeDashOffsetProperty(),
                                            maxOffset,
                                            Interpolator.LINEAR
                                    )
                            )
                    );
                    timeline.setCycleCount(Timeline.INDEFINITE);
                    timeline.play();
                    pane.getChildren().add(1, line);
                }
            }
        }
        ScreenController.deactivate();
    }
}