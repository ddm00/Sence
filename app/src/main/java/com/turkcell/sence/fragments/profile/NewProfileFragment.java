package com.turkcell.sence.fragments.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.fragments.profile.tab.UserProfileBenceFragment;
import com.turkcell.sence.fragments.profile.tab.UserProfileSenceFragment;
import com.turkcell.sence.fragments.profile.tab.UserProfileSurveyFragment;
import com.turkcell.sence.models.User;

/**
 * A simple {@link Fragment} subclass.
 */

@SuppressLint("ValidFragment")
public class NewProfileFragment extends Fragment {

    private View view;
    private FragmentManager supportFragmentManager;
    private TextView fullNameTv, followingTv, followerTv, surveyTv, senceTv, benceTv;
    private ViewPager userProfileVp;
    private Activity activity;
    private User user;
    private boolean isFollowing = false;

    public NewProfileFragment(FragmentManager supportFragmentManager, User user, Activity activity) {
        this.supportFragmentManager = supportFragmentManager;
        this.user = user;
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_old_profile, container, false);
        fullNameTv = view.findViewById(R.id.newprofileFullname_Tv);
        followingTv = view.findViewById(R.id.newprofileFollowing_Tv);
        followerTv = view.findViewById(R.id.newprofileFollower_Tv);
        surveyTv = view.findViewById(R.id.newprofileSurveys_Tv);
        senceTv = view.findViewById(R.id.newprofileSence_Tv);
        benceTv = view.findViewById(R.id.newprofileBence_Tv);
        userProfileVp = view.findViewById(R.id.newprofileUserProfile_Vp);
        fullNameTv.setText(user.getFullname());

        getTakipTakipciIstekToplam();

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (user.getId().equals(snapshot.getKey())) {
                        isFollowing = true;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //ilk başlangıç
        userProfileVp.setCurrentItem(0);
        setTextView(20, 14, 14, getResources().getColor(R.color.colorBlack), getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray));

        surveyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileVp.setCurrentItem(0);
                setTextView(20, 14, 14, getResources().getColor(R.color.colorBlack), getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray));
            }
        });

        senceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileVp.setCurrentItem(1);
                setTextView(14, 20, 14, getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorBlack), getResources().getColor(R.color.colorGray));
            }
        });

        benceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfileVp.setCurrentItem(2);
                setTextView(14, 14, 20, getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorBlack));
            }
        });

        userProfileVp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new UserProfileSurveyFragment(activity, user, true);
                    case 1:
                        return new UserProfileSenceFragment(activity, user, true);
                    case 2:
                        return new UserProfileBenceFragment(activity, user, true);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        userProfileVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setTextView(20, 14, 14, getResources().getColor(R.color.colorBlack), getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray));
                        break;
                    case 1:
                        setTextView(14, 20, 14, getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorBlack), getResources().getColor(R.color.colorGray));
                        break;
                    case 2:
                        setTextView(14, 14, 20, getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorBlack));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        followingTv.setOnClickListener(new View.OnClickListener() { // takip ettiklerim
            @Override
            public void onClick(View v) {
                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    UserProfileFollowingFragment myFragment = new UserProfileFollowingFragment(supportFragmentManager, activity, user);
                    transaction.replace(R.id.fragmentContainer, myFragment, "UserProfileFollowingFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli ve siz onu takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }

            }
        });

        followerTv.setOnClickListener(new View.OnClickListener() { // takipçilerim
            @Override
            public void onClick(View v) {
                if (isFollowing || user.isOpen()) {
                    FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                    UserProfileFollowersFragment myFragment = new UserProfileFollowersFragment(supportFragmentManager, activity, user);
                    transaction.replace(R.id.fragmentContainer, myFragment, "UserProfileFollowersFragment");
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(view.getContext(), "Kullanıcı hesabı gizli ve siz takip etmiyorsunuz.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getTakipTakipciIstekToplam() {

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(user.getId()).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followerTv.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(user.getId()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingTv.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void setTextView(int i1, int i2, int i3, int color1, int color2, int color3) {

        surveyTv.setTextColor(color1);
        senceTv.setTextColor(color2);
        benceTv.setTextColor(color3);

        surveyTv.setTextSize(i1);
        senceTv.setTextSize(i2);
        benceTv.setTextSize(i3);

    }

}
