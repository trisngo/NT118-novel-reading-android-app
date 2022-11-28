package com.example.appmanga;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    TextView textv;
    TextInputEditText et_tk, et_mk, et_nhaplaimk;
    Button btndk, btnhuy;
    TextView textclick;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Anhxa();
        //Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Load...");
        progressDialog.setCanceledOnTouchOutside(false);


        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, introActivity.class);
                startActivity(intent);
            }
        });
        textclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
        }

    private void Anhxa() {
        textv = findViewById(R.id.textregister);
        et_tk = findViewById(R.id.tk);
        et_mk = findViewById(R.id.mk);
        et_nhaplaimk = findViewById(R.id.nhaplai_mk);
        btndk = findViewById(R.id.btndk);
        btnhuy = findViewById(R.id.btnhuy);
        textclick = findViewById(R.id.textlogin);
    }

    public void txtlogin(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
    // validate data
    private String  email ="", password = "";
    private void validateData() {

        //get data

        email = et_tk.getText().toString().trim();
        password = et_mk.getText().toString().trim();

        //validate data
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Email không hợp lệ... !", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Nhập mật khẩu...", Toast.LENGTH_SHORT).show();
        }else {
            createUserAccount();
        }
    }

    private void createUserAccount() {
        //show progress dialog
        progressDialog.setTitle("Đang tạo tài khoản...");
        progressDialog.show();

        //create user in firebase auth
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        Intent intent = new Intent(
                                Register.this, MainActivity.class
                        );
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //update user info to db firebase auth
    private void updateUserInfo() {
        progressDialog.setTitle("Saving user info...");

        Long timestamp = System.currentTimeMillis();

        //get uid
        String uid = firebaseAuth.getUid();

        //set up data to add in db
        HashMap<String, Object>  hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("email", email);
        hashMap.put("profileImage", "");
        hashMap.put("userType", "user");
        hashMap.put("timestamp", timestamp);}}

