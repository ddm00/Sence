package com.turkcell.sence.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.turkcell.sence.R;

public class StartActivity extends AppCompatActivity {

    Button loginBtn,registerBtn;
    FirebaseUser mUser;

    @Override
    protected void onStart() {
        super.onStart();
        mUser= FirebaseAuth.getInstance().getCurrentUser();
        if(mUser!=null){
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        loginBtn=findViewById(R.id.startLogin_Btn);
        registerBtn=findViewById(R.id.startRegister_Btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this,RegisterActivity.class));
            }
        });
    }
}