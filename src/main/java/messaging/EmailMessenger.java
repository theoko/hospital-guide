package messaging;

import messaging.exception.InvalidEmailFormatException;

import java.io.File;
import java.util.*;
import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class EmailMessenger {
    String messageText;
    String recipient;
    private static final String sender = "FuchsiaFauns@fuchsiafauns.com";
    private static final String host = "smtp.gmail.com";

    private static final String username = "FuchsiaFaunsMessaging@gmail.com";
    private static final String password = "fingData9887";
    private final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
    private ArrayList<File> imageFiles;
    public EmailMessenger() {

    }

    public ArrayList<File> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(ArrayList<File> imageFiles) {
        this.imageFiles = imageFiles;
    }

    public void setRecipient(String s) {
        try {
            if(!s.contains("@")) {
                throw new InvalidEmailFormatException("Invalid email, doesn't contain @.");
            }
            recipient = s;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public Properties getConfiguredProperty() {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
        properties.setProperty("mail.smtp.socketFactory.fallback", "false");
        properties.setProperty("mail.smtp.port", "465");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.debug", "true");
        properties.put("mail.store.protocol", "pop3");
        properties.put("mail.transport.protocol", "smtp");
        return properties;
    }
    public void setMessageText(String s) {
        messageText = s;
    }
    public void sendMessage() {
        if(messageText == null || recipient == null) {
            System.out.println("Message or recipient are null.");
            return;
        }
        try {
            Properties properties = getConfiguredProperty();
            Session session = Session.getDefaultInstance(properties,
                    new Authenticator(){
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(username, password);
                        }});

            MimeMessage message = new MimeMessage(session);
            // Contains all parts of the message
//            MimeMultipart multipart = new MimeMultipart("related");
////            getHTMLDirections(messageText);
//            // Text direction part
//            BodyPart messageTextBodyPart = new MimeBodyPart();
//            messageTextBodyPart.setText(messageText);
//            multipart.addBodyPart(messageTextBodyPart);
//
//            // Image part
//            if(imageFiles != null)
//            for(File f : imageFiles) {
//                BodyPart imageMessageBodyPart = new MimeBodyPart();
//                String htmlText = "<H1>/H1>" +
//                        "<img src=\"cid:image\" alt=\"Floor Map\" ></img>" +
//                        "";
//                imageMessageBodyPart.setContent(htmlText, "text/html");
//                imageMessageBodyPart = new MimeBodyPart();
//                DataSource fds = new FileDataSource(
//                        f.getAbsolutePath());
//
//                imageMessageBodyPart.setDataHandler(new DataHandler(fds));
//                imageMessageBodyPart.setHeader("Content-ID", "<image>");
//                multipart.addBodyPart(imageMessageBodyPart);
//            }
            message.setContent(getHTML(messageText, imageFiles));
            // add it


            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSentDate(new Date());
//            message.addRecipients(Message.RecipientType.TO, new Address[...]); // email to multiple recipients
            message.setSubject("Brigham & Young's Directions");
//            message.setText(messageText);

            Transport.send(message);
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
    public MimeMultipart getHTML(String directions, ArrayList<File> files) {
        String dirs = "";
        dirs += "<html><head><title>Brigham and Young Directions</title></head>" +
                "<body><div><strong>Directions</strong><br><br>" +
                "<div>";
        MimeBodyPart textPart = new MimeBodyPart();
        MimeMultipart content = new MimeMultipart();
        boolean complete = false;
        ArrayList<String> floorSplitter = new ArrayList<String>();
        while(!complete) {
            int nextBreak = directions.indexOf("\n");
            if(nextBreak != -1) {
                directions = directions.substring(0, nextBreak) + "<br>" + directions.substring(nextBreak+"\n".length());
            } else {
                int nextSplit = directions.indexOf("Take");
                if(nextSplit == -1) {
//                    String current = floorSplitter.get(floorSplitter.size() - 1);
                    floorSplitter.add(directions);
                    directions = "";
                    complete = true;
                } else {
                    floorSplitter.add(directions.substring(0, nextSplit));
                    directions = directions.substring(nextSplit);
                    int brBreak = directions.indexOf("<br>");
                    if (brBreak != -1) {

                        String current = floorSplitter.get(floorSplitter.size() - 1);
                        floorSplitter.set(floorSplitter.size() - 1, current + directions.substring(0, brBreak));
                        directions = directions.substring(brBreak + "<br>".length());
                    } else {
                        complete = true;
                        String current = floorSplitter.get(floorSplitter.size() - 1);
                        floorSplitter.set(floorSplitter.size() - 1, current + directions);
                        directions = "";
                    }
                }
            }
        }
        ArrayList<MimeBodyPart> imageDependencies = new ArrayList<MimeBodyPart>();
        for(String floorDirs : floorSplitter) {
            String cid = ContentIdGenerator.getContentId();
            dirs += floorDirs;
            dirs += "<img src=\"cid:"
                    + cid
                    + "\" /></div><div>";
            try {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.attachFile(files.remove(0));
                imagePart.setContentID("<" + cid + ">");
//                imagePart.setDisposition(MimeBodyPart.INLINE);
                imageDependencies.add(imagePart);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        dirs += "</div></body></html>";
        try {
            textPart.setText(dirs, "US-ASCII", "html");
            content.addBodyPart(textPart);
            for(MimeBodyPart imagePart : imageDependencies) {
                content.addBodyPart(imagePart);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
//        textPart.setText("<html><head>"
//
//                + "<title>This is not usually displayed</title>"
//
//                + "</head>n"
//
//                + "<body><div><strong>Hi there!</strong></div>"
//
//                + "<div>Sending HTML in email is so <em>cool!</em> </div>n"
//
//                + "<div>And here's an image: <img src="cid:"
//
//                + cid
//
//                + "" /></div>n" + "<div>I hope you like it!</div></body></html>",
//
//        "US-ASCII", "html");
        return content;
    }

}
