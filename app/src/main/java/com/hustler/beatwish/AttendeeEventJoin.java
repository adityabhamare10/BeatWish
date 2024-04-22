package com.hustler.beatwish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleEventObserver;

import android.content.Intent;
import android.media.metrics.Event;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AttendeeEventJoin extends AppCompatActivity {

    ImageView joinImage;
    EditText eventCode;

    FirebaseDatabase db;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_event_join);

        db = FirebaseDatabase.getInstance();
        myRef = db.getReference("Artist");


        joinImage = findViewById(R.id.joinImage);
        eventCode = findViewById(R.id.eventCode);

        joinImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String eventCodeStr = eventCode.getText().toString();

                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                            String key = childSnapshot.getKey();
                            String userCode = childSnapshot.child("joincode").getValue(String.class);
                            if(userCode.equals(eventCodeStr)){
                                Intent i = new Intent(AttendeeEventJoin.this, SelectActivity.class);
                                startActivity(i);
                                return;
                            }
                        }

                        Toast.makeText(AttendeeEventJoin.this, "Invalid Code", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

            }
        });


    }
}