package com.turkcell.sence.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        fillList();



        completedPollAdapter = new CompletedPollAdapter(getContext(),complatedList);
        listView.setAdapter(completedPollAdapter);

        return  view;
    }

    void fillList ()
    {
        complatedList.add(new Complated(R.drawable.kitap1,R.drawable.kitap2,"Sizce ilk Hangisini okumalıyım? İkisi de mükemmel, karar veremiyorum.","01:00","50"));
        complatedList.add(new Complated(R.drawable.ruj1,R.drawable.ruj2,"Hangi renk akşam yemeği için daha uygun?","01:00","25"));

    }

}
