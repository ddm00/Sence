package com.turkcell.sence.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.adapters.SurveyAdapter;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.Survey;
import com.turkcell.sence.models.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    ListView surveyListView;
    FrameLayout progressHomeFrame;
    SurveyAdapter surveyAdapter;
    List<Survey> surveyList = new ArrayList<>();
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        surveyListView = view.findViewById(R.id.surveyList_Lv);
        progressHomeFrame = view.findViewById(R.id.homeFrame_Fl);
        progressHomeFrame.setVisibility(View.VISIBLE);
        surveyListView.setVisibility(View.GONE);

        Dao.getInstance().getFirebaseDatabase().getReference("Surveys").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    final List<Map<String, Object>> mapList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                        mapList.add(hashMap);
                    }
                    surveyAdapter = new SurveyAdapter(view.getContext(), surveyList);
                    String publisher, surveyId, surveyQuestion, surveyCategory, surveyTime, surveyFirstImage, surveySecondImage;
                    Long time;
                    boolean isSecret;
                    for (int i = 0; i < mapList.size(); i++) {
                        publisher = (String) mapList.get(i).get("publisher");
                        surveyId = (String) mapList.get(i).get("surveyId");
                        surveyQuestion = (String) mapList.get(i).get("question");
                        surveyCategory = (String) mapList.get(i).get("category");
                        surveyFirstImage = (String) mapList.get(i).get("surveyFirstImage");
                        surveySecondImage = (String) mapList.get(i).get("surveySecondImage");
                        isSecret = (boolean) mapList.get(i).get("isSecret");
                        surveyTime = (String) mapList.get(i).get("time");
                        time = (Long) mapList.get(i).get("t");
                        if (time != null) {
                            String differenceDay=timeCalculate(time,surveyTime);
                            if (!differenceDay.equals("")){
                                Map<String,Object> map=(Map<String, Object>) mapList.get(i).get("Users");
                                Boolean isWhichOne=null;
                                int reySize=0;
                                if (map!=null){
                                    Map<String,Object> objectMap=(Map<String, Object>) map.get(MainActivity.CurrentUser.getId());
                                    if (objectMap!=null){
                                        isWhichOne=(Boolean)objectMap.get("value");
                                    }
                                    reySize=map.size();
                                }
                                final Survey survey=new Survey(surveyId, surveyQuestion, surveyFirstImage, surveySecondImage, differenceDay, surveyCategory, publisher, time, isWhichOne, reySize, isSecret);
                                final int finalI=i;
                                Dao.getInstance().getFirebaseDatabase().getReference("Users").child(survey.getSurveyPublisher())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                final User user=dataSnapshot.getValue(User.class);
                                                Map<String,Object> map=(Map<String, Object>)dataSnapshot.getValue();
                                                user.setOpen((boolean)map.get("isOpen"));
                                                if (user.isOpen()){
                                                    survey.setUser(user);
                                                    surveyList.add(survey);
                                                    surveyAdapter.notifyDataSetChanged();
                                                }else{
                                                    if(user.getId().equals(MainActivity.CurrentUser.getId())){
                                                        survey.setUser(user);
                                                        surveyList.add(survey);
                                                        surveyAdapter.notifyDataSetChanged();
                                                    }else{
                                                        FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                                                .child("following").child(user.getId()).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                Boolean isOpen = (Boolean) dataSnapshot.getValue();
                                                                if (isOpen != null && isOpen) {
                                                                    survey.setUser(user);
                                                                    surveyList.add(survey);
                                                                    surveyAdapter.notifyDataSetChanged();

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                }
                                                addAdapter(finalI,mapList);
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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private String timeCalculate(long longDate, String time) {

        Date today = new Date();
        Date calculateDate;
        long todayDate = today.getTime();
        String day, hour, minute, second, sDate, dif_day = "";

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd / HH:mm:ss");
        String[] timeList = {"30 dk", "1 saat", "1 gün", "3 gün", "5 gün", "7 gün"};

        calculateDate = new Date(longDate - todayDate);
        sDate = formatter.format(calculateDate);
        day = sDate.substring(0, 2);
        hour = sDate.substring(5, 7);
        minute = sDate.substring(8, 10);
        second = sDate.substring(11, 13);

        Log.e("date", sDate + " " + day + " " + hour + " " + minute + " " + second);

        for (int i = 0; i < timeList.length; i++) {
            if (timeList[i].equals(time)) {
                switch (i) {
                    case 0:
                        if (Integer.valueOf(minute) != 0 && Integer.valueOf(second) != 0) {
                            dif_day = "Kalan süre: " + minute + ":" + second;
                        }
                        break;
                    case 1:
                        if (Integer.valueOf(minute) != 0 && Integer.valueOf(second) != 0) {
                            dif_day = "Kalan süre: " + minute + ":" + second;
                        }
                        break;
                    case 2:
                        if (Integer.valueOf(hour) != 0 && Integer.valueOf(minute) != 0 && Integer.valueOf(second) != 0) {
                            dif_day = "Kalan süre: " + hour + ":" + minute + ":" + second;
                        }
                        break;
                    case 3:
                        if (Integer.valueOf(day) != 1 && Integer.valueOf(hour) != 0 && Integer.valueOf(minute) != 0 && Integer.valueOf(second) != 0) {
                            dif_day = "Kalan süre: " + day + " gün " + hour + ":" + minute + ":" + second;
                        }
                        break;
                    case 4:
                        if (Integer.valueOf(day) != 1 && Integer.valueOf(hour) != 0 && Integer.valueOf(minute) != 0 && Integer.valueOf(second) != 0) {
                            dif_day = "Kalan süre: " + day + " gün " + hour + ":" + minute + ":" + second;
                        }
                        break;
                    case 5:
                        if (Integer.valueOf(day) != 1 && Integer.valueOf(hour) != 0 && Integer.valueOf(minute) != 0 && Integer.valueOf(second) != 0) {
                            dif_day = "Kalan süre: " + day + " gün " + hour + ":" + minute + ":" + second;
                        }
                        break;
                }
            }
        }
        return dif_day;
    }

    private void addAdapter(int finalI, List<Map<String, Object>> mapList) {
        if ((finalI + 1) == mapList.size()) {
            surveyAdapter = new SurveyAdapter(view.getContext(), surveyList);
            surveyListView.setAdapter(surveyAdapter);
            progressHomeFrame.setVisibility(GONE);
            surveyListView.setVisibility(View.VISIBLE);
        }

    }


}
