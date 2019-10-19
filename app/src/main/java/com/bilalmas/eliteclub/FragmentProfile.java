package com.bilalmas.eliteclub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class FragmentProfile extends Fragment {

    //Button editbutton;
    TextView username;
    TextView bio;
    TextView interests;
    ImageView profileimage;
    TextView photochange;
    private static final int Gallerypick = 1;
    private DatabaseReference rootref;
    private FirebaseAuth mAuth;
    private StorageReference UserProfileimageref;
    private  String userID;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallerypick && resultCode== RESULT_OK && data!=null)
        {
            Uri imageuri = data.getData();
            //profileimage.setImageResource();
            StorageReference filepath = UserProfileimageref.child(userID + ".jpg");
            filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getActivity(),"Profile Image Uploaded Successfully!",Toast.LENGTH_SHORT).show();
                       // final String downloadUrl = task.getResult (). getMetadata (). getReference (). getDownloadUrl (). toString ();
                        final String downloadUrl = task.getResult().getDownloadUrl().toString();
                        rootref.child("Users").child(userID).child("image")
                                .setValue(downloadUrl)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        if (task.isSuccessful())
                                        {
                                            Toast.makeText(getActivity(), "Image save in Database, Successfully...", Toast.LENGTH_SHORT).show();
                                           // loadingBar.dismiss();
                                        }
                                        else
                                        {
                                            String message = task.getException().toString();
                                            Toast.makeText(getActivity(), "Error: " + message, Toast.LENGTH_SHORT).show();
                                            //loadingBar.dismiss();
                                        }
                                    }
                                });
                    }
                    else {
                        String message = task.getException().toString();
                        Toast.makeText(getActivity(),"Error :"+message,Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inputfragmentview = inflater.inflate(R.layout.fragment_profile,null);
        username = (TextView)inputfragmentview.findViewById(R.id.user);
        bio = (TextView)inputfragmentview.findViewById(R.id.bio);
        interests = (TextView)inputfragmentview.findViewById(R.id.interests);
        profileimage = (ImageView)inputfragmentview.findViewById(R.id.profile_image);
        photochange = (TextView)inputfragmentview.findViewById(R.id.profilechange);
        rootref = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        UserProfileimageref = FirebaseStorage.getInstance().getReference().child("Profile Images");
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

        photochange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryintent = new Intent();
                galleryintent.setAction(Intent.ACTION_GET_CONTENT);
                galleryintent.setType("image/*");
                startActivityForResult(galleryintent,Gallerypick);
            }
        });









        return inputfragmentview;
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){

            if(ds.child(userID).exists()) {
                Users uInfo = new Users();
                uInfo.setUsername(ds.child(userID).getValue(Users.class).getUsername()); //set the name
                uInfo.setBio(ds.child(userID).getValue(Users.class).getBio()); //set the email
                uInfo.setInterests(ds.child(userID).getValue(Users.class).getInterests()); //set the phone_num
                uInfo.setImage(ds.child(userID).getValue(Users.class).getImage());
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
               if(uInfo.getImage()!="Empty"){
                   Picasso.with(getContext()).load(uInfo.getImage()).placeholder(R.drawable.profile_image).into(profileimage);
               }

            }
        }
    }



}
