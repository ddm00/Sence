package com.turkcell.sence.fragments.profile.tab;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.adapters.UserProfileSurveyBenceAdapter;
import com.turkcell.sence.adapters.UserProfileSurveySenceAdapter;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.Survey;
import com.turkcell.sence.models.User;
import com.turkcell.sence.time.DateRegulative;
import com.turkcell.sence.time.MyDateFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.view.View.GONE;


public class UserProfileBenceFragment extends Fragment {

    private View view;
    private ListView surveyListView;
    private TextView warningTv;
    private FrameLayout homeFrameFl;
    private ArrayList<Survey> surveyList = new ArrayList<>();
    private UserProfileSurveyBenceAdapter surveyAdapter;
    private Activity activity;
    private User user;
    private boolean isFollowing;


    public UserProfileBenceFragment(Activity activity, User user) {
        this.activity = activity;
        this.user = user;
    }

    public UserProfileBenceFragment(Activity activity, User user, boolean isFollowing) {
        this.activity = activity;
        this.user = user;
        this.isFollowing = isFollowing;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile_bence, container, false);

        surveyListView = view.findViewById(R.id.userprofilebenceSurveys_Lv);
        warningTv = view.findViewById(R.id.userprofilebenceWarning_Tv);
        homeFrameFl = view.findViewById(R.id.userprofilebenceFrame_Fl);
        surveyAdapter = new UserProfileSurveyBenceAdapter(activity,view.getContext(),surveyList);
        surveyListView.setAdapter(surveyAdapter);

        warningTv.setVisibility(View.VISIBLE);
        if (isFollowing || user.isOpen()) {

            Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("following").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (user.getId().equals(snapshot.getKey())) {
                            getSurvey();
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
            getSurvey();
            warningTv.setVisibility(GONE);
        }
        return view;
    }


    private void getSurvey() {
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

                        publisher = (String) mapList.get(i).get("publisher");
                        surveyId = (String) mapList.get(i).get("surveyId");
                        question = (String) mapList.get(i).get("question");
                        category = (String) mapList.get(i).get("category");
                        surveyFirstImage = (String) mapList.get(i).get("surveyFirstImage");
                        surveySecondImage = (String) mapList.get(i).get("surveySecondImage");
                        time = (String) mapList.get(i).get("time");
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
                    homeFrameFl.setVisibility(GONE);
                    surveyListView.setVisibility(View.VISIBLE);
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
        double firstReyPersent = 0, firstRey = 0;
        if (map != null) {
            reySize = map.size();

            Set<String> stringSet = map.keySet();
            for (String s : stringSet) {
                Map<String, Object> value = (Map<String, Object>) map.get(s);

                if (value != null) {
                    boolean rey = (boolean) value.get("value");

                    if (rey) {
                        firstRey = firstRey + 1;
                    }
                }
            }
            firstReyPersent = (firstRey / reySize) * 100d;

            for (String s : stringSet) {
                if (s.equals(user.getId())) {
                    final Survey survey = new Survey(surveyId, question, surveyFirstImage, surveySecondImage, time, category, publisher, t, (int) firstReyPersent);
                    if (publisher != null) {
                        Dao.getInstance().getFirebaseDatabase().getReference("Users").child(publisher).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final User user = dataSnapshot.getValue(User.class);
                                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                user.setOpen((boolean) map.get("isOpen"));

                                survey.setUser(user);
                                surveyList.add(survey);
                                addAdapter();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        }

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
