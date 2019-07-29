package com.turkcell.sence.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.turkcell.sence.R;
import com.turkcell.sence.models.Ongoing;

import java.util.List;

public class OngoingPollAdapter extends BaseAdapter{

    Context context;
    List<Ongoing> ongoingList ;


    public OngoingPollAdapter(Context context, List<Ongoing>ongoingList)
    {
        this.context=context;
        this.ongoingList=ongoingList;
    }

    @Override
    public int getCount() {
        return ongoingList.size();
    }

    @Override
    public Object getItem(int position) {
        return ongoingList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view;
        view=View.inflate(context, R.layout.ongoing_row,null);

        ImageView image=view.findViewById(R.id.firstPhoto_iv);
        ImageView  image2=view.findViewById(R.id.secondPhoto_iv);
        TextView question_Tv= view.findViewById(R.id.question_Tv);
        TextView time_Tv= view.findViewById(R.id.time_Tv);
        TextView number_of_participants= view.findViewById(R.id.participants);


        image.setImageResource( ongoingList.get(position).firstPhoto_iv());
        image2.setImageResource(ongoingList.get(position).secondPhoto_iv());
        question_Tv.setText(ongoingList.get(position).question_Tv());
        time_Tv.setText(ongoingList.get(position).time_Tv());
        number_of_participants.setText(ongoingList.get(position).participants());


        return view;
    }


}
