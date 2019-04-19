package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import controllers.search.SearchEngineController;
import helpers.Constants;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Transform;
import map.MapDisplay;
import models.map.Location;
import models.search.SearchAPI;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminMapController extends MapController {
    private final static String MOVER_EDGE = "";

    public VBox vboxDock;
    public ImageView imgLogOut;
    public JFXButton btnLogOut;
    public AnchorPane roomBooking;
    public AnchorPane UserD;
    public AnchorPane Algo;
    public AnchorPane Clean;
    public AnchorPane TabEdit;

    public JFXTextField search;

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

        SearchAPI searchAPI = new SearchAPI(search, true);
        searchAPI.searchable();

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

        ImageView imgSearch = new ImageView();
        imgSearch.setImage(new Image("images/Icons/search.png"));
        imgSearch.setFitHeight(30);
        imgSearch.setFitWidth(30);
        imgSearch.setPreserveRatio(true);
        imgSearch.setPickOnBounds(true);

        JFXButton btnSearch = new JFXButton("",imgSearch);
        btnSearch.setAlignment(Pos.CENTER);
        btnSearch.setPrefWidth(60);
        btnSearch.setPrefHeight(60);
        btnSearch.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnSearch.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgArrow = new ImageView();
        imgArrow.setImage(new Image("images/Icons/arrow.png"));
        imgArrow.setFitHeight(30);
        imgArrow.setFitWidth(30);
        imgArrow.setPreserveRatio(true);
        imgArrow.setPickOnBounds(true);

        JFXButton btnArrow = new JFXButton("",imgArrow);
        btnArrow.setAlignment(Pos.CENTER);
        btnArrow.setPrefWidth(60);
        btnArrow.setPrefHeight(60);
        btnArrow.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnArrow.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgRoom = new ImageView();
        imgRoom.setImage(new Image("images/Icons/room.png"));
        imgRoom.setFitHeight(30);
        imgRoom.setFitWidth(30);
        imgRoom.setPreserveRatio(true);
        imgRoom.setPickOnBounds(true);

        JFXButton btnRoom = new JFXButton("",imgRoom);
        btnRoom.setAlignment(Pos.CENTER);
        btnRoom.setPrefWidth(60);
        btnRoom.setPrefHeight(60);
        btnRoom.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnRoom.setTextOverrun(OverrunStyle.CLIP);

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

        btnLogOut.setStyle("-fx-background-radius: 30;" );
        btnLogOut.setButtonType(JFXButton.ButtonType.RAISED);
        imgLogOut.setImage(new Image("images/Icons/signout.png"));

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

        HBox searchBox = new HBox();

//        JFXTextField search = new JFXTextField();
//        search.setPromptText(" Search");
//        search.setPrefHeight(34);
//        search.setPrefWidth(450);
//        search.setAlignment(Pos.CENTER);
//        search.setStyle("-fx-font-size: 18px;"
//                + "-fx-font-weight: bold;"
//                + "-fx-font-family: fantasy;"
//                + "-fx-text-fill: #022D5A;"
//                + "-fx-background-color: white");


        searchBox.getChildren().add(search);
        searchBox.getChildren().add(btnArrow);
        searchBox.setPrefHeight(60);
        searchBox.setPrefWidth(370);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(-20);

        Label lblRoom = new Label("Conference Room and Workspace Booking");
        lblRoom.setPrefHeight(50);
        lblRoom.setPrefWidth(1200);
        lblRoom.setTextFill(Color.WHITE);
        lblRoom.setAlignment(Pos.CENTER);
        lblRoom.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblRoom.setPadding(new Insets(10, 10, 10, 10));

        VBox boxRoom = new VBox();
        boxRoom.getChildren().add(lblRoom);
        boxRoom.getChildren().add(roomBooking);
        boxRoom.setAlignment(Pos.CENTER_LEFT);
        boxRoom.setPrefSize(1200,760);
        boxRoom.setSpacing(5);

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

        JFXNodesList nodeListSearch = new JFXNodesList();
        JFXNodesList nodeListUser = new JFXNodesList();
        JFXNodesList nodeListRoom = new JFXNodesList();
        JFXNodesList nodesListEdit = new JFXNodesList();
        JFXNodesList nodesListAlgo = new JFXNodesList();
        JFXNodesList nodesListClean = new JFXNodesList();
        JFXNodesList nodesListTab = new JFXNodesList();

        nodeListSearch.addAnimatedNode(btnSearch);
        nodeListSearch.addAnimatedNode(searchBox);
        nodeListSearch.setRotate(90);
        nodeListSearch.setSpacing(150);

        nodeListUser.addAnimatedNode(btnUser);
        nodeListUser.addAnimatedNode(userBox);
        nodeListUser.setRotate(90);
        nodeListUser.setSpacing(60);

        nodeListRoom.addAnimatedNode(btnRoom);
        nodeListRoom.addAnimatedNode(boxRoom);
        nodeListRoom.setRotate(85);
        nodeListRoom.setSpacing(240);

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

        vboxDock.getChildren().add(nodeListUser);
        vboxDock.getChildren().add(nodeListSearch);
        vboxDock.getChildren().add(nodeListRoom);
        vboxDock.getChildren().add(nodesListEdit);
        vboxDock.getChildren().add(nodesListAlgo);
        vboxDock.getChildren().add(nodesListClean);
        vboxDock.getChildren().add(nodesListTab);
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnLogOut);
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
}

