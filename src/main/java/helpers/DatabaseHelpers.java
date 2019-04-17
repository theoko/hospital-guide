package helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class DatabaseHelpers {

    public static boolean isValidFloor(String floor) {
        ArrayList<String> validFloorList = new ArrayList<String>(
                Arrays.asList("1", "2", "3", "G", "L1", "L2")
        );
        return validFloorList.contains(floor);
    }

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

    public static ZonedDateTime getCalDateTime(String date, String time) {
        //LocalDateTime parsedDate = LocalDateTime.parse(date + "T" + time + ":00", DateTimeFormatter.ofPattern(Constants.calDateFormat));
        String str = date + "T" + time + "-04:00";
        ZonedDateTime zdt = ZonedDateTime.parse(str);
        // ZonedDateTime zonedDateTime = ZonedDateTime.parse("2011-12-03T10:15:30+01:00");
        return zdt;
    }


}
