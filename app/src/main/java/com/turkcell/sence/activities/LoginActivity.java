package com.turkcell.sence.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.turkcell.sence.R;
import com.turkcell.sence.database.Dao;


public class LoginActivity extends AppCompatActivity {

    EditText userEmailEt, userPasswordEt;
    Button loginBtn;
    TextView registerTv;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        userEmailEt = findViewById(R.id.loginEmail_Et);
        userPasswordEt = findViewById(R.id.loginPassword_Et);
        loginBtn = findViewById(R.id.loginBtn);
        registerTv = findViewById(R.id.loginRegister_Tv);

        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("Lütfen bekleyiniz..");
                dialog.show();
                String userEmail = userEmailEt.getText().toString().trim();
                String userPassword = userPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(userEmail) || TextUtils.isEmpty(userPassword)) {
                    Toast.makeText(getApplicationContext(), "Lütfen tüm alanları doldurunuz!", Toast.LENGTH_SHORT).show();
                } else {
                    login(userEmail, userPassword);
                }
            }
        });

    }

    private void login(String email, String password) {
        Dao.getInstance().getmAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Dao.getInstance().getFirebaseDatabase().getReference("Users").child(Dao.getInstance().getmAuth().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dialog.dismiss();
                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            dialog.dismiss();
                        }
                    });
                    Log.d("EMail", "signInWithEmail:success");
                } else {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this,"Giriş hatalı. Lütfen kontrol ediniz!",Toast.LENGTH_SHORT).show();
                    Log.w("Fail", "signInWithEmail:failure", task.getException());
                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Uygulamadan çıkmak istediğinize emin misiniz?")
                .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                })
                .setNegativeButton("HAYIR", null)
                .show();
    }

}
