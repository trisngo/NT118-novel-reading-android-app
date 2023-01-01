package com.example.appmanga.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Model.Book;
import com.example.appmanga.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class FavoriteTabFragment extends Fragment {
    private RecyclerView rvbooks;
    private ArrayList<Book> bookArrayList;


    private FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.tab_vote, container, false);
       firebaseAuth = FirebaseAuth.getInstance();
        return root;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
