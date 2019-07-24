package com.turkcell.sence.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.turkcell.sence.R;

public class LoginActivity extends AppCompatActivity {

    EditText userEmailEt, userPasswordEt;
    Button loginBtn, registerBtn;
    FirebaseAuth mAuth;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_profile:
                    return true;
                case R.id.navigation_add:
                    return true;
                case R.id.navigation_following:
                    return true;
                case R.id.navigation_search:
                    return true;
                case R.id.navigation_like:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    private void init() {
        userEmailEt = findViewById(R.id.loginUserEmail_Et);
        userPasswordEt = findViewById(R.id.loginUserPassword_Et);
        loginBtn = findViewById(R.id.loginLogin_Btn);
        registerBtn = findViewById(R.id.loginGoRegister_Btn);
        mAuth = FirebaseAuth.getInstance();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = userEmailEt.getText().toString().trim();
                String userPassword = userPasswordEt.getText().toString().trim();
                if (!userEmail.isEmpty() && !userPassword.isEmpty()) {
                    login(userEmail, userPassword);
                } else {
                    Toast.makeText(getApplicationContext(), "Email ya da parola boş bırakılamaz!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    Log.d("EMail", "signInWithEmail:success");
                } else {
                    Log.w("Fail", "signInWithEmail:failure", task.getException());

                }
            }
        });
    }

}
