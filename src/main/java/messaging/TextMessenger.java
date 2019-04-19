package messaging;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import messaging.exception.InvalidNumberFormatException;

public class TextMessenger {
    public static final String ACCOUNT_SID = "ACc21723db8079e8681e407a88d8b64a70";
    public static final String AUTH_TOKEN = "f614252ffa8351cbee77cc0668967456";



    private Message message;
    private String recipient;
    private String messageText = "";
    public static final String callerID = "+17867892578";
    public TextMessenger() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void declareMessage(String s) {
        this.messageText = s;

    }
    public void declareRecipient(String recipient) {
        try {
            if (recipient.charAt(0) != '+') {
                throw new InvalidNumberFormatException("+ not found at start, invalid number.");
            }
            for(int i = 1; i < recipient.length(); i++) {
                char currentChar = recipient.charAt(i);
                if(currentChar < 48 || currentChar > 57) {
                    throw new InvalidNumberFormatException("Invalid character! " + currentChar);
                }
            }
            if(recipient.length() < 8 || recipient.length() > 15) {
                throw new InvalidNumberFormatException("Number has invalid length: " + recipient.length());
            }
            this.recipient = recipient;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void sendMessage() {
        if(messageText == null) {
            System.out.println("Message is not set!");
            return;
        }
        if(recipient == null) {
            System.out.println("Recipient is not set!");
            return;
        }
        try {
            message = Message.creator(
                    new com.twilio.type.PhoneNumber(recipient),
                    new com.twilio.type.PhoneNumber(callerID),
                    messageText)
                    .create();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
