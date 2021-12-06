package com.example.myapplication.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapp2021-2822d-default-rtdb.firebaseio.com/");
    private String chatKey;
    String getUserMobile;
    private RecyclerView chattingRegion;
    private final List<ChatList> chatLists = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private boolean loadingFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final ImageView back_btn = findViewById(R.id.back_btn);
        final CircleImageView profilePic = findViewById(R.id.profilePic);
        final TextView userName = findViewById(R.id.name);
        final EditText messageEditText = findViewById(R.id.message_txt);
        final ImageView send_btn = findViewById(R.id.send_btn);

        chattingRegion = findViewById(R.id.chattingRegion);

        //retrieve messages from message adapter class
        final String getName = getIntent().getStringExtra("name");
        final String getProfilePic = getIntent().getStringExtra("profile_pic");
        chatKey = getIntent().getStringExtra("chat_key");
        final String getMobile = getIntent().getStringExtra("mobile");

        //retrieve user mobile from memory
        getUserMobile = MemoryData.getData(Chat.this);

        userName.setText(getName);
        //Picasso.get().load(getProfilePic).into(profilePic);

        chattingRegion.setHasFixedSize(true);
        chattingRegion.setLayoutManager(new LinearLayoutManager(Chat.this));
        chatAdapter = new ChatAdapter(chatLists, Chat.this);
        chattingRegion.setAdapter(chatAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if(chatKey.isEmpty()){
                    // generate chat key
                    chatKey = "1";

                    if(snapshot.hasChild("chat")){
                        chatKey = String.valueOf(snapshot.child("chat").getChildrenCount() + 1);
                    }
                }
                 if(snapshot.hasChild("chat")){
                     if(snapshot.child("chat").child(chatKey).hasChild("messages")) {
                         chatLists.clear();
                         for (DataSnapshot messagesSnap : snapshot.child("chat").child(chatKey).child("messages").getChildren()) {

                             if (messagesSnap.hasChild("msg") && messagesSnap.hasChild("mobile")) {
                                 final String messageTimeStamps = messagesSnap.getKey();
                                 final String getMobile = messagesSnap.child("mobile").getValue(String.class);
                                 final String getMsg = messagesSnap.child("msg").getValue(String.class);
                                 Timestamp timestamp = new Timestamp(Long.parseLong(messageTimeStamps));
                                 Date date = new Date(timestamp.getTime());
                                 SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                 SimpleDateFormat stf = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
                                 ChatList chatList = new ChatList(getMobile, getName, getMsg, sdf.format(date),stf.format(date));
                                 chatLists.add(chatList);

                                 if(loadingFirstTime || Long.parseLong(messageTimeStamps) > Long.parseLong(MemoryData.getLastMsg(Chat.this, chatKey))){
                                     loadingFirstTime = false;
                                     MemoryData.saveLastMsg(messageTimeStamps, chatKey, Chat.this);
                                     chatAdapter.updateChatList(chatLists);

                                     chattingRegion.scrollToPosition(chatLists.size() -1);
                                 }
                             }
                         }
                     }
                 }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

                // Reset edit textfield
                messageEditText.setText("");
                // Close soft keyboard automatically after sending message
                InputMethodManager imm = (InputMethodManager) getSystemService(Chat.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY,0);

                databaseReference.child("chat").child(chatKey).child("user_1").setValue(getUserMobile);
                databaseReference.child("chat").child(chatKey).child("user_2").setValue(getMobile);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTStamp).child("msg").setValue(getTextMsg);
                databaseReference.child("chat").child(chatKey).child("messages").child(currentTStamp).child("mobile").setValue(getUserMobile);
            }
        });

    }



}