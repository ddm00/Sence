package com.turkcell.sence.fragments.profile;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.fragments.CompletedPollFragment;
import com.turkcell.sence.fragments.OngoingPollFragment;
import com.turkcell.sence.models.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class OldProfileFragment extends Fragment {

    private View view;
    private FragmentManager supportFragmentManager;
    private ImageView userImage;
    private TextView userName;
    private Button followerBtn, followingBtn, ongoingSurveyBtn, ongoingNumberBtn, completedSurveyBtn, completedNumberBtn, votedSurveyBtn, votedNumberBtn;
    private Activity activity;
    private User user;
    private boolean isFollowing = false;


    public OldProfileFragment(FragmentManager supportFragmentManager, User user, Activity activity) {
        this.supportFragmentManager = supportFragmentManager;
        this.user = user;
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_old_profile, container, false);
        userImage = view.findViewById(R.id.oldprofileUserImage_Iv);
        userName = view.findViewById(R.id.oldprofileUsername_Tv);

        followerBtn = view.findViewById(R.id.oldprofileFollower_Btn);
        followingBtn = view.findViewById(R.id.oldprofileFollowing_Btn);
        ongoingSurveyBtn = view.findViewById(R.id.oldprofileOngoingSurvey_Btn);
        completedSurveyBtn = view.findViewById(R.id.oldprofileCompletedSurvey_Btn);
        votedSurveyBtn = view.findViewById(R.id.oldprofileVotedSurvey_Btn);

        ongoingNumberBtn = view.findViewById(R.id.oldprofileOngoingNumber_Btn);
        completedNumberBtn = view.findViewById(R.id.oldprofileCompletedNumber_Btn);
        votedNumberBtn = view.findViewById(R.id.oldprofileVotedNumber_Btn);

        followerBtn.setEnabled(false);
        followingBtn.setEnabled(false);
        ongoingSurveyBtn.setEnabled(false);
        completedSurveyBtn.setEnabled(false);
        votedSurveyBtn.setEnabled(false);
        ongoingNumberBtn.setEnabled(false);
        completedNumberBtn.setEnabled(false);
        votedNumberBtn.setEnabled(false);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_account_circle_black_24dp);
        Glide.with(view.getContext()).setDefaultRequestOptions(requestOptions).load(user.getImageurl()).into(userImage);
        userName.setText(user.getFullname());

        if (user.isOpen()) {
            isFollowing = true;
            followerBtn.setEnabled(true);
            followingBtn.setEnabled(true);
            ongoingSurveyBtn.setEnabled(true);
            completedSurveyBtn.setEnabled(true);
            votedSurveyBtn.setEnabled(true);
            ongoingNumberBtn.setEnabled(true);
            completedNumberBtn.setEnabled(true);
            votedNumberBtn.setEnabled(true);
        }

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (user.getId().equals(snapshot.getKey())) {
                        isFollowing = true;
                        followerBtn.setEnabled(true);
                        followingBtn.setEnabled(true);
                        ongoingSurveyBtn.setEnabled(true);
                        completedSurveyBtn.setEnabled(true);
                        votedSurveyBtn.setEnabled(true);
                        ongoingNumberBtn.setEnabled(true);
                        completedNumberBtn.setEnabled(true);
                        votedNumberBtn.setEnabled(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        init();
        return view;
    }

    private void init() {

        followerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    UserProfileFollowersFragment userProfileFollowersFragment = new UserProfileFollowersFragment(supportFragmentManager, activity, user);
                    transaction.replace(R.id.fragmentContainer, userProfileFollowersFragment, "UserProfileFollowersFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli veya siz onu takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }
            }
        });

        followingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    UserProfileFollowingFragment userProfileFollowingFragment = new UserProfileFollowingFragment(supportFragmentManager, activity, user);
                    transaction.replace(R.id.fragmentContainer, userProfileFollowingFragment, "UserProfileFollowingFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli veya siz onu takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }
            }
        });

        ongoingSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    OngoingPollFragment ongoingPollFragment = new OngoingPollFragment();
                    transaction.replace(R.id.fragmentContainer, ongoingPollFragment, "OngoingPollFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli veya siz onu takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }
            }
        });


        completedSurveyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    CompletedPollFragment completedPollFragment = new CompletedPollFragment();
                    transaction.replace(R.id.fragmentContainer, completedPollFragment, "CompletedPollFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli veya siz onu takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

}
