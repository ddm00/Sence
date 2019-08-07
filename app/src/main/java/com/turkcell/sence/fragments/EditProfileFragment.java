package com.turkcell.sence.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.turkcell.sence.R;


public class EditProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public EditProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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

    EditText username, name, surname, email, phone;
    Button editButton, backButton;
    ImageView profilePhoto;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);


        username = view.findViewById(R.id.editProfile_username_et);
        surname = view.findViewById(R.id.editProfile_surname_et);
        name = view.findViewById(R.id.editProfile_name_et);
        email = view.findViewById(R.id.editProfile_eposta_et);
        phone = view.findViewById(R.id.editProfile_phone_et);
        editButton = view.findViewById(R.id.editProfile_edit_btn);
        backButton = view.findViewById(R.id.editProfile_back_btn);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO : GÜNCELLEME İŞLEMİ YAPILACAK.
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragTrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragTrans.replace(R.id.fragmentContainer, new UserProfileFragment()).commit();
            }
        });
        profilePhoto = view.findViewById(R.id.editProfile_profilePhoto_iv);
        // Inflate the layout for this fragment
        return view;
    }


}
