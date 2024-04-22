package com.hustler.beatwish;

import static com.hustler.beatwish.ArtistEventCreation.eventDate;
import static com.hustler.beatwish.ArtistEventCreation.eventTime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EventCountdown extends AppCompatActivity {

    TextView countdownText;
    CountDownTimer countDownTimer;
    long eventStartTimeMillis;

    ImageView addSongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_countdown);

        countdownText = findViewById(R.id.countdownText);
        addSongs = findViewById(R.id.addSongs);

        String eventStartTimeStr = eventDate + " " +eventTime;

//        String eventStartTimeStr = "2024-03-20 12:00"; // Example format YYYY-MM-DD HH:mm
        eventStartTimeMillis = convertStringToMillis(eventStartTimeStr);

        startCountdown();

        addSongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EventCountdown.this, SongsPlaylist.class);
                startActivity(i);
            }
        });

    }
    private void startCountdown() {
        countDownTimer = new CountDownTimer(eventStartTimeMillis - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished);
                long hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished) % 24;
                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60;

                String countdownStr = String.format(Locale.getDefault(),
                        "Event Begins (in %d days %d hours %d minutes)",
                        days, hours, minutes);
                countdownText.setText(countdownStr);
            }

            @Override
            public void onFinish() {
                // Redirect to another activity when countdown finishes
                Intent intent = new Intent(EventCountdown.this, LiveRequestPanel.class);
                startActivity(intent);
                finish(); // Finish this activity to prevent going back with back button
            }
        }.start();
    }

    private long convertStringToMillis(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date date = format.parse(dateString);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancel the countdown timer to avoid memory leaks
        }
    }
}