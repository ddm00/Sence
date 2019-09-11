package com.turkcell.sence.adapters;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.turkcell.sence.fragments.home.HomeChildFragment;
import com.turkcell.sence.models.Survey;

import java.util.List;

public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Survey> surveyList;
    private Activity activity;


    public ViewPagerFragmentAdapter(FragmentManager childFragmentManager, List<Survey> surveyList, Activity activity) {
        super(childFragmentManager);
        this.surveyList = surveyList;
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        return new HomeChildFragment(surveyList.get(position), activity);
    }

    @Override
    public int getCount() {
        return surveyList.size();
    }


}
