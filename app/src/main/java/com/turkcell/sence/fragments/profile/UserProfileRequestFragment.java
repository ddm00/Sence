package com.turkcell.sence.fragments.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.adapters.UserRequestAdapter;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.User;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileRequestFragment extends Fragment {

    private View view;
    private List<User> userList = new ArrayList<>();
    private FrameLayout progressFrame;
    private ListView usersLv;
    private TextView warningTv;
    private UserRequestAdapter userRequestAdapter;
    private FragmentManager supportFragmentManager;

    public UserProfileRequestFragment(FragmentManager supportFragmentManager) {
        this.supportFragmentManager = supportFragmentManager;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile_request, container, false);
        progressFrame = view.findViewById(R.id.userprofilerequestFrame_Fl);
        usersLv = view.findViewById(R.id.userprofilerequestUsers_Lv);
        warningTv = view.findViewById(R.id.userprofilerequestWarning_Tv);
        warningTv.setText("Hiç takip istek yok");
        userRequestAdapter = new UserRequestAdapter(view.getContext(), userList, supportFragmentManager);
        usersLv.setAdapter(userRequestAdapter);

        progressFrame.setVisibility(View.VISIBLE);
        usersLv.setVisibility(View.GONE);
        warningTv.setVisibility(View.GONE);

        userList.clear();
        Dao.getInstance().getFirebaseDatabase().getReference("Follow").child(MainActivity.CurrentUser.getId()).child("requestGet").addValueEventListener(new ValueEventListener() {
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
                                    getUser.setImageurl(hashMap.get("imageUrl").toString());
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
            usersLv.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        userRequestAdapter.notifyDataSetChanged();
        progressFrame.setVisibility(View.GONE);
        usersLv.setVisibility(View.VISIBLE);
        warningTv.setVisibility(View.GONE);

    }

}
