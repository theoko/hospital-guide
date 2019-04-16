package controllers.maps;

import controllers.ScreenController;
import controllers.search.SearchEngineController;
import helpers.Constants;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;
import map.MapDisplay;
import models.map.Location;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMapController extends MapController {

    private static Location edgLoc = null;

    static class Delta {
        boolean bolDragged;
    }

    static class Mover {
        Line line;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        SearchEngineController.setParentController(this);
        MapDisplay.displayAdmin(this);

        Delta deltaDragged = new Delta();
        panMap.setOnMousePressed((e) -> {
            deltaDragged.bolDragged = false;
        });
        panMap.setOnMouseDragged((e) -> {
            deltaDragged.bolDragged = true;
        });
        panMap.setOnMouseReleased((e) -> {
            try {
                if (!deltaDragged.bolDragged) {
                    ScreenController.addPopUp(this,(int) e.getX(), (int) e.getY(), map);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        Mover mover = new Mover();
        panMap.setOnMouseMoved(e -> {
            Location edgLoc = AdminMapController.getEdgLoc();
            if (edgLoc != null) {
                if (mover.line == null) {
                    mover.line = new Line();
                    mover.line.setStrokeWidth(MapDisplay.edgeWidth);
                    panMap.getChildren().add(0, mover.line);
                    mover.line.setStartX(edgLoc.getxCord());
                    mover.line.setStartY(edgLoc.getyCord());
                }
                mover.line.setEndX(e.getX());
                mover.line.setEndY(e.getY());
            } else if (mover.line != null) {
                panMap.getChildren().remove(mover.line);
                mover.line = null;
            }
        });
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnReturn);
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    @Override
    public void btnFloor3_Click(MouseEvent mouseEvent) {
        showFloor("3");
    }

    @Override
    public void btnFloor2_Click(MouseEvent mouseEvent) {
        showFloor("2");
    }

    @Override
    public void btnFloor1_Click(MouseEvent mouseEvent) {
        showFloor("1");
    }

    @Override
    public void btnFloorG_Click(MouseEvent mouseEvent) {
        showFloor("G");
    }

    @Override
    public void btnFloorL1_Click(MouseEvent mouseEvent) {
        showFloor("L1");
    }

    @Override
    public void btnFloorL2_Click(MouseEvent mouseEvent) {
        showFloor("L2");
    }

    private void clearEdges() {
        List<Node> lstNodes = new ArrayList<>();
        for (Node n : panMap.getChildren()) {
            if (n instanceof Line) {
                lstNodes.add(n);
            }
        }
        panMap.getChildren().removeAll(lstNodes);
    }

    @Override
    public void showFloor(String newFloor) {
        super.showFloor(newFloor);
        clearEdges();
        MapDisplay.displayAdmin(this);
    }

    @Override
    public boolean isAdmin() {
        return true;
    }

    public static Location getEdgLoc() {
        return edgLoc;
    }

    public static void setEdgLoc(Location edgLoc) {
        AdminMapController.edgLoc = edgLoc;
    }
}

