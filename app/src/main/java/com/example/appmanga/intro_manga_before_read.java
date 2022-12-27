package com.example.appmanga;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Map;
import java.util.TooManyListenersException;

public class intro_manga_before_read extends AppCompatActivity {
    Button btnRead;
    TextView name,category,chapter,author,dsc, view;
    ConstraintLayout background_intro_read_manga;
    ImageButton like_button;
    String book_name, image_link, book_category, book_author, book_description;
    int view_number, book_chapters_number;

    ImageView image;
    int count_category;
    private Book book = new Book();
    private DatabaseReference database, books_database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_manga_before_read);
        getBookDataFromBookId();

    }

    public void initButton() {
        name = findViewById(R.id.tvNameManga);
        dsc=findViewById(R.id.dsc);
        category = findViewById(R.id.category);
        chapter = findViewById(R.id.chapter);
        image = findViewById(R.id.thumbnailintro);
        btnRead = findViewById(R.id.btnRead_Manga);
        author = findViewById(R.id.author);
        view = findViewById(R.id.view_number);
        like_button = findViewById(R.id.btn_like);
        background_intro_read_manga = findViewById(R.id.background_intro_read_manga);
        name.setText("");
        category.setText("");
        chapter.setText("");
        author.setText("");

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Intent intent = getIntent();
        name.setText(book_name);
        name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name.setSelected(true);
        name.setSingleLine(true);

        Picasso.get().load(image_link).into(image);
        Picasso.get().load(image_link).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                background_intro_read_manga.setBackground(new BitmapDrawable(bitmap));
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });

        count_category = intent.getIntExtra("count_category",0);
        category.setText("Thể loại: "+ book_category +" ");
        view.setText("Số lượt xem: "+ String.valueOf(view_number));

        chapter.setText("Số chương: " + String.valueOf(book_chapters_number));
        author.setText("Tác giả: "+ book_author);
        dsc.setText(intent.getStringExtra("dsc"));
        dsc.setMovementMethod(new ScrollingMovementMethod());
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book_id =  intent.getStringExtra("book_id");
                Intent intent1 = new Intent(intro_manga_before_read.this,ReadingActivity.class);
//                String chapter = String.valueOf(book.getChapters().size());
//                Log.d("debug",  book_id);
                intent1.putExtra("book_id",book_id);
                intent1.putExtra("name",book_name);
//                Log.d("debug", String.valueOf(Integer.valueOf(intent.getStringExtra("chapter"))));
                intent1.putExtra("chapter_size",  Integer.valueOf(book_chapters_number));
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

    public void getBookDataFromBookId() {
        Intent intent = getIntent();
        String received_book_id = intent.getStringExtra("book_id");
        books_database = FirebaseDatabase.getInstance().getReference("books");
        books_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(received_book_id)) {
                        Book book = dataSnapshot.getValue(Book.class);
                        book_name = book.getBook_title();
                        book_author = book.getAuthor_name();
                        book_category = book.getCategories();
                        image_link = book.getThumbnail();
                        view_number = book.getViews();
                        book_chapters_number = book.getChapters().size();
                        book_description = book.getBook_description();
                        initButton();
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