package helpers;

public class Constants {

    /**
     * DATABASE
     */
    public static String DB_NAME = "teamF";

    public static String USERS_TABLE = "USERS";
    public static String EMPLOYEE_TABLE = "EMPLOYEE";
    public static String CUSTODIAN_TABLE = "CUSTODIAN";
    public static String ADMIN_TABLE = "ADMIN";
    public static String NODES_TABLE = "NODES";
    public static String EDGES_TABLE = "EDGES";

    public static String DB_PROJECTION = "projection";
    public static String DB_RELATION = "relation";
    public static String DB_CONDITION = "condition";

    /**
     * FILES
     */
    public static String CSV_NODES = "/data/nodes.csv";
    public static String CSV_EDGES = "/data/edges.csv";


    /**
     * UI
     */
    public static String SETTINGS_BUTTON_TOOLTIP = "Access user settings";
    public static String DOWNLOAD_BUTTON_TOOLTIP = "Download the map";
    public static String LOGOUT_BUTTON_TOOLTIP = "Logout";
    public static String EXIT_BUTTON_TOOLTIP = "Exit";
    public static final boolean START_FIRST = true;
    public static final boolean END_FIRST = false;
    public static final boolean SELECTED = true;
    public static final boolean DESELECTED = false;

    public enum NodeType {
        BATH, CONF, DEPT, ELEV, EXIT, HALL, INFO, LABS, REST, RETL, SERV, STAI
    }

    public enum Auth {
        USER, EMPLOYEE, ADMIN
    }
}
