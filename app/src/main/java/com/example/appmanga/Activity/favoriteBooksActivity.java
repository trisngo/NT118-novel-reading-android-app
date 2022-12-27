package com.example.appmanga.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.appmanga.Adapter.SearchAdapter;
import com.example.appmanga.Model.Book;
import com.example.appmanga.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class favoriteBooksActivity extends AppCompatActivity {

    private ArrayList<Book> list;
    private SearchAdapter adapterBook;
    private RecyclerView favBooks;
    private ArrayList<String> user_liked_books;

    DatabaseReference database;

    private String author_name;
    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout nothingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite_books);
        favBooks = findViewById(R.id.fav_books);
        shimmerFrameLayout= findViewById(R.id.shimmer_books);
        nothingLayout = findViewById(R.id.layout_nothing);

        favBooks.setHasFixedSize(true);
        favBooks.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterBook = new SearchAdapter(this, list);
        favBooks.setAdapter(adapterBook);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        favBooks.setLayoutManager(mLayoutManager);

        Intent intent = getIntent();
        author_name = intent.getStringExtra("author_name");
        user_liked_books = (ArrayList<String>) intent.getExtras().getSerializable("user_liked_books");
        getAllBooks();

    }

    private void getAllBooks() {
        database = FirebaseDatabase.getInstance().getReference("books");
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (list != null) {
                    list.clear();
                }
                Log.d("size",String.valueOf(user_liked_books.size()));
                Log.d("value",user_liked_books.get(0));
                if (user_liked_books.size()==1 && user_liked_books.get(0).equals("default")) {
                    Log.d("Hehe","=========================");
                    nothingLayout.setVisibility(View.VISIBLE);
                    shimmerFrameLayout.setVisibility(View.GONE);
                }
                else {
                    nothingLayout.setVisibility(View.GONE);
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Book book = dataSnapshot.getValue(Book.class);
                        if (user_liked_books.contains(dataSnapshot.getKey())) {
                            list.add(book);
                        }
                    /*Log.d("Book name =============================",book.book_title);
                    for (int i = 0; i < book.categories.size(); i++)
                    {
                        Log.d("Book category",book.categories.get(i));
                    }*/
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    adapterBook.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    protected void onPause() {
        shimmerFrameLayout.stopShimmer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        shimmerFrameLayout.startShimmer();
        super.onResume();
    }
}