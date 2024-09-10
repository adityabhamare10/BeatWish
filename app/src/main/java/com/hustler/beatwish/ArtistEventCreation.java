package com.hustler.beatwish;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class ArtistEventCreation extends AppCompatActivity {

    ImageView createEvent;

    EditText eArtist, eDate, eTime, eVenue, eRequestFee, eDuration, eEmail;

    FirebaseDatabase database;
    DatabaseReference myRef;

    public static String emailId;
    public static String eventDate;
    public static String eventTime;
    public static String eventVenue;
    public static String artistName;
    public static String requestFees;
    public static String eventDuration;
//    public static Boolean contain =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_event_creation);

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Artists");

        // Initialize UI elements
        createEvent = findViewById(R.id.createEvent);
        eArtist = findViewById(R.id.nameOfArtist);
        eDate = findViewById(R.id.eDate);
        eTime = findViewById(R.id.eTime);
        eVenue = findViewById(R.id.venue);
        eRequestFee = findViewById(R.id.requestFee);
        eDuration = findViewById(R.id.durationOfEvent);
        eEmail = findViewById(R.id.emailId);

        String randomNum = getRandomString();  // Generate random code for event

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get values from input fields
                artistName = eArtist.getText().toString().trim();
                eventDate = eDate.getText().toString().trim();
                eventTime = eTime.getText().toString().trim();
                eventVenue = eVenue.getText().toString().trim();
                requestFees = eRequestFee.getText().toString().trim();
                eventDuration = eDuration.getText().toString().trim();
                emailId = eEmail.getText().toString().trim();

                // Validate input fields
                validateInput();


                // Create a HashMap for the event data
                HashMap<String, String> childMap = new HashMap<>();
                childMap.put("Name", artistName);
                childMap.put("Date", eventDate);
                childMap.put("Time", eventTime);
                childMap.put("Venue", eventVenue);
                childMap.put("RequestFees", requestFees);
                childMap.put("joincode", randomNum);

                // Save the event data to Firebase
                DatabaseReference artistRef = myRef.child(emailId);
                artistRef.setValue(childMap);
                System.out.println("Sent");

                // Clear input fields after creating the event
//                eArtist.setText("");
//                eDate.setText("");
//                eTime.setText("");
//                eVenue.setText("");
//                eRequestFee.setText("");
//                eDuration.setText("");

                // Start next activity
                Intent intent = new Intent(ArtistEventCreation.this, EventCountdown.class);
                startActivity(intent);
            }
        });
    }


    private void uploadToFirebase(){

    }

    private void validateInput(){
        if (artistName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty() ||
                eventVenue.isEmpty() || requestFees.isEmpty() || eventDuration.isEmpty()) {
            Toast.makeText(ArtistEventCreation.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
        if (!isValidDateFormat(eventDate)) {
            Toast.makeText(ArtistEventCreation.this, "Invalid date format", Toast.LENGTH_SHORT).show();
        }
        if (!isValidTimeFormat(eventTime)) {
            Toast.makeText(ArtistEventCreation.this, "Invalid time format", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidDateFormat(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // Function to check valid time format (24-hour format)
    private boolean isValidTimeFormat(String time) {
        return time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }

    public static String getRandomString() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int charCount = 'z' - 'a' + 1 + 'Z' - 'A' + 1 + '9' - '0' + 1; // A-Z, a-z, 0-9
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(charCount);
            char randomChar;
            if (randomIndex < 'Z' - 'A' + 1) {
                randomChar = (char) ('A' + randomIndex); // A-Z
            } else if (randomIndex < ('Z' - 'A' + 1) + 'z' - 'a' + 1) {
                randomChar = (char) ('a' + randomIndex - ('Z' - 'A' + 1)); // a-z
            } else {
                randomChar = (char) ('0' + randomIndex - ('Z' - 'A' + 1) - ('z' - 'a' + 1)); // 0-9
            }
            sb.append(randomChar);
        }
        return sb.toString();
    }
}