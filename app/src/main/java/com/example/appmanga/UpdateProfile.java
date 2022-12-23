package com.example.appmanga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UpdateProfile extends AppCompatActivity {

    private TextView tv_username, tv_userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        initButton();
    }

    public void initButton() {
        tv_username = (TextView) findViewById(R.id.tv_username);
        tv_userEmail = (TextView) findViewById(R.id.tv_email);
        Intent intent = getIntent();
        String received_username = intent.getStringExtra("username");
        String received_user_email = intent.getStringExtra("user_email");
        tv_username.setText(received_username);
        tv_userEmail.setText(received_user_email);
    }
}