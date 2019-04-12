package controllers.requests;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import controllers.ScreenController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import models.requests.GIFT;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class GiftStoreController {

    public JFXButton submitBTN;
    public JFXComboBox cmbItem;
    public JFXComboBox cmbSize;
    public JFXTextField txtRecipient;
    public JFXTextField txtMessage;
    public JFXTextField txtSender;
    public String FLOWERS1;
    public String CHOCOLATE1;
    public String STUFFEDBEAR1;
    public String SMALL;
    public String MEDIUM;
    public String LARGE;
    public Text price;

    public TableView<GIFT> tblGifts;
    public TableColumn<GIFT, String> tblRequestID;
    public TableColumn<GIFT, String> tblGift;
    public TableColumn<GIFT, String> tblType;
    public TableColumn<GIFT, String> tblSize;
    public TableColumn<GIFT, String> tblNote;
    public TableColumn<GIFT, String> tblRecipient;
    public TableColumn<GIFT, String> tblSender;
    public int requestID = 1;
    public JFXComboBox cmbSize1;
    public JFXComboBox cmbItem1;
    boolean A =false;
    boolean B= false;

    ObservableList<GIFT> giftsREGS = FXCollections.observableArrayList();

    private static DecimalFormat df2 = new DecimalFormat(".##");



    public void initialize() {

        initGift();

        cmbSize1.valueProperty().addListener(((observable, oldValue, newValue) -> {
            A=true;
            calculate();
            enableBtns();
        }));
        cmbItem1.valueProperty().addListener(((observable, oldValue, newValue) -> {
            B=true;
            calculate();
            enableBtns();
        }));
    }



//    DocumentListener listener = new DocumentListener() {
//        @Override
//        public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
//        @Override
//        public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
//
//        @Override
//        public void changedUpdate(DocumentEvent e) {
//            boolean canEnable = true;
////            for (JFXTextField tf : listFields) {
////                if (tf.getText().isEmpty()) {
////                    canEnable = false;
////                }
//            //}

    @FXML
    private void enableBtns(){

            boolean canEnable=((!txtSender.getText().equals("")&&!txtRecipient.getText().equals("")&&!txtMessage.getText().equals("")))&&(A&&B);



            submitBTN.setDisable(!canEnable);
        }


    public void calculate(){
        if (A&&B){
            double cost=0.0;

            String gift=(String) cmbItem1.getValue();
            String size=(String) cmbSize1.getValue();

            if(gift.equals("Flowers")){
                cost=2.3;
            }else if (gift.equals("Chocolate")){
                cost=1.9;
            }else if (gift.equals("Stuffed_Bear")){
                cost=4.3;
            }
            if(size.equals("Large")){
                cost=cost*3.0;
            }else if (size.equals("Medium")){
                cost=cost*2.0;
            }else if(size.equals("Small")){
                cost=cost*1.0;
            }


            price.setText("$"+df2.format(cost));

        }else{
            price.setText("$0.00");
        }
    }
    public void sendRequest(MouseEvent event) {

        event.consume();
        String recipient = txtRecipient.getText();
        String note = txtMessage.getText();
        String sender = txtSender.getText();

        String type = (String) cmbItem1.getValue();
        String size = (String) cmbSize1.getValue();

        GIFT req = new GIFT(requestID++, GIFT.Type.valueOf(type), GIFT.Size.valueOf(size),note,recipient,sender);
        GIFTdetails.add(req);
        giftsREGS.clear();
        giftsREGS.addAll(GIFTdetails);
        tblGifts.refresh();
        event.consume();
    }


    List<GIFT> GIFTdetails = new ArrayList<>();
    private void initGift() {
        tblRequestID.setCellValueFactory(new PropertyValueFactory<>("RequestID"));
        tblType.setCellValueFactory(new PropertyValueFactory<>("Type"));
        tblSize.setCellValueFactory(new PropertyValueFactory<>("Size"));
        tblNote.setCellValueFactory(new PropertyValueFactory<>("Note"));
        tblRecipient.setCellValueFactory(new PropertyValueFactory<>("Recipient"));
        tblSender.setCellValueFactory(new PropertyValueFactory<>("Sender"));
        tblGifts.setItems(giftsREGS);
        if(GIFTdetails != null) {
            giftsREGS.clear();
            giftsREGS.addAll(GIFTdetails);
            tblGifts.refresh();
        }
    }
    public void completeReq(MouseEvent event) {
//        event.consume();
//        GIFT selected = tblGift.getSelectionModel().getSelectedItem();
//        System.out.println(selected.toString());
//        GIFTdetails.remove(selected);
//        giftsREGS.clear();
//        giftsREGS.addAll(GIFTdetails);
//        tblGift.refresh();
    }
    public void cancelScr(MouseEvent event) {
        event.consume();
        ScreenController.deactivate();
    }

}
