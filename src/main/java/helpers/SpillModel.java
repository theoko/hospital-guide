package helpers;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;

public class SpillModel extends RecursiveTreeObject<SpillModel> {


    private SimpleStringProperty loc;
    private SimpleStringProperty priority;
    private SimpleStringProperty status;
    private SimpleStringProperty tblBTN;

    public String getLoc() {
        return loc.get();
    }

    public SimpleStringProperty locProperty() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc.set(loc);
    }

    public String getPriority() {
        return priority.get();
    }

    public SimpleStringProperty priorityProperty() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority.set(priority);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getTblBTN() {
        return tblBTN.get();
    }

    public SimpleStringProperty tblBTNProperty() {
        return tblBTN;
    }

    public void setTblBTN(String tblBTN) {
        this.tblBTN.set(tblBTN);
    }

    public SpillModel(String loc, String priority){
        this.loc= new SimpleStringProperty(loc);
        this.priority= new SimpleStringProperty(priority);

        //this.tblBTN
    }
}

