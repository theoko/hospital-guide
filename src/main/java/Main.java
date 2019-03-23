import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class Main extends Application {

    @FXML
    JFXTextField emailField;

    @FXML
    JFXPasswordField passwordField;

    @FXML
    Label errorMessage;

    ScreenController screenController;
    Stage stage;

    final String TEMP_USERNAME = "admin@hospital.com";
    final String TEMP_PASSWORD = "admin";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.stage = primaryStage;

        // Initialize screens
        initializeScreens(primaryStage);

        // Activate login screen
        screenController.activate("login");

    }

    private void initializeScreens(Stage stage) throws Exception {

        // Initialize screen controller to switch between different scenes
        screenController = new ScreenController(stage);
        screenController.addScreen("login","/Login.fxml");
        screenController.addScreen("main","/Main.fxml");

    }

    private boolean authenticate(String username, String password) {
        return username.equals(TEMP_USERNAME) && password.equals(TEMP_PASSWORD);
    }

    public void handleLogin(ActionEvent actionEvent) throws Exception {

        // Button was clicked, check credentials
        // Username: emailField.getText()
        // Password: passwordField.getText()
        if(authenticate(emailField.getText(), passwordField.getText())) {
            errorMessage.setVisible(false);
            errorMessage.setManaged(false);

            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();

            initializeScreens(this.stage);
            screenController.activate("main");

        } else {
            errorMessage.setText("Invalid credentials");
            errorMessage.setManaged(true);
            errorMessage.setVisible(true);
        }

    }

}
