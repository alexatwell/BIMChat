package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private String mobile;
    private String email;
    private String name;
    private RecyclerView messagesRecyclerView;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView);

        //get Intent data from Register.class activity
        mobile = getIntent().getStringExtra("mobile");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");

        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

}
