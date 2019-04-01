package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.scene.input.InputMethodEvent;

public class RoomBookingController {
    public JFXDatePicker datStartDay;
    public JFXDatePicker datEndDay;
    public JFXTimePicker datStartTime;
    public JFXTimePicker datEndTime;
    public JFXComboBox cmbRoom;

    public void datChanged(InputMethodEvent inputMethodEvent) {
        System.out.println("Hi");
    }
}
