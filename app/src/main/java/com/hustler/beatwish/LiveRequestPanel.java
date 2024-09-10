package com.hustler.beatwish;

import static com.hustler.beatwish.ArtistEventCreation.emailId;

import static com.hustler.beatwish.SelectActivity.maxKey;
import static com.hustler.beatwish.SelectActivity.songsMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Locale;
import java.util.Map;


public class LiveRequestPanel extends AppCompatActivity {

    ImageView acceptBtn, skipBtn;
    TextView songTitle, songName, artistName;
    TextToSpeech t1;

    FirebaseDatabase db;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_request_panel);




        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Artist");


        acceptBtn = findViewById(R.id.acceptBtn);
        skipBtn = findViewById(R.id.skipBtn);
        songTitle = findViewById(R.id.songNameTitle);
        songName = findViewById(R.id.songName);
        artistName = findViewById(R.id.artistName);



        myRef.child("Songs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String key = "";
                int maxCount = 0;
                t1 = new TextToSpeech(LiveRequestPanel.this, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status != TextToSpeech.ERROR) {
                            t1.setLanguage(Locale.ENGLISH);
                            t1.setSpeechRate(0.8F);
                            // Speak "Hello" after successful initialization
                            Bundle bd = new Bundle();
                            t1.speak("Next song is Unstoppable", TextToSpeech.QUEUE_FLUSH,bd, null);
                        } else {
                            Log.e("TTS", "TTS Initialization Failed");
                        }
                    }
                });





                songTitle.setText("Love me Like You Do");
                songName.setText("Can I");
            }




            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        myRef.child(emailId).child("Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = (String) snapshot.getValue(String.class);
                    artistName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LiveRequestPanel.this, "Song Accepted", Toast.LENGTH_SHORT).show();
            }
        });

        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LiveRequestPanel.this, "Song Rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }
}