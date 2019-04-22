package controllers.booking;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import controllers.ScreenController;
import database.BookWorkspaceTable;
import database.LocationTable;
import database.WorkspaceTable;
import helpers.Constants;
import helpers.DatabaseHelpers;
import helpers.UserHelpers;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import models.map.Location;
import models.map.Workspace;
import models.room.Book;
import models.room.Room;
import javafx.geometry.Point2D;
import java.awt.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class WorkspaceMapController {

    private final static double xShift = 20;
    private final static double yShift = -262;
    private final static double scale = 0.44;
    private final static double locRadius = 5.5;
    private final static Color nodeFill = Color.WHITE;
    private final static Color nodeOutline = Color.BLACK;
    private final static double locWidth = 2.0;
    protected double sceneX, sceneY;
    protected double translateX, translateY;

    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;

    public JFXButton btnReturn;
    Workspace enter;

    LocalDate startDate;
    LocalDate endDate;

    LocalTime startTime;
    LocalTime endTime;

    ArrayList<Circle> themCircles = new ArrayList<>();

    List<Workspace> workspacesAvailable;
    ArrayList<Workspace> workspacesBooked = new ArrayList<>();
    HashMap<String, Workspace> workspaces;
    List<Book> workspacesCurrent;
    List<Book> workspacesCurrent1;
    ArrayList<Workspace> myWorkspaces = new ArrayList<>();
    ArrayList<Workspace> myWorkspaces1 = new ArrayList<>();

    @FXML
    AnchorPane workS;

    public void initialize() {

        HashMap<String, Workspace> lstWorkspaces = WorkspaceTable.getWorkspaces();
        if (lstWorkspaces != null) {
            for (Workspace ws : lstWorkspaces.values()) {
                if (ws.getNodeType() != null) {
                    double xLoc = scaleX(ws.getxCord());
                    double yLoc = scaleY(ws.getyCord());
                    Circle circle = new Circle(xLoc, yLoc, locRadius, nodeFill);
                    circle.setStroke(nodeOutline);
                    circle.setStrokeWidth(locWidth);
                    circle.setOnMouseClicked(event -> {
                        try {
                            event.consume();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                    workS.getChildren().add(circle);
                    themCircles.add(circle);
                }
            }
        }

        workspaces = WorkspaceTable.getWorkspaces();

        workspacesCurrent1 = BookWorkspaceTable.getBookingsForUser(UserHelpers.getCurrentUser());

        if (workspacesCurrent1 != null) {
            for (Book b : workspacesCurrent1) {
                for (Workspace ws1 : workspaces.values()) {
                    if (ws1.getNodeID().equals(b.getRoomID())) {
                        myWorkspaces1.add(ws1);
                        break;
                    }
                }
            }
        }

        for (Workspace ws : myWorkspaces1) {
            if (ws.getNodeType() != null) {
                double xLoc = scaleX(ws.getxCord());
                double yLoc = scaleY(ws.getyCord());
                for (Circle c : themCircles) {
                    if (c.getCenterX() == xLoc && c.getCenterY() == yLoc) {
                        c.setFill(Color.ORANGE);
                        c.setOnMouseClicked(Event -> {
                            try {
                                Event.consume();
                                for(Workspace ws1 : myWorkspaces1) {
                                    if(scaleX(ws1.getxCord()) == c.getCenterX() && scaleY(ws1.getyCord()) == c.getCenterY()) {
                                        enter = ws1;
                                        break;
                                    }
                                }
                                ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                    }
                }
            }
        }
    }

    public void floorOneMapOnMousePressed(MouseEvent event) {

        // Handle onMousePressed event
        sceneX = event.getSceneX();
        sceneY = event.getSceneY();

        translateX = ((AnchorPane) event.getSource()).getTranslateX();
        translateY = ((AnchorPane) event.getSource()).getTranslateY();
    }

    public final void floorOneMapOnMouseDragged(MouseEvent event) {

        // Handle onMouseDragged event
        double offsetX = event.getSceneX() - sceneX;
        double offsetY = event.getSceneY() - sceneY;
        double newTranslateX = translateX + offsetX;
        double newTranslateY = translateY + offsetY;

        ((AnchorPane) event.getSource()).setTranslateX(newTranslateX);
        ((AnchorPane) event.getSource()).setTranslateY(newTranslateY);
    }

    public final void floorOneMapScroll(ScrollEvent event) {
        ((AnchorPane) event.getSource()).setTranslateX(((AnchorPane) event.getSource()).getTranslateX() + event.getDeltaX());
        ((AnchorPane) event.getSource()).setTranslateY(((AnchorPane) event.getSource()).getTranslateY() + event.getDeltaY());

        if (((AnchorPane) event.getSource()).getTranslateX() <= -500) {
            ((AnchorPane) event.getSource()).setTranslateX(-500);
        }

        if (((AnchorPane) event.getSource()).getTranslateX() >= 200) {
            ((AnchorPane) event.getSource()).setTranslateX(200);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() <= -600) {
            ((AnchorPane) event.getSource()).setTranslateY(-600);
        }

        if (((AnchorPane) event.getSource()).getTranslateY() >= 250) {
            ((AnchorPane) event.getSource()).setTranslateY(250);
        }
    }

    public final void floorOneMapZoom(ZoomEvent event) {
        ((AnchorPane) event.getSource()).setScaleX(((AnchorPane) event.getSource()).getScaleX() * event.getZoomFactor());
        ((AnchorPane) event.getSource()).setScaleY(((AnchorPane) event.getSource()).getScaleY() * event.getZoomFactor());
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.45 && ((AnchorPane) event.getSource()).getScaleY() <= 0.45) {
            ((AnchorPane) event.getSource()).setScaleX(0.45);
            ((AnchorPane) event.getSource()).setScaleY(0.45);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 8 && ((AnchorPane) event.getSource()).getScaleY() >= 8) {
            ((AnchorPane) event.getSource()).setScaleX(8);
            ((AnchorPane) event.getSource()).setScaleY(8);
        }
    }

    public final void floorOneMapZoomDone(ZoomEvent event) {
        if(((AnchorPane) event.getSource()).getScaleX() <= 0.55 && ((AnchorPane) event.getSource()).getScaleY() <= 0.55) {
            ((AnchorPane) event.getSource()).setScaleX(0.55);
            ((AnchorPane) event.getSource()).setScaleY(0.55);
        }
        if(((AnchorPane) event.getSource()).getScaleX() >= 4 && ((AnchorPane) event.getSource()).getScaleY() >= 4) {
            ((AnchorPane) event.getSource()).setScaleX(4);
            ((AnchorPane) event.getSource()).setScaleY(4);
        }
    }

    public static double scaleX(double x) {
        return (x - xShift) * scale;
    }

    public static double scaleY(double y) {
        return (y - yShift) * scale;
    }

    public void btnSearch(MouseEvent event) {
        event.consume();
        if(datStartDay != null && datEndDay != null && datStartTime != null && datEndTime != null) {
            startDate = datStartDay.getValue();
            endDate = datEndDay.getValue();
            startTime = datStartTime.getValue();
            endTime = datEndTime.getValue();

            workspacesAvailable = WorkspaceTable.checkAvailabilityByTime(
                    DatabaseHelpers.getDateTime(startDate, startTime),
                    DatabaseHelpers.getDateTime(endDate, endTime)
            );

            for(Workspace ws : workspaces.values()) {
                boolean isBooked = true;
                for(Workspace ws1 : workspacesAvailable) {
                    if(ws1 == ws) {
                        isBooked = false;
                        break;
                    }
                }
                if(isBooked) {
                    workspacesBooked.add(ws);
                }
            }

            for (Workspace ws : workspacesBooked) {
                if (ws.getNodeType() != null) {
                    double xLoc = scaleX(ws.getxCord());
                    double yLoc = scaleY(ws.getyCord());
                    for (Circle c : themCircles) {
                        if (c.getCenterX() == xLoc && c.getCenterY() == yLoc) {
                            c.setFill(Color.RED);
                            break;
                        }
                    }
                }
            }

            workspacesCurrent = BookWorkspaceTable.getBookingsForUser(UserHelpers.getCurrentUser());

            for(Book b : workspacesCurrent) {
                for(Workspace ws1 : workspaces.values()) {
                    if(ws1.getNodeID().equals(b.getRoomID())) {
                        myWorkspaces.add(ws1);
                        break;
                    }
                }
            }

            for (Workspace ws : myWorkspaces) {
                if (ws.getNodeType() != null) {
                    double xLoc = scaleX(ws.getxCord());
                    double yLoc = scaleY(ws.getyCord());
                    for (Circle c : themCircles) {
                        if (c.getCenterX() == xLoc && c.getCenterY() == yLoc) {
                            c.setFill(Color.ORANGE);
                            c.setOnMouseClicked(Event -> {
                                try {
                                    Event.consume();
                                    for(Workspace ws1 : myWorkspaces) {
                                        if(scaleX(ws1.getxCord()) == c.getCenterX() && scaleY(ws1.getyCord()) == c.getCenterY()) {
                                            enter = ws1;
                                            break;
                                        }
                                    }
                                    ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            break;
                        }
                    }
                }
            }

            for (Workspace ws : workspacesAvailable) {
                if (ws.getNodeType() != null) {
                    double xLoc = scaleX(ws.getxCord());
                    double yLoc = scaleY(ws.getyCord());
                    for (Circle c : themCircles) {
                        if (c.getCenterX() == xLoc && c.getCenterY() == yLoc) {
                            c.setFill(Color.YELLOW);
                            c.setOnMouseClicked(Event -> {
                                try {
                                    Event.consume();
                                    for(Workspace ws1 : workspacesAvailable) {
                                        if(scaleX(ws1.getxCord()) == c.getCenterX() && scaleY(ws1.getyCord()) == c.getCenterY()) {
                                            enter = ws1;
                                            break;
                                        }
                                    }
                                    ScreenController.popUp(Constants.Routes.WORKSPACE_POPUP, enter, c, startTime, startDate, endTime, endDate);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            });
                            break;
                        }
                    }
                }
            }
        }
    }

    public void logOut(MouseEvent event) {
        event.consume();
        ScreenController.logOut(btnReturn);
        try {
            ScreenController.activate(Constants.Routes.WELCOME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
