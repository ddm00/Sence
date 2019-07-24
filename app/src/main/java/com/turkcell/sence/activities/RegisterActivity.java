package com.turkcell.sence.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.turkcell.sence.R;

public class RegisterActivity extends AppCompatActivity {

    EditText userEmailEt, userPasswordEt, userConfirmPasswordEt;
    Button registerBtn;
    FirebaseAuth mAuth;
    String userEmail, userPassword;

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
        setContentView(R.layout.activity_register);
        init();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void init() {
        userEmailEt = findViewById(R.id.registerUserEmail_Et);
        userPasswordEt = findViewById(R.id.registerPassword_Et);
        userConfirmPasswordEt = findViewById(R.id.registerConfirmPassword_Et);
        registerBtn = findViewById(R.id.register_Btn);
        mAuth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = userEmailEt.getText().toString().trim();
                userPassword = userPasswordEt.getText().toString().trim();
                String userConfirmPassword = userConfirmPasswordEt.getText().toString();
                if (!userEmail.isEmpty() && !userPassword.isEmpty() && !userConfirmPassword.isEmpty()) {
                    if (userPassword.equals(userConfirmPassword)) {
                        register();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Kayıt için tüm alanları doldurunuz.Lütfen!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void register() {
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    Toast.makeText(getApplicationContext(), "Kayıt işlemi başarılı", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);


                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthException) {
                    if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_WEAK_PASSWORD")) {
                        Toast.makeText(getApplicationContext(), "Eksik Şifre", Toast.LENGTH_SHORT).show();

                    } else if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_INVALID_EMAIL")) {
                        Toast.makeText(getApplicationContext(), "Geçersiz mail", Toast.LENGTH_SHORT).show();

                    } else if (((FirebaseAuthException) e).getErrorCode().equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                        Toast.makeText(getApplicationContext(), "Mail zaten kayıtlı", Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });

    }

}
