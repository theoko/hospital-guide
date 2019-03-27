package helpers;

public class DatabaseHelpers {

    /**
     * Translates data from constant.NodeType to an int to put into database
     */
    public static String enumToString(Constants.NodeType type){
        switch (type) {
            case BATH:
                return "BATH";
            case CONF:
                return "CONF";
            case DEPT:
                return "DEPT";
            case ELEV:
                return "ELEV";
            case EXIT:
                return "EXIT";
            case HALL:
                return "HALL";
            case INFO:
                return "INFO";
            case LABS:
                return "LABS";
            case REST:
                return "REST";
            case RETL:
                return "RETL";
            case SERV:
                return "SERV";
            default:
                return "STAI";
        }
    }
    /**
     * Translates data from int to an enum
     */
    public static Constants.NodeType stringToEnum(String str){
        switch (str) {
            case "BATH":
                return Constants.NodeType.BATH;
            case "CONF":
                return Constants.NodeType.CONF;
            case "DEPT":
                return Constants.NodeType.DEPT;
            case "ELEV":
                return Constants.NodeType.ELEV;
            case "EXIT":
                return Constants.NodeType.EXIT;
            case "HALL":
                return Constants.NodeType.HALL;
            case "INFO":
                return Constants.NodeType.INFO;
            case "LABS":
                return Constants.NodeType.LABS;
            case "REST":
                return Constants.NodeType.REST;
            case "RETL":
                return Constants.NodeType.RETL;
            case "SERV":
                return Constants.NodeType.SERV;
            default:
                return Constants.NodeType.STAI;
        }
    }

}
