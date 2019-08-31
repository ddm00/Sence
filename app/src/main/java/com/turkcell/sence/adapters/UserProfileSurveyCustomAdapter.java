package com.turkcell.sence.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.turkcell.sence.R;
import com.turkcell.sence.models.Survey;


import java.util.List;

public class UserProfileSurveyCustomAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Survey> myList;
    private Survey survey;

    public UserProfileSurveyCustomAdapter(Context context, List<Survey> myList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.myList = myList;
        this.context = context;
    }

    public List<Survey> getMyList() {
        return myList;
    }

    public void updateResults(List<Survey> myCarsVeriModeli) {
        this.myList = myCarsVeriModeli;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final TextView usersurveyUsername, usersurveyQuestion, usersurveyVotes, userSurveyTime;
        final ImageView usersurveyFirstimage, usersurveySecondimage;

        View view = layoutInflater.inflate(R.layout.list_view_item_user_survey, null);

        usersurveyUsername = view.findViewById(R.id.usersurveyUsername_Tv);
        usersurveyQuestion = view.findViewById(R.id.usersurveyQuestion_Tv);
        usersurveyVotes = view.findViewById(R.id.usersurveyUsers_Tv);
        userSurveyTime = view.findViewById(R.id.usersurveyTime_Tv);

        usersurveyFirstimage = view.findViewById(R.id.usersurveyFirstImage_Iv);
        usersurveySecondimage = view.findViewById(R.id.usersurveySecondImage_Iv);

        survey = myList.get(position);
        usersurveyUsername.setText(survey.getUser().getFullname() + " / " + survey.getUser().getUsername());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(survey.getSurveyFirstImage()).into(usersurveyFirstimage);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(survey.getSurveySecondImage()).into(usersurveySecondimage);

        usersurveyQuestion.setText(survey.getSurveyQuestion());
        usersurveyVotes.setText("30 oylama yapıldı.");
        userSurveyTime.setText(survey.getSurveyTime());

        return view;
    }
}
