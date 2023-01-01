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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.appmanga.Activity.ReadingActivity;
import com.example.appmanga.Model.Book;
import com.example.appmanga.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class Description_BookFragment extends Fragment {

    private TextView dsc;
    private String received_book_id;
    private String book_id,book_name,dsc1;
    private DatabaseReference books_database;

    public Description_BookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_description__book, container, false);
        dsc = root.findViewById(R.id.dsc);

        Intent intent =getActivity().getIntent();
        received_book_id = intent.getStringExtra("book_id");
        getDescription();


        return root;
    }

    public void getDescription() {
        books_database = FirebaseDatabase.getInstance().getReference("books");
        books_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(received_book_id)) {
                        Book book = dataSnapshot.getValue(Book.class);
                        book_name = book.getBook_title();
                        dsc1 = book.getBook_description();

                        dsc.setText(dsc1);
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