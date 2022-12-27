package com.example.appmanga.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Adapter.ReadingAdapter;

import com.example.appmanga.Model.Book;
import com.example.appmanga.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReadinglistTabFragment extends Fragment  {
    private RecyclerView rvbooks;
    private ArrayList<Book> bookArrayList;
    private ReadingAdapter adapterBook;
    public String Uid;
    private FirebaseAuth firebaseAuth;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.tab_readinglist, container, false);
        rvbooks = root.findViewById(R.id.rcvBook);
        firebaseAuth = FirebaseAuth.getInstance();

        rvbooks.setLayoutManager(new LinearLayoutManager(getActivity()));
        Intent intent =getActivity().getIntent();
         Uid = intent.getStringExtra("Uid");
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            loadBookReading(Uid);
        }
        return root;
    }

    private void loadBookReading(String Uid) {
        bookArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(Uid).child("Reading")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()){
                            String book_id = ""+ds.child("book_id").getValue();
                            Book model = ds.getValue(Book.class);
                            model.setBookId(book_id);
                            bookArrayList.add(model);
                        }
                        adapterBook = new ReadingAdapter(getContext(), bookArrayList);
                        rvbooks.setAdapter(adapterBook);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }}
