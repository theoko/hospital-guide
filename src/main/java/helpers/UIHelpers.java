package helpers;

import javafx.stage.Screen;

public class UIHelpers {

    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

}
