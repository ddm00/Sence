package com.turkcell.sence.fragments.profile.tab;


import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.adapters.UserProfileSurveyCustomAdapter;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.Survey;
import com.turkcell.sence.models.User;
import com.turkcell.sence.time.DateRegulative;
import com.turkcell.sence.time.MyDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

public class UserProfileSurveyFragment extends Fragment {


    private View view;
    private ListView surveyListView;
    private TextView warningTv;
    private FrameLayout homeFrameFl;
    private ArrayList<Survey> surveyList = new ArrayList<>();
    private UserProfileSurveyCustomAdapter surveyAdapter;
    private Activity activity;
    private User user;
    private boolean isFollowing;

    public UserProfileSurveyFragment(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
    }

    public UserProfileSurveyFragment(Activity activity, User user, boolean isFollowing) {
        this.activity = activity;
        this.user = user;
        this.isFollowing = isFollowing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile_survey, container, false);
        surveyListView = view.findViewById(R.id.userprofilesurveySurveys_Lv);
        warningTv = view.findViewById(R.id.userprofilesurveyWarning_Tv);
        homeFrameFl = view.findViewById(R.id.userprofilesurveyFrame_Fl);
        surveyAdapter = new UserProfileSurveyCustomAdapter(activity, view.getContext(), surveyList);
        surveyListView.setAdapter(surveyAdapter);

        warningTv.setVisibility(View.VISIBLE);
        if (isFollowing || user.isOpen()) {

            Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("following").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (user.getId().equals(snapshot.getKey())) {
                            getMySurvey();
                            warningTv.setVisibility(GONE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        if (user.getId().equals(MainActivity.CurrentUser.getId())) {
            getMySurvey();
            warningTv.setVisibility(GONE);
        }

        return view;
    }

    private void getMySurvey() {
        homeFrameFl.setVisibility(View.VISIBLE);
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
                    String publisher, surveyId, question, category, time, surveyFirstImage, surveySecondImage;
                    Long t;
                    for (int i = 0; i < mapList.size(); i++) {

                        publisher = (String) mapList.get(i).get("Publisher");
                        surveyId = (String) mapList.get(i).get("SurveyId");
                        question = (String) mapList.get(i).get("Question");
                        category = (String) mapList.get(i).get("Category");
                        surveyFirstImage = (String) mapList.get(i).get("SurveyFirstImage");
                        surveySecondImage = (String) mapList.get(i).get("SurveySecondImage");
                        time = (String) mapList.get(i).get("Time");
                        t = (Long) mapList.get(i).get("t");

                        if (t != null && time != null) {

                            String farkDay = farkHesap(t, time);
                            if (!farkDay.equals("")) {
                                setSurvey(i, publisher, surveyId, question, category, surveyFirstImage, surveySecondImage, time, t, mapList);

                            } else {
                                setSurvey(i, publisher, surveyId, question, category, surveyFirstImage, surveySecondImage, "Anketin süresi doldu.", t, mapList);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setSurvey(int i, String publisher, String surveyId, String question, String category, String surveyFirstImage, String surveySecondImage, String time, Long t, List<Map<String, Object>> mapList) {
        Map<String, Object> map = (Map<String, Object>) mapList.get(i).get("Users");
        int reySize = 0;
        if (map != null) {
            reySize = map.size();
        }

        if (publisher != null) {
            if (publisher.equals(user.getId())) {
                final Survey survey = new Survey(surveyId, question, surveyFirstImage, surveySecondImage, time, category, publisher, t, reySize);
                survey.setUser(user);
                surveyList.add(survey);
            }
        }
        addAdapter();
    }

    private void addAdapter() {
        surveyAdapter.notifyDataSetChanged();
        homeFrameFl.setVisibility(GONE);
        surveyListView.setVisibility(View.VISIBLE);
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
