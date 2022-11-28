package com.example.appmanga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {


    Button btndn, btnhuy;
    TextInputEditText username, password;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private static final int REQUEST_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Load...");
        progressDialog.setCanceledOnTouchOutside(false);


        //click btnhuy
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, introActivity.class);
                startActivity(intent);
            }
        });
        btndn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }

    private void Anhxa() {
        btndn = findViewById(R.id.btndn);
        btnhuy = findViewById(R.id.btnhuy);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


    }

    // click btn dang ki
    public void txtregister1(View view) {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        finish();//hủy activity Login để chuyển sang activity Register
    }

    private String email = "", password1 = "";

    private void validateData() {
        // get data
        email = username.getText().toString().trim();
        password1 = password.getText().toString().trim();

        // validate data
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(Login.this, "Nhập Email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password1)) {
            Toast.makeText(Login.this, "Nhập mật khẩu...", Toast.LENGTH_SHORT).show();
        } else {
            loginUser();
        }
    }

    private void loginUser() {
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password1)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Intent intent = new Intent(
                                Login.this, MainActivity.class
                        );
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Login.this, "Email hay password không chính xác...\nVui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}