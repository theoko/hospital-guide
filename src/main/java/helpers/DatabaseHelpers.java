package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class DatabaseHelpers {

    /**
     * Translates data from constant.NodeType to an int to put into database
     */
    public static String enumToString(Constants.NodeType type) {
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
            case STAI:
                return "STAI";
            case WORK:
                return "WORK";
            case WRKT:
                return "WRKT";
            default:
                return null;
        }
    }

    /**
     * Translates data from int to an enum
     */
    public static Constants.NodeType stringToEnum(String str) {
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
            case "STAI":
                return Constants.NodeType.STAI;
            case "WORK":
                return Constants.NodeType.WORK;
            case "WRKT":
                return Constants.NodeType.WRKT;
            default:
                return null;
        }
    }

    /**
     * Constructs a SimpleDateFormat object by concatenating the LocalDate and LocalTime objects
     * passed as arguments
     *
     * @param date
     * @param time
     * @return
     */
    public static String getDateTime(LocalDate date, LocalTime time) {

        try {

            Date parsedDate = new SimpleDateFormat(Constants.dateFormat).parse(
                    date.toString() + " " + time.toString() + ":00"
            );

            return new SimpleDateFormat(Constants.dateFormat).format(parsedDate);

        } catch (ParseException e) {
            e.printStackTrace();

            return null;
        }

    }


}
