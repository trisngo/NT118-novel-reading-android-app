package com.example.appmanga.ExtraFeature;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.appmanga.Activity.MainActivity;
import com.example.appmanga.R;


public class CheckInternet extends AppCompatActivity {

    Button try_again;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkinternet);
        try_again = findViewById(R.id.try_again_btn);

        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (IsNetwork.IsNetworkAvailable(CheckInternet.this)) {

                    Intent i = new Intent(CheckInternet.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }else   {
                    Toast.makeText(CheckInternet.this,"Không có kết nối internet",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }



}