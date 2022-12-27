package com.example.appmanga;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.appmanga.Activity.LoginActivity;
import com.example.appmanga.Activity.ReadingActivity;
import com.example.appmanga.Adapter.AdapterViewIntroBeforeRead;
import com.example.appmanga.Adapter.ReadingAdapter;
import com.example.appmanga.ExtraFeature.MyFuntion;
import com.example.appmanga.Model.Book;
import com.example.appmanga.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class intro_manga_before_read extends AppCompatActivity {

    Button btnRead;
    TextView name, category, chapter, author, dsc, view;
    ConstraintLayout backgournd_intro_read_manga;

    ImageView image;
    int count_category;
    private Book book = new Book();
    private DatabaseReference database;
    public String Uid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_manga_before_read);
        name = findViewById(R.id.tvNameManga);
        dsc = findViewById(R.id.dsc);
        category = findViewById(R.id.category);
        chapter = findViewById(R.id.chapter);
        image = findViewById(R.id.thumbnailintro);
        btnRead = findViewById(R.id.btnRead_Manga);
        author = findViewById(R.id.author);
        view = findViewById(R.id.view_number);
        backgournd_intro_read_manga = findViewById(R.id.backgournd_intro_read_manga);
        name.setText("");
        category.setText("");
        chapter.setText("");
        author.setText("");
        getUIDFromEmail();


        Intent intent = getIntent();
        String book_id = intent.getStringExtra("book_id");
        name.setText(intent.getStringExtra("name"));
        name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name.setSelected(true);
        name.setSingleLine(true);


        Picasso.get().load(intent.getStringExtra("image")).into(image);
        Picasso.get().load(intent.getStringExtra("image")).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                backgournd_intro_read_manga.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });
        count_category = intent.getIntExtra("count_category", 0);
        category.setText("Thể loại: " + intent.getStringExtra("category") + " ");
        view.setText("Số lượt xem: " + intent.getIntExtra("view_number", 0));

        chapter.setText("Số chương: " + intent.getStringExtra("chapter"));
        author.setText("Tác giả: " + intent.getStringExtra("author"));
        dsc.setText(intent.getStringExtra("dsc"));
        dsc.setMovementMethod(new ScrollingMovementMethod());
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book_id = intent.getStringExtra("book_id");
                Intent intent1 = new Intent(intro_manga_before_read.this, ReadingActivity.class);
//                String chapter = String.valueOf(book.getChapters().size());
//                Log.d("debug",  book_id);
                intent1.putExtra("book_id", book_id);
                intent1.putExtra("Uid", Uid);
                intent1.putExtra("name", intent.getStringExtra("name"));
//                Log.d("debug", String.valueOf(Integer.valueOf(intent.getStringExtra("chapter"))));
                intent1.putExtra("chapter_size", Integer.valueOf(intent.getStringExtra("chapter")));

                MyFuntion.addToReading(intro_manga_before_read.this, book_id, Uid);
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
                    Toast.makeText(intro_manga_before_read.this, "data send", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(intro_manga_before_read.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void getUIDFromEmail() {
        DatabaseReference users_database;
        users_database = FirebaseDatabase.getInstance().getReference("users");
        users_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        Uid = dataSnapshot.getKey();

                        break;
                    }
                }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                    if (book.getBook_title().equals(name.getText().toString())) {
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