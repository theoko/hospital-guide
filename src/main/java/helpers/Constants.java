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

    /**
     * UI
     */
    public static String SETTINGS_BUTTON_TOOLTIP = "Access user settings";
    public static String LOGOUT_BUTTON_TOOLTIP = "Logout";

    public static enum NodeType {
        BATH, CONF, DEPT, ELEV, EXIT, HALL, INFO, LABS, REST, RETL, SERV, STAI
    }

}
