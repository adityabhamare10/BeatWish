package com.hustler.beatwish;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class SongsPlaylist extends AppCompatActivity {

    private EditText songInput;
    private ListView songList;
    private ArrayList<String> songsList;
    private ArrayAdapter<String> adapter;
    private Button submitDB, uploadQr;

    FirebaseStorage storage;


    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_playlist);

        storage = FirebaseStorage.getInstance();


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Artist");

        songInput = findViewById(R.id.songInput);
        songList = findViewById(R.id.songList);
        Button addSongButton = findViewById(R.id.addSongButton);
        submitDB = findViewById(R.id.submitDB);
        uploadQr = findViewById(R.id.uploadQr);

        songsList = new ArrayList<>();

        uploadQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
                Toast.makeText(SongsPlaylist.this, "Qr Code Uploaded SuccessFully!!", Toast.LENGTH_SHORT).show();
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songsList);
        songList.setAdapter(adapter);



        addSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSongToList();
            }
        });

        // Handle remove song action
        songList.setOnItemClickListener((parent, view, position, id) -> {
            songsList.remove(position);
            adapter.notifyDataSetChanged();
            Toast.makeText(SongsPlaylist.this, "Song removed", Toast.LENGTH_SHORT).show();
        });

        submitDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                HashMap<String, Integer> childData;
                childData = new HashMap<>();
                for(String element : songsList){
                    childData.put(element, 0);
                }
                myRef.child("Songs").setValue(childData);
                Toast.makeText(SongsPlaylist.this, "Submitted!!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void addSongToList() {
        String songName = songInput.getText().toString().trim();
        if (!songName.isEmpty()) {
            songsList.add(songName);
            adapter.notifyDataSetChanged();
            songInput.getText().clear();
        } else {
            Toast.makeText(this, "Please enter a song name", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        if (result.getData() != null) {
                            // Get the selected image Uri
                            Uri imageUri = result.getData().getData();
                            uploadImageToFirebase(imageUri);
                        }
                    } else {
                        Toast.makeText(SongsPlaylist.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    private void uploadImageToFirebase(Uri imageUri) {
        // Create a reference for the image in Firebase Storage
        StorageReference storageRef = storage.getReference().child("qr_codes/" + UUID.randomUUID().toString());

        // Upload the image to Firebase Storage
        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(SongsPlaylist.this, "QR code uploaded successfully!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SongsPlaylist.this, "Error uploading QR code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
