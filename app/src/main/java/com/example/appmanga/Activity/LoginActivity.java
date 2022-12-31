package com.example.appmanga.Activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appmanga.Model.User;
import com.example.appmanga.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    Button btndn, btnhuy;
    TextInputEditText username, password;

    // firebase authentication
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private static final int REQUEST_PERMISSION = 0;
    ArrayList<String> user_liked_books_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initButton();

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("");
        progressDialog.setCanceledOnTouchOutside(false);


        //click btnhuy
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
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

    private void initButton() {
        btndn = findViewById(R.id.btndn);
        btnhuy = findViewById(R.id.btnhuy);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    // click btn dang ki
    public void txtregister1(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();//hủy activity Login để chuyển sang activity Register
    }

    private String email = "", password1 = "";

    private void validateData() {
        // get data
        email = Objects.requireNonNull(username.getText()).toString().trim();
        password1 = Objects.requireNonNull(password.getText()).toString().trim();
        // validate data
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginActivity.this, "Nhập Email...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password1)) {
            Toast.makeText(LoginActivity.this, "Nhập mật khẩu...", Toast.LENGTH_SHORT).show();
        } else {
            loginUser();
        }
    }

    private void loginUser() {
        progressDialog.setTitle("Đang đăng nhập...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password1)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Intent intent = new Intent(
                                LoginActivity.this, MainActivity.class
                        );
                        subscribeLikedBook();
                        startActivity(intent);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Lỗi...\nVui lòng thử lại!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
    public void subscribeLikedBook() {
        DatabaseReference users_database;
        users_database = FirebaseDatabase.getInstance().getReference("users");
        users_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        user_liked_books_list = user.getLiked_books();
                        for(String book_id : user_liked_books_list) {
                            FirebaseMessaging.getInstance().subscribeToTopic(book_id);
                            Log.d("notify", book_id);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}