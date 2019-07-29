package com.turkcell.sence.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.turkcell.sence.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    View view;
    ImageView profilePhoto_Iv;
    TextView profileName_Tv;
    Button followers_Btn, followed_Btn, ongoingPoll_Btn, numberOngoingPoll_Btn, completedPoll_Btn, numberCompletedPoll_Btn,
            votedPoll_Btn, numberVotedPoll_Btn;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        init();
        return view;
    }


    public void init(){
        profilePhoto_Iv = view.findViewById(R.id.profilePhoto_Iv);
        profileName_Tv =view.findViewById(R.id.profileName_Tv);
        followers_Btn = view.findViewById(R.id.followers_Btn);
        followed_Btn = view.findViewById(R.id.followed_Btn);
        ongoingPoll_Btn = view.findViewById(R.id.ongoingPoll_Btn);
        numberOngoingPoll_Btn =view.findViewById(R.id.numberOngoingPoll_Btn);
        completedPoll_Btn =view.findViewById(R.id.completedPoll_Btn);
        numberCompletedPoll_Btn = view.findViewById(R.id.numberCompletedPoll_Btn);
        votedPoll_Btn =view.findViewById(R.id.votedPoll_Btn);
        numberVotedPoll_Btn =view.findViewById(R.id.numberVotedPoll_Btn);
    }

}
