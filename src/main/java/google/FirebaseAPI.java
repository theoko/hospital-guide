package google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import helpers.Constants;
import helpers.UIHelpers;
import helpers.UserHelpers;
import models.room.Book;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FirebaseAPI {

    private FileInputStream serviceAccount;
    private FirebaseOptions options;

    public FirebaseAPI() {

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

}
