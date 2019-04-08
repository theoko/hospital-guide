package controllers;

import com.jfoenix.controls.JFXButton;
import database.UserTable;
import helpers.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.user.User;

import java.util.List;

public class CreateUserController {

    public JFXButton btnBookSelected;

    // Table that displays available rooms
    public TableView<User> tblUsers;

    // Columns
    public TableColumn<User, String> tblUserID;
    public TableColumn<User, String> tblUserName;
    public TableColumn<User, String> tblUserPassword;
    public TableColumn<User, String> tblUserType;
    public JFXButton btnLogout;

    ObservableList<User> users = FXCollections.observableArrayList();

    public void initialize() {

        initUsers();
    }

    List<User> currUsers;
    public void initUsers() {

        tblUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        tblUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        tblUserPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        tblUserType.setCellValueFactory(new PropertyValueFactory<>("userType"));
        tblUsers.setItems(users);
        currUsers = UserTable.getUsers();
        populateUserTable(currUsers);
    }

    /**
     * Populates the users table
     * @param usersA
     */
    private void populateUserTable(List<User> usersA) {
        users.clear();
        users.addAll(usersA);
        tblUsers.refresh();
    }

    public void refreshTable(MouseEvent event) {
        event.consume();
        users.clear();
        List<User> usersA = UserTable.getUsers();
        if(usersA != null)
            users.addAll(usersA);
        tblUsers.refresh();
    }

    public void addUser(MouseEvent event) {
        event.consume();
        try {
            ScreenController.popUp(Constants.Routes.USER_POPUP);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public void updateUser(MouseEvent event) {
        event.consume();
        User selected = tblUsers.getSelectionModel().getSelectedItem();
        try {
            ScreenController.popUpUser(Constants.Routes.EDIT_POPUP, selected);
        } catch(Exception exception) {
            exception.printStackTrace();
        }
    }

    public void deleteUser(MouseEvent event) {
        event.consume();
        User selected = tblUsers.getSelectionModel().getSelectedItem();
        System.out.println(selected.toString());
        UserTable.deleteUser(selected);
        users.clear();
        List<User> usersA = UserTable.getUsers();
        if(usersA != null)
            users.addAll(usersA);
        tblUsers.refresh();
    }


    public final void logOut(MouseEvent event) throws Exception{
        event.consume();
        ScreenController.logOut(btnLogout);
        ScreenController.activate(Constants.Routes.LOGIN);
    }
}