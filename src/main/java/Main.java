import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.awt.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Returns the screen resolution
     * @return an array containing the width and the screen height
     */
    private double[] getScreenResolution() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        double[] dimensions = new double[2];
        dimensions[0] = screenSize.getWidth();
        dimensions[1] = screenSize.getHeight();

        return dimensions;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        /*FlowPane main = new FlowPane();
        main.setVgap(20);
        main.setHgap(20);

        JFXHamburger h1 = new JFXHamburger();
        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(h1);
        burgerTask.setRate(-1);
        h1.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            burgerTask.setRate(burgerTask.getRate() * -1);
            burgerTask.play();
        });

        main.getChildren().add(h1);
        */

        // Load map with tabs
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));

        /*StackPane pane = new StackPane();
        pane.getChildren().add(main);
        StackPane.setMargin(main, new Insets(60));
        pane.setStyle("-fx-background-color:WHITE");
        */

        double[] screenResolution = getScreenResolution();

        final Scene scene = new Scene(root, (int) screenResolution[0], (int) screenResolution[1]);
        scene.getStylesheets().add(Main.class.getResource("/css/jfoenix-components.css").toExternalForm());
        primaryStage.setTitle("App");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);

        primaryStage.show();

    }
}
