package com.turkcell.sence.fragments.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.fragments.profile.tab.UserProfileBenceFragment;
import com.turkcell.sence.fragments.profile.tab.UserProfileSenceFragment;
import com.turkcell.sence.models.User;
import com.turkcell.sence.time.DateRegulative;
import com.turkcell.sence.time.MyDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */public class OtherProfileFragment extends Fragment {

    private View view;
    private FragmentManager supportFragmentManager;
    private CircleImageView profilePhotoIv;
    private TextView profileNameTv;
    private Button followFollowingBtn,followerBtn, followBtn, ongoingPollBtn, numberOngoingPollBtn, completedPollBtn, numberCompletedPollBtn, votedPollBtn, numberVotedPollBtn;
    private Activity activity;
    private User user;
    private boolean isFollowing = false;
    private List<User> mUsers;


    public OtherProfileFragment(FragmentManager supportFragmentManager, User user, Activity activity) {
        this.supportFragmentManager = supportFragmentManager;
        this.user = user;
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_other_profile, container, false);
        profilePhotoIv = view.findViewById(R.id.otherprofileUserImage_Iv);
        profileNameTv = view.findViewById(R.id.otherprofileUsername_Tv);
        followFollowingBtn=view.findViewById(R.id.otherprofileFollowFollower_Btn);
        followerBtn = view.findViewById(R.id.otherprofileFollower_Btn);
        followBtn = view.findViewById(R.id.otherprofileFollow_Btn);
        ongoingPollBtn = view.findViewById(R.id.otherprofileOngoingSurvey_Btn);
        completedPollBtn = view.findViewById(R.id.otherprofileCompletedSurvey_Btn);
        votedPollBtn = view.findViewById(R.id.otherprofileVotedSurvey_Btn);

        numberOngoingPollBtn = view.findViewById(R.id.otherprofileOngoingNumber_Btn);
        numberCompletedPollBtn = view.findViewById(R.id.otherprofileCompletedNumber_Btn);
        numberVotedPollBtn = view.findViewById(R.id.otherprofileVotedNumber_Btn);

        followFollowingBtn.setVisibility(View.VISIBLE);
        isFollowing(followFollowingBtn);

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
        followFollowingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.CurrentUser.getId() != null && user != null) {

                    if (followFollowingBtn.getText().toString().equals("takip et")) {
                        if (user.isOpen()) {
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                    .child("following").child(user.getId()).setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                    .child("followers").child(MainActivity.CurrentUser.getId()).setValue(true);

                            if (user.getToken() != null && !user.getToken().equals("")) {
                                sendFCMPush(user.getToken(), "Sence", MainActivity.CurrentUser.getFullname() + " seni takip etti");
                            }

                        } else {
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                    .child("requestPust").child(user.getId()).setValue(true);
                            FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                    .child("requestGet").child(MainActivity.CurrentUser.getId()).setValue(true);

                            if (user.getToken() != null && !user.getToken().equals("")) {
                                sendFCMPush(user.getToken(), "Sence", MainActivity.CurrentUser.getFullname() + " sana bir arkadaşlık isteği gönderdi.");
                            }

                        }

                    } else if (followFollowingBtn.getText().toString().equals("takip etme")) {

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                .child("following").child(user.getId()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                .child("followers").child(MainActivity.CurrentUser.getId()).removeValue();

                    } else if (followFollowingBtn.getText().toString().equals("istek")) {

                        FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                .child("requestPust").child(user.getId()).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                                .child("requestGet").child(MainActivity.CurrentUser.getId()).removeValue();
                    }
                }
            }

        });

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

    private void isFollowing(final Button button) {

        if (user.isOpen()) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("Follow").child(MainActivity.CurrentUser.getId()).child("following");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (user.getId() != null && !user.getId().isEmpty() && dataSnapshot.child(user.getId()).exists()) {
                        button.setText("takip etme");
                    } else {
                        button.setText("takip et");
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                    .child("Follow").child(MainActivity.CurrentUser.getId()).child("requestPust");
            reference1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (user.getId() != null && !user.getId().isEmpty() && dataSnapshot.child(user.getId()).exists()) {
                        button.setText("istek");
                    } else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                                .child("Follow").child(MainActivity.CurrentUser.getId()).child("following");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (user.getId() != null && !user.getId().isEmpty() && dataSnapshot.child(user.getId()).exists()) {
                                    button.setText("takip etme");
                                } else {
                                    button.setText("takip et");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    button.setText("takip et");
                }
            });
        }
    }

    private void sendFCMPush(String token, String title, String msg) {
        final String SERVER_KEY = "AAAAf88Hcr8:APA91bEzHQIJna7TyTIdIA9G7QYbkDx-2Wvblseslr8xgsjtgeXnxy_SdsBSBnbCWuIvlKZx1ELDdbhNOJw4RQ13Q0UVWXDrpZB4fOdtjL1rcN5bgoBLk96CepDJKnhePEQOVZQDGYqL";

        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("return here>>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("True", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("False", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity);
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

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
