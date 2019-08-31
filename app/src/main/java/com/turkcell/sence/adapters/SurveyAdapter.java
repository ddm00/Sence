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
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.Survey;

import java.util.List;

public class SurveyAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Survey> surveyList;

    public SurveyAdapter(Context context, List<Survey> surveyList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.surveyList = surveyList;
        this.context = context;
    }

    public List<Survey> getSurveyList() {
        return surveyList;
    }

    public void updateResults(List<Survey> sList) {
        this.surveyList = sList;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return surveyList.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ImageView surveyFirstImage, surveyFirstLike, surveySecondImage, surveySecondLike;
        final TextView userName, surveyVotes, surveyQuestion, surveyTime;
        View view = layoutInflater.inflate(R.layout.list_view_item_survey, null);

        userName = view.findViewById(R.id.userName_Tv);
        surveyQuestion = view.findViewById(R.id.surveyQuestion_Tv);
        surveyVotes = view.findViewById(R.id.surveyVotes_Tv);
        surveyTime = view.findViewById(R.id.surveyTime_Tv);

        surveyFirstImage = view.findViewById(R.id.surveyFirstImage_Iv);
        surveyFirstLike = view.findViewById(R.id.surveyFirstLike_Iv);
        surveySecondImage = view.findViewById(R.id.surveySecondImage_Iv);
        surveySecondLike = view.findViewById(R.id.surveySecondLike_Iv);

        final Survey survey = surveyList.get(position);
        if (survey.getWhichOne() == null) {
            surveyFirstLike.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
            surveySecondLike.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
        } else {
            if (survey.getWhichOne()) {
                surveyFirstLike.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                surveySecondLike.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
            } else {
                surveyFirstLike.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
                surveySecondLike.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
            }
        }
        surveyFirstLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dao.getInstance().getFirebaseDatabase().getReference("Surveys").child(survey.getSurveyId())
                        .child("Users").child(MainActivity.CurrentUser.getId()).child("value").setValue(true);
                surveyFirstLike.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                surveySecondLike.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
                if (survey.getWhichOne() == null) {
                    survey.setReySize(survey.getReySize() + 1);
                    surveyVotes.setText(survey.getReySize() + "oylama yapıldı.");
                    survey.setWhichOne(true);
                }
            }
        });
        surveySecondLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dao.getInstance().getFirebaseDatabase().getReference("Surveys").child(survey.getSurveyId())
                        .child("Users").child(MainActivity.CurrentUser.getId()).child("value").setValue(false);
                surveyFirstLike.setImageDrawable(context.getResources().getDrawable(R.drawable.unlike));
                surveySecondLike.setImageDrawable(context.getResources().getDrawable(R.drawable.like));
                if (survey.getWhichOne() == null) {
                    survey.setReySize(survey.getReySize() + 1);
                    surveyVotes.setText(survey.getReySize() + "oylama yapıldı.");
                    survey.setWhichOne(false);
                }
            }
        });
        if (!survey.getSecret() || MainActivity.CurrentUser.getId().equals(survey.getSurveyPublisher())) {
            userName.setText(survey.getUser().getFullname() + "/" + survey.getUser().getUsername());
        } else {
            userName.setText("Kullanıcı ismini paylaşmak istemiyor");
        }
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(survey.getSurveyFirstImage()).into(surveyFirstImage);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(survey.getSurveySecondImage()).into(surveySecondImage);

        surveyQuestion.setText(survey.getSurveyQuestion());
        surveyVotes.setText(survey.getReySize() + "oylama yapıldı.");
        surveyTime.setText(survey.getSurveyTime());

        return view;

    }


}
