package com.hustler.beatwish;

import static com.hustler.beatwish.ArtistEventCreation.artistName;
import static com.hustler.beatwish.ArtistEventCreation.eventTime;
import static com.hustler.beatwish.ArtistEventCreation.eventVenue;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RequestPage extends AppCompatActivity {

    TextView Name, locationVenue, startTime;
    ImageView selectSongs;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_page);

        Name = findViewById(R.id.arName);
        locationVenue = findViewById(R.id.locationVenue);
        startTime = findViewById(R.id.startTime);

        Name.setText(artistName);
        locationVenue.setText(eventVenue);
        startTime.setText(eventTime);


        selectSongs = findViewById(R.id.selectSongs);

        selectSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(RequestPage.this, SelectActivity.class);
                startActivity(i);

            }
        });


    }
}