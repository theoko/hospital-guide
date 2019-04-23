package controllers.maps;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import controllers.search.SearchEngineController;
import google.FirebaseAPI;
import helpers.Constants;
import helpers.UIHelpers;
import javafx.fxml.FXML;
import helpers.UserHelpers;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Path;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import map.MapDisplay;
import messaging.TextMessenger;
import models.search.SearchAPI;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMapController extends MapController {

    public VBox vboxDock;
    public ImageView imgLogOut;
    public JFXButton btnLogOut;
    public AnchorPane tilDirections;
    public AnchorPane roomBooking;
    public AnchorPane IT;
    public AnchorPane Flo;
    public AnchorPane Int;
    public AnchorPane Lock;
    public AnchorPane Cal;
    public AnchorPane Drug;
    public AnchorPane Av;
    public AnchorPane InTr;
    public AnchorPane Out;
    public AnchorPane Gift;
    public AnchorPane Info;

    public JFXTextField search;
    public JFXTextField textNum;
    public ImageView imgText;
    public JFXButton btnText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        SearchEngineController.setParentController(this);

        /*SearchAPI searchAPI = new SearchAPI(search, true);
        searchAPI.searchable();*/

        MapDisplay.displayEmployee(this);
        initDirections();
    }

    @Override
    public void zoomOut() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(3700);
                gesMap.reset();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        t.setDaemon(true);
        t.start();
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
    public void showFloor(String newFloor) {
        super.showFloorHelper(newFloor);
        MapDisplay.displayEmployee(this);
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

        UIHelpers.btnRaise(btnArrow);

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

        ImageView imgRoom = new ImageView();
        imgRoom.setImage(new Image("images/Icons/room.png"));
        imgRoom.setFitHeight(30);
        imgRoom.setFitWidth(30);
        imgRoom.setPreserveRatio(true);
        imgRoom.setPickOnBounds(true);

        JFXButton btnRoom = new JFXButton("", imgRoom);
        btnRoom.setAlignment(Pos.CENTER);
        btnRoom.setPrefWidth(60);
        btnRoom.setPrefHeight(60);
        btnRoom.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnRoom.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgCal = new ImageView();
        imgCal.setImage(new Image("images/Icons/cald.png"));
        imgCal.setFitHeight(30);
        imgCal.setFitWidth(30);
        imgCal.setPreserveRatio(true);
        imgCal.setPickOnBounds(true);

        JFXButton btnCal = new JFXButton("", imgCal);
        btnCal.setAlignment(Pos.CENTER);
        btnCal.setPrefWidth(60);
        btnCal.setPrefHeight(60);
        btnCal.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnCal.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgExl = new ImageView();
        imgExl.setImage(new Image("images/Icons/excl.png"));
        imgExl.setFitHeight(30);
        imgExl.setFitWidth(30);
        imgExl.setPreserveRatio(true);
        imgExl.setPickOnBounds(true);

        JFXButton btnExl = new JFXButton("", imgExl);
        btnExl.setAlignment(Pos.CENTER);
        btnExl.setPrefWidth(60);
        btnExl.setPrefHeight(60);
        btnExl.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnExl.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgComp1 = new ImageView();
        imgComp1.setImage(new Image("images/Icons/comp.png"));
        imgComp1.setFitHeight(30);
        imgComp1.setFitWidth(30);
        imgComp1.setPreserveRatio(true);
        imgComp1.setPickOnBounds(true);

        JFXButton btnComp1 = new JFXButton("", imgComp1);
        btnComp1.setAlignment(Pos.CENTER);
        btnComp1.setPrefWidth(60);
        btnComp1.setPrefHeight(60);
        btnComp1.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnComp1.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnComp1);


        ImageView imgFlo = new ImageView();
        imgFlo.setImage(new Image("images/Icons/florist.png"));
        imgFlo.setFitHeight(30);
        imgFlo.setFitWidth(30);
        imgFlo.setPreserveRatio(true);
        imgFlo.setPickOnBounds(true);

        JFXButton btnFlo = new JFXButton("", imgFlo);
        btnFlo.setAlignment(Pos.CENTER);
        btnFlo.setPrefWidth(60);
        btnFlo.setPrefHeight(60);
        btnFlo.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnFlo.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnFlo);

        ImageView imgSign = new ImageView();
        imgSign.setImage(new Image("images/Icons/sign.png"));
        imgSign.setFitHeight(30);
        imgSign.setFitWidth(30);
        imgSign.setPreserveRatio(true);
        imgSign.setPickOnBounds(true);

        JFXButton btnSign = new JFXButton("", imgSign);
        btnSign.setAlignment(Pos.CENTER);
        btnSign.setPrefWidth(60);
        btnSign.setPrefHeight(60);
        btnSign.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnSign.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnSign);

        ImageView imgLock = new ImageView();
        imgLock.setImage(new Image("images/Icons/lock.png"));
        imgLock.setFitHeight(30);
        imgLock.setFitWidth(30);
        imgLock.setPreserveRatio(true);
        imgLock.setPickOnBounds(true);

        JFXButton btnLock = new JFXButton("", imgLock);
        btnLock.setAlignment(Pos.CENTER);
        btnLock.setPrefWidth(60);
        btnLock.setPrefHeight(60);
        btnLock.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnLock.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnLock);

        ImageView imgDrug = new ImageView();
        imgDrug.setImage(new Image("images/Icons/drug.png"));
        imgDrug.setFitHeight(30);
        imgDrug.setFitWidth(30);
        imgDrug.setPreserveRatio(true);
        imgDrug.setPickOnBounds(true);

        JFXButton btnDrug = new JFXButton("", imgDrug);
        btnDrug.setAlignment(Pos.CENTER);
        btnDrug.setPrefWidth(60);
        btnDrug.setPrefHeight(60);
        btnDrug.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnDrug.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnDrug);

        ImageView imgAv = new ImageView();
        imgAv.setImage(new Image("images/Icons/av.png"));
        imgAv.setFitHeight(30);
        imgAv.setFitWidth(30);
        imgAv.setPreserveRatio(true);
        imgAv.setPickOnBounds(true);

        JFXButton btnAv = new JFXButton("", imgAv);
        btnAv.setAlignment(Pos.CENTER);
        btnAv.setPrefWidth(60);
        btnAv.setPrefHeight(60);
        btnAv.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnAv.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnAv);

        ImageView imgIn = new ImageView();
        imgIn.setImage(new Image("images/Icons/in.png"));
        imgIn.setFitHeight(30);
        imgIn.setFitWidth(30);
        imgIn.setPreserveRatio(true);
        imgIn.setPickOnBounds(true);

        JFXButton btnIn = new JFXButton("", imgIn);
        btnIn.setAlignment(Pos.CENTER);
        btnIn.setPrefWidth(60);
        btnIn.setPrefHeight(60);
        btnIn.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnIn.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnIn);

        ImageView imgGift = new ImageView();
        imgGift.setImage(new Image("images/Icons/gift.png"));
        imgGift.setFitHeight(30);
        imgGift.setFitWidth(30);
        imgGift.setPreserveRatio(true);
        imgGift.setPickOnBounds(true);

        JFXButton btnGift = new JFXButton("", imgGift);
        btnGift.setAlignment(Pos.CENTER);
        btnGift.setPrefWidth(60);
        btnGift.setPrefHeight(60);
        btnGift.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnGift.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnGift);

        ImageView imgOut = new ImageView();
        imgOut.setImage(new Image("images/Icons/out.png"));
        imgOut.setFitHeight(30);
        imgOut.setFitWidth(30);
        imgOut.setPreserveRatio(true);
        imgOut.setPickOnBounds(true);

        JFXButton btnOut = new JFXButton("", imgOut);
        btnOut.setAlignment(Pos.CENTER);
        btnOut.setPrefWidth(60);
        btnOut.setPrefHeight(60);
        btnOut.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnOut.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnOut);

        ImageView imgInfo = new ImageView();
        imgInfo.setImage(new Image("images/Icons/info.png"));
        imgInfo.setFitHeight(30);
        imgInfo.setFitWidth(30);
        imgInfo.setPreserveRatio(true);
        imgInfo.setPickOnBounds(true);

        JFXButton btnInfo = new JFXButton("", imgInfo);
        btnInfo.setAlignment(Pos.CENTER);
        btnInfo.setPrefWidth(60);
        btnInfo.setPrefHeight(60);
        btnInfo.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnInfo.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnInfo);


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

        ImageView imgInfo1 = new ImageView();
        imgInfo1.setImage(new Image("images/Icons/info.png"));
        imgInfo1.setFitHeight(30);
        imgInfo1.setFitWidth(30);
        imgInfo1.setPreserveRatio(true);
        imgInfo1.setPickOnBounds(true);

        JFXButton btnInfo1 = new JFXButton("",imgInfo1);
        btnInfo1.setAlignment(Pos.CENTER);
        btnInfo1.setPrefWidth(60);
        btnInfo1.setPrefHeight(60);
        btnInfo1.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnInfo1.setTextOverrun(OverrunStyle.CLIP);


        UIHelpers.btnRaise(btnInfo1);

        btnLogOut.setStyle("-fx-background-radius: 30;" );
        btnLogOut.setStyle("-fx-background-radius: 30;");
        btnLogOut.setButtonType(JFXButton.ButtonType.RAISED);
        imgLogOut.setImage(new Image("images/Icons/signout.png"));

        UIHelpers.btnRaise(btnLogOut);

        btnText.setStyle("-fx-background-radius: 30;" );
        btnText.setButtonType(JFXButton.ButtonType.RAISED);
        imgText.setImage(new Image("images/Icons/text.png"));

       UIHelpers.btnRaise(btnText);




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
        searchIcons.getChildren().add(btnInfo1);
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
        dir.setPrefWidth(400);
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
        pathDir.setPrefSize(400,700);
        pathDir.setSpacing(5);

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

        Label lblCal = new Label("My Bookings Calendar");
        lblCal.setPrefHeight(50);
        lblCal.setPrefWidth(1200);
        lblCal.setTextFill(Color.WHITE);
        lblCal.setAlignment(Pos.CENTER);
        lblCal.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblCal.setPadding(new Insets(10, 10, 10, 10));

        VBox boxCal = new VBox();
        boxCal.getChildren().add(lblCal);
        boxCal.getChildren().add(Cal);
        boxCal.setAlignment(Pos.CENTER_LEFT);
        boxCal.setPrefSize(1200,760);
        boxCal.setSpacing(5);

        Label lblComp = new Label("IT Service Request");
        lblComp.setPrefHeight(50);
        lblComp.setPrefWidth(1200);
        lblComp.setTextFill(Color.WHITE);
        lblComp.setAlignment(Pos.CENTER);
        lblComp.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblComp.setPadding(new Insets(10, 10, 10, 10));

        VBox boxComp = new VBox();
        boxComp.getChildren().add(lblComp);
        boxComp.getChildren().add(IT);
        boxComp.setAlignment(Pos.CENTER_LEFT);
        boxComp.setPrefSize(1200,700);
        boxComp.setSpacing(5);

        Label lblFlo = new Label("Florist Service Request");
        lblFlo.setPrefHeight(50);
        lblFlo.setPrefWidth(1200);
        lblFlo.setTextFill(Color.WHITE);
        lblFlo.setAlignment(Pos.CENTER);
        lblFlo.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblFlo.setPadding(new Insets(10, 10, 10, 10));

        VBox boxFlo = new VBox();
        boxFlo.getChildren().add(lblFlo);
        boxFlo.getChildren().add(Flo);
        boxFlo.setAlignment(Pos.CENTER_LEFT);
        boxFlo.setPrefSize(1200,700);
        boxFlo.setSpacing(5);

        Label lblInt = new Label("Interpreter Service Request");
        lblInt.setPrefHeight(50);
        lblInt.setPrefWidth(1200);
        lblInt.setTextFill(Color.WHITE);
        lblInt.setAlignment(Pos.CENTER);
        lblInt.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblFlo.setPadding(new Insets(10, 10, 10, 10));

        VBox boxInt = new VBox();
        boxInt.getChildren().add(lblInt);
        boxInt.getChildren().add(Int);
        boxInt.setAlignment(Pos.CENTER_LEFT);
        boxInt.setPrefSize(1200,700);
        boxInt.setSpacing(5);

        Label lblLock = new Label("Interpreter Service Request");
        lblLock.setPrefHeight(50);
        lblLock.setPrefWidth(1200);
        lblLock.setTextFill(Color.WHITE);
        lblLock.setAlignment(Pos.CENTER);
        lblLock.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblLock.setPadding(new Insets(10, 10, 10, 10));

        VBox boxLock = new VBox();
        boxLock.getChildren().add(lblLock);
        boxLock.getChildren().add(Lock);
        boxLock.setAlignment(Pos.CENTER_LEFT);
        boxLock.setPrefSize(1200,700);
        boxLock.setSpacing(5);

        Label lblDrug = new Label("Prescription Service Request");
        lblDrug.setPrefHeight(50);
        lblDrug.setPrefWidth(1200);
        lblDrug.setTextFill(Color.WHITE);
        lblDrug.setAlignment(Pos.CENTER);
        lblDrug.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblDrug.setPadding(new Insets(10, 10, 10, 10));

        VBox boxDrug = new VBox();
        boxDrug.getChildren().add(lblDrug);
        boxDrug.getChildren().add(Drug);
        boxDrug.setAlignment(Pos.CENTER_LEFT);
        boxDrug.setPrefSize(1200,700);
        boxDrug.setSpacing(5);

        Label lblAv = new Label("Audio/Visual Service Request");
        lblAv.setPrefHeight(50);
        lblAv.setPrefWidth(1200);
        lblAv.setTextFill(Color.WHITE);
        lblAv.setAlignment(Pos.CENTER);
        lblAv.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblAv.setPadding(new Insets(10, 10, 10, 10));

        VBox boxAv = new VBox();
        boxAv.getChildren().add(lblAv);
        boxAv.getChildren().add(Av);
        boxAv.setAlignment(Pos.CENTER_LEFT);
        boxAv.setPrefSize(1200,700);
        boxAv.setSpacing(5);

        Label lblInTr = new Label("Internal Transport Service Request");
        lblInTr.setPrefHeight(50);
        lblInTr.setPrefWidth(1200);
        lblInTr.setTextFill(Color.WHITE);
        lblInTr.setAlignment(Pos.CENTER);
        lblInTr.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblInTr.setPadding(new Insets(10, 10, 10, 10));

        VBox boxInTr = new VBox();
        boxInTr.getChildren().add(lblInTr);
        boxInTr.getChildren().add(InTr);
        boxInTr.setAlignment(Pos.CENTER_LEFT);
        boxInTr.setPrefSize(1200,700);
        boxInTr.setSpacing(5);

        Label lblOut = new Label("External Transport Service Request");
        lblOut.setPrefHeight(50);
        lblOut.setPrefWidth(1200);
        lblOut.setTextFill(Color.WHITE);
        lblOut.setAlignment(Pos.CENTER);
        lblOut.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblOut.setPadding(new Insets(10, 10, 10, 10));

        VBox boxOut = new VBox();
        boxOut.getChildren().add(lblOut);
        boxOut.getChildren().add(Out);
        boxOut.setAlignment(Pos.CENTER_LEFT);
        boxOut.setPrefSize(1200,700);
        boxOut.setSpacing(5);

        Label lblGift = new Label("Gift Service Request");
        lblGift.setPrefHeight(50);
        lblGift.setPrefWidth(1200);
        lblGift.setTextFill(Color.WHITE);
        lblGift.setAlignment(Pos.CENTER);
        lblGift.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblGift.setPadding(new Insets(10, 10, 10, 10));

        VBox boxGift = new VBox();
        boxGift.getChildren().add(lblGift);
        boxGift.getChildren().add(Gift);
        boxGift.setAlignment(Pos.CENTER_LEFT);
        boxGift.setPrefSize(1200,700);
        boxGift.setSpacing(5);

        Label lblInfo = new Label("Patient Information Service Request");
        lblInfo.setPrefHeight(50);
        lblInfo.setPrefWidth(1200);
        lblInfo.setTextFill(Color.WHITE);
        lblInfo.setAlignment(Pos.CENTER);
        lblInfo.setStyle("-fx-background-color: radial-gradient(radius 120%, #022D5A, derive(#022D5A, -60%), derive(#022D5A, 60%));" +
                "-fx-background-radius: 30;" +
                "-fx-font-size: 24;" +
                "-fx-font-weight: BOLD");
        lblInfo.setPadding(new Insets(10, 10, 10, 10));

        VBox boxInfo = new VBox();
        boxInfo.getChildren().add(lblInfo);
        boxInfo.getChildren().add(Info);
        boxInfo.setAlignment(Pos.CENTER_LEFT);
        boxInfo.setPrefSize(1200,700);
        boxInfo.setSpacing(5);

        JFXNodesList nodeListSearch = new JFXNodesList();
        JFXNodesList nodeListUser = new JFXNodesList();
        JFXNodesList nodeListRoute = new JFXNodesList();
        JFXNodesList nodeListRoom = new JFXNodesList();
        JFXNodesList nodeListCal = new JFXNodesList();
        JFXNodesList nodeListExl = new JFXNodesList();
        JFXNodesList nodesListComp = new JFXNodesList();
        JFXNodesList nodesListFlo = new JFXNodesList();
        JFXNodesList nodesListInt = new JFXNodesList();
        JFXNodesList nodesListLock = new JFXNodesList();
        JFXNodesList nodesListDrug = new JFXNodesList();
        JFXNodesList nodesListAv = new JFXNodesList();
        JFXNodesList nodesListInTr = new JFXNodesList();
        JFXNodesList nodesListOut = new JFXNodesList();
        JFXNodesList nodesListGift = new JFXNodesList();
        JFXNodesList nodesListInfo = new JFXNodesList();

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
        nodeListRoute.setSpacing(-135);

        nodeListRoom.addAnimatedNode(btnRoom);
        nodeListRoom.addAnimatedNode(boxRoom);
        nodeListRoom.setRotate(95);
        nodeListRoom.setSpacing(240);

        nodeListCal.addAnimatedNode(btnCal);
        nodeListCal.addAnimatedNode(boxCal);
        nodeListCal.setRotate(95);
        nodeListCal.setSpacing(240);

        nodesListComp.addAnimatedNode(btnComp1);
        nodesListComp.addAnimatedNode(boxComp);
        nodesListComp.setRotate(65);
        nodesListComp.setSpacing(330);

        nodesListFlo.addAnimatedNode(btnFlo);
        nodesListFlo.addAnimatedNode(boxFlo);
        nodesListFlo.setRotate(70);
        nodesListFlo.setSpacing(320);

        nodesListInt.addAnimatedNode(btnSign);
        nodesListInt.addAnimatedNode(boxInt);
        nodesListInt.setRotate(75);
        nodesListInt.setSpacing(310);

        nodesListLock.addAnimatedNode(btnLock);
        nodesListLock.addAnimatedNode(boxLock);
        nodesListLock.setRotate(80);
        nodesListLock.setSpacing(300);

        nodesListDrug.addAnimatedNode(btnDrug);
        nodesListDrug.addAnimatedNode(boxDrug);
        nodesListDrug.setRotate(85);
        nodesListDrug.setSpacing(290);

        nodesListAv.addAnimatedNode(btnAv);
        nodesListAv.addAnimatedNode(boxAv);
        nodesListAv.setRotate(90);
        nodesListAv.setSpacing(280);

        nodesListInTr.addAnimatedNode(btnIn);
        nodesListInTr.addAnimatedNode(boxInTr);
        nodesListInTr.setRotate(100);
        nodesListInTr.setSpacing(280);

        nodesListOut.addAnimatedNode(btnOut);
        nodesListOut.addAnimatedNode(boxOut);
        nodesListOut.setRotate(105);
        nodesListOut.setSpacing(290);

        nodesListGift.addAnimatedNode(btnGift);
        nodesListGift.addAnimatedNode(boxGift);
        nodesListGift.setRotate(110);
        nodesListGift.setSpacing(310);

        nodesListInfo.addAnimatedNode(btnInfo);
        nodesListInfo.addAnimatedNode(boxInfo);
        nodesListInfo.setRotate(115);
        nodesListInfo.setSpacing(330);

        VBox boxReq = new VBox();
        boxReq.getChildren().add(nodesListComp);
        boxReq.getChildren().add(nodesListFlo);
        boxReq.getChildren().add(nodesListInt);
        boxReq.getChildren().add(nodesListLock);
        boxReq.getChildren().add(nodesListDrug);
        boxReq.getChildren().add(nodesListAv);
        boxReq.getChildren().add(nodesListInTr);
        boxReq.getChildren().add(nodesListOut);
        boxReq.getChildren().add(nodesListGift);
        boxReq.getChildren().add(nodesListInfo);
        boxReq.setAlignment(Pos.CENTER);
        boxReq.setPrefSize(60,700);
        boxReq.setSpacing(5);

        nodeListExl.addAnimatedNode(btnExl);
        nodeListExl.addAnimatedNode(boxReq);
        nodeListExl.setRotate(150);
        nodeListExl.setSpacing(-220);

        vboxDock.getChildren().add(nodeListUser);
        vboxDock.getChildren().add(nodeListSearch);
        vboxDock.getChildren().add(nodeListRoute);
        vboxDock.getChildren().add(nodeListRoom);
        vboxDock.getChildren().add(nodeListCal);
        vboxDock.getChildren().add(nodeListExl);

        Platform.runLater(() -> {
            // Add listener for commands
            FirebaseAPI.setCaller(EmployeeMapController.this);
            FirebaseAPI.checkForCommands(UserHelpers.getCurrentUser().getUsername());
        });

    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnLogOut);
        FirebaseAPI.setCaller(null);
        ScreenController.activate(Constants.Routes.LOGIN);
    }
}
