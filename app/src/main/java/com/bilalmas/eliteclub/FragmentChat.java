package com.bilalmas.eliteclub;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentChat extends Fragment {
    /*private RecyclerView FindFriendsRecyclerList;
    private DatabaseReference UsersRef;
    //private String retImage = "default_image";



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat,null);

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        FindFriendsRecyclerList = (RecyclerView)view.findViewById(R.id.find_friends_recycler_list);
        FindFriendsRecyclerList.setLayoutManager(new LinearLayoutManager(getContext()));


        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(UsersRef, Users.class)
                        .build();

        FirebaseRecyclerAdapter<Users, FragmentFeed.FindFriendViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, FragmentFeed.FindFriendViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull FragmentFeed.FindFriendViewHolder holder, final int position, @NonNull Users model)
                    {

                        holder.userName.setText(model.getUsername());
                       // holder.userStatus.setText(model.getBio());
                        //holder.interests.setText(model.getInterests());
                        Picasso.with(getContext()).load(model.getImage()).placeholder(R.drawable.profile_image).into(holder.profileImage);


                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String visit_user_id = getRef(position).getKey();
                               // String name = UsersRef.child(visit_user_id).child("Name")
                                Intent profileIntent = new Intent(getActivity(), ChatActivity.class);
                                //profileIntent.putExtra("visit_user_id", visit_user_id);
                                startActivity(profileIntent);
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public FragmentFeed.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_chatlayout, viewGroup, false);
                        FragmentFeed.FindFriendViewHolder viewHolder = new FragmentFeed.FindFriendViewHolder(view);
                        return viewHolder;
                    }
                };

        FindFriendsRecyclerList.setAdapter(adapter);

        adapter.startListening();
    }



    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName, userStatus,interests;
        CircleImageView profileImage;


        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.username);
            userStatus = itemView.findViewById(R.id.bio);
            interests =  itemView.findViewById(R.id.interests);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }*/
    private View PrivateChatsView;
    private RecyclerView chatsList;

    private DatabaseReference ChatsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID="";





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        PrivateChatsView = inflater.inflate(R.layout.fragment_chat, container, false);


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        //ChatsRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");


        chatsList = (RecyclerView) PrivateChatsView.findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));


        return PrivateChatsView;
    }


    @Override
    public void onStart()
    {
        super.onStart();


        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(UsersRef, Users.class)
                        .build();


        FirebaseRecyclerAdapter<Users, ChatsViewHolder> adapter =
                new FirebaseRecyclerAdapter<Users, ChatsViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Users model)
                    {
                        final String usersIDs = getRef(position).getKey();
                        final String[] retImage = {"default_image"};

                        UsersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                if (dataSnapshot.exists())
                                {
                                    if (dataSnapshot.hasChild("image"))
                                    {
                                        if(dataSnapshot.child("image").getValue().toString()!="Empty") {
                                            retImage[0] = dataSnapshot.child("image").getValue().toString();
                                            Picasso.with(getContext()).load(retImage[0]).into(holder.profileImage);
                                        }
                                    }

                                    final String retName = dataSnapshot.child("username").getValue().toString();
                                    //final String retStatus = dataSnapshot.child("status").getValue().toString();

                                    holder.userName.setText(retName);



                                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view)
                                        {
                                            Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                            chatIntent.putExtra("visit_user_id", usersIDs);
                                            chatIntent.putExtra("visit_user_name", retName);
                                            chatIntent.putExtra("visit_image", retImage[0]);
                                            startActivity(chatIntent);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
                    {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.users_chatlayout, viewGroup, false);
                        return new ChatsViewHolder(view);
                    }
                };

        chatsList.setAdapter(adapter);
        adapter.startListening();
    }




    public static class  ChatsViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView profileImage;
        TextView userStatus, userName,interests;


        public ChatsViewHolder(@NonNull View itemView)
        {
            super(itemView);

            userName = itemView.findViewById(R.id.username);
            userStatus = itemView.findViewById(R.id.bio);
            interests =  itemView.findViewById(R.id.interests);
            profileImage = itemView.findViewById(R.id.users_profile_image);
        }
    }
}
