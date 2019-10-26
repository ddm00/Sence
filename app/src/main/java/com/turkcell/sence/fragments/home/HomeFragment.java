package com.turkcell.sence.fragments.home;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thomashaertel.widget.MultiSpinner;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.adapters.ViewPagerFragmentAdapter;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.Survey;
import com.turkcell.sence.models.User;
import com.turkcell.sence.time.DateRegulative;
import com.turkcell.sence.time.MyDateFormat;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private MultiSpinner multiSpinner;
    private ArrayAdapter<String> adapter;
    private View view;
    private ViewPager viewPager;
    private List<Survey> surveyList = new ArrayList<>();
    private FrameLayout homeFrame;
    private TextView pageTv, warningTv;
    private Activity activity;
    private ViewPagerFragmentAdapter viewPagerFragmentAdapter;
    private boolean[] isMultiSelected;

    String[] catogories = {"Alışveriş", "Elektronik", "Ev-Yaşam", "Anne-Bebek", "Spor", "Kitap-Müzik-Film-Oyun", "Tatil-Eğlence", "Otomobil-Motorsiklet", "Diğer"};

    public HomeFragment(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.homeViewpager_Vp);
        pageTv = view.findViewById(R.id.homePage_Tv);
        warningTv = view.findViewById(R.id.homeWarning_Tv);

        viewPagerFragmentAdapter = new ViewPagerFragmentAdapter(getChildFragmentManager(), surveyList, activity);
        setMultiSpinner();
        getSurveys();

        return view;
    }

    private void getSurveys() {
        List<Fragment> fragmentList = getChildFragmentManager().getFragments();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        for (Fragment fragment : fragmentList) {
            if (fragment instanceof HomeChildFragment) {
                fragmentTransaction.remove(fragment);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
        surveyList.clear();
        viewPagerFragmentAdapter.notifyDataSetChanged();
        homeFrame = view.findViewById(R.id.homeProgressFrame_Fl);
        homeFrame.setVisibility(View.VISIBLE);
        viewPager.setVisibility(GONE);
        warningTv.setVisibility(GONE);
        Dao.getInstance().getFirebaseDatabase().getReference("Surveys").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    final List<Map<String, Object>> mapList = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HashMap<String, Object> hashMap = (HashMap<String, Object>) snapshot.getValue();
                        if (hashMap != null) {
                            mapList.add(hashMap);
                        }
                    }
                    String publisher, surveyId, question, category, time, surveyFirstImage, surveySecondImage;
                    Long t;
                    boolean isSecret;
                    for (int i = 0; i < mapList.size(); i++) {

                        publisher = (String) mapList.get(i).get("publisher");
                        surveyId = (String) mapList.get(i).get("surveyId");
                        question = (String) mapList.get(i).get("question");
                        category = (String) mapList.get(i).get("category");
                        surveyFirstImage = (String) mapList.get(i).get("surveyFirstImage");
                        surveySecondImage = (String) mapList.get(i).get("surveySecondImage");
                        isSecret = (boolean) mapList.get(i).get("isSecret");
                        time = (String) mapList.get(i).get("time");
                        t = (Long) mapList.get(i).get("t");

                        for (int k = 0; k < isMultiSelected.length; k++) {

                            if (t != null && time != null && category != null) {

                                String farkDay = farkHesap(t, time);
                                if (!farkDay.equals("") && category.equals(catogories[k]) && isMultiSelected[k]) {

                                    Map<String, Object> map = (Map<String, Object>) mapList.get(i).get("Users");
                                    Boolean isWhichOne = null;
                                    int reySize = 0;
                                    if (map != null) {
                                        Map<String, Object> map1 = (Map<String, Object>) map.get(MainActivity.CurrentUser.getId());
                                        if (map1 != null) {
                                            isWhichOne = (Boolean) map1.get("value");
                                        }
                                        reySize = map.size();

                                    }

                                    final Survey survey = new Survey(surveyId, question, surveyFirstImage, surveySecondImage, time, category, publisher, t, isWhichOne, reySize, isSecret);

                                    Dao.getInstance().getFirebaseDatabase().getReference("Users").child(survey.getSurveyPublisher())
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    final User user = dataSnapshot.getValue(User.class);
                                                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                                    user.setOpen((boolean) map.get("isOpen"));

                                                    if (user.isOpen() && !user.getId().equals(MainActivity.CurrentUser.getId())) {
                                                        survey.setUser(user);
                                                        surveyList.add(survey);
                                                        addAdapter();

                                                    } else {
                                                        if (!user.getId().equals(MainActivity.CurrentUser.getId())) {
                                                            FirebaseDatabase.getInstance().getReference().child("Follow").child(MainActivity.CurrentUser.getId())
                                                                    .child("following").child(user.getId()).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    Boolean isOpen = (Boolean) dataSnapshot.getValue();
                                                                    if (isOpen != null && isOpen) {
                                                                        survey.setUser(user);
                                                                        surveyList.add(survey);
                                                                        addAdapter();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                }
                                                            });
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                }
                                            });
                                }
                            }

                        }
                        if (i + 1 == mapList.size()) {
                            hasSurvey();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hasSurvey() {
        if (surveyList.size() == 0) {
            warningTv.setVisibility(View.VISIBLE);
            warningTv.setText("Seçtiğiniz kategori veya kategorilerde anket bulunamadı.");
            homeFrame.setVisibility(GONE);
            viewPager.setVisibility(View.GONE);
        } else {
            warningTv.setVisibility(GONE);
            homeFrame.setVisibility(View.VISIBLE);
            viewPager.setVisibility(GONE);
        }
    }

    private void setMultiSpinner() {
        adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item);
        adapter.add("Alışveriş");
        adapter.add("Elektronik");
        adapter.add("Ev-Yaşam");
        adapter.add("Anne-Bebek");
        adapter.add("Spor");
        adapter.add("Kitap-Müzik-Film-Oyun");
        adapter.add("Tatil-Eğlence");
        adapter.add("Otomobil-Motorsiklet");
        adapter.add("Diğer");

        multiSpinner = view.findViewById(R.id.homeMultispinner_Sp);
        multiSpinner.setAdapter(adapter, false, onSelectedListener);

        isMultiSelected = new boolean[adapter.getCount()];
        isMultiSelected[0] = true;
        multiSpinner.setSelected(isMultiSelected);
    }

    private MultiSpinner.MultiSpinnerListener onSelectedListener = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] selected) {
            isMultiSelected = selected;
            getSurveys();
        }
    };

    @SuppressLint("SetTextI18n")
    private void addAdapter() {
        pageTv.setText(surveyList.size() + "/1");

        viewPager.setSaveFromParentEnabled(false);
        viewPager.setAdapter(viewPagerFragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageTv.setText(surveyList.size() + "/" + (position + 1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        homeFrame.setVisibility(GONE);
        viewPager.setVisibility(View.VISIBLE);

    }

    private String farkHesap(long longDate, String sTime) {

        String fark = "";
        MyDateFormat myDateFormat = DateRegulative.getInstance().getDifference(longDate);

        switch (sTime) {
            case "30 dk":
                if (myDateFormat.getsMinute() < 30 && myDateFormat.getsHour() == 0 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "1 saat":
                if (myDateFormat.getsMinute() < 60 && myDateFormat.getsHour() == 0 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "1 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() == 0 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "3 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 2 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "5 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 4 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
            case "7 gün":
                if (myDateFormat.getsHour() < 24 && myDateFormat.getsDay() < 6 && myDateFormat.getsMonth() == 0 && myDateFormat.getsYear() == 0) {
                    fark = DateRegulative.getInstance().getStringFormat(myDateFormat);
                }
                break;
        }
        return fark;
    }

}
