package com.example.appmanga.Activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Adapter.SearchAdapter;
import com.example.appmanga.Model.Book;
import com.example.appmanga.R;
import com.example.appmanga.databinding.ActivitySearchBinding;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private ArrayList<Book> list;
    private SearchAdapter adapterBook;
    private RecyclerView rcvSach;

    DatabaseReference database;
    private EditText editText;

    private String author_name;
    ShimmerFrameLayout shimmerFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.appmanga.databinding.ActivitySearchBinding binding = ActivitySearchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rcvSach = findViewById(R.id.rcv_book);
        shimmerFrameLayout= findViewById(R.id.shimmer_books);

        rcvSach.setHasFixedSize(true);
        rcvSach.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapterBook = new SearchAdapter(this, list);
        rcvSach.setAdapter(adapterBook);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rcvSach.setLayoutManager(mLayoutManager);

        Intent i = getIntent();
        author_name = i.getStringExtra("author_name");


        getAllBooks();

        editText = findViewById(R.id.searchEt);//timkiem
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = editText.getText().toString();
                if (text.equals("")) {
                    getAllBooks();
                }

                try {
                    adapterBook.getFilter().filter(s);
                } catch (Exception ignored) {

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Book book = dataSnapshot.getValue(Book.class);
                    list.add(book);
                }
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                adapterBook.notifyDataSetChanged();

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





