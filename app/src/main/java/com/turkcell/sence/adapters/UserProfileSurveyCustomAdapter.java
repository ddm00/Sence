package com.turkcell.sence.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.turkcell.sence.R;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.Survey;
import com.turkcell.sence.time.DateRegulative;
import com.turkcell.sence.time.MyDateFormat;


import java.util.Calendar;
import java.util.List;

public class UserProfileSurveyCustomAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Survey> myList;
    private Survey survey;
    private Activity activity;

    public UserProfileSurveyCustomAdapter(Activity activity, Context context, List<Survey> myList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.myList = myList;
        this.context = context;
        this.activity = activity;
    }

    public List<Survey> getMyList() {
        return myList;
    }

    public void updateResults(List<Survey> surveyList) {
        this.myList = surveyList;
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

        final TextView userName, question, votes, time;
        final ImageView firstImage, secondImage;

        View view = layoutInflater.inflate(R.layout.list_view_item_user_survey, null);

        userName = view.findViewById(R.id.usersurveyUsername_Tv);
        question = view.findViewById(R.id.usersurveyQuestion_Tv);
        votes = view.findViewById(R.id.usersurveyUsers_Tv);
        time = view.findViewById(R.id.usersurveyTime_Tv);
        LinearLayout delete = view.findViewById(R.id.usersurveyDelete_Ll);

        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Sence");
                builder.setMessage("Silmek istediğinizden emin misiniz ?");
                builder.setIcon(context.getResources().getDrawable(R.mipmap.ic_launcher));

                builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseReference databaseReference = Dao.getInstance().getFirebaseDatabase().getReference("Surveys").child(myList.get(position).getSurveyId());

                        databaseReference.removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                myList.remove(position);
                                updateResults(myList);
                                Toast.makeText(context, "İşleminiz başarı ile gerçeklertirildi.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();


                return false;
            }
        });

        firstImage = view.findViewById(R.id.usersurveyFirstImage_Iv);
        secondImage = view.findViewById(R.id.usersurveySecondImage_Iv);

        survey = myList.get(position);
        userName.setText(survey.getUser().getFullname() + " / " + survey.getUser().getUsername());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(survey.getSurveyFirstImage()).into(firstImage);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(survey.getSurveySecondImage()).into(secondImage);

        question.setText(survey.getSurveyCategory() + ":" + survey.getSurveyQuestion());
        votes.setText(survey.getReySize() + " oylama yapıldı.");
        time.setText(survey.getSurveyTime());
        if (!survey.getSurveyTime().equals("Anketin süresi doldu.")) {
            Thread thread = new Thread() {
                public void run() {

                    while (true) {
                        try {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (myList != null && myList.size() >= 1) {
                                        time.setText(farkHesap(myList.get(position).getT(), myList.get(position).getSurveyTime()));
                                    }
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
        } else {
            time.setText(survey.getSurveyTime());
        }
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
