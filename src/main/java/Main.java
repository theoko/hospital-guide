import controllers.ScreenController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    static ScreenController screenController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        screenController = new ScreenController(primaryStage);

    }

}
