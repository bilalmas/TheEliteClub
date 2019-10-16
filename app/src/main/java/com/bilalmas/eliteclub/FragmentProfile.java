package com.bilalmas.eliteclub;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentProfile extends Fragment {

    //Button editbutton;
    TextView username;
    TextView bio;
    TextView interests;
    private DatabaseReference rootref;
    private FirebaseAuth mAuth;
    private  String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inputfragmentview = inflater.inflate(R.layout.fragment_profile,null);
        username = (TextView)inputfragmentview.findViewById(R.id.user);
        bio = (TextView)inputfragmentview.findViewById(R.id.bio);
        interests = (TextView)inputfragmentview.findViewById(R.id.interests);
        rootref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();

        // Read from the database
        rootref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        return inputfragmentview;
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){

            if(ds.child(userID)!=null) {
                Users uInfo = new Users();
                uInfo.setUsername(ds.child(userID).getValue(Users.class).getUsername()); //set the name
                uInfo.setBio(ds.child(userID).getValue(Users.class).getBio()); //set the email
                uInfo.setInterests(ds.child(userID).getValue(Users.class).getInterests()); //set the phone_num

               /* //display all the information
                Log.d(TAG, "showData: name: " + uInfo.getName());
                Log.d(TAG, "showData: email: " + uInfo.getEmail());
                Log.d(TAG, "showData: phone_num: " + uInfo.getPhone_num());

                ArrayList<String> array = new ArrayList<>();
                array.add(uInfo.getName());
                array.add(uInfo.getEmail());
                array.add(uInfo.getPhone_num());
                ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array);
                mListView.setAdapter(adapter);*/
               username.setText(uInfo.getUsername());
               bio.setText(uInfo.getBio());
               interests.setText(uInfo.getInterests());

            }
        }
    }

}
