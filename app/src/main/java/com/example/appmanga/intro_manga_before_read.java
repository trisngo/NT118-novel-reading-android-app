package com.example.appmanga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class intro_manga_before_read extends AppCompatActivity {
    Button btnRead;
    TextView name,category,chapter;
    ImageView image;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_manga_before_read);
        name = findViewById(R.id.tvNameManga);
        category = findViewById(R.id.category);
        chapter = findViewById(R.id.chapter);
        image = findViewById(R.id.thumbnailintro);
        btnRead = findViewById(R.id.btnRead_Manga);

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        category.setText(intent.getStringExtra("category"));
        chapter.setText("10");
        Picasso.get().load(intent.getStringExtra("image")).into(image);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(intro_manga_before_read.this,ReadingActivity.class);
                startActivity(intent1);
            }
        });
    }
}