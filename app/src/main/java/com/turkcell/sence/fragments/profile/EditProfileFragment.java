package com.turkcell.sence.fragments.profile;

import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.turkcell.sence.R;
import com.turkcell.sence.activities.MainActivity;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.fragments.survey.SurveyFragment;
import com.turkcell.sence.models.User;

import java.util.HashMap;
import java.util.Map;


public class EditProfileFragment extends Fragment {

    public static Uri imageUri;
    private String sImage;
    private EditText userName, fullName;
    private Button editButton;
    private ImageView userImage;
    private Switch isOpen;
    private View view;

    @Override
    public void onStart() {
        super.onStart();
        if (imageUri!=null){
            userImage.setImageURI(imageUri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        userName = view.findViewById(R.id.editUserName_Et);
        fullName = view.findViewById(R.id.editFullName_Et);
        editButton = view.findViewById(R.id.editProfile_Btn);
        isOpen = view.findViewById(R.id.editIsOpen_Sw);
        userImage=view.findViewById(R.id.editUserImage_Iv);

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SurveyFragment.value=3;
                CropImage.activity().setAspectRatio(1,1).start(getActivity());
            }
        });

        final User user = MainActivity.CurrentUser;

        userName.setText(user.getUsername());
        fullName.setText(user.getFullname());
        isOpen.setChecked(user.isOpen());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.ic_account_circle_black_24dp);
        Glide.with(getActivity()).setDefaultRequestOptions(requestOptions).load(user.getImageurl()).into(userImage);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null) {
                    final StorageReference userImage = Dao.getInstance().getStorageReference()
                            .child("userProfileImage").child(user.getId() + ".jpg");

                    userImage.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            userImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    sImage = uri.toString();
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("username", userName.getText().toString());
                                    map.put("fullname", fullName.getText().toString());
                                    map.put("isOpen", isOpen.isChecked());
                                    map.put("imageUrl", sImage);
                                    userUpdate(map);
                                    imageUri = null;
                                }
                            });
                        }
                    });
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("username", userName.getText().toString());
                    map.put("fullname", fullName.getText().toString());
                    map.put("isOpen", isOpen.isChecked());
                    map.put("imageUrl", "");
                    userUpdate(map);
                    imageUri = null;
                }

            }
        });
        return view;
    }

    private void userUpdate(Map<String, Object> map) {
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


}
