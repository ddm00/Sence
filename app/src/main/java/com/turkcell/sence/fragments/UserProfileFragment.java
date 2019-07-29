package com.turkcell.sence.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.turkcell.sence.R;
import com.turkcell.sence.adapters.UserProfileSurveyCustomAdapter;
import com.turkcell.sence.models.UserProfileSurvey;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class UserProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    ListView surveyListView;
    ArrayList<UserProfileSurvey> surveyList = new ArrayList<>();
    UserProfileSurveyCustomAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        surveyListView = view.findViewById(R.id.userProfie_anketlerim_lv);
        surveyList.add(new UserProfileSurvey("Soru1","Giyim", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(), R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,10,3,7));
        surveyList.add(new UserProfileSurvey("Soru2 hangisi dah gljflsd feslkfs fsklfjs gslgkjsg gksjdgl s sdlgsjdg gsdlgjsdlgkjsd gsdgkjsdlgkjd gsdlgkjdsgs dglsdkjg sldgj sdlgjsd gld gdlgjsdg","Giyim", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(), R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,10,3,7));
        surveyList.add(new UserProfileSurvey("Soru3 hangisi dah gljflsd feslkfs fsklfjs gslgkjsg gksjdgl s sdlgsjdg gsdlgjsdlgkjsd gsdgkjsdlgkjd gsdlgkjdsgs dglsdkjg sldgj sdlgjsd gld gdlgjsdg","Giyim", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(), R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,10,3,7));

        adapter = new UserProfileSurveyCustomAdapter(getContext(),surveyList);
        surveyListView.setAdapter(adapter);
        return view;
    }

}
