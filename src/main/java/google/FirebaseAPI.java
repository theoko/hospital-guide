package google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import controllers.maps.MapController;
import database.LocationTable;
import helpers.Constants;
import helpers.UserHelpers;
import javafx.application.Platform;
import map.PathFinder;
import models.map.Location;
import models.room.Book;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class FirebaseAPI {

    private static final String STORAGE_BUCKET = "hospital-guide-47dc0.appspot.com";

    private FileInputStream serviceAccount;
    private FirebaseOptions options;

    public FirebaseAPI() {

        System.out.println("Firebase: created instance");

        serviceAccount = null;
        options = null;

        try {

            InputStream serviceAccount = getClass().getResourceAsStream("/google/hospital-guide-47dc0-firebase-adminsdk-jpnxx-7f46220d9c.json");

            options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(
                            serviceAccount
                    ))
                    .setDatabaseUrl("https://hospital-guide-47dc0.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // TODO: generate identified to support multiple bookings for same room
    public static void sendBooking(Book booking) {

        Map<String, Object> currBooking = new HashMap<>();

        currBooking.put("bookingID", booking.getBookingID());
        currBooking.put("roomID", booking.getRoomID());
        currBooking.put("startDate", booking.getStartDate());
        currBooking.put("endDate", booking.getEndDate());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.BOOK_LOCATION_TABLE)
                .child(UserHelpers.getCurrentUser().getUsername())
                .child(String.valueOf(booking.getRoomID()));

        databaseReference.updateChildren(currBooking, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                System.out.println("Firebase db: added booking!");
            }

        });

    }

    public static void deleteBooking(String roomID) {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(Constants.BOOK_LOCATION_TABLE)
                .child(UserHelpers.getCurrentUser().getUsername())
                .child(roomID);

        databaseReference.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                System.out.println("Firebase db: deleted booking!");
            }
        });

    }

    public static void uploadDirectionsImage(File directionsImage) {

        new Thread(() -> {
            Storage storage = StorageOptions.getDefaultInstance().getService();

            BlobId blobId = BlobId.of(
                    STORAGE_BUCKET,
                            Base64.getEncoder().encodeToString("start location, end location".getBytes()) +
                            "_" +
                            directionsImage.getName()
            );
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("image/png")
                    .build();
            BufferedImage bImage = null;
            try {
                bImage = ImageIO.read(directionsImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try {
                ImageIO.write(bImage, "png", bos );
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte [] data = bos.toByteArray();
            Blob blob = storage.create(blobInfo, data);
        }).start();

    }

    public static void setCaller(Object newCaller) {
        caller = newCaller;
    }

    private static Object caller;
    public static void checkForCommands(String username) {

        DatabaseReference commandsRef = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child(Constants.FIREBASE_COMMANDS)
                                        .child(username);

        commandsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if(snapshot.exists() && snapshot.getChildrenCount() > 0 && caller != null) {

                    System.out.println("we got commands!");
                    System.out.println(snapshot.getValue().toString());

                    Iterable<DataSnapshot> children = snapshot.getChildren();
                    Iterator<DataSnapshot> iterator = children.iterator();

//                    System.out.println(iterator.next());

                    while(iterator.hasNext()) {

                        DataSnapshot pair = iterator.next();

                        System.out.println(pair.getValue().toString().substring(0, pair.getValue().toString().indexOf(":")));
                        System.out.println(pair.getValue().toString().substring(pair.getValue().toString().indexOf(":") + 1));

                        if(pair.getKey().equals("pathfinding")) {
                            System.out.println("we got it 1");
                            Platform.runLater(() -> {

                                String startingLocation = pair.getValue().toString().substring(0, pair.getValue().toString().indexOf(":"));
                                String destination = pair.getValue().toString().substring(pair.getValue().toString().indexOf(":") + 1);

                                if(!startingLocation.equals(MapController.getTempStart())) {
                                    MapController.setTempStart(startingLocation);
                                }

                                PathFinder.printPath(
                                        (MapController) caller,
                                        ((MapController) caller).getMap().getLocation(startingLocation),
                                        ((MapController) caller).getMap().getLocation(destination)
                                );

                                // Remove commands after executing them
                                commandsRef.child("pathfinding").removeValue((error, ref) -> System.out.println("Removed children"));
                            });

//                            commandsRef.removeEventListener(this);
                            System.out.println("we got it 2");
                        }

                        iterator.remove();
                    }

                } else {
                    System.out.println("no commands");
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });


    }

}
