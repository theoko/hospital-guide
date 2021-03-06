package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import controllers.search.SearchEngineController;
import google.FirebaseAPI;
import helpers.Constants;
import helpers.UIHelpers;
import helpers.UserHelpers;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import map.MapDisplay;
import map.PathFinder;
import messaging.TextMessenger;
import models.map.Location;
import models.search.SearchAPI;

import java.net.URL;
import java.util.ResourceBundle;

public class CustodianMapController extends MapController {


    public AnchorPane paneDock;
    public VBox vboxDock;
    public ImageView imgLogOut;
    public JFXButton btnLogOut;
    public AnchorPane tilDirections;
    public AnchorPane sanitation;
    public JFXTextField search;
    public JFXTextField textNum;
    public ImageView imgText;
    public JFXButton btnText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        SearchEngineController.setParentController(this);
        MapDisplay.displayCust(this);
        initDirections();

        SearchAPI searchAPI = new SearchAPI(search, true);
        searchAPI.searchable();

    }

    public void btn_SendDirections (MouseEvent event) {
        if(currentRoute == null) return;
        TextMessenger tm = new TextMessenger();
        String input_phone_number = "+1"+textNum.getText();
        tm.declareRecipient(input_phone_number);
        tm.declareMessage(MapController.currentDirections);
        tm.sendMessage();
        event.consume();
        event.consume();
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnLogOut);
        ScreenController.activate(Constants.Routes.LOGIN);
    }



    @Override
    public void showFloor(String newFloor) {
        super.showFloorHelper(newFloor);
        MapDisplay.displayCust(this);
    }

    @Override
    protected void addDoc() {

        //path.setEditable(true);
        start.setPromptText("Select Path");
        start.setStyle("-fx-background-color: #ffffff");
        start.setPrefWidth(300);
        start.setPrefHeight(40);

        //path.setEditable(true);
        end.setPromptText("Select Path");
        end.setStyle("-fx-background-color: #ffffff");
        end.setPrefWidth(300);
        end.setPrefHeight(40);

        ImageView imgPath = new ImageView();
        imgPath.setImage(new Image("images/Icons/path.png"));
        imgPath.setFitWidth(30);
        imgPath.setFitHeight(30);
        imgPath.setPreserveRatio(true);
        imgPath.setPickOnBounds(true);

        JFXButton btnPath = new JFXButton("", imgPath);
        btnPath.setAlignment(Pos.CENTER);
        btnPath.setPrefWidth(60);
        btnPath.setPrefHeight(60);
        btnPath.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnPath.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.mouseHover(btnPath);



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
        UIHelpers.mouseHover(btnSearch);
        btnSearch.setTooltip(new Tooltip("Search Bar"));

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
        UIHelpers.mouseHover(btnArrow);
        btnArrow.setTooltip(new Tooltip("Enter"));

        ImageView imgRoute = new ImageView();
        imgRoute.setImage(new Image("images/Icons/route.png"));
        imgRoute.setFitHeight(30);
        imgRoute.setFitWidth(30);
        imgRoute.setPreserveRatio(true);
        imgRoute.setPickOnBounds(true);

        JFXButton btnRoute = new JFXButton("",imgRoute);
        btnRoute.setAlignment(Pos.CENTER);
        btnRoute.setPrefWidth(60);
        btnRoute.setPrefHeight(60);
        btnRoute.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnRoute.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.mouseHover(btnRoute);
        btnRoute.setTooltip(new Tooltip("Directions"));

        ImageView imgExl = new ImageView();
        imgExl.setImage(new Image("images/Icons/excl.png"));
        imgExl.setFitHeight(30);
        imgExl.setFitWidth(30);
        imgExl.setPreserveRatio(true);
        imgExl.setPickOnBounds(true);

        JFXButton btnExl = new JFXButton("",imgExl);
        btnExl.setAlignment(Pos.CENTER);
        btnExl.setPrefWidth(60);
        btnExl.setPrefHeight(60);
        btnExl.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnExl.setTextOverrun(OverrunStyle.CLIP);
        UIHelpers.mouseHover(btnExl);
        btnExl.setTooltip(new Tooltip("Service Requests"));

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
        btnCoffee.setOnMouseClicked((e) -> {
            PathFinder.printByType(this, map, Constants.NodeType.RETL);
        });
        UIHelpers.btnRaise(btnCoffee);

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
        btnRest.setOnMouseClicked((e) -> {
            PathFinder.printByType(this, map, Constants.NodeType.REST, Constants.NodeType.BATH);
        });
        UIHelpers.btnRaise(btnRest);

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
        btnExit.setOnMouseClicked((e) -> {
            PathFinder.printByType(this, map, Constants.NodeType.EXIT);
        });
        UIHelpers.btnRaise(btnExit);

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
        btnElev.setOnMouseClicked((e) -> {
            PathFinder.printByType(this, map, Constants.NodeType.ELEV, Constants.NodeType.STAI);
        });
        UIHelpers.btnRaise(btnElev);

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
        btnInfo.setOnMouseClicked((e) -> {
            PathFinder.printByType(this, map, Constants.NodeType.INFO);
        });
        UIHelpers.btnRaise(btnInfo);

        btnLogOut.setStyle("-fx-background-radius: 30;" );
        btnLogOut.setButtonType(JFXButton.ButtonType.RAISED);
        imgLogOut.setImage(new Image("images/Icons/signout.png"));

        btnText.setStyle("-fx-background-radius: 30;" );
        btnText.setButtonType(JFXButton.ButtonType.RAISED);
        imgText.setImage(new Image("images/Icons/text.png"));
        UIHelpers.btnRaise(btnLogOut);

        vboxDock.setSpacing(8);

        HBox textBox = new HBox();

        textBox.getChildren().add(textNum);
        textBox.getChildren().add(btnText);
        textBox.setStyle( "-fx-background-radius: 20;");
        textBox.setPrefHeight(60);
        textBox.setPrefWidth(180);
        textBox.setAlignment(Pos.CENTER);
        textBox.setSpacing(-20);

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


        Label lblStart = new Label("Start");
        lblStart.setPrefHeight(40);
        lblStart.setPrefWidth(150);
        lblStart.setTextFill(Color.WHITE);
        lblStart.setAlignment(Pos.CENTER);
        lblStart.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 20;" +
                "-fx-font-size: 18;" +
                "-fx-font-weight: BOLD");
        lblStart.setPadding(new Insets(5, 10, 5, 10));


        Label lblEnd = new Label("End");
        lblEnd.setPrefHeight(40);
        lblEnd.setPrefWidth(150);
        lblEnd.setTextFill(Color.WHITE);
        lblEnd.setAlignment(Pos.CENTER);
        lblEnd.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 20;" +
                "-fx-font-size: 18;" +
                "-fx-font-weight: BOLD");
        lblEnd.setPadding(new Insets(5, 10, 5, 10));

        HBox pathBox = new HBox();

        HBox pathBox1 = new HBox();
        pathBox1.setSpacing(-25.0);
        pathBox1.getChildren().add(start);
        pathBox1.getChildren().add(lblStart);
        pathBox.getChildren().add(pathBox1);
        HBox pathBox2 = new HBox();
        pathBox2.setSpacing(-25.0);
        pathBox2.getChildren().add(end);
        pathBox2.getChildren().add(lblEnd);
        pathBox.getChildren().add(pathBox2);
        pathBox.setAlignment(Pos.CENTER);
        pathBox.setPrefSize(700,40);
        pathBox.setSpacing(20);
        pathBox.setTranslateX(-400.0);
        pathBox.setTranslateY(-50.0);

        HBox searchBox = new HBox();

        searchBox.getChildren().add(search);
        searchBox.getChildren().add(btnArrow);
        searchBox.setPrefHeight(60);
        searchBox.setPrefWidth(370);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setSpacing(-20);

        HBox searchIcons = new HBox();
        searchIcons.setSpacing(10);
        searchIcons.getChildren().add(btnCoffee);
        searchIcons.getChildren().add(btnRest);
        searchIcons.getChildren().add(btnExit);
        searchIcons.getChildren().add(btnElev);
        searchIcons.getChildren().add(btnInfo);
        searchIcons.setAlignment(Pos.CENTER);

        VBox searchNear = new VBox();
        searchNear.setPrefWidth(370);
        searchNear.setPrefHeight(150);
        searchNear.setSpacing(5);
        searchNear.getChildren().add(searchBox);
        searchNear.getChildren().add(searchIcons);
        searchNear.setAlignment(Pos.CENTER);

        Label dir = new Label("Text Directions");
        dir.setPrefHeight(50);
        dir.setPrefWidth(510);
        dir.setTextFill(Color.WHITE);
        dir.setAlignment(Pos.CENTER);
        dir.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        dir.setPadding(new Insets(10, 10, 10, 10));

        VBox pathDir = new VBox();
        pathDir.getChildren().add(dir);
        pathDir.getChildren().add(tilDirections);
        pathDir.getChildren().add(textBox);
        pathDir.setAlignment(Pos.CENTER);
        pathDir.setPrefSize(510,700);
        pathDir.setSpacing(5);

        Label lblExl = new Label("Sanitation Requests");
        lblExl.setPrefHeight(50);
        lblExl.setPrefWidth(1170);
        lblExl.setTextFill(Color.WHITE);
        lblExl.setAlignment(Pos.CENTER);
        lblExl.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblExl.setPadding(new Insets(10, 10, 10, 10));

        VBox boxExl = new VBox();
        boxExl.getChildren().add(lblExl);
        boxExl.getChildren().add(sanitation);
        boxExl.setAlignment(Pos.CENTER_LEFT);
        boxExl.setPrefSize(1200,760);
        boxExl.setSpacing(5);


        JFXNodesList nodeListSearch = new JFXNodesList();
        JFXNodesList nodeListUser = new JFXNodesList();
        JFXNodesList nodeListRoute = new JFXNodesList();
        JFXNodesList nodeListExl = new JFXNodesList();
        JFXNodesList nodesListPath = new JFXNodesList();

        nodesListPath.addAnimatedNode(btnPath);
        nodesListPath.addAnimatedNode(pathBox);

        nodeListSearch.addAnimatedNode(btnSearch);
        nodeListSearch.addAnimatedNode(searchNear);
        nodeListSearch.setRotate(90);
        nodeListSearch.setSpacing(105);

        nodeListUser.addAnimatedNode(btnUser);
        nodeListUser.addAnimatedNode(userBox);
        nodeListUser.setRotate(90);
        nodeListUser.setSpacing(60);

        nodeListRoute.addAnimatedNode(btnRoute);
        nodeListRoute.addAnimatedNode(pathDir);
        nodeListRoute.setRotate(90);
        nodeListRoute.setSpacing(-75);

        nodeListExl.addAnimatedNode(btnExl);
        nodeListExl.addAnimatedNode(boxExl);
        nodeListExl.setRotate(90);
        nodeListExl.setSpacing(210);

        vboxDock.getChildren().add(nodeListUser);
        vboxDock.getChildren().add(nodeListSearch);
        vboxDock.getChildren().add(nodeListRoute);
        vboxDock.getChildren().add(nodesListPath);
        vboxDock.getChildren().add(nodeListExl);
    }

    @Override
    public void associateUserWithDirections(Location start, Location end) {
        FirebaseAPI.addDirectionsForUser(UserHelpers.getCurrentUser().getUsername(), start, end);
    }
}


