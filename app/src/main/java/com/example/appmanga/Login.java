package com.example.appmanga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {


    Button btndn, btnhuy;
    TextInputEditText username, password;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();
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
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, HomeFragment.class);
                startActivity(intent);
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
    public void txtregister1(View view)
    {
        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
        finish();//hủy activity Login để chuyển sang activity Register
    }
}