package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import controllers.ScreenController;
import controllers.search.SearchEngineController;
import google.FirebaseAPI;
import helpers.Constants;
import helpers.UIHelpers;
import helpers.UserHelpers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import map.MapDisplay;
import models.map.Location;
import models.search.SearchAPI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static controllers.ScreenController.mouseCnt;
import static controllers.ScreenController.secCnt;

public class AdminMapController extends MapController {
    private final static String MOVER_EDGE = "";

    public ImageView imgLogOut;
    public JFXButton btnLogOut;
    public AnchorPane UserD;
    public AnchorPane Algo;
    public AnchorPane Clean;
    public AnchorPane TabEdit;



    public JFXTextField search;
    public JFXToggleButton tglSpace;
    public JFXToggleButton tglZone;
    public AnchorPane Time;

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
                if (!deltaDragged.bolDragged && edgLoc == null) {
                    ScreenController.addPopUp(this,(int) e.getX(), (int) e.getY(), map);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        imgMap.setOnMousePressed((e) -> {
            deltaDragged.bolDragged = false;
        });
        imgMap.setOnMouseDragged((e) -> {
            deltaDragged.bolDragged = true;
        });
        imgMap.setOnMouseReleased((e) -> {
            try {
                if (!deltaDragged.bolDragged && edgLoc == null) {
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
                    mover.line.setId(MOVER_EDGE);
                }
                mover.line.setEndX(e.getX());
                mover.line.setEndY(e.getY());
            } else if (mover.line != null) {
                panMap.getChildren().remove(mover.line);
                mover.line = null;
            }
        });
        imgMap.setOnMouseMoved(e -> {
            Location edgLoc = AdminMapController.getEdgLoc();
            if (edgLoc != null) {
                if (mover.line == null) {
                    mover.line = new Line();
                    mover.line.setStrokeWidth(MapDisplay.edgeWidth);
                    panMap.getChildren().add(0, mover.line);
                    mover.line.setStartX(edgLoc.getxCord());
                    mover.line.setStartY(edgLoc.getyCord());
                    mover.line.setId(MOVER_EDGE);
                }
                mover.line.setEndX(e.getX());
                mover.line.setEndY(e.getY());
            } else if (mover.line != null) {
                panMap.getChildren().remove(mover.line);
                mover.line = null;
            }
        });
        TableEditorController.mapC = getCurrMapControl();
        TableEditorController.localMap = map;
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnLogOut);
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    private void clearEdges() {
        List<Node> lstNodes = new ArrayList<>();
        for (Node n : panMap.getChildren()) {
            if (n instanceof Line && !n.getId().equals(MOVER_EDGE)) {
                lstNodes.add(n);
            }
        }
        panMap.getChildren().removeAll(lstNodes);
    }

    @Override
    public void showFloor(String newFloor) {
        super.showFloorHelper(newFloor);
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

    @Override
    protected void addDoc() {
        ImageView imgUser = new ImageView();
        imgUser.setImage(new Image("images/Icons/user.png"));
        imgUser.setFitHeight(30);
        imgUser.setFitWidth(30);
        imgUser.setPreserveRatio(true);
        imgUser.setPickOnBounds(true);

        JFXButton btnUser = new JFXButton("",imgUser);
        btnUser.setAlignment(Pos.CENTER);
        btnUser.setPrefWidth(60);
        btnUser.setPrefHeight(60);
        btnUser.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnUser.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.mouseHover(btnUser);
        btnUser.setTooltip(new Tooltip("Account"));

        ImageView imgEdit = new ImageView();
        imgEdit.setImage(new Image("images/Icons/edit.png"));
        imgEdit.setFitHeight(30);
        imgEdit.setFitWidth(30);
        imgEdit.setPreserveRatio(true);
        imgEdit.setPickOnBounds(true);

        JFXButton btnEdit = new JFXButton("",imgEdit);
        btnEdit.setAlignment(Pos.CENTER);
        btnEdit.setPrefWidth(60);
        btnEdit.setPrefHeight(60);
        btnEdit.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnEdit.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.mouseHover(btnEdit);
        btnEdit.setTooltip(new Tooltip("Edit"));

        ImageView imgAlgo = new ImageView();
        imgAlgo.setImage(new Image("images/Icons/algo.png"));
        imgAlgo.setFitHeight(30);
        imgAlgo.setFitWidth(30);
        imgAlgo.setPreserveRatio(true);
        imgAlgo.setPickOnBounds(true);

        JFXButton btnAlgo = new JFXButton("",imgAlgo);
        btnAlgo.setAlignment(Pos.CENTER);
        btnAlgo.setPrefWidth(60);
        btnAlgo.setPrefHeight(60);
        btnAlgo.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnAlgo.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.mouseHover(btnAlgo);
        btnAlgo.setTooltip(new Tooltip("Search Algorithm"));

        ImageView imgClean = new ImageView();
        imgClean.setImage(new Image("images/Icons/clean.png"));
        imgClean.setFitHeight(30);
        imgClean.setFitWidth(30);
        imgClean.setPreserveRatio(true);
        imgClean.setPickOnBounds(true);

        JFXButton btnClean = new JFXButton("",imgClean);
        btnClean.setAlignment(Pos.CENTER);
        btnClean.setPrefWidth(60);
        btnClean.setPrefHeight(60);
        btnClean.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnClean.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.mouseHover(btnClean);
        btnClean.setTooltip(new Tooltip("Spill Request"));

        ImageView imgTab = new ImageView();
        imgTab.setImage(new Image("images/Icons/tabEdit.png"));
        imgTab.setFitHeight(30);
        imgTab.setFitWidth(30);
        imgTab.setPreserveRatio(true);
        imgTab.setPickOnBounds(true);

        JFXButton btnTab = new JFXButton("",imgTab);
        btnTab.setAlignment(Pos.CENTER);
        btnTab.setPrefWidth(60);
        btnTab.setPrefHeight(60);
        btnTab.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnTab.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.mouseHover(btnTab);
        btnTab.setTooltip(new Tooltip("Tab"));

        ImageView imgCoffee = new ImageView();
        imgCoffee.setImage(new Image("images/SearchIcons/coffee.png"));
        imgCoffee.setFitHeight(30);
        imgCoffee.setFitWidth(30);
        imgCoffee.setPreserveRatio(true);
        imgCoffee.setPickOnBounds(true);

        JFXButton btnCoffee = new JFXButton("",imgCoffee);
        btnCoffee.setAlignment(Pos.CENTER);
        btnCoffee.setPrefWidth(60);
        btnCoffee.setPrefHeight(60);
        btnCoffee.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnCoffee.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.btnRaise(btnCoffee);
        btnCoffee.setTooltip(new Tooltip("Food"));

        ImageView imgRest = new ImageView();
        imgRest.setImage(new Image("images/SearchIcons/rest.png"));
        imgRest.setFitHeight(30);
        imgRest.setFitWidth(30);
        imgRest.setPreserveRatio(true);
        imgRest.setPickOnBounds(true);

        JFXButton btnRest = new JFXButton("",imgRest);
        btnRest.setAlignment(Pos.CENTER);
        btnRest.setPrefWidth(60);
        btnRest.setPrefHeight(60);
        btnRest.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnRest.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.btnRaise(btnRest);
        btnRest.setTooltip(new Tooltip("Restroom"));

        ImageView imgExit = new ImageView();
        imgExit.setImage(new Image("images/SearchIcons/exit.png"));
        imgExit.setFitHeight(30);
        imgExit.setFitWidth(30);
        imgExit.setPreserveRatio(true);
        imgExit.setPickOnBounds(true);

        JFXButton btnExit = new JFXButton("",imgExit);
        btnExit.setAlignment(Pos.CENTER);
        btnExit.setPrefWidth(60);
        btnExit.setPrefHeight(60);
        btnExit.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnExit.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.btnRaise(btnExit);
        btnExit.setTooltip(new Tooltip("Exit"));

        ImageView imgElev = new ImageView();
        imgElev.setImage(new Image("images/SearchIcons/elev.png"));
        imgElev.setFitHeight(30);
        imgElev.setFitWidth(30);
        imgElev.setPreserveRatio(true);
        imgElev.setPickOnBounds(true);

        JFXButton btnElev = new JFXButton("",imgElev);
        btnElev.setAlignment(Pos.CENTER);
        btnElev.setPrefWidth(60);
        btnElev.setPrefHeight(60);
        btnElev.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnElev.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.btnRaise(btnElev);
        btnElev.setTooltip(new Tooltip("Elevator"));

        ImageView imgInfo = new ImageView();
        imgInfo.setImage(new Image("images/Icons/info.png"));
        imgInfo.setFitHeight(30);
        imgInfo.setFitWidth(30);
        imgInfo.setPreserveRatio(true);
        imgInfo.setPickOnBounds(true);

        JFXButton btnInfo = new JFXButton("",imgInfo);
        btnInfo.setAlignment(Pos.CENTER);
        btnInfo.setPrefWidth(60);
        btnInfo.setPrefHeight(60);
        btnInfo.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnInfo.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.btnRaise(btnInfo);
        btnInfo.setTooltip(new Tooltip("Information"));
        btnTab.setTooltip(new Tooltip("Tabular Node Edit"));

        ImageView imgTime = new ImageView();
        imgTime.setImage(new Image("images/Icons/time.png"));
        imgTime.setFitHeight(30);
        imgTime.setFitWidth(30);
        imgTime.setPreserveRatio(true);
        imgTime.setPickOnBounds(true);

        JFXButton btnTime = new JFXButton("",imgTime);
        btnTime.setAlignment(Pos.CENTER);
        btnTime.setPrefWidth(60);
        btnTime.setPrefHeight(60);
        btnTime.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnTime.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.mouseHover(btnTab);
        btnTime.setTooltip(new Tooltip("Logout Time"));

        btnLogOut.setStyle("-fx-background-radius: 30;" );
        btnLogOut.setButtonType(JFXButton.ButtonType.RAISED);
        imgLogOut.setImage(new Image("images/Icons/signout.png"));
        UIHelpers.btnRaise(btnLogOut);

        vboxDock.setSpacing(8);

        Label user = new Label("Logout");
        user.setPrefHeight(50);
        user.setPrefWidth(120);
        user.setTextFill(Color.WHITE);
        user.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" + "-fx-background-radius: 10;" + "-fx-font-size: 24;");
        user.setPadding(new Insets(0, 0, 0, 10));

        HBox userBox = new HBox();

        userBox.getChildren().add(user);
        userBox.getChildren().add(btnLogOut);
        userBox.setStyle( "-fx-background-radius: 20;");
        userBox.setPrefHeight(60);
        userBox.setPrefWidth(180);
        userBox.setAlignment(Pos.CENTER);
        userBox.setSpacing(-20);

        Label lblEdit = new Label("User Dashboard");
        lblEdit.setPrefHeight(50);
        lblEdit.setPrefWidth(1200);
        lblEdit.setTextFill(Color.WHITE);
        lblEdit.setAlignment(Pos.CENTER);
        lblEdit.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblEdit.setPadding(new Insets(10, 10, 10, 10));

        VBox boxEdit = new VBox();
        boxEdit.getChildren().add(lblEdit);
        boxEdit.getChildren().add(UserD);
        boxEdit.setAlignment(Pos.CENTER_LEFT);
        boxEdit.setPrefSize(1200,760);
        boxEdit.setSpacing(5);

        Label lblAlgo = new Label("Algorithm");
        lblAlgo.setPrefHeight(40);
        lblAlgo.setPrefWidth(150);
        lblAlgo.setTextFill(Color.WHITE);
        lblAlgo.setAlignment(Pos.CENTER);
        lblAlgo.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 20;" +
                "-fx-font-size: 18;" +
                "-fx-font-weight: BOLD");
        lblAlgo.setPadding(new Insets(5, 10, 5, 10));

        HBox boxAlgo = new HBox();
        boxAlgo.getChildren().add(Algo);
        boxAlgo.getChildren().add(lblAlgo);
        boxAlgo.setAlignment(Pos.CENTER);
        boxAlgo.setPrefSize(550,40);
        boxAlgo.setSpacing(-30);

        Label lblClean = new Label("Sanitation Requests");
        lblClean.setPrefHeight(50);
        lblClean.setPrefWidth(1200);
        lblClean.setTextFill(Color.WHITE);
        lblClean.setAlignment(Pos.CENTER);
        lblClean.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblClean.setPadding(new Insets(10, 10, 10, 10));

        VBox boxClean = new VBox();
        boxClean.getChildren().add(lblClean);
        boxClean.getChildren().add(Clean);
        boxClean.setAlignment(Pos.CENTER_LEFT);
        boxClean.setPrefSize(1200,760);
        boxClean.setSpacing(5);

        Label lblTab = new Label("Tabular Map Editor");
        lblTab.setPrefHeight(50);
        lblTab.setPrefWidth(1200);
        lblTab.setTextFill(Color.WHITE);
        lblTab.setAlignment(Pos.CENTER);
        lblTab.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblTab.setPadding(new Insets(10, 10, 10, 10));

        VBox boxTab = new VBox();
        boxTab.getChildren().add(lblTab);
        boxTab.getChildren().add(TabEdit);
        boxTab.setAlignment(Pos.CENTER_LEFT);
        boxTab.setPrefSize(1200,760);
        boxTab.setSpacing(5);

        JFXNodesList nodeListUser = new JFXNodesList();
        JFXNodesList nodesListEdit = new JFXNodesList();
        JFXNodesList nodesListAlgo = new JFXNodesList();
        JFXNodesList nodesListClean = new JFXNodesList();
        JFXNodesList nodesListTab = new JFXNodesList();
        JFXNodesList nodesListTime = new JFXNodesList();

        nodeListUser.addAnimatedNode(btnUser);
        nodeListUser.addAnimatedNode(userBox);
        nodeListUser.setRotate(90);
        nodeListUser.setSpacing(60);

        nodesListEdit.addAnimatedNode(btnEdit);
        nodesListEdit.addAnimatedNode(boxEdit);
        nodesListEdit.setRotate(90);
        nodesListEdit.setSpacing(240);

        nodesListAlgo.addAnimatedNode(btnAlgo);
        nodesListAlgo.addAnimatedNode(boxAlgo);
        nodesListAlgo.setRotate(90);
        nodesListAlgo.setSpacing(250);

        nodesListClean.addAnimatedNode(btnClean);
        nodesListClean.addAnimatedNode(boxClean);
        nodesListClean.setRotate(100);
        nodesListClean.setSpacing(250);

        nodesListTab.addAnimatedNode(btnTab);
        nodesListTab.addAnimatedNode(boxTab);
        nodesListTab.setRotate(105);
        nodesListTab.setSpacing(260);

        nodesListTime.addAnimatedNode(btnTime);
        nodesListTime.addAnimatedNode(Time);
        nodesListTime.setRotate(90);
        nodesListTime.setSpacing(145);

        vboxDock.getChildren().add(nodeListUser);
        vboxDock.getChildren().add(nodesListEdit);
        vboxDock.getChildren().add(nodesListAlgo);
        vboxDock.getChildren().add(nodesListTime);
        vboxDock.getChildren().add(nodesListClean);
        vboxDock.getChildren().add(nodesListTab);
    }

    @Override
    public void associateUserWithDirections(Location start, Location end) {
        FirebaseAPI.addDirectionsForUser(UserHelpers.getCurrentUser().getUsername(), start, end);
    }
}

