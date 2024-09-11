package com.hustler.beatwish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    ImageView artist, attendee;

    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(
                () -> {
                    // Initialize the Google Mobile Ads SDK on a background thread.
                    MobileAds.initialize(this, initializationStatus -> {});
                })
                .start();

        adView = findViewById(R.id.adView);

        // Create a new ad view.
//        adView = new AdView(this);
//        adView.setAdUnitId("ca-app-pub-8048594935916934/5493002503");
//        adView.setAdSize(getAdSize());

//     // Replace ad container with new ad view.
//        adContainerView.removeAllViews();
//        adContainerView.addView(adView);


        // Start loading the ad in the background.
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        attendee = findViewById(R.id.attendee);
        artist = findViewById(R.id.artist);



        attendee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,AttendeeEventJoin.class);
                startActivity(i);
            }
        });

        artist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ArtistEventCreation.class);
                startActivity(i);
            }
        });


    }
}