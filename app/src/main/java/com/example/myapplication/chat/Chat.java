package com.example.myapplication.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.MemoryData;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapp2021-2822d-default-rtdb.firebaseio.com/");
    private String chatKey;
    private String getUserMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageView back_btn = findViewById(R.id.back_btn);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final TextView userName = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.message_txt);
        final ImageView send_btn = findViewById(R.id.send_btn);

        //retrieve messages from message adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");

        //retrieve user mobile from memory
        getUserMobile = MemoryData.getData(Chat.this);

        userName.setText(getName);
        Picasso.get().load(getProfilePic).into(profilePic);

       if(chatKey.isEmpty()){
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    // generate chat key
                    chatKey = "1";
                    if(snapshot.hasChild("chat")){
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get current time
                final String currentTStamp = String.valueOf(System.currentTimeMillis()).substring(0,10);
                final String getTextMsg = messageEditText.getText().toString();
                messageEditText.setText("");

                MemoryData.saveLastMsg(currentTStamp, chatKey, Chat.this);

                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTStamp).child("msg").setValue(getTextMsg);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTStamp).child("mobile").setValue(getUserMobile);
            }
        });

    }

}