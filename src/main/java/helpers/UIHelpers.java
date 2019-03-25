package helpers;

import javafx.stage.Screen;

public class UIHelpers {

    public static final double MIN_WIDTH = 400.0;
    public static final double MIN_PIXELS = 600.0;

    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

}
