package com.turkcell.sence.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.turkcell.sence.R;
import com.turkcell.sence.models.UserProfileSurvey;

import java.util.ArrayList;

public class UserProfileSurveyCustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<UserProfileSurvey> surveyList;


    public UserProfileSurveyCustomAdapter(Context context, ArrayList<UserProfileSurvey> surveyList){
        this.context = context;
        this.surveyList = surveyList;
    }

    @Override
    public int getCount() {
        return surveyList.size();
    }

    @Override
    public Object getItem(int position) {
        return surveyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(context, R.layout.user_profile_survey_row, null);

        TextView soru = view.findViewById(R.id.userProfileSurvey_soru_tv);
        ImageView image1 = view.findViewById(R.id.userProfileSurvey_image1_iv);
        ImageView image2 = view.findViewById(R.id.userProfileSurvey_image2_iv);
        TextView image1OySayisi = view.findViewById(R.id.userProfileSurvey_image1OySayisi_tv);
        TextView image2OySayisi = view.findViewById(R.id.userProfileSurvey_image2OySayisi_tv);
        TextView kategori = view.findViewById(R.id.userProfileSurvey_kategori_tv);
        TextView bitisZamani = view.findViewById(R.id.userProfileSurvey_bitisZamani_tv);

        image1.setBackgroundResource(surveyList.get(position).getImage1());
        image2.setBackgroundResource(surveyList.get(position).getImage2());
        soru.setText(surveyList.get(position).getSoru());
        image1OySayisi.setText(surveyList.get(position).getImage1OySayisi()+"");
        image2OySayisi.setText(surveyList.get(position).getImage2OySayisi()+"");
        kategori.setText(surveyList.get(position).getKategori());
        bitisZamani.setText(surveyList.get(position).getBitisZamani().toString());


        return view;
    }
}
