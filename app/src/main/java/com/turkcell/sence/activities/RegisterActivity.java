package com.turkcell.sence.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.turkcell.sence.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText userNameEt, fullNameEt, eMailEt, passwordEt;
    Button registerBtn;
    TextView loginTv;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    ProgressDialog dialog;

    String userName, userFullname, userEmail, userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        userNameEt = findViewById(R.id.registerUsername_Et);
        fullNameEt = findViewById(R.id.registerFullname_Et);
        eMailEt = findViewById(R.id.registerEmail_Et);
        passwordEt = findViewById(R.id.registerPassword_Et);
        registerBtn = findViewById(R.id.registerBtn);
        loginTv = findViewById(R.id.registerLogin_Tv);
        mAuth = FirebaseAuth.getInstance();
        loginTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(RegisterActivity.this);
                dialog.setMessage("Lütfen bekleyiniz..");
                dialog.show();
                userName = userNameEt.getText().toString().trim();
                userFullname = fullNameEt.getText().toString().trim();
                userEmail = eMailEt.getText().toString().trim();
                userPassword = passwordEt.getText().toString().trim();
                if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userFullname) || TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(getApplicationContext(), "Kayıt için tüm alanları doldurunuz.Lütfen!", Toast.LENGTH_LONG).show();
                } else if (userPassword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Belirlediğiniz şifre en az 6 karakter olmalıdır!", Toast.LENGTH_LONG).show();
                } else{
                    register(userName,userFullname,userEmail,userPassword);
                }
            }
        });
    }

    private void register(final String username, final String fullname, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    String userId=firebaseUser.getUid();
                    reference= FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                    HashMap<String,Object> hashMap=new HashMap<>();
                    hashMap.put("Id",userId);
                    hashMap.put("Username",username.toLowerCase());
                    hashMap.put("Fullname",fullname);
                    hashMap.put("ImageUrl","https://firebasestorage.googleapis.com/v0/b/sence-af3aa.appspot.com/o/person.png?alt=media&token=d9664199-66e3-4324-8a84-22f134a7ba50");
                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    });
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
