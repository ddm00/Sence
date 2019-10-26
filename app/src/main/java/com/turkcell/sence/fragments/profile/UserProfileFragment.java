package com.turkcell.sence.fragments.profile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.LoginActivity;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.fragments.profile.tab.UserProfileBenceFragment;
import com.turkcell.sence.fragments.profile.tab.UserProfileSenceFragment;
import com.turkcell.sence.fragments.profile.tab.UserProfileSurveyFragment;

@SuppressLint("ValidFragment")
public class UserProfileFragment extends Fragment {


    private View view;
    private FragmentManager supportFragmentManager;
    private ImageView userImageIv;
    private Button logoutBtn, editBtn;
    private TextView usernameTv, followingNumberTv, followerNumberTv, requestNumberTv, surveyTv, senceTv, benceTv, followTv, followerTv, requestBtn;
    private ViewPager userprofileVp;
    private Activity activity;

    public UserProfileFragment(FragmentManager supportFragmentManager, Activity activity) {
        this.supportFragmentManager = supportFragmentManager;
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        usernameTv = view.findViewById(R.id.userprofileUsername_Tv);
        followingNumberTv = view.findViewById(R.id.userprofileFollowingNumber_Tv);
        followerNumberTv = view.findViewById(R.id.userprofileFollowerNumber_Tv);
        requestNumberTv = view.findViewById(R.id.userprofileRequestNumber_Tv);
        followTv = view.findViewById(R.id.userprofileFollowing_Tv);
        followerTv = view.findViewById(R.id.userprofileFollower_Tv);
        requestBtn = view.findViewById(R.id.userprofileRequest_Tv);
        userImageIv = view.findViewById(R.id.userprofileUserimage_Iv);

        surveyTv = view.findViewById(R.id.userprofileSurvey_Tv);
        senceTv = view.findViewById(R.id.userprofileSence_Tv);
        benceTv = view.findViewById(R.id.userprofileBence_Tv);
        userprofileVp = view.findViewById(R.id.userprofileViewPager);
        usernameTv.setText(MainActivity.CurrentUser.getFullname());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_account_circle_black_24dp);
        Glide.with(activity).setDefaultRequestOptions(requestOptions).load(MainActivity.CurrentUser.getImageurl()).into(userImageIv);

        getTakipTakipciIstekToplam();

        //ilk başlangıç
        userprofileVp.setCurrentItem(0);
        setTextView(20, 14, 14, getResources().getColor(R.color.colorBlack), getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray));

        surveyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userprofileVp.setCurrentItem(0);
                setTextView(20, 14, 14, getResources().getColor(R.color.colorBlack), getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray));
            }
        });

        senceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userprofileVp.setCurrentItem(1);
                setTextView(14, 20, 14, getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorBlack), getResources().getColor(R.color.colorGray));
            }
        });

        benceTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userprofileVp.setCurrentItem(2);
                setTextView(14, 14, 20, getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorGray), getResources().getColor(R.color.colorBlack));
            }
        });

        userprofileVp.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new UserProfileSurveyFragment(activity, MainActivity.CurrentUser);
                    case 1:
                        return new UserProfileSenceFragment(activity, MainActivity.CurrentUser);
                    case 2:
                        return new UserProfileBenceFragment(activity, MainActivity.CurrentUser);
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        userprofileVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

        followingNumberTv.setOnClickListener(new View.OnClickListener() { // takip ettiklerim
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                UserProfileFollowingFragment followingFragment = new UserProfileFollowingFragment(supportFragmentManager, activity, MainActivity.CurrentUser);
                transaction.replace(R.id.fragmentContainer, followingFragment, "UserProfileFollowingFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        followerNumberTv.setOnClickListener(new View.OnClickListener() { // takipçilerim
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                UserProfileFollowersFragment followersFragment = new UserProfileFollowersFragment(supportFragmentManager, activity, MainActivity.CurrentUser);
                transaction.replace(R.id.fragmentContainer, followersFragment, "UserProfileFollowersFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        requestNumberTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                UserProfileRequestFragment requestFragment = new UserProfileRequestFragment(supportFragmentManager);
                transaction.replace(R.id.fragmentContainer, requestFragment, "UserProfileRequestFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        followTv.setOnClickListener(new View.OnClickListener() { // takip ettiklerim
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                UserProfileFollowingFragment followingFragment = new UserProfileFollowingFragment(supportFragmentManager, activity, MainActivity.CurrentUser);
                transaction.replace(R.id.fragmentContainer, followingFragment, "UserProfileFollowingFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        followerTv.setOnClickListener(new View.OnClickListener() { // takipçilerim
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                UserProfileFollowersFragment followersFragment = new UserProfileFollowersFragment(supportFragmentManager, activity, MainActivity.CurrentUser);
                transaction.replace(R.id.fragmentContainer, followersFragment, "UserProfileFollowersFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        requestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                UserProfileRequestFragment requestFragment = new UserProfileRequestFragment(supportFragmentManager);
                transaction.replace(R.id.fragmentContainer, requestFragment, "UserProfileRequestFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        logoutBtn = view.findViewById(R.id.userprofileLogout_Btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                startActivity(new Intent(view.getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });


        editBtn = view.findViewById(R.id.userprofileEdit_Btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = supportFragmentManager.beginTransaction();
                EditProfileFragment myCarFragment = new EditProfileFragment();
                transaction.replace(R.id.fragmentContainer, myCarFragment, "EditProfileFragment");
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void getTakipTakipciIstekToplam() {

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followerNumberTv.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingNumberTv.setText(dataSnapshot.getChildrenCount() + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("requestGet").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                requestNumberTv.setText(dataSnapshot.getChildrenCount() + "");
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
