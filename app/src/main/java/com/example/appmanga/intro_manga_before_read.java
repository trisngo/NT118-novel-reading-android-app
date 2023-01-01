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
import android.util.EventLogTags;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Activity.LoginActivity;
import com.example.appmanga.Activity.ReadingActivity;
import com.example.appmanga.Adapter.AdapterViewIntroBeforeRead;
import com.example.appmanga.Adapter.RankingAdapter;
import com.example.appmanga.Adapter.ReadingListAdapter;
import com.example.appmanga.Adapter.TabDetailAdapter;
import com.example.appmanga.Adapter.commentAdapter;
import com.example.appmanga.ExtraFeature.MyFuntion;
import com.example.appmanga.Fragment.Chapter_BookFragment;
import com.example.appmanga.Fragment.CommentFragment;
import com.example.appmanga.Fragment.Description_BookFragment;
import com.example.appmanga.Model.Book;
import com.example.appmanga.Model.User;
import com.example.appmanga.Model.comment;

import com.example.appmanga.databinding.ActivityBookDetailBinding;
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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TooManyListenersException;

public class intro_manga_before_read extends AppCompatActivity {
    ActivityBookDetailBinding binding;

    Button btnRead;
    TextView name,category,chapter,author,dsc, view;
    RelativeLayout background_intro_read_manga;
    ImageButton like_button;
    String book_name, image_link, book_category, book_author, book_description, received_book_id, current_user_id;
    int view_number, book_chapters_number, like_number;
    ArrayList<String> user_liked_books_list;

    ImageView image;
    int count_category;
    private Book book = new Book();
    private Book book1 = new Book();
    private DatabaseReference database, books_database, users_database;;
    public String Uid;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        like_button = findViewById(R.id.btn_like);
        getBookDataFromBookId();
        dscsend();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TabDetailAdapter tabDetailAdapter = new TabDetailAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        binding.viewTabDetail.setAdapter(tabDetailAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewTabDetail);
        database = FirebaseDatabase.getInstance().getReference("books");
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

    public void getBookDataFromBookId() {
        Intent intent = getIntent();
        received_book_id = intent.getStringExtra("book_id");

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
                        like_number = book.getLikes();
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getUserLikedBooksFromEmail();
        } else {
            like_button.setImageResource(R.drawable.like_on_intro_1);
        }
    }

    public void initButton() {
        name = findViewById(R.id.tvNameManga);
        dsc = findViewById(R.id.dsc);
        category = findViewById(R.id.category);
        chapter = findViewById(R.id.chapter);
        image = findViewById(R.id.thumbnailintro);
        btnRead = findViewById(R.id.btnRead_Manga);
        author = findViewById(R.id.author);

        background_intro_read_manga = findViewById(R.id.background_intro_read_manga);
        name.setText("");
        category.setText("");
        chapter.setText("");
        author.setText("");
        getUIDFromEmail();


        Intent intent = getIntent();
        name.setText(book_name);
        name.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        name.setSelected(true);
        name.setSingleLine(true);

        Picasso.get().load(image_link).into(image);


        count_category = intent.getIntExtra("count_category",0);
        category.setText("Thể loại: "+ book_category +" ");


        chapter.setText("Số chương: " + String.valueOf(book_chapters_number));
        author.setText("Tác giả: "+ book_author);
        // dsc.setText(book_description);
        //     dsc.setMovementMethod(new ScrollingMovementMethod());


        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String book_id = intent.getStringExtra("book_id");
                Intent intent1 = new Intent(intro_manga_before_read.this, ReadingActivity.class);
//                String chapter = String.valueOf(book.getChapters().size());
//                Log.d("debug",  book_id);
                intent1.putExtra("book_id", book_id);
                intent1.putExtra("Uid", Uid);
                intent1.putExtra("name", book_name);
//                Log.d("debug", String.valueOf(Integer.valueOf(intent.getStringExtra("chapter"))));
                intent1.putExtra("chapter_size", Integer.valueOf(book_chapters_number));

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

        like_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                users_database = FirebaseDatabase.getInstance().getReference("users");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    if (user_liked_books_list.contains(received_book_id))
                    {
                        user_liked_books_list.remove(received_book_id);
                        like_number -= 1;
                        like_button.setImageResource(R.drawable.like_on_intro_1);
                    }
                    else
                    {
                        user_liked_books_list.add(received_book_id);
                        like_number += 1;
                        like_button.setImageResource(R.drawable.like_on_intro_2);
                    }
                    users_database.child(current_user_id + "/liked_books").setValue(user_liked_books_list);
                    books_database.child(received_book_id + "/likes").setValue(like_number);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Vui lòng đăng nhập để like truyện",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    public void dscsend(){
        Description_BookFragment description_bookFragment = new Description_BookFragment();
        CommentFragment commentFragment = new CommentFragment();
        Chapter_BookFragment chapter_bookFragment = new Chapter_BookFragment();
        Bundle bundle = new Bundle();
        bundle.putString("dsc",book_description);
        description_bookFragment.setArguments(bundle);
        Bundle bundle1 = new Bundle();
        bundle1.putString("book_id",received_book_id);
        commentFragment.setArguments(bundle1);
        Bundle bundle2 = new Bundle();
        bundle2.putString("book_id",received_book_id);
        chapter_bookFragment.setArguments(bundle2);
    }

    public void getUserLikedBooksFromEmail() {
        DatabaseReference users_database;
        users_database = FirebaseDatabase.getInstance().getReference("users");
        users_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        current_user_id = dataSnapshot.getKey();
                        user_liked_books_list = user.getLiked_books();
                        if (user_liked_books_list.contains(received_book_id))
                        {
                            like_button.setImageResource(R.drawable.like_on_intro_2);
                        }
                        else
                        {
                            like_button.setImageResource(R.drawable.like_on_intro_1);
                        }
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