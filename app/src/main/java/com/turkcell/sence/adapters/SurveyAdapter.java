package com.turkcell.sence.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SurveyAdapter {
    public ImageView profileImage,surveyFirstImage,surveySecondImage,surveyVote;
    public TextView userName,surveyVotes,surveyPublisher,surveyQuestion,surveyTime;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
