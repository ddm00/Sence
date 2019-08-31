package com.turkcell.sence.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.LoginActivity;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.adapters.UserProfileSurveyCustomAdapter;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.Survey;
import com.turkcell.sence.models.UserProfileSurvey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;


@SuppressLint("ValidFragment")
public class UserProfileFragment extends Fragment {


    View view;
    FragmentManager supportFragmentManager;
    FrameLayout progressHomeFrame;
    ListView surveyListView;
    ArrayList<Survey> surveyList = new ArrayList<>();
    UserProfileSurveyCustomAdapter adapter;
    Button editProfile, sence, bence, logout, takipEttiklerim, takipcilerim;
    TextView userNameTv;

    public UserProfileFragment(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        surveyListView = view.findViewById(R.id.surveysList_Lv);
        progressHomeFrame = view.findViewById(R.id.progressHomeFrame);
        userNameTv = view.findViewById(R.id.userprofileUsername_Tv);
        userNameTv.setText(MainActivity.CurrentUser.getFullname());
        getMySurvey();

        editProfile = view.findViewById(R.id.userProfile_profilDuzenlbtn);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                EditProfileFragment myCarFragment = new EditProfileFragment();
                transaction.replace(R.id.fragmentContainer, myCarFragment, "EditProfileFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        takipcilerim = view.findViewById(R.id.btn_follower);
        takipcilerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                UserProfileFollowersFragment userProfileFollowersFragment = new UserProfileFollowersFragment();
                transaction.replace(R.id.fragmentContainer, userProfileFollowersFragment, "UserProfileFollowersFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        takipEttiklerim = view.findViewById(R.id.btn_following);
        takipEttiklerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                UserProfileFollowingFragment userProfileFollowingFragment = new UserProfileFollowingFragment();
                transaction.replace(R.id.fragmentContainer, userProfileFollowingFragment, "UserProfileFollowingFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        logout = view.findViewById(R.id.userProfile_logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                Intent intent = new Intent(view.getContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getMySurvey() {
        progressHomeFrame.setVisibility(View.VISIBLE);
        surveyListView.setVisibility(GONE);

        Dao.getInstance().getFirebaseDatabase().getReference("Surveys").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    final List<Map<String, Object>> mapList = new ArrayList<>();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                        mapList.add(hashMap);
                    }

                    surveyList.clear();
                    String publisher, surveyId, surveyQuestion, surveyCategory, surveyTime, surveyFirstImage, surveySecondImage;
                    Long time;
                    for (int i = 0; i < mapList.size(); i++) {

                        publisher = (String) mapList.get(i).get("publisher");
                        surveyId = (String) mapList.get(i).get("surveyId");
                        surveyQuestion = (String) mapList.get(i).get("question");
                        surveyCategory = (String) mapList.get(i).get("category");
                        surveyFirstImage = (String) mapList.get(i).get("surveyFirstImage");
                        surveySecondImage = (String) mapList.get(i).get("surveySecondImage");
                        surveyTime = (String) mapList.get(i).get("time");
                        time = (Long) mapList.get(i).get("t");

                        Log.e("hata", publisher);
                        if (publisher.equals(MainActivity.CurrentUser.getId())) {
                            final Survey survey = new Survey(surveyId, surveyQuestion, surveyFirstImage, surveySecondImage, surveyTime, surveyCategory, publisher, time);
                            survey.setUser(MainActivity.CurrentUser);
                            surveyList.add(survey);
                        }
                        addAdapter(i, mapList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addAdapter(int finalI, List<Map<String, Object>> mapList) {
        if ((finalI + 1) == mapList.size()) {
            UserProfileSurveyCustomAdapter surveyAdapter = new UserProfileSurveyCustomAdapter(view.getContext(), surveyList);
            surveyListView.setAdapter(surveyAdapter);
            progressHomeFrame.setVisibility(GONE);
            surveyListView.setVisibility(View.VISIBLE);
        }

    }

}
