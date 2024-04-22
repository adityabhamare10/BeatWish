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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class ArtistEventCreation extends AppCompatActivity {

    ImageView createEvent;

    EditText nameOfArtist, eDate, eTime, venue, requestFee, eDuration, emailId;

    FirebaseDatabase database;
    DatabaseReference myRef;

    public static String eId;
    public static String eventDate;
    public static String eventTime;
    public static String eventVenue;
    public static String artistName;
    public static Boolean contain=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_event_creation);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Artist");

        createEvent = findViewById(R.id.createEvent);
        nameOfArtist = findViewById(R.id.nameOfArtist);
        eDate = findViewById(R.id.eDate);
        eTime = findViewById(R.id.eTime);
        venue = findViewById(R.id.venue);
        requestFee = findViewById(R.id.requestFee);
        eDuration = findViewById(R.id.durationOfEvent);
        emailId = findViewById(R.id.emailId);
        String randomNmbr=getRandomString(6);


        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                artistName = nameOfArtist.getText().toString().trim();
                eventDate = eDate.getText().toString().trim();
                eventTime = eTime.getText().toString().trim();
                eventVenue = venue.getText().toString().trim();
                String requestFees = requestFee.getText().toString().trim();
                String eventDuration = eDuration.getText().toString().trim();
                eId = emailId.getText().toString().trim();

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                             if(eId.equals(key)){

                             }

                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });



                // Check if any field is empty
                if (artistName.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty() ||
                        eventVenue.isEmpty() || requestFees.isEmpty() || eventDuration.isEmpty()) {
                    Toast.makeText(ArtistEventCreation.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method
                }

                // Check date format (YYYY-DD-MM)
                if (!isValidDateFormat(eventDate)) {
                    Toast.makeText(ArtistEventCreation.this, "Invalid date format", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method
                }

                // Check time format (24-hour format)
                if (!isValidTimeFormat(eventTime)) {
                    Toast.makeText(ArtistEventCreation.this, "Invalid time format", Toast.LENGTH_SHORT).show();
                    return; // Exit the onClick method
                }

                HashMap<String, String> childMap = new HashMap<>();
//                childMap.put("Id", eId);
                childMap.put("Name", artistName);
                childMap.put("Date", eventDate);
                childMap.put("Time", eventTime);
                childMap.put("Venue", eventVenue);
                childMap.put("RequestFees", requestFees);
                childMap.put("joincode",randomNmbr);

                DatabaseReference artistRef = myRef.child(eId);


                    artistRef.setValue(childMap);

                    nameOfArtist.setText("");
                    eDate.setText("");
                    eTime.setText("");
                    venue.setText("");
                    requestFee.setText("");
                    eDuration.setText("");

                    // All checks passed, proceed to switch activity
                    Intent intent = new Intent(ArtistEventCreation.this, EventCountdown.class);
                    // Pass data to the next activity if needed
                    startActivity(intent);












            }
        });

    }

    private boolean isValidDateFormat(String date) {
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    // Function to check valid time format (24-hour format)
    private boolean isValidTimeFormat(String time) {
        return time.matches("([01]?[0-9]|2[0-3]):[0-5][0-9]");
    }

    public static String getRandomString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        int charCount = 'z' - 'a' + 1 + 'Z' - 'A' + 1 + '9' - '0' + 1; // A-Z, a-z, 0-9
        for (int i = 0; i < length; i++) {
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