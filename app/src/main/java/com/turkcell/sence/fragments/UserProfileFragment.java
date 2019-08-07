package com.turkcell.sence.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.LoginActivity;
import com.turkcell.sence.adapters.UserProfileSurveyCustomAdapter;
import com.turkcell.sence.models.UserProfileSurvey;

import java.util.ArrayList;
import java.util.Calendar;


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
    Button editProfile, sence, bence, logout, takipEttiklerim, takipcilerim;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_profile, container, false);


        surveyListView = view.findViewById(R.id.userProfie_anketlerim_lv);
        surveyList.add(new UserProfileSurvey("Soru1","Giyim", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(), R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,10,3,7));
        surveyList.add(new UserProfileSurvey("Soru2 hangisi dah gljflsd feslkfs fsklfjs gslgkjsg gksjdgl s sdlgsjdg gsdlgjsdlgkjsd gsdgkjsdlgkjd gsdlgkjdsgs dglsdkjg sldgj sdlgjsd gld gdlgjsdg","Giyim", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(), R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,10,3,7));
        surveyList.add(new UserProfileSurvey("Soru3 hangisi dah gljflsd feslkfs fsklfjs gslgkjsg gksjdgl s sdlgsjdg gsdlgjsdlgkjsd gsdgkjsdlgkjd gsdlgkjdsgs dglsdkjg sldgj sdlgjsd gld gdlgjsdg","Giyim", Calendar.getInstance().getTime(),Calendar.getInstance().getTime(), R.drawable.ic_menu_gallery, R.drawable.ic_menu_gallery,10,3,7));

        adapter = new UserProfileSurveyCustomAdapter(getContext(),surveyList);
        surveyListView.setAdapter(adapter);

        editProfile = view.findViewById(R.id.userProfile_profilDuzenlbtn);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragTrans.replace(R.id.fragmentContainer, new EditProfileFragment()).commit();
            }
        });

        sence = view.findViewById(R.id.userProfile_sence_btn);
        sence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragTrans.replace(R.id.fragmentContainer, new UserProfileSenceFragment()).commit();
            }
        });
        bence = view.findViewById(R.id.userProfile_bence_btn);
        bence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragTrans.replace(R.id.fragmentContainer, new UserProfileBenceFragment()).commit();
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

        takipcilerim = view.findViewById(R.id.userProfile_follower_btn);

        takipcilerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragTrans.replace(R.id.fragmentContainer, new UserProfileFollowersFragment()).commit();
            }
        });
        takipEttiklerim = view.findViewById(R.id.userProfile_following_btn);

        takipEttiklerim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragTrans.replace(R.id.fragmentContainer, new UserProfileFollowingFragment()).commit();
            }
        });
        return view;
    }

}
