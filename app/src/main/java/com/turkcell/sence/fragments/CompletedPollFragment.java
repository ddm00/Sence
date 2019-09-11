package com.turkcell.sence.fragments;


import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.turkcell.sence.R;
import com.turkcell.sence.adapters.CompletedPollAdapter;
import com.turkcell.sence.models.Complated;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedPollFragment extends Fragment {
    View view;
    ListView listView;
    CompletedPollAdapter completedPollAdapter;
    List<Complated> complatedList= new ArrayList<>();

    public CompletedPollFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_completed_poll, container, false);
        listView= view.findViewById(R.id.completed_Lv);




        completedPollAdapter = new CompletedPollAdapter(getContext(),complatedList);
        listView.setAdapter(completedPollAdapter);

        return  view;
    }


}
