package com.turkcell.sence.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.turkcell.sence.R;
import com.turkcell.sence.adapters.OngoingPollAdapter;
import com.turkcell.sence.models.Ongoing;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class OngoingPollFragment extends Fragment {

    View view;
    ListView listView;
    OngoingPollAdapter ongoingPollAdapter;
    List<Ongoing>ongoingList=new ArrayList<>();

    public OngoingPollFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view= inflater.inflate(R.layout.fragment_ongoing_poll, container, false);
        listView= view.findViewById(R.id.ongoing_Lv);

        ongoingPollAdapter= new OngoingPollAdapter(getContext(),ongoingList);
        listView.setAdapter(ongoingPollAdapter);
        return view;

    }

}
