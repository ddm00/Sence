package com.turkcell.sence.fragments.survey;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.turkcell.sence.R;
import com.turkcell.sence.database.Dao;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SurveyFragment extends Fragment {
    ImageView surveyFirstimage, surveySecondimage;
    TextView surveyShare, surveyCategory, surveyTime;
    EditText surveyQuestion;
    Spinner spCategory, spTime;
    Object selectedCategory, selectedTime;
    String firstImage, secondImage, surveyId;
    DatabaseReference reference;
    Switch isSecret;

    public static Uri firstImageUri, secondImageUri;
    public static int value = 0;

    View view;
    ProgressDialog progressDialog;

    public SurveyFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        if (firstImageUri != null) {
            surveyFirstimage.setImageURI(firstImageUri);
        }
        if (secondImageUri != null) {
            surveySecondimage.setImageURI(secondImageUri);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_survey, container, false);
        init();
        return view;
    }

    public void init() {
        surveyFirstimage = view.findViewById(R.id.addFirstImage_Iv);
        surveySecondimage = view.findViewById(R.id.addSecondImage_Iv);
        surveyShare = view.findViewById(R.id.surveyShare_Tv);
        surveyQuestion = view.findViewById(R.id.addQuestion_Et);
        spCategory = view.findViewById(R.id.addCategory_Sp);
        spTime = view.findViewById(R.id.addTime_Sp);
        surveyCategory = view.findViewById(R.id.addCategory_Tv);
        surveyTime = view.findViewById(R.id.addSurveyTime_Tv);
        isSecret = view.findViewById(R.id.addIsSecret_Sw);
        surveyShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        surveyFirstimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = 1;
                CropImage.activity().setAspectRatio(1, 1).start(getActivity());
            }
        });
        surveySecondimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value = 2;
                CropImage.activity().setAspectRatio(1, 1).start(getActivity());
            }
        });
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(view.getContext(), "Kategori seçimi yapmadınız!", Toast.LENGTH_SHORT).show();
            }
        });
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime=parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(view.getContext(), "Süre seçimi yapmadınız!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setTitle(getActivity().getResources().getText(R.string.app_name));
        progressDialog.setMessage(getActivity().getResources().getText(R.string.setText));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        reference = Dao.getInstance().getFirebaseDatabase().getReference("Surveys");
        surveyId = reference.push().getKey();
        if (firstImageUri != null && secondImageUri != null) {
            final StorageReference userImage = Dao.getInstance().getStorageReference()
                    .child("Surveys").child(surveyId).child("1.jpg");
            userImage.putFile(firstImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    userImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firstImage = uri.toString();
                            Log.e("firstImage", firstImage);

                            final StorageReference userImage = Dao.getInstance().getStorageReference()
                                    .child("Surveys").child(surveyId).child("2.jpg");
                            userImage.putFile(secondImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    userImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            secondImage = uri.toString();
                                            Log.e("secondImage", secondImage);
                                            addSurvey(reference);
                                        }
                                    });
                                }
                            });

                        }
                    });
                }
            });

        }
    }

    private void addSurvey(DatabaseReference reference) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("surveyId", surveyId);
        hashMap.put("question", surveyQuestion.getText().toString());
        hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("category", selectedCategory);
        hashMap.put("surveyFirstImage", firstImage);
        hashMap.put("surveySecondImage", secondImage);
        hashMap.put("time", selectedTime);
        hashMap.put("isSecret", isSecret.isChecked());

        Date date = new Date();
        String[] timeList = {"30 dk", "1 saat", "1 gün", "3 gün", "5 gün", "7 gün"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        for (int i = 0; i < timeList.length; i++) {
            if (timeList[i].equals(selectedTime)) {
                switch (i) {
                    case 0:
                        calendar.add(Calendar.MINUTE, 30);
                        break;
                    case 1:
                        calendar.add(Calendar.HOUR, 1);
                        break;
                    case 2:
                        calendar.add(Calendar.DATE, 1);
                    case 3:
                        calendar.add(Calendar.DATE, 3);
                        break;
                    case 4:
                        calendar.add(Calendar.DATE, 5);
                        break;
                    case 5:
                        calendar.add(Calendar.DATE, 7);
                        break;

                }
            }
        }

        hashMap.put("t", calendar.getTime().getTime());

        reference.child(surveyId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    surveyFirstimage.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
                    surveySecondimage.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.ic_launcher));
                    surveyQuestion.setText("");
                    spCategory.setSelection(0);
                    spTime.setSelection(0);
                    progressDialog.dismiss();
                    firstImageUri = null;
                    secondImageUri = null;
                    Toast.makeText(view.getContext(), "Kayıt başarılı.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Kayıt başarısız!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}


