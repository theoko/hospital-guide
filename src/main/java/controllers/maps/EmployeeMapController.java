package controllers.maps;

import com.jfoenix.controls.*;
import controllers.ScreenController;
import controllers.search.SearchEngineController;
import controllers.search.TwoLocSearchPopupController;
import database.BookLocationTable;
import database.LocationTable;
import edu.wpi.cs3733.d19.teamMService.Main;
import floral.api.FloralApi;
import google.FirebaseAPI;
import helpers.Constants;
import helpers.api.APIHelper;
import helpers.DatabaseHelpers;
import helpers.UIHelpers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import helpers.UserHelpers;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import map.MapDisplay;
import map.PathFinder;
import messaging.TextMessenger;
import models.map.Location;
import models.room.Book;
import models.room.Room;
import models.search.SearchAPI;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import static controllers.ScreenController.mouseCnt;
import static controllers.ScreenController.secCnt;

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
    public AnchorPane WorkBooking;

    public JFXTextField search;
    public JFXTextField textNum;
    public ImageView imgText;
    public JFXButton btnText;
    public JFXToggleButton tglSpace;
    public JFXToggleButton tglZone;
    public JFXToggleButton tglConf;

    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;

    List<Room> workspacesAvailable;
    List<Room> workzonesAvailable;
    List<Room> confAvailable;

    ArrayList<Location> workspacesBooked = new ArrayList<>();
    HashMap<String, Location> workspaces;
    List<Book> workspacesCurrent;
    List<Book> workspacesCurrent1;
    ArrayList<Location> myWorkspaces = new ArrayList<>();
    ArrayList<Location> myWorkspaces1 = new ArrayList<>();

    ArrayList<Location> workzonesBooked = new ArrayList<>();
    HashMap<String, Location> workzones;
    List<Book> workzonesCurrent;
    List<Book> workzonesCurrent1;
    ArrayList<Location> myWorkzones = new ArrayList<>();
    ArrayList<Location> myWorkzones1 = new ArrayList<>();

    ArrayList<Location> confBooked = new ArrayList<>();
    HashMap<String, Location> conf;
    List<Book> confCurrent;
    List<Book> confCurrent1;
    ArrayList<Location> myConf = new ArrayList<>();
    ArrayList<Location> myConf1 = new ArrayList<>();

    Location enter;

    private final static double locRadius = 11;
    private final static Color nodeFill = Color.GREEN;
    private final static Color nodeOutline = Color.BLACK;
    private final static double locWidth = 2.0;

    LocalDate startDate;
    LocalDate endDate;

    LocalTime startTime;
    LocalTime endTime;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        SearchEngineController.setParentController(this);

        SearchAPI searchAPI = new SearchAPI(search, true);
        searchAPI.searchable();
        gesMap.setOnMouseMoved( (e) -> {
            mouseCnt += 1;
            secCnt = 0L;
            System.out.println(mouseCnt);
                }
        );

        MapDisplay.displayEmployee(this);
        initDirections();
    }

    @Override
    public void zoomOut() {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(3700);
                gesMap.centreOn(gesMap.targetPointAtViewportCentre());
                gesMap.animate(Duration.millis(1000)).zoomTo(.5, gesMap.targetPointAtViewportCentre());
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

    public void initializeZones() {
        workzones = LocationTable.getLocations();
        if (workzones != null) {
            for (Location ws : workzones.values()) {
                if (ws.getNodeType().equals(Constants.NodeType.WORK)) {
                    double xLoc = ws.getxCord();
                    double yLoc = ws.getyCord();
                    Circle circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                    circle.setId(ws.getNodeID());
                    circle.setStroke(nodeOutline);
                    circle.setStrokeWidth(locWidth);
                    circle.setOnMouseEntered(event -> {
                        circle.setRadius(locRadius + 6);
                        ScreenController.sceneThing.setCursor(Cursor.HAND);
                    });
                    circle.setOnMouseExited(event -> {
                        circle.setRadius(locRadius);
                        ScreenController.sceneThing.setCursor(Cursor.DEFAULT);
                    });
                    circle.setOnMouseClicked(event -> {
                        try {
                            event.consume();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    this.panMap.getChildren().add(circle);
                    ws.setNodeCircle(circle);
                }
            }
        }

        workzonesCurrent1 = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());

        if (workzonesCurrent1 != null) {
            for (Book b : workzonesCurrent1) {
                for (Location ws1 : workzones.values()) {
                    if (ws1.getNodeID().equals(b.getRoomID()) && ws1.getNodeType().equals(Constants.NodeType.WORK)) {
                        myWorkzones1.add(ws1);
                        break;
                    }
                }
            }
        }

        for (Location ws : myWorkzones1) {
            if (ws.getNodeType().equals(Constants.NodeType.WORK)) {
                Circle c = ws.getNodeCircle();
                c.setFill(Color.ORANGE);
                c.setOnMouseEntered(event -> {
                    c.setRadius(locRadius + 6);
                    ScreenController.sceneThing.setCursor(Cursor.HAND);
                });
                c.setOnMouseExited(event -> {
                    c.setRadius(locRadius);
                    ScreenController.sceneThing.setCursor(Cursor.DEFAULT);
                });
                c.setOnMouseClicked(Event -> {
                    try {
                        Event.consume();
                        for (Location ws1 : myWorkzones1) {
                            if (ws1.getxCord() == c.getCenterX() && ws1.getyCord() == c.getCenterY()) {
                                enter = ws1;
                                break;
                            }
                        }
                        ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    public void initializeSpaces() {
        workspaces = LocationTable.getLocations();
        if (workspaces != null) {
            for (Location ws : workspaces.values()) {
                if (ws.getNodeType().equals(Constants.NodeType.WRKT)) {
                    double xLoc = ws.getxCord();
                    double yLoc = ws.getyCord();
                    Circle circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                    circle.setStroke(nodeOutline);
                    circle.setStrokeWidth(locWidth);
                    circle.setId(ws.getNodeID());
                    circle.setOnMouseEntered(event -> {
                        circle.setRadius(locRadius + 6);
                        ScreenController.sceneThing.setCursor(Cursor.HAND);
                    });
                    circle.setOnMouseExited(event -> {
                        circle.setRadius(locRadius);
                        ScreenController.sceneThing.setCursor(Cursor.DEFAULT);
                    });
                    circle.setOnMouseClicked(event -> {
                        try {
                            event.consume();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    this.panMap.getChildren().add(circle);
                    ws.setNodeCircle(circle);
                }
            }
        }

        workspacesCurrent1 = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());

        if (workspacesCurrent1 != null) {
            for (Book b : workspacesCurrent1) {
                for (Location ws1 : workspaces.values()) {
                    if (ws1.getNodeID().equals(b.getRoomID()) && ws1.getNodeType().equals(Constants.NodeType.WRKT)) {
                        myWorkspaces1.add(ws1);
                        break;
                    }
                }
            }
        }

        for (Location ws : myWorkspaces1) {
            if (ws.getNodeType().equals(Constants.NodeType.WRKT)) {
                Circle c = ws.getNodeCircle();
                c.setFill(Color.ORANGE);
                c.setOnMouseEntered(event -> {
                    c.setRadius(locRadius + 6);
                    ScreenController.sceneThing.setCursor(Cursor.HAND);
                });
                c.setOnMouseExited(event -> {
                    c.setRadius(locRadius);
                    ScreenController.sceneThing.setCursor(Cursor.DEFAULT);
                });
                c.setOnMouseClicked(Event -> {
                    try {
                        Event.consume();
                        for (Location ws1 : myWorkspaces1) {
                            if (ws1.getxCord() == c.getCenterX() && ws1.getyCord() == c.getCenterY()) {
                                enter = ws1;
                                break;
                            }
                        }
                        ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        }
    }

    public void initializeConf() {
        conf = LocationTable.getLocations();
        if (conf != null) {
            for (Location ws : conf.values()) {
                if (ws.getNodeType().equals(Constants.NodeType.CONF) && ws.getFloor().equals(floor)) {
                    double xLoc = ws.getxCord();
                    double yLoc = ws.getyCord();
                    Circle circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                    circle.setStroke(nodeOutline);
                    circle.setStrokeWidth(locWidth);
                    circle.setId(ws.getNodeID());
                    circle.setOnMouseEntered(event -> {
                        circle.setRadius(locRadius + 6);
                        ScreenController.sceneThing.setCursor(Cursor.HAND);
                    });
                    circle.setOnMouseExited(event -> {
                        circle.setRadius(locRadius);
                        ScreenController.sceneThing.setCursor(Cursor.DEFAULT);
                    });
                    circle.setOnMouseClicked(event -> {
                        try {
                            event.consume();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    this.panMap.getChildren().add(circle);
                    ws.setNodeCircle(circle);
                }
            }
        }

        confCurrent1 = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());

        if (confCurrent1 != null) {
            for (Book b : confCurrent1) {
                for (Location ws1 : conf.values()) {
                    if (ws1.getNodeID().equals(b.getRoomID()) && ws1.getNodeType().equals(Constants.NodeType.CONF)) {
                        myConf1.add(ws1);
                        break;
                    }
                }
            }
        }

        for (Location ws : myConf1) {
            if (ws.getNodeType().equals(Constants.NodeType.CONF) && ws.getFloor().equals(floor)) {
                Circle c = ws.getNodeCircle();
                c.setFill(Color.ORANGE);
                c.setOnMouseEntered(event -> {
                    c.setRadius(locRadius + 6);
                    ScreenController.sceneThing.setCursor(Cursor.HAND);
                });
                c.setOnMouseExited(event -> {
                    c.setRadius(locRadius);
                    ScreenController.sceneThing.setCursor(Cursor.DEFAULT);
                });
                c.setOnMouseClicked(Event -> {
                    try {
                        Event.consume();
                        for (Location ws1 : myConf1) {
                            if (ws1.getxCord() == c.getCenterX() && ws1.getyCord() == c.getCenterY()) {
                                enter = ws1;
                                break;
                            }
                        }
                        ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        }
    }

    public void selectZone(ActionEvent event) {
        event.consume();
        if(getFloor().equals("4")) {
            if(tglZone.isSelected()) {
                this.clearMap();
                initializeZones();
                tglSpace.setSelected(false);
                tglConf.setSelected(false);
            }
            else {
                this.clearMap();
                this.showFloor(getFloor());
            }
        }
    }

    public void selectSpace(ActionEvent event) {
        event.consume();
        if(getFloor().equals("4")) {
            if(tglSpace.isSelected()) {
                this.clearMap();
                initializeSpaces();
                tglZone.setSelected(false);
                tglConf.setSelected(false);
            }
            else {
                this.clearMap();
                this.showFloor(getFloor());
            }
        }
    }

    public void selectConf(ActionEvent event) {
        event.consume();
        if (tglConf.isSelected()) {
            this.clearMap();
            initializeConf();
            tglZone.setSelected(false);
            tglSpace.setSelected(false);
        } else {
            this.clearMap();
            this.showFloor(getFloor());
        }
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
        UIHelpers.mouseHover(btnRoute);
        btnRoute.setTooltip(new Tooltip("Directions"));

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
        UIHelpers.mouseHover(btnRoom);
        btnRoom.setTooltip(new Tooltip("Book Room"));

        ImageView imgBookG = new ImageView();
        imgBookG.setImage(new Image("images/Icons/bookG.png"));
        imgBookG.setFitHeight(30);
        imgBookG.setFitWidth(30);
        imgBookG.setPreserveRatio(true);
        imgBookG.setPickOnBounds(true);

        JFXButton btnBookG = new JFXButton("", imgBookG);
        btnBookG.setAlignment(Pos.CENTER);
        btnBookG.setPrefWidth(60);
        btnBookG.setPrefHeight(60);
        btnBookG.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnBookG.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnBookG);

        ImageView imgBookT = new ImageView();
        imgBookT.setImage(new Image("images/Icons/bookT.png"));
        imgBookT.setFitHeight(30);
        imgBookT.setFitWidth(30);
        imgBookT.setPreserveRatio(true);
        imgBookT.setPickOnBounds(true);

        JFXButton btnBookT = new JFXButton("", imgBookT);
        btnBookT.setAlignment(Pos.CENTER);
        btnBookT.setPrefWidth(60);
        btnBookT.setPrefHeight(60);
        btnBookT.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnBookT.setTextOverrun(OverrunStyle.CLIP);

        UIHelpers.btnRaise(btnBookT);

        ImageView imgSpace = new ImageView();
        imgSpace.setImage(new Image("images/Icons/space.png"));
        imgSpace.setFitHeight(30);
        imgSpace.setFitWidth(30);
        imgSpace.setPreserveRatio(true);
        imgSpace.setPickOnBounds(true);

        JFXButton btnSpace = new JFXButton("", imgSpace);
        btnSpace.setAlignment(Pos.CENTER);
        btnSpace.setPrefWidth(60);
        btnSpace.setPrefHeight(60);
        btnSpace.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnSpace.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgZone = new ImageView();
        imgZone.setImage(new Image("images/Icons/zone.png"));
        imgZone.setFitHeight(30);
        imgZone.setFitWidth(30);
        imgZone.setPreserveRatio(true);
        imgZone.setPickOnBounds(true);

        JFXButton btnZone = new JFXButton("", imgZone);
        btnZone.setAlignment(Pos.CENTER);
        btnZone.setPrefWidth(60);
        btnZone.setPrefHeight(60);
        btnZone.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnZone.setTextOverrun(OverrunStyle.CLIP);

        ImageView imgConf = new ImageView();
        imgConf.setImage(new Image("images/Icons/conf.png"));
        imgConf.setFitHeight(30);
        imgConf.setFitWidth(30);
        imgConf.setPreserveRatio(true);
        imgConf.setPickOnBounds(true);

        JFXButton btnConf = new JFXButton("", imgConf);
        btnConf.setAlignment(Pos.CENTER);
        btnConf.setPrefWidth(60);
        btnConf.setPrefHeight(60);
        btnConf.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnConf.setTextOverrun(OverrunStyle.CLIP);

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
        UIHelpers.mouseHover(btnCal);
        btnCal.setTooltip(new Tooltip("Calendar"));

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
        UIHelpers.mouseHover(btnExl);
        btnExl.setTooltip(new Tooltip("Service Requests"));

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
        UIHelpers.mouseHover(btnComp1);
        btnComp1.setTooltip(new Tooltip("IT Services"));
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
        UIHelpers.mouseHover(btnFlo);
        btnFlo.setTooltip(new Tooltip("Flower Request"));
        btnFlo.setOnMouseClicked(event -> {
            try {
                ScreenController.popUp(Constants.Routes.TWO_NODE_SEARCH);
                TwoLocSearchPopupController.setOnSendClick(EmployeeMapController::runFloralAPI);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

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
        btnSign.setOnMouseClicked(event -> {
            runLanguageAPI();
            /*
            try {
                ScreenController.popUp(Constants.Routes.TWO_NODE_SEARCH);
                TwoLocSearchPopupController.setOnSendClick(EmployeeMapController::runLanguageAPI);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            */
        });
        UIHelpers.mouseHover(btnSign);
        btnSign.setTooltip(new Tooltip("Interpreter"));
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
        UIHelpers.mouseHover(btnLock);
        btnLock.setTooltip(new Tooltip("Security"));

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
        UIHelpers.mouseHover(btnDrug);
        btnDrug.setTooltip(new Tooltip("Prescription"));
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
        UIHelpers.mouseHover(btnAv);
        btnAv.setTooltip(new Tooltip("Audio/Visual"));
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
        UIHelpers.mouseHover(btnIn);
        btnIn.setTooltip(new Tooltip("Internal Transport"));
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
        UIHelpers.mouseHover(btnGift);
        btnGift.setTooltip(new Tooltip("Gift"));
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
        UIHelpers.mouseHover(btnOut);
        btnOut.setTooltip(new Tooltip("Patient Information"));
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
        UIHelpers.mouseHover(btnInfo);
        btnInfo.setTooltip(new Tooltip("Information"));
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
        UIHelpers.mouseHover(btnCoffee);
        btnCoffee.setTooltip(new Tooltip("Food"));
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
        UIHelpers.mouseHover(btnRest);
        btnRest.setTooltip(new Tooltip("Rest Rooms"));
        btnRest.setTooltip(new Tooltip("RestRoom"));
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
        UIHelpers.mouseHover(btnExit);
        btnExit.setTooltip(new Tooltip("Exit"));
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
        UIHelpers.mouseHover(btnElev);
        btnElev.setTooltip(new Tooltip("Elevators"));
        btnElev.setTooltip(new Tooltip("Elevator"));
        btnElev.setOnMouseClicked((e) -> {
            PathFinder.printByType(this, map, Constants.NodeType.ELEV, Constants.NodeType.STAI);
        });

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
        btnInfo1.setOnMouseClicked((e) -> {
            PathFinder.printByType(this, map, Constants.NodeType.INFO);
        });

        UIHelpers.mouseHover(btnInfo1);
        btnInfo1.setTooltip(new Tooltip("Information"));
        UIHelpers.btnRaise(btnInfo1);

        ImageView imgFood = new ImageView();
        imgFood.setImage(new Image("images/Icons/food.png"));
        imgFood.setFitHeight(30);
        imgFood.setFitWidth(30);
        imgFood.setPreserveRatio(true);
        imgFood.setPickOnBounds(true);

        JFXButton btnFood = new JFXButton("",imgFood);
        btnFood.setAlignment(Pos.CENTER);
        btnFood.setPrefWidth(60);
        btnFood.setPrefHeight(60);
        btnFood.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnFood.setTextOverrun(OverrunStyle.CLIP);
        btnFood.setOnMouseClicked( event -> {
            try {
                ScreenController.popUp(Constants.Routes.TWO_NODE_SEARCH);
                TwoLocSearchPopupController.setOnSendClick(EmployeeMapController::runFoodAPI);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        ImageView imgBaby = new ImageView();
        imgBaby.setImage(new Image("images/Icons/baby.png"));
        imgBaby.setFitHeight(30);
        imgBaby.setFitWidth(30);
        imgBaby.setPreserveRatio(true);
        imgBaby.setPickOnBounds(true);

        JFXButton btnBaby = new JFXButton("",imgBaby);
        btnBaby.setAlignment(Pos.CENTER);
        btnBaby.setPrefWidth(60);
        btnBaby.setPrefHeight(60);
        btnBaby.setStyle("-fx-background-color: #022D5A;" + "-fx-background-radius: 30;");
        btnBaby.setTextOverrun(OverrunStyle.CLIP);
        btnBaby.setOnMouseClicked( event -> {
            //add here
        });

        btnLogOut.setStyle("-fx-background-radius: 30;" );
        btnLogOut.setStyle("-fx-background-radius: 30;");
        btnLogOut.setButtonType(JFXButton.ButtonType.RAISED);
        imgLogOut.setImage(new Image("images/Icons/signout.png"));
        UIHelpers.mouseHover(btnLogOut);
        btnLogOut.setTooltip(new Tooltip("Log Out"));
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


        HBox boxSpace = new HBox();
        boxSpace.getChildren().add(btnSpace);
        boxSpace.getChildren().add(tglSpace);
        boxSpace.setPrefHeight(60);
        boxSpace.setPrefWidth(150);
        boxSpace.setAlignment(Pos.CENTER);
        boxSpace.setSpacing(-5);

        HBox boxZone = new HBox();
        boxZone.getChildren().add(btnZone);
        boxZone.getChildren().add(tglZone);
        boxZone.setPrefHeight(60);
        boxZone.setPrefWidth(150);
        boxZone.setAlignment(Pos.CENTER);
        boxZone.setSpacing(-5);

        HBox boxConf = new HBox();
        boxConf.getChildren().add(btnConf);
        boxConf.getChildren().add(tglConf);
        boxConf.setPrefHeight(60);
        boxConf.setPrefWidth(150);
        boxConf.setAlignment(Pos.CENTER);
        boxConf.setSpacing(-5);

        HBox hboxWork = new HBox();
        hboxWork.getChildren().add(boxSpace);
        hboxWork.getChildren().add(boxZone);
        hboxWork.getChildren().add(boxConf);
        hboxWork.setPrefHeight(60);
        hboxWork.setPrefWidth(500);
        hboxWork.setAlignment(Pos.CENTER);
        hboxWork.setSpacing(5);

        VBox boxWork = new VBox();
        boxWork.getChildren().add(hboxWork);
        boxWork.getChildren().add(WorkBooking);
        boxWork.setAlignment(Pos.CENTER_RIGHT);
        boxWork.setPrefSize(150,150);
        boxWork.setSpacing(-15);

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

        Label lblLock = new Label("Security Service Request");
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
        JFXNodesList nodesListBook = new JFXNodesList();
        JFXNodesList nodesListWork = new JFXNodesList();

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

        nodesListBook.addAnimatedNode(btnBookT);
        nodesListBook.addAnimatedNode(boxRoom);
        nodesListBook.setRotate(95);
        nodesListBook.setSpacing(240);

        nodesListWork.addAnimatedNode(btnBookG);
        nodesListWork.addAnimatedNode(boxWork);
        nodesListWork.setRotate(75);
        nodesListWork.setSpacing(-150);

        nodeListCal.addAnimatedNode(btnCal);
        nodeListCal.addAnimatedNode(boxCal);
        nodeListCal.setRotate(95);
        nodeListCal.setSpacing(240);

        nodesListComp.addAnimatedNode(btnComp1);
        nodesListComp.addAnimatedNode(boxComp);
        nodesListComp.setRotate(65);
        nodesListComp.setSpacing(330);

        nodesListFlo.addAnimatedNode(btnFlo);
        // nodesListFlo.addAnimatedNode(boxFlo);
        nodesListFlo.setRotate(70);
        nodesListFlo.setSpacing(320);

        nodesListInt.addAnimatedNode(btnSign);
        // nodesListInt.addAnimatedNode(boxInt);
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

        VBox boxBook = new VBox();
        boxBook.getChildren().add(nodesListWork);
        boxBook.getChildren().add(nodesListBook);
        boxBook.setAlignment(Pos.CENTER_LEFT);
        boxBook.setPrefSize(60,150);
        boxBook.setSpacing(5);

        nodeListRoom.addAnimatedNode(btnRoom);
        nodeListRoom.addAnimatedNode(boxBook);
        nodeListRoom.setRotate(90);
        nodeListRoom.setSpacing(-30);

        HBox boxReqMain = new HBox();
        boxReqMain.setAlignment(Pos.CENTER);
        boxReqMain.setPrefSize(130,700);
        boxReqMain.setSpacing(5);

        VBox boxReq = new VBox();
        boxReq.getChildren().add(nodesListComp);
        boxReq.getChildren().add(nodesListFlo);
        boxReq.getChildren().add(nodesListInt);
        boxReq.getChildren().add(nodesListLock);
        boxReq.getChildren().add(nodesListDrug);
        boxReq.getChildren().add(nodesListAv);
        boxReq.getChildren().add(nodesListInTr);
        boxReq.getChildren().add(nodesListOut);
        boxReq.setAlignment(Pos.CENTER);
        boxReq.setPrefSize(60,700);
        boxReq.setSpacing(5);

        VBox boxReq1 = new VBox();
        boxReq1.getChildren().add(nodesListGift);
        boxReq1.getChildren().add(nodesListInfo);
        boxReq1.getChildren().add(btnFood);
        boxReq1.getChildren().add(btnBaby);
        boxReq1.setAlignment(Pos.CENTER);
        boxReq1.setPrefSize(60,700);
        boxReq1.setSpacing(5);

        boxReqMain.getChildren().add(boxReq1);
        boxReqMain.getChildren().add(boxReq);

        nodeListExl.addAnimatedNode(btnExl);
        nodeListExl.addAnimatedNode(boxReqMain);
        nodeListExl.setRotate(150);
        nodeListExl.setSpacing(-170);

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

    /**
     * Runs Food request API
     */
    public static void runFoodAPI() {
        String startLocID = APIHelper.getStartLocID();
        String endLocID = APIHelper.getEndLocID();
        FoodRequestTeamI.API api = new FoodRequestTeamI.API();
        try {
            api.run(10, 10, 800, 600, "/css/jfoenix-components.css", endLocID, startLocID);
        } catch (Exception exception) {
            System.out.println("Failed to run Food API");
            exception.printStackTrace();
        }
    }

    /**
     * Runs Floral request API
     */
    public static void runFloralAPI() {
        String endLocID = APIHelper.getEndLocID();
        FloralApi floralApi = new FloralApi();
        try {
            floralApi.run(10, 10, "/css/jfoenix-components.css", endLocID);
        } catch (Exception exception){
            System.out.println("Failed to run Floral API");
            exception.printStackTrace();
        }
    }

    /**
     * Runs Language request API
     */
    public static void runLanguageAPI() {
        Main api = new Main();
        try {
            api.run(10, 10, 800, 600, "/css/jfoenix-components.css", "destination");
        } catch (Exception exception) {
            System.out.println("Failed to run Language API");
            exception.printStackTrace();
        }
    }

    @Override
    public void associateUserWithDirections(Location start, Location end) {
        FirebaseAPI.addDirectionsForUser(UserHelpers.getCurrentUser().getUsername(), start, end);
    }

    @Override
    public void btnReturn_Click(MouseEvent mouseEvent) throws Exception {
        ScreenController.logOut(btnLogOut);
        FirebaseAPI.setCaller(null);
        ScreenController.activate(Constants.Routes.LOGIN);
    }

    public void btnSearch(MouseEvent event) {
        event.consume();
        if(tglSpace.isSelected()) {
            if(datStartDay != null && datEndDay != null && datStartTime != null && datEndTime != null) {
                startDate = datStartDay.getValue();
                endDate = datEndDay.getValue();
                startTime = datStartTime.getValue();
                endTime = datEndTime.getValue();

                workspacesAvailable = LocationTable.checkAvailabilityByTime(
                        DatabaseHelpers.getDateTime(startDate, startTime),
                        DatabaseHelpers.getDateTime(endDate, endTime)
                );

                for(Room ws2 : workspacesAvailable) {
                    System.out.println(ws2.toString());
                }

                for(Location ws : workspaces.values()) {
                    boolean isBooked = true;
                    for(Room ws1 : workspacesAvailable) {
                        if(ws1.getRoomID().equals(ws.getNodeID())) {
                            isBooked = false;
                            break;
                        }
                    }
                    if(isBooked) {
                        workspacesBooked.add(ws);
                    }
                }

                for (Location ws : workspacesBooked) {
                    if (ws.getNodeType().equals(Constants.NodeType.WRKT)) {
                        System.out.println("works");
                        Circle c = ws.getNodeCircle();
                        c.setFill(Color.RED);
                    }
                }

                workspacesCurrent = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());

                for(Book b : workspacesCurrent) {
                    for(Location ws1 : workspaces.values()) {
                        if(ws1.getNodeID().equals(b.getRoomID()) && ws1.getNodeType().equals(Constants.NodeType.WRKT)) {
                            myWorkspaces.add(ws1);
                            break;
                        }
                    }
                }

                for (Location ws : myWorkspaces) {
                    if (ws.getNodeType().equals(Constants.NodeType.WRKT)) {
                        Circle c = ws.getNodeCircle();
                        c.setFill(Color.ORANGE);
                        c.setOnMouseClicked(Event -> {
                            try {
                                Event.consume();
                                for(Location ws1 : myWorkspaces) {
                                    if(ws1.getxCord() == c.getCenterX() && ws1.getyCord() == c.getCenterY()) {
                                        enter = ws1;
                                        break;
                                    }
                                }
                                ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

                for (Room ws : workspacesAvailable) {
                    Location ls = new Location("",0,0,"","", Constants.NodeType.HALL,"","");
                    for(Location ls1 : workspaces.values()) {
                        if(ls1.getNodeID().equals(ws.getRoomID())) {
                            ls = ls1;
                            break;
                        }
                    }
                    if (ls.getNodeType().equals(Constants.NodeType.WRKT)) {
                        Circle c = ls.getNodeCircle();
                        c.setFill(Color.YELLOW);
                        c.setOnMouseClicked(Event -> {
                            try {
                                for(Room ws1 : workspacesAvailable) {
                                    Location ls1 = map.getLocation(ws1.getRoomID());
                                    if(ls1.getxCord() == c.getCenterX() && ls1.getyCord() == c.getCenterY()) {
                                        enter = ls1;
                                        break;
                                    }
                                }

                                ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        }
        else if(tglZone.isSelected()) {
            if(datStartDay != null && datEndDay != null && datStartTime != null && datEndTime != null) {
                startDate = datStartDay.getValue();
                endDate = datEndDay.getValue();
                startTime = datStartTime.getValue();
                endTime = datEndTime.getValue();

                workzonesAvailable = LocationTable.checkAvailabilityByTime(
                        DatabaseHelpers.getDateTime(startDate, startTime),
                        DatabaseHelpers.getDateTime(endDate, endTime)
                );

                for(Room ws2 : workzonesAvailable) {
                    System.out.println(ws2.toString());
                }

                for(Location ws : workzones.values()) {
                    boolean isBooked = true;
                    for(Room ws1 : workzonesAvailable) {
                        if(ws1.getRoomID().equals(ws.getNodeID())) {
                            isBooked = false;
                            break;
                        }
                    }
                    if(isBooked) {
                        workzonesBooked.add(ws);
                    }
                }

                for (Location ws : workzonesBooked) {
                    if (ws.getNodeType().equals(Constants.NodeType.WORK)) {
                        Circle c = ws.getNodeCircle();
                        c.setFill(Color.RED);
                    }
                }

                workzonesCurrent = BookLocationTable.getBookingsForUser(UserHelpers.getCurrentUser());

                for(Book b : workzonesCurrent) {
                    for(Location ws1 : workzones.values()) {
                        if(ws1.getNodeID().equals(b.getRoomID()) && ws1.getNodeType().equals(Constants.NodeType.WORK)) {
                            myWorkzones.add(ws1);
                            break;
                        }
                    }
                }

                for (Location ws : myWorkzones) {
                    if (ws.getNodeType().equals(Constants.NodeType.WORK)) {
                        Circle c = ws.getNodeCircle();
                        c.setFill(Color.ORANGE);
                        c.setOnMouseClicked(Event -> {
                            try {
                                Event.consume();
                                for(Location ws1 : myWorkzones) {
                                    if(ws1.getxCord() == c.getCenterX() && ws1.getyCord() == c.getCenterY()) {
                                        enter = ws1;
                                        break;
                                    }
                                }
                                ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }

                for (Room ws : workzonesAvailable) {
                    Location ls = new Location("",0,0,"","", Constants.NodeType.HALL,"","");
                    for(Location ls1 : workzones.values()) {
                        if(ls1.getNodeID().equals(ws.getRoomID())) {
                            ls = ls1;
                            break;
                        }
                    }
                    if (ls.getNodeType().equals(Constants.NodeType.WORK)) {
                        Circle c = ls.getNodeCircle();
                        c.setFill(Color.YELLOW);
                        c.setOnMouseClicked(Event -> {
                            try {
                                Event.consume();
                                for(Room ws1 : workzonesAvailable) {
                                    Location ls1 = map.getLocation(ws1.getRoomID());
                                    if(ls1.getxCord() == c.getCenterX() && ls1.getyCord() == c.getCenterY()) {
                                        enter = ls1;
                                        break;
                                    }
                                }
                                ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                }
            }
        }

    }
}