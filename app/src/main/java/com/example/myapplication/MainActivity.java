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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private final int PICK_CONTACT = 1;
    private String mobile;
    private String email;
    private String name;
    private RecyclerView messagesRecyclerView;
    private String contact_name;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapp2021-2822d-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivity(pickContact, PICK_CONTACT);
            }
        });

        final CircleImageView userProfilePic = findViewById(R.id.userProfilePic);
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
        progressDialog.show();

        // get profile pic from firebase database
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                final String profilePicUrl = snapshot.child("users").child(mobile).child("profile_pic").getValue(String.class);

                if(!profilePicUrl.isEmpty()){
                    //set profile pic to circle image view
                    Picasso.get().load(profilePicUrl).into(userProfilePic);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return values for.
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // Perform your query - the contactUri is like a "where" clause here
            try (Cursor c = getContentResolver()
                    .query(contactUri, queryFields, null, null, null)) {
                // Double-check that you actually got results
                if (c.getCount() == 0) {
                    return;
                }
                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c.moveToFirst();
                contact_name = c.getString(0);
                Toast.makeText(MainActivity.this, contact_name, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openContact(){
        startActivity
    }
}
