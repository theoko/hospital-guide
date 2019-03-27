package helpers;

public class DatabaseHelpers {

    /**
     * Translates data from constant.NodeType to an int to put into database
     */
    public static int enumToInt(Constants.NodeType num){
        switch (num) {
            case BATH:
                return 0;
            case CONF:
                return 1;
            case DEPT:
                return 2;
            case ELEV:
                return 3;
            case EXIT:
                return 4;
            case HALL:
                return 5;
            case INFO:
                return 6;
            case LABS:
                return 7;
            case REST:
                return 8;
            case RETL:
                return 9;
            case SERV:
                return 10;
            default:
                return -1;
        }
    }
    /**
     * Translates data from int to an enum
     */
    public static Constants.NodeType intToEnum(int num){
        switch (num) {
            case 0:
                return Constants.NodeType.BATH;
            case 1:
                return Constants.NodeType.CONF;
            case 2:
                return Constants.NodeType.DEPT;
            case 3:
                return Constants.NodeType.ELEV;
            case 4:
                return Constants.NodeType.EXIT;
            case 5:
                return Constants.NodeType.HALL;
            case 6:
                return Constants.NodeType.INFO;
            case 7:
                return Constants.NodeType.LABS;
            case 8:
                return Constants.NodeType.REST;
            case 9:
                return Constants.NodeType.RETL;
            case 10:
                return Constants.NodeType.SERV;
            default:
                return null;
        }
    }

}
