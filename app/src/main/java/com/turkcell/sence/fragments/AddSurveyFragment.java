package com.turkcell.sence.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.turkcell.sence.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddSurveyFragment extends Fragment {
    View view;
    EditText Question_Et;
    ImageView First_Iv,Second_Iv;
    Spinner Category_Sp, Duration_Sp;
    Button Publish_Btn;
    List<String> Categories=new ArrayList<>();
    List<String> Durations=new ArrayList<>();


    public AddSurveyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_add_survey, container, false);
        Init();
        return view;
    }
    public void Init(){
        Question_Et=view.findViewById(R.id.addsurveyQuestion_Et);
        First_Iv=view.findViewById(R.id.addsurveyFirst_Iv);
        Second_Iv=view.findViewById(R.id.addsurveySecond_Iv);
        Category_Sp=view.findViewById(R.id.addsurveyCategory_Sp);
        Duration_Sp=view.findViewById(R.id.addsurveyTime_Sp);
        Publish_Btn=view.findViewById(R.id.addsurveyPublish_Btn);
    }

}
