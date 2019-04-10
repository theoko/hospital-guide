package helpers;

public class Constants {

    /**
     * DATABASE
     */
    public static String DB_NAME = "teamF";

    public static String USERS_TABLE = "USERS";
    public static String LOCATION_TABLE = "LOCATION";
    public static String EDGES_TABLE = "EDGES";
    public static String ROOM_TABLE = "ROOM";
    public static String BOOK_LOCATION_TABLE = "BOOKLOCATION";
    public static String BOOK_WORKSPACE_TABLE = "BOOKWORKSPACE";

    public static String SANITATION_TABLE = "SANITATION";
    public static String WORKSPACE_TABLE = "WORKSPACE";

    public static String DB_PROJECTION = "projection";
    public static String DB_RELATION = "relation";
    public static String DB_CONDITION = "condition";

    /**
     * FILES
     */
    public static String CSV_NODES = "/data/nodes.csv";
    public static String CSV_EDGES = "/data/edges.csv";
    public static String CSV_WORKSPACES = "/data/workspaceZone3.csv";


    /**
     * UI
     */

    /**
     * Labels
     */

    /**
     * Buttons
     */

    public static String DOWNLOAD_BUTTON_TEXT = "Download";
    public static String BOOKING_BUTTON_TEXT = "Booking";

    public static String SETTINGS_BUTTON_TOOLTIP = "Access user settings";
    public static String DOWNLOAD_BUTTON_TOOLTIP = "Download the map";
    public static String LOGOUT_BUTTON_TOOLTIP = "Logout";
    public static String EXIT_BUTTON_TOOLTIP = "Exit";

    /**
     * Styles
     */
    public static String defaultButtonStyles = "-fx-text-fill: WHITE; -fx-font-size: 24px;";
    public static final boolean START_FIRST = true;
    public static final boolean END_FIRST = false;
    public static final boolean SELECTED = true;
    public static final boolean DESELECTED = false;

    /**
     * General types
     */
    public static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    /**
     * Enums
     */
    public enum NodeType {
        BATH, CONF, DEPT, ELEV, EXIT, HALL, INFO, LABS, REST, RETL, SERV, STAI, WORK, WRKT
    }

    public enum Auth {
        USER, EMPLOYEE, ADMIN, CUSTODIAN
    }

    public enum Routes {
        LOGO, WELCOME, LOGIN, USER_MAP, EMPLOYEE_MAP, CUSTODIAN_MAP, ADMIN_MAP, USER_INFO, EMPLOYEE_INFO,
        CUSTODIAN_INFO, EDIT_LOCATION, SANITATION_REQUEST, DIRECTIONS, BOOKING_WINDOW, DOWNLOAD, DOWNLOADED,
        CREATE_USER, USER_POPUP, EDIT_POPUP, REQUESTS, WORKSPACE, WORKSPACE_POPUP, IT, PERSCRIPTION, INTERPRETER, INTERNAL_TRANS, GIFT_STORE,
        FLOURIST, SECURITY, VISUAL_AUDIO, EXTERNAL_TRANS, PATIENT_INFO
    }
}
