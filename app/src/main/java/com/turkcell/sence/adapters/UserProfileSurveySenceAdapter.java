package com.turkcell.sence.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.turkcell.sence.time.DateRegulative;
import com.turkcell.sence.time.MyDateFormat;

import java.util.Calendar;
import java.util.List;

public class UserProfileSurveySenceAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private Context context;
    private List<Survey> myList;
    private Survey survey;
    private Activity activity;

    public UserProfileSurveySenceAdapter(Activity activity, Context context, List<Survey> myList) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.myList = myList;
        this.context = context;
        this.activity = activity;
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

        final TextView username, question, surveyTime, fistimagePercent, secondimagePercent;
        final ImageView fistImage, secondImage, fistimageWin, secondimageWin;

        View view = layoutInflater.inflate(R.layout.list_view_item_sence_survey, null);

        username = view.findViewById(R.id.senceUsername_Tv);
        question = view.findViewById(R.id.senceQuestion_Tv);
        surveyTime = view.findViewById(R.id.senceSurveyTime);
        fistimagePercent = view.findViewById(R.id.senceFirstImagePercent_Tv);
        secondimagePercent = view.findViewById(R.id.senceSecondImagePercent_Tv);

        fistImage = view.findViewById(R.id.senceFirstImage_Iv);
        secondImage = view.findViewById(R.id.senceSecondImage_Iv);

        fistimageWin = view.findViewById(R.id.senceFirstImageWin_Iv);
        secondimageWin = view.findViewById(R.id.senceSecondImageWin_Iv);

        survey = myList.get(position);
        username.setText(survey.getUser().getFullname() + " / " + survey.getUser().getUsername());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.ic_launcher);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(survey.getSurveyFirstImage()).into(fistImage);
        Glide.with(context).setDefaultRequestOptions(requestOptions).load(survey.getSurveySecondImage()).into(secondImage);

        question.setText(survey.getSurveyCategory() + ": " + survey.getSurveyQuestion());
        surveyTime.setText(survey.getSurveyTime());

        //burada hali hazırda var olna entity yi bozmamak adına oylama sayılarının geldiği "survey.getReySize()"
        //üzerinde 1. resmin yüzdesini alıcaz ve diğerini burada hesaplıyacağız. yani gelen veri 1. resmin hesaplanmış yüzdesi olacak
        //ikinci resminkini biz burada hesaplayacağız.

        int firstImagePercent = myList.get(position).getReySize();

        int secondImagePercent = 100 - firstImagePercent;

        if (firstImagePercent == secondImagePercent) {
            fistimagePercent.setText("% " + firstImagePercent);
            fistimagePercent.setTextColor(context.getResources().getColor(R.color.colorYellow));
            fistimageWin.setImageDrawable(context.getResources().getDrawable(R.drawable.image_equal_background));

            secondimagePercent.setText("% " + secondImagePercent);
            secondimagePercent.setTextColor(context.getResources().getColor(R.color.colorYellow));
            secondimageWin.setImageDrawable(context.getResources().getDrawable(R.drawable.image_equal_background));

        } else if (firstImagePercent < secondImagePercent) {
            fistimagePercent.setText("% " + firstImagePercent);
            fistimagePercent.setTextColor(context.getResources().getColor(R.color.colorRed));
            fistimageWin.setImageDrawable(context.getResources().getDrawable(R.drawable.image_lose_background));

            secondimagePercent.setText("% " + secondImagePercent);
            secondimagePercent.setTextColor(context.getResources().getColor(R.color.colorGreen));
            secondimageWin.setImageDrawable(context.getResources().getDrawable(R.drawable.image_win_background));

        } else {
            secondimagePercent.setText("% " + firstImagePercent);
            secondimagePercent.setTextColor(context.getResources().getColor(R.color.colorRed));
            secondimageWin.setImageDrawable(context.getResources().getDrawable(R.drawable.image_lose_background));

            fistimagePercent.setText("% " + secondImagePercent);
            fistimagePercent.setTextColor(context.getResources().getColor(R.color.colorGreen));
            fistimageWin.setImageDrawable(context.getResources().getDrawable(R.drawable.image_win_background));
        }


        if (!survey.getSurveyTime().equals("Anketin süresi doldu.")) {
            Thread thread = new Thread() {
                public void run() {

                    while (true) {
                        try {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    surveyTime.setText(farkHesap(myList.get(position).getT(), myList.get(position).getSurveyTime()));
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
            surveyTime.setText(survey.getSurveyTime());
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
