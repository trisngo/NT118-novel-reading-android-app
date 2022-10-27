package com.example.appmanga;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {
    TextView textv;
    TextInputEditText tk, mk, email;
    Button btndk, btnhuy;
    TextView textclick;

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
                Intent intent = new Intent(Register.this, HomeFragment.class);
                startActivity(intent);
       }});
    }
    private void Anhxa() {
        textv = findViewById(R.id.textregister);
        tk = findViewById(R.id.tk);
        mk = findViewById(R.id.mk);
        btndk = findViewById(R.id.btndk);
        btnhuy = findViewById(R.id.btnhuy);
        textclick = findViewById(R.id.textlogin);
    }
    // click dang ki
    public void txtlogin(View view){
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}