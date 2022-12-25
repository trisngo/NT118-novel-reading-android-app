package com.example.appmanga;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.TooManyListenersException;

public class intro_manga_before_read extends AppCompatActivity {
    Button btnRead;
    TextView name,category,chapter,author,dsc;
    ImageView image;
    int count_category;
    private Book book = new Book();
    private DatabaseReference database;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_manga_before_read);
        name = findViewById(R.id.tvNameManga);
        dsc=findViewById(R.id.dsc);
        category = findViewById(R.id.category);
        chapter = findViewById(R.id.chapter);
        image = findViewById(R.id.thumbnailintro);
        btnRead = findViewById(R.id.btnRead_Manga);
        author = findViewById(R.id.author);

        name.setText("");
        category.setText("");
        chapter.setText("");
        author.setText("");

        Intent intent = getIntent();
        name.setText(intent.getStringExtra("name"));
        Picasso.get().load(intent.getStringExtra("image")).into(image);
        count_category = intent.getIntExtra("count_category",0);
        category.setText("Categories: "+ intent.getStringExtra("category")+" ");
        chapter.setText("Chapter :" + intent.getStringExtra("chapter"));
        author.setText("Author: "+intent.getStringExtra("author"));
        dsc.setText(intent.getStringExtra("dsc"));
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book_id =  intent.getStringExtra("book_id");
                Intent intent1 = new Intent(intro_manga_before_read.this,ReadingActivity.class);
//                String chapter = String.valueOf(book.getChapters().size());
//                Log.d("debug",  book_id);
                intent1.putExtra("book_id",book_id);
                intent1.putExtra("name",intent.getStringExtra("name"));
//                Log.d("debug", String.valueOf(Integer.valueOf(intent.getStringExtra("chapter"))));
                intent1.putExtra("chapter_size",  Integer.valueOf(intent.getStringExtra("chapter")));
                startActivity(intent1);
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    getCurrentBook();
                    book.likes++;
                    Toast.makeText(intro_manga_before_read.this,"data send", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(intro_manga_before_read.this, Login.class);
                    startActivity(intent);
                }
            }
        });
    }
    private void getCurrentBook() {
        database = FirebaseDatabase.getInstance().getReference("books");
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    book = dataSnapshot.getValue(Book.class);
                    if (book.getBook_title().equals(name.getText().toString())){
                        break;
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