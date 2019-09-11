package com.turkcell.sence.fragments.profile;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.adapters.UserAdapter;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserProfileFollowingFragment extends Fragment {

    private View view;
    private List<User> userList = new ArrayList<>();
    private FrameLayout progressFrame;
    private RecyclerView recyclerView;
    private TextView warningTv;
    private FragmentManager supportFragmentManager;
    private UserAdapter userAdapter;
    private Activity activity;
    private User user;

    public UserProfileFollowingFragment(FragmentManager supportFragmentManager, Activity activity, User user) {
        this.supportFragmentManager = supportFragmentManager;
        this.activity = activity;
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile_following, container, false);
        progressFrame = view.findViewById(R.id.userprofilefollowingFrame_Fl);
        recyclerView = view.findViewById(R.id.userprofilefollowingUsers_Lv);
        warningTv = view.findViewById(R.id.userprofilefollowingWarning_Tv);
        warningTv.setText("Hiç arkadaşın yok, yalnız kalma");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userAdapter = new UserAdapter(activity, userList, supportFragmentManager);
        recyclerView.setAdapter(userAdapter);
        progressFrame.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        warningTv.setVisibility(View.GONE);

        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(user.getId()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String getUserId = snapshot.getKey();
                    if (getUserId != null && !getUserId.equals("")) {
                        Dao.getInstance().getFirebaseDatabase().getReference("Users").child(getUserId).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                User getUser = dataSnapshot.getValue(User.class);
                                HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                                if (getUser != null && hashMap != null) {
                                    getUser.setOpen((boolean) hashMap.get("isOpen"));
                                    userList.add(getUser);
                                    setAdapter();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
                setWarning();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        return view;
    }

    private void setWarning() {
        if (userList.size() == 0) {
            warningTv.setVisibility(View.VISIBLE);
            progressFrame.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        userAdapter.notifyDataSetChanged();
        progressFrame.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        warningTv.setVisibility(View.GONE);

    }
}

