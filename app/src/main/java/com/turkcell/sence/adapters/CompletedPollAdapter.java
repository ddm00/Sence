package com.turkcell.sence.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.turkcell.sence.R;
import com.turkcell.sence.models.Complated;


import java.util.List;

public class CompletedPollAdapter extends BaseAdapter {
    Context context;
    List<Complated> complatedList ;


    public CompletedPollAdapter(Context context, List<Complated>ongoingList)
    {
        this.context=context;
        this.complatedList=ongoingList;
    }

    @Override
    public int getCount() {
        return complatedList.size();
    }

    @Override
    public Object getItem(int position) {
        return complatedList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        view=View.inflate(context, R.layout.completed_row,null);

        ImageView image=view.findViewById(R.id.firstPhotoF_iv);
        ImageView  image2=view.findViewById(R.id.secondPhotoS_iv);
        TextView question_Tv= view.findViewById(R.id.questionQ_Tv);
        TextView time_Tv= view.findViewById(R.id.timeT_Tv);
        TextView number_of_participants= view.findViewById(R.id.participantsP);


        image.setImageResource( complatedList.get(position).firstPhotoF_iv());
        image2.setImageResource(complatedList.get(position).secondPhotoS_iv());
        question_Tv.setText(complatedList.get(position).questionQ_Tv());
        time_Tv.setText(complatedList.get(position).timeT_Tv());
        number_of_participants.setText(complatedList.get(position).participantsP());


        return view;
    }


}
