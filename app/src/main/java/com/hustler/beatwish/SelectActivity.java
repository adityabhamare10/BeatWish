package com.hustler.beatwish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SelectActivity extends AppCompatActivity {

    private ListView listView;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public static String maxKey = "";
    public static HashMap<String,Integer> songsMap = new HashMap<>();
    public ArrayList<String> songNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Artist");

        listView = findViewById(R.id.listView);

        // Create an array of song names



        fetchSongsFromFirebase();

//        // Create an ArrayAdapter

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, songNames);

        // Set the adapter to the ListView
        listView.setAdapter(adapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                myRef.child("Songs").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


//                        HashMap<String,Integer> songsMap = new HashMap<>();


                        for(DataSnapshot childSnapshot : snapshot.getChildren()){
                            String key = childSnapshot.getKey();
                            Integer value = childSnapshot.getValue(Integer.class);
                            songsMap.put(key,value);
                        }

                        for(String key : songsMap.keySet()){
                            if(key.equals(songNames.get(position).toString())){
                                songsMap.put(key, songsMap.get(key)+1);
                            }
                        }

//
                    }




                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                Intent intent = new Intent(SelectActivity.this, PaymentActivity.class);

                startActivity(intent);
            }

        });

    }



    private void fetchSongsFromFirebase() {


        myRef.child("Songs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ArrayList<String> songNames = new ArrayList<>();
                for(DataSnapshot childSnapshot : snapshot.getChildren()){
                    String key = childSnapshot.getKey();
                   // Integer value = childSnapshot.getValue(Integer.class);
                    //songsMap.put(key,value);
                    //String ans=key+"           requestcount: "+value.toString();
                    songNames.add(key);


                }
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(SelectActivity.this,
//                        android.R.layout.simple_list_item_1, android.R.id.text1, songNames);
//                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}