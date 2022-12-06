package com.example.appmanga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class intro_manga_before_read extends AppCompatActivity {
    Button btnRead;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_manga_before_read);
        btnRead = findViewById(R.id.btnRead_Manga);
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(intro_manga_before_read.this, ReadingActivity.class);
                startActivity(intent);
            }
        });
    }
}