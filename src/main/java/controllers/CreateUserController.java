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
import models.user.User;

import java.util.List;

public class CreateUserController extends AdminMapController{

    public JFXButton btnBookSelected;

    // Table that displays available rooms
    public TableView<User> tblUsers;

    // Columns
    public TableColumn<User, String> tblUserID;
    public TableColumn<User, String> tblUserName;
    public TableColumn<User, String> tblUserPassword;
    public TableColumn<User, String> tblUserType;

    ObservableList<User> users = FXCollections.observableArrayList();

    public void initialize() {

        initUsers();
    }

    List<User> currUsers;

    public void initUsers() {
        tblUserID.setCellValueFactory(new PropertyValueFactory<>("User ID"));
        tblUserName.setCellValueFactory(new PropertyValueFactory<>("User Name"));
        tblUserPassword.setCellValueFactory(new PropertyValueFactory<>("User Password"));
        tblUserType.setCellValueFactory(new PropertyValueFactory<>("User Type"));
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
        tblUsers.refresh();
    }

    public void updateUser(MouseEvent event) {
        event.consume();
        try {
            ScreenController.popUp(Constants.Routes.USER_POPUP);
        } catch(Exception exception) {
            exception.printStackTrace();
        }

    }
}
