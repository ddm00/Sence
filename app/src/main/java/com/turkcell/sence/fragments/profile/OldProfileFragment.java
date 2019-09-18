package com.turkcell.sence.fragments.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.turkcell.sence.fragments.profile.tab.UserProfileBenceFragment;
import com.turkcell.sence.fragments.profile.tab.UserProfileSenceFragment;
import com.turkcell.sence.models.User;
import com.turkcell.sence.time.DateRegulative;
import com.turkcell.sence.time.MyDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */public class OldProfileFragment extends Fragment {

    private View view;
    private FragmentManager supportFragmentManager;
    private CircleImageView profilePhotoIv;
    private TextView profileNameTv;
    private Button followerBtn, followBtn, ongoingPollBtn, numberOngoingPollBtn, completedPollBtn, numberCompletedPollBtn, votedPollBtn, numberVotedPollBtn;
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
        profilePhotoIv = view.findViewById(R.id.oldprofileUserImage_Iv);
        profileNameTv = view.findViewById(R.id.oldprofileUsername_Tv);

        followerBtn = view.findViewById(R.id.oldprofileFollower_Btn);
        followBtn = view.findViewById(R.id.oldprofileFollowing_Btn);
        ongoingPollBtn = view.findViewById(R.id.oldprofileOngoingSurvey_Btn);
        completedPollBtn = view.findViewById(R.id.oldprofileCompletedSurvey_Btn);
        votedPollBtn = view.findViewById(R.id.oldprofileVotedSurvey_Btn);

        numberOngoingPollBtn = view.findViewById(R.id.oldprofileOngoingNumber_Btn);
        numberCompletedPollBtn = view.findViewById(R.id.oldprofileCompletedNumber_Btn);
        numberVotedPollBtn = view.findViewById(R.id.oldprofileVotedNumber_Btn);

        followerBtn.setEnabled(false);
        followBtn.setEnabled(false);
        ongoingPollBtn.setEnabled(false);
        completedPollBtn.setEnabled(false);
        votedPollBtn.setEnabled(false);
        numberOngoingPollBtn.setEnabled(false);
        numberCompletedPollBtn.setEnabled(false);
        numberVotedPollBtn.setEnabled(false);

        onGoing();
        onComplete();
        onVolley();

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_account_circle_black_24dp);
        Glide.with(view.getContext()).setDefaultRequestOptions(requestOptions).load(user.getImageurl()).into(profilePhotoIv);
        profileNameTv.setText(user.getFullname());

        if (user.isOpen()) {
            isFollowing = true;
            followerBtn.setEnabled(true);
            followBtn.setEnabled(true);
            ongoingPollBtn.setEnabled(true);
            completedPollBtn.setEnabled(true);
            votedPollBtn.setEnabled(true);
            numberOngoingPollBtn.setEnabled(true);
            numberCompletedPollBtn.setEnabled(true);
            numberVotedPollBtn.setEnabled(true);
        }

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (user.getId().equals(snapshot.getKey())) {
                        isFollowing = true;
                        followerBtn.setEnabled(true);
                        followBtn.setEnabled(true);
                        ongoingPollBtn.setEnabled(true);
                        completedPollBtn.setEnabled(true);
                        votedPollBtn.setEnabled(true);
                        numberOngoingPollBtn.setEnabled(true);
                        numberCompletedPollBtn.setEnabled(true);
                        numberVotedPollBtn.setEnabled(true);
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

        followBtn.setOnClickListener(new View.OnClickListener() {
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


        ongoingPollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    UserProfileOnGoingFragment onGoingFragment = new UserProfileOnGoingFragment(activity, user, true);
                    transaction.replace(R.id.fragmentContainer, onGoingFragment, "UserProfileOnGoingFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli veya siz onu takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }

            }
        });

        completedPollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    UserProfileSenceFragment senceFragment = new UserProfileSenceFragment(activity, user, true);
                    transaction.replace(R.id.fragmentContainer, senceFragment, "UserProfileSenceFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli veya siz onu takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }
            }
        });

        votedPollBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    UserProfileBenceFragment benceFragment = new UserProfileBenceFragment(activity, user, true);
                    transaction.replace(R.id.fragmentContainer, benceFragment, "UserProfileSenceFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli veya siz onu takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //on going counter
    private void onGoing() {

        Dao.getInstance().getFirebaseDatabase().getReference("Surveys").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    final List<Map<String, Object>> mapList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                        mapList.add(hashMap);
                    }

                    String publisher, time;
                    Long t;
                    int counter = 0;
                    for (int i = 0; i < mapList.size(); i++) {

                        publisher = (String) mapList.get(i).get("publisher");
                        time = (String) mapList.get(i).get("time");
                        t = (Long) mapList.get(i).get("t");

                        if (t != null && time != null) {

                            String farkDay = farkHesap(t, time);
                            if (!farkDay.equals("")) {
                                if (publisher != null) {
                                    if (publisher.equals(user.getId())) {
                                        counter = counter + 1;
                                    }
                                }
                            }
                        }
                    }
                    numberOngoingPollBtn.setText(counter + "");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onComplete() {

        Dao.getInstance().getFirebaseDatabase().getReference("Surveys").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    final List<Map<String, Object>> mapList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                        mapList.add(hashMap);
                    }

                    String publisher, time;
                    Long t;
                    int counter = 0;
                    for (int i = 0; i < mapList.size(); i++) {

                        publisher = (String) mapList.get(i).get("publisher");
                        time = (String) mapList.get(i).get("time");
                        t = (Long) mapList.get(i).get("t");

                        if (t != null && time != null) {

                            String farkDay = farkHesap(t, time);
                            if (farkDay.equals("")) {
                                if (publisher != null) {
                                    if (publisher.equals(user.getId())) {
                                        counter = counter + 1;
                                    }
                                }
                            }
                        }
                    }
                    numberCompletedPollBtn.setText(counter + "");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onVolley() {

        Dao.getInstance().getFirebaseDatabase().getReference("Surveys").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    final List<Map<String, Object>> mapList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                        mapList.add(hashMap);
                    }

                    String time;
                    Long t;
                    int counter = 0;
                    for (int i = 0; i < mapList.size(); i++) {

                        time = (String) mapList.get(i).get("time");
                        t = (Long) mapList.get(i).get("t");

                        if (t != null && time != null) {

                            String farkDay = farkHesap(t, time);
                            if (!farkDay.equals("")) {
                                Map<String, Object> map = (Map<String, Object>) mapList.get(i).get("Users");
                                if (map != null) {
                                    Set<String> stringSet = map.keySet();
                                    for (String s : stringSet) {
                                        if (s.equals(user.getId())) {
                                            counter = counter + 1;
                                        }
                                    }
                                }
                            } else {
                                Map<String, Object> map = (Map<String, Object>) mapList.get(i).get("Users");
                                if (map != null) {
                                    Set<String> stringSet = map.keySet();
                                    for (String s : stringSet) {
                                        if (s.equals(user.getId())) {
                                            counter = counter + 1;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    numberVotedPollBtn.setText(counter + "");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private String farkHesap(long longDate, String sTime) {

        String fark = "";
        MyDateFormat myDateFormat = DateRegulative.getInstance().getDifference(longDate);

        switch (sTime) {
            case "30 dk":
                if (myDateFormat.getsMinute() < 30 && myDateFormat.getsHour() == 0 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "1 saat":
                if (myDateFormat.getsMinute() < 60 && myDateFormat.getsHour() == 0 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "1 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "3 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 2 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "5 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 4 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "7 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 6 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
        }
        return fark;
    }

}
