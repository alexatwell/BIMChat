package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://chatapp2021-2822d-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText name = findViewById(R.id.r_name);
        final EditText mobile = findViewById(R.id.r_mobile);
        final EditText email = findViewById(R.id.r_email);
        final AppCompatButton registerBtn = findViewById(R.id.r_registerBtn);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        checkLogged();

        registerBtn.setOnClickListener(v -> {

            progressDialog.show();

            final String nameText = name.getText().toString();
            final String mobileText = mobile.getText().toString();
            final String emailText = email.getText().toString();

            if (nameText.isEmpty() || mobileText.isEmpty() || emailText.isEmpty()){
                Toast.makeText(Register.this, "All fields Required!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child("users").hasChild(mobileText)){
                            if (!checkLogged()){
                                Toast.makeText(Register.this, "Mobile already exists", Toast.LENGTH_SHORT).show();
                                Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(Register.this, Register.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            databaseReference.child("users").child(mobileText).child("email").setValue(emailText);
                            databaseReference.child("users").child(mobileText).child("name").setValue(nameText);
                            databaseReference.child("users").child(mobileText).child("profile_pic").setValue("");

                            // save data to memory
                            MemoryData.saveData(mobileText, Register.this);

                            // save name to memory
                            MemoryData.saveName(nameText, Register.this);

                            Toast.makeText(Register.this, "Success", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Register.this, MainActivity.class);
                            intent.putExtra("mobile", mobileText);
                            intent.putExtra("name", nameText);
                            intent.putExtra("email", emailText);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }
    public boolean checkLogged() {
        // check if user already logged in
        if(!(MemoryData.getData(this).isEmpty())){
            Intent intent = new Intent(Register.this, MainActivity.class);
            intent.putExtra("mobile", MemoryData.getData(this));
            intent.putExtra("name", MemoryData.getName(this));
            intent.putExtra("email", "");
            startActivity(intent);
            finish();
        }
        return false;
    }

}