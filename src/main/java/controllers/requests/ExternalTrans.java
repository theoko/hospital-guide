package controllers.requests;

public class ExternalTrans {
    private int externalReqID;
    private TransType type;
    private String time;
    private String name;

    public enum TransType{
        CAB, UBER, LIMO, BUS, PBUS
    }

    public ExternalTrans(int externalReqID, String name, TransType type, String time){
        this.externalReqID = externalReqID;
        this.name = name;
        this.type = type;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExternalReqID() {
        return externalReqID;
    }

    public void setExternalReqID(int externalReqID) {
        this.externalReqID = externalReqID;
    }

    public String getType() {
        switch(type) {
            case CAB:
                return "Cab";
            case UBER:
                return "Uber";
            case LIMO:
                return "Limo";
            case BUS:
                return "Bus";
            default:
                return "Party Bus";
        }
    }

    public void setType(TransType type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
