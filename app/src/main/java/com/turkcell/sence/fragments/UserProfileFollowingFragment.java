package com.turkcell.sence.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.turkcell.sence.R;
import com.turkcell.sence.adapters.FollowingListCustomAdapter;
import com.turkcell.sence.adapters.UserProfileSurveyCustomAdapter;
import com.turkcell.sence.models.User;
import com.turkcell.sence.models.UserProfileSurvey;

import java.util.ArrayList;
import java.util.Calendar;


public class UserProfileFollowingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ListView userListView;
    ArrayList<User> userList = new ArrayList<>();
    FollowingListCustomAdapter adapter;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public UserProfileFollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFollowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFollowingFragment newInstance(String param1, String param2) {
        UserProfileFollowingFragment fragment = new UserProfileFollowingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_profile_following, container, false);

        userListView = view.findViewById(R.id.followingList_lv);
        userList.add(new User("ds","dido","didem öz","fds","fsdf",R.drawable.ic_home_black_24dp));
        userList.add(new User("ds","dido","didem öz d","fds","fsdf",R.drawable.ic_home_black_24dp));
        userList.add(new User("ds","dido","didem öz t","fds","fsdf",R.drawable.ic_home_black_24dp));
        userList.add(new User("ds","dido","didem öz rf","fds","fsdf",R.drawable.ic_home_black_24dp));
        adapter = new FollowingListCustomAdapter(getContext(),userList);
        userListView.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }


}
