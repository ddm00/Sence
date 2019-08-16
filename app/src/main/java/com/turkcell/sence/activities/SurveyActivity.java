package com.turkcell.sence.activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.turkcell.sence.R;

import java.util.HashMap;

public class SurveyActivity extends AppCompatActivity {

    Uri firstImageUri, secondImageUri;
    String mUrl = "";
    StorageTask uploadTask;
    StorageReference storageReference;


    ImageView closeIv, firstImageIv, secondImageIv;
    TextView shareTv, categoryTv, timeTv;
    EditText questionEt;
    Spinner categoriesSp, timesSp;

    Object selectedCategory, selectedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);
        init();
    }

    public void init() {
        closeIv = findViewById(R.id.close_Iv);
        firstImageIv = findViewById(R.id.firstImage_Iv);
        secondImageIv = findViewById(R.id.secondImage_Iv);
        shareTv = findViewById(R.id.share_Tv);
        questionEt = findViewById(R.id.surveyQuestion_Et);
        categoriesSp = findViewById(R.id.category_Sp);
        timesSp = findViewById(R.id.time_Sp);
        categoryTv = findViewById(R.id.surveyCategory_Tv);
        timeTv = findViewById(R.id.surveyTime_Tv);

        storageReference = FirebaseStorage.getInstance().getReference("Surveys");

        closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SurveyActivity.this, MainActivity.class));
                finish();
            }
        });
        shareTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
        firstImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1)
                        .start(SurveyActivity.this);
            }
        });
        secondImageIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1, 1)
                        .start(SurveyActivity.this);
            }
        });


        categoriesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SurveyActivity.this, "Kategori seçimi yapmadınız!", Toast.LENGTH_SHORT).show();
            }
        });
        timesSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = parent.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(SurveyActivity.this, "Süre seçimi yapmadınız!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Paylaşılıyor");
        progressDialog.show();
        if (firstImageUri != null) {
            final StorageReference firstImageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(firstImageUri));
            uploadTask = firstImageReference.putFile(firstImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return firstImageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mUrl = downloadUri.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Surveys");
                        String surveyId = reference.push().getKey();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("SurveyId", surveyId);
                        hashMap.put("FirstImage", mUrl);
                        hashMap.put("SecondImage", "");
                        hashMap.put("Question", questionEt.getText().toString());
                        hashMap.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("Category", selectedCategory);
                        hashMap.put("Time", selectedTime);
                        reference.child(surveyId).setValue(hashMap);

                    } else {
                        Toast.makeText(SurveyActivity.this, "Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SurveyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(SurveyActivity.this, "Resim seçimi yapmadınız!", Toast.LENGTH_SHORT).show();
        }
        if (secondImageUri != null) {
            final StorageReference secondImageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(secondImageUri));
            uploadTask = secondImageReference.putFile(secondImageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return secondImageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mUrl = downloadUri.toString();

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Surveys");

                        String surveyId = reference.push().getKey();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("SurveyId", surveyId);
                        hashMap.put("FirstImage", mUrl);
                        hashMap.put("SecondImage", mUrl);
                        hashMap.put("Question", questionEt.getText().toString());
                        hashMap.put("Publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        hashMap.put("Category", selectedCategory);
                        hashMap.put("Time", selectedTime);

                        reference.child(surveyId).setValue(hashMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SurveyActivity.this, MainActivity.class));
                        finish();

                    } else {
                        Toast.makeText(SurveyActivity.this, "Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SurveyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            Toast.makeText(SurveyActivity.this, "Resim seçimi yapmadınız!", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            firstImageUri = result.getUri();
            secondImageUri = result.getUri();

            firstImageIv.setImageURI(firstImageUri);
            secondImageIv.setImageURI(secondImageUri);
        } else {
            Toast.makeText(this, "Bir şeyler yanlış gitti!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SurveyActivity.this, MainActivity.class));
            finish();
        }
    }
}
