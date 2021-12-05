package com.example.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.example.myapplication.messages.MessagesAdapter;
import com.example.myapplication.messages.MessagesList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private final List<MessagesList> messagesLists = new ArrayList<>();
    private final int PICK_CONTACT = 1;
    private String mobile;
    private String email;
    private String name;
    private RecyclerView messagesRecyclerView;
    private String contact_name;
    private String contact_num;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapp2021-2822d-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI),PICK_CONTACT);
            }
        });

        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
        userProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showContact();
            }
        });
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        //get Intent data from Register.class activity
        mobile = getIntent().getStringExtra("mobile");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        //progressDialog.show();

        // get profile pic from firebase database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesLists.clear();

                for (DataSnapshot dataSnapshot : snapshot.child("users").getChildren()){
                    final String getMobile = dataSnapshot.getKey();

                    if(!getMobile.isEmpty()){
                        final String getName = dataSnapshot.child("name").getValue(String.class);
                        final String getProfilePic = dataSnapshot.child("profile_pic").getValue(String.class);

                        MessagesList messagesList = new MessagesList(getName, getMobile, "", getProfilePic, 0);
                        messagesLists.add(messagesList);
                    }
                }

                messagesRecyclerView.setAdapter(new MessagesAdapter(messagesLists, MainActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_CONTACT && data != null) {
                Uri contactUri = data.getData();
                // Specify which fields you want your query to return
                // values for.
                String[] queryFields = new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME
                };
                // Perform your query - the contactUri is like a "where"
                // clause here
                Cursor c = getContentResolver()
                        .query(contactUri, queryFields, null, null, null);
                try {
                    // Double-check that you actually got results
                    if (c.getCount() == 0) {
                        return;
                    }
                    // Pull out the first column of the first row of data -
                    // that is your suspect's name.
                    c.moveToFirst();
                    contact_name = c.getString(0);
                    contact_num = "8" + genNumber();
                } finally {
                    c.close();
                }
            }
        }
    }

    private int genNumber(){
        return new Random().nextInt(899999) + 100000;
    }

    public void showContact(){
        Toast.makeText(MainActivity.this, "Contact Name: " + contact_name +
                        "\nMobile: " + contact_num,
                Toast.LENGTH_SHORT).show();
    }
}
