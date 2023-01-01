package com.example.appmanga.Fragment;


import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appmanga.Activity.ReadingActivity;
import com.example.appmanga.Activity.favoriteBooksActivity;
import com.example.appmanga.Model.Book;
import com.example.appmanga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class Chapter_BookFragment extends Fragment{
    private ListView listView;
    private View view;
    private String received_book_id, book_name;
    private DatabaseReference books_database;
    private ArrayList<String> book_chapters;


    public Chapter_BookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chapter__book, container, false);
        listView = view.findViewById(R.id.lvChapter);
        Intent intent =getActivity().getIntent();
        received_book_id = intent.getStringExtra("book_id");
        getAllChapters();
        return view;
    }

    public void getAllChapters() {
        books_database = FirebaseDatabase.getInstance().getReference("books");
        books_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(received_book_id)) {
                        Book book = dataSnapshot.getValue(Book.class);
                        book_name = book.getBook_title();
                        book_chapters = new ArrayList<String>(book.getChapters().keySet());
                        Collections.sort(book_chapters);
                        initLayout();
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

    public void initLayout() {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(),R.layout.chapters_item,book_chapters);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String chapter_id = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getContext(), ReadingActivity.class);
                intent.putExtra("book_id",received_book_id);
                intent.putExtra("name", book_name);
                intent.putExtra("chapter_id",chapter_id);
                intent.putExtra("chapter_size", Integer.valueOf(book_chapters.size()));
                startActivity(intent);
            }
        });
    }


}