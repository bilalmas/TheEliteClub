package com.bilalmas.eliteclub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference rootref;
    private String userid;
    private EditText username;
    private EditText bio;
    private EditText interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        username = (EditText) findViewById(R.id.editname);
        bio = (EditText) findViewById(R.id.editbio);
        interests = (EditText) findViewById(R.id.editinterests);
        mAuth = FirebaseAuth.getInstance();
        rootref = FirebaseDatabase.getInstance().getReference();
        userid = mAuth.getUid();
    }
    public void Savedetails(View view){

        String name = username.getText().toString().trim();
        String bios = bio.getText().toString().trim();
        String interest = interests.getText().toString().trim();
        //final String name = Name.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter your username!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(bios)) {
            Toast.makeText(getApplicationContext(), "Enter your bio!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(interest)) {
            Toast.makeText(getApplicationContext(), "Enter your interests!", Toast.LENGTH_SHORT).show();
            return;
        }

        Users userinfo = new Users(name,bios,interest);
        rootref.child("Users").child(userid).setValue(userinfo);
        Toast.makeText(getApplicationContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();

    }
}
