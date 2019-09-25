package com.turkcell.sence.fragments.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.Survey;
import com.turkcell.sence.time.DateRegulative;
import com.turkcell.sence.time.MyDateFormat;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeChildFragment extends Fragment {

    private View view;
    private Survey survey;
    private Activity activity;

    public HomeChildFragment(final Survey survey, Activity activity) {
        this.survey = survey;
        this.activity = activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_child, container, false);

        final TextView username, question, votes, surveyTime;
        final ImageView fistImage, secondImage, firstimageLike, secondimageLike;

        username = view.findViewById(R.id.homechildUsername_Tv);
        question = view.findViewById(R.id.homechildQuestion_Tv);
        votes = view.findViewById(R.id.homechildVotes_Tv);
        surveyTime = view.findViewById(R.id.homechildSurveyTime_Tv);

        fistImage = view.findViewById(R.id.homechildFistImage_Iv);
        secondImage = view.findViewById(R.id.homechildSecondImage_Iv);
        firstimageLike = view.findViewById(R.id.homechildFirstLike_Iv);
        secondimageLike = view.findViewById(R.id.homechildSecondLike_Iv);

        if (survey.getWhichOne() == null) {
            firstimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.unlike));
            secondimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.unlike));
        } else {
            if (survey.getWhichOne()) {
                firstimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.like));
                secondimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.unlike));
            } else {
                firstimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.unlike));
                secondimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.like));
            }
        }

        firstimageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dao.getInstance().getFirebaseDatabase().getReference("Surveys").child(survey.getSurveyId())
                        .child("Users").child(MainActivity.CurrentUser.getId()).child("value").setValue(true);
                firstimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.like));
                secondimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.unlike));
                if (survey.getWhichOne() == null) {
                    survey.setReySize(survey.getReySize() + 1);
                    votes.setText(survey.getReySize() + " oylama yapıldı.");
                    survey.setWhichOne(true);
                }
            }
        });

        secondimageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dao.getInstance().getFirebaseDatabase().getReference("Surveys").child(survey.getSurveyId())
                        .child("Users").child(MainActivity.CurrentUser.getId()).child("value").setValue(false);
                firstimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.unlike));
                secondimageLike.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.like));
                if (survey.getWhichOne() == null) {
                    survey.setReySize(survey.getReySize() + 1);
                    votes.setText(survey.getReySize() + " oylama yapıldı.");
                    survey.setWhichOne(false);
                }
            }
        });

        if (!survey.getSecret() || MainActivity.CurrentUser.getId().equals(survey.getSurveyPublisher())) {
            username.setText(survey.getUser().getFullname() + " / " + survey.getUser().getUsername());
        } else {
            username.setText("Kullanıcı ismini paylaşmak istemiyor.");
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(view.getContext()).setDefaultRequestOptions(requestOptions).load(survey.getSurveyFirstImage()).into(fistImage);
        Glide.with(view.getContext()).setDefaultRequestOptions(requestOptions).load(survey.getSurveySecondImage()).into(secondImage);

        question.setText(survey.getSurveyCategory() + ": " + survey.getSurveyQuestion());
        votes.setText(survey.getReySize() + " oylama yapıldı.");

        Thread thread = new Thread() {
            public void run() {

                while (true) {
                    try {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                surveyTime.setText(farkHesap(survey.getT(), survey.getSurveyTime()));
                            }
                        });
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread.start();

        return view;
    }

    private String farkHesap(long longDate, String sTime) {

        String fark = "";
        MyDateFormat myDateFormat = DateRegulative.getInstance().getDifference(longDate);

        switch (sTime) {
            case "30 dk":
                if (myDateFormat.getsMinute() < 30 && myDateFormat.getsHour() == 0 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {

                    Calendar currentDiffirentTime = Calendar.getInstance();
                    currentDiffirentTime.set(myDateFormat.getsYear(), myDateFormat.getsMonth(), myDateFormat.getsDay(), myDateFormat.getsHour(), myDateFormat.getsMinute(), myDateFormat.getsSecond());
                    Calendar setTime = Calendar.getInstance();
                    setTime.set(Calendar.YEAR, 0);
                    setTime.set(Calendar.MONTH, 0);
                    setTime.set(Calendar.DAY_OF_MONTH, 0);
                    setTime.set(Calendar.HOUR_OF_DAY, 0);
                    setTime.set(Calendar.MINUTE, 30);
                    setTime.set(Calendar.SECOND, 0);

                    MyDateFormat newMyDateFormat = DateRegulative.getInstance().getDifference(setTime.getTimeInMillis(), currentDiffirentTime.getTimeInMillis());
                    fark = DateRegulative.getInstance().getStringFormat(newMyDateFormat);
                }
                break;
            case "1 saat":
                if (myDateFormat.getsMinute() < 60 && myDateFormat.getsHour() == 0 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {

                    Calendar currentDiffirentTime = Calendar.getInstance();
                    currentDiffirentTime.set(myDateFormat.getsYear(), myDateFormat.getsMonth(), myDateFormat.getsDay(), myDateFormat.getsHour(), myDateFormat.getsMinute(), myDateFormat.getsSecond());
                    Calendar setTime = Calendar.getInstance();
                    setTime.set(Calendar.YEAR, 0);
                    setTime.set(Calendar.MONTH, 0);
                    setTime.set(Calendar.DAY_OF_MONTH, 0);
                    setTime.set(Calendar.HOUR_OF_DAY, 1);
                    setTime.set(Calendar.MINUTE, 0);
                    setTime.set(Calendar.SECOND, 0);

                    MyDateFormat newMyDateFormat = DateRegulative.getInstance().getDifference(setTime.getTimeInMillis(), currentDiffirentTime.getTimeInMillis());
                    fark = DateRegulative.getInstance().getStringFormat(newMyDateFormat);
                }
                break;
            case "1 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    Calendar currentDiffirentTime = Calendar.getInstance();
                    currentDiffirentTime.set(myDateFormat.getsYear(), myDateFormat.getsMonth(), myDateFormat.getsDay(), myDateFormat.getsHour(), myDateFormat.getsMinute(), myDateFormat.getsSecond());
                    Calendar setTime = Calendar.getInstance();
                    setTime.set(Calendar.YEAR, 0);
                    setTime.set(Calendar.MONTH, 0);
                    setTime.set(Calendar.DAY_OF_MONTH, 1);
                    setTime.set(Calendar.HOUR_OF_DAY, 0);
                    setTime.set(Calendar.MINUTE, 0);
                    setTime.set(Calendar.SECOND, 0);

                    MyDateFormat newMyDateFormat = DateRegulative.getInstance().getDifference(setTime.getTimeInMillis(), currentDiffirentTime.getTimeInMillis());

                    fark = DateRegulative.getInstance().getStringFormat(newMyDateFormat);
                }
                break;
            case "3 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 2 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    Calendar currentDiffirentTime = Calendar.getInstance();
                    currentDiffirentTime.set(myDateFormat.getsYear(), myDateFormat.getsMonth(), myDateFormat.getsDay(), myDateFormat.getsHour(), myDateFormat.getsMinute(), myDateFormat.getsSecond());
                    Calendar setTime = Calendar.getInstance();
                    setTime.set(Calendar.YEAR, 0);
                    setTime.set(Calendar.MONTH, 0);
                    setTime.set(Calendar.DAY_OF_MONTH, 3);
                    setTime.set(Calendar.HOUR_OF_DAY, 0);
                    setTime.set(Calendar.MINUTE, 0);
                    setTime.set(Calendar.SECOND, 0);

                    MyDateFormat newMyDateFormat = DateRegulative.getInstance().getDifference(setTime.getTimeInMillis(), currentDiffirentTime.getTimeInMillis());

                    fark = DateRegulative.getInstance().getStringFormat(newMyDateFormat);
                }
                break;
            case "5 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 4 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    Calendar currentDiffirentTime = Calendar.getInstance();
                    currentDiffirentTime.set(myDateFormat.getsYear(), myDateFormat.getsMonth(), myDateFormat.getsDay(), myDateFormat.getsHour(), myDateFormat.getsMinute(), myDateFormat.getsSecond());
                    Calendar setTime = Calendar.getInstance();
                    setTime.set(Calendar.YEAR, 0);
                    setTime.set(Calendar.MONTH, 0);
                    setTime.set(Calendar.DAY_OF_MONTH, 5);
                    setTime.set(Calendar.HOUR_OF_DAY, 0);
                    setTime.set(Calendar.MINUTE, 0);
                    setTime.set(Calendar.SECOND, 0);

                    MyDateFormat newMyDateFormat = DateRegulative.getInstance().getDifference(setTime.getTimeInMillis(), currentDiffirentTime.getTimeInMillis());

                    fark = DateRegulative.getInstance().getStringFormat(newMyDateFormat);
                }
                break;
            case "7 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 6 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    Calendar currentDiffirentTime = Calendar.getInstance();
                    currentDiffirentTime.set(myDateFormat.getsYear(), myDateFormat.getsMonth(), myDateFormat.getsDay(), myDateFormat.getsHour(), myDateFormat.getsMinute(), myDateFormat.getsSecond());
                    Calendar setTime = Calendar.getInstance();
                    setTime.set(Calendar.YEAR, 0);
                    setTime.set(Calendar.MONTH, 0);
                    setTime.set(Calendar.DAY_OF_MONTH, 7);
                    setTime.set(Calendar.HOUR_OF_DAY, 0);
                    setTime.set(Calendar.MINUTE, 0);
                    setTime.set(Calendar.SECOND, 0);

                    MyDateFormat newMyDateFormat = DateRegulative.getInstance().getDifference(setTime.getTimeInMillis(), currentDiffirentTime.getTimeInMillis());

                    fark = DateRegulative.getInstance().getStringFormat(newMyDateFormat);
                }
                break;
        }
        return fark;
    }

}
