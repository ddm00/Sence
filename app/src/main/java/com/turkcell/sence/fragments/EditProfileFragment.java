package com.turkcell.sence.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.models.User;

import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment {

    EditText userName, fullName;
    Button editButton;
    ImageView profilePhoto;
    Switch isOpen;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        userName = view.findViewById(R.id.editUserName_Et);
        fullName = view.findViewById(R.id.editFullName_Et);
        editButton = view.findViewById(R.id.editProfile_Btn);
        isOpen = view.findViewById(R.id.editIsOpen_Sw);

        User user = MainActivity.CurrentUser;

        userName.setText(user.getUsername());
        fullName.setText(user.getFullname());
        isOpen.setChecked(user.isOpen());
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("username", userName.getText().toString());
                map.put("fullname", fullName.getText().toString());
                map.put("isOpen", isOpen.isChecked());

                DatabaseReference reference = Dao.getInstance().getFirebaseDatabase().getReference("Users").child(MainActivity.CurrentUser.getId());
                reference.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(view.getContext(), "İşlem başarılı.", Toast.LENGTH_SHORT).show();
                        MainActivity.CurrentUser.setUsername(userName.getText().toString());
                        MainActivity.CurrentUser.setFullname(fullName.getText().toString());
                        MainActivity.CurrentUser.setOpen(isOpen.isChecked());
                        getActivity().onBackPressed();
                    }
                });
            }
        });

        profilePhoto = view.findViewById(R.id.editUserImage_Iv);
        return view;
    }


}
