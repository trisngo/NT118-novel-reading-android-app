package com.example.appmanga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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
    TextInputEditText et_tk, et_mk, et_email, et_nhaplaimk;
    Button btndk, btnhuy;
    TextView textclick;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();
    DatabaseReference usersRef = root.child("users");

    public class User {
        public String username;
        public String password;
        public String email;

        public User(String username, String password, String email) {
            this.username = username;
            this.password = password;
            this.email = email;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Anhxa();
        btnhuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, introActivity.class);
                startActivity(intent);
            }
        });
        textclick.setOnClickListener(new View.OnClickListener(

        ) {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
            }
        });
        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_nhaplaimk.getText().toString().equals(et_mk.getText().toString()))
                {
                    Query query = usersRef.orderByKey();
                    query.addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                boolean isFound = false;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                                    if (isFound) break;
                                    for (String name: userData.keySet()) {
                                        if (name.toString().equals("username")) {
                                            if (userData.get(name).toString().equals(et_tk.getText().toString())) {
                                                Toast.makeText(getApplicationContext(),"User này đã được đăng ký rồi!", Toast.LENGTH_LONG).show();
                                                isFound = true;
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (isFound!=true) createUser();
                            }
                            else {
                                createUser();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Mật khẩu không khớp!", Toast.LENGTH_LONG).show();
                }

       }});
    }
    private void Anhxa() {
        textv = findViewById(R.id.textregister);
        et_tk = findViewById(R.id.tk);
        et_mk = findViewById(R.id.mk);
        et_nhaplaimk = findViewById(R.id.nhaplai_mk);
        et_email = findViewById(R.id.email);
        btndk = findViewById(R.id.btndk);
        btnhuy = findViewById(R.id.btnhuy);
        textclick = findViewById(R.id.textlogin);
    }

    public void createUser() {
        User newUser = new User(et_tk.getText().toString(),et_mk.getText().toString(),et_email.getText().toString());
        Map<String, Object> register_user = new HashMap<>();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        register_user.put("user" + ts, newUser);
        usersRef.updateChildren(register_user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Đăng ký thành công!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),"Có lỗi xảy ra, vui lòng thử lại sau!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void txtlogin(View view){
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }

}