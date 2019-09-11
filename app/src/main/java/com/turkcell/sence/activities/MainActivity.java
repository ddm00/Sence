package com.turkcell.sence.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import com.theartofdev.edmodo.cropper.CropImage;
import com.turkcell.sence.R;
import com.turkcell.sence.database.Dao;
import com.turkcell.sence.fragments.UserProfileFragment;
import com.turkcell.sence.fragments.home.HomeFragment;
import com.turkcell.sence.fragments.profile.EditProfileFragment;
import com.turkcell.sence.fragments.search.SearchFragment;
import com.turkcell.sence.fragments.survey.SurveyFragment;

import com.turkcell.sence.models.User;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    public static User CurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getUserLogin();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    private void getUserLogin() {
        if (Dao.getInstance().getmAuth().getCurrentUser() != null) {
            Dao.getInstance().getFirebaseDatabase().getReference("Users").child(Dao.getInstance().getmAuth().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    MainActivity.CurrentUser = dataSnapshot.getValue(User.class);
                    HashMap<String, Object> hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    if (hashMap != null) {
                        MainActivity.CurrentUser.setOpen((boolean) hashMap.get("isOpen"));
                        getToken();
                        Fragment selectedFragment = new HomeFragment(MainActivity.this);
                        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
                        fragTrans.replace(R.id.fragmentContainer, selectedFragment).commit();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        }
    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();

                            Map<String, Object> map = new HashMap<>();
                            map.put("token", token);

                            Dao.getInstance().getFirebaseDatabase().getReference("Users").child(MainActivity.CurrentUser.getId()).updateChildren(map);

                        }

                    }
                });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    selectedFragment = new UserProfileFragment(getSupportFragmentManager(), MainActivity.this);
                    break;
                case R.id.navigation_add:
                    selectedFragment = new SurveyFragment();
                    break;
                case R.id.navigation_search:
                    selectedFragment = new SearchFragment(MainActivity.this, getSupportFragmentManager());
                    break;
                case R.id.navigation_home:
                    selectedFragment = new HomeFragment(MainActivity.this);
                    break;

            }
            FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
            fragTrans.replace(R.id.fragmentContainer, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (SurveyFragment.value == 1) {
                SurveyFragment.firstImageUri = result.getUri();
                SurveyFragment.value = 0;
            }
            if (SurveyFragment.value == 2) {
                SurveyFragment.secondImageUri = result.getUri();
                SurveyFragment.value = 0;
            }
            if (SurveyFragment.value == 3) {
                EditProfileFragment.imageUri = result.getUri();
                SurveyFragment.value = 0;
            }

        } else {
            Toast.makeText(this, "Bir şeyler yanlış gitti!", Toast.LENGTH_SHORT).show();
        }
    }

}
