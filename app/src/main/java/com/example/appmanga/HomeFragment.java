package com.example.appmanga;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.appmanga.Adapter.AdapterHomeBook1;
import com.example.appmanga.Adapter.AdapterHomeBook2;
import com.example.appmanga.Adapter.clickListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.ActionCodeEmailInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements clickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    public View fragment_layout;

    private String mParam1;
    private String mParam2;
    private String categories ;
    private ArrayList<Book> listHighlights, list4U, listNewest;
    private AdapterHomeBook1 adapterHighlights;
    AdapterHomeBook2 adapter4U, adapterNewest;
    private RecyclerView rcvBookHighlights, rcvBook4U, rcvBookNewest;

    DatabaseReference database;
    private EditText editText;

    private String author_name;
    ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayout2, shimmerFrameLayout3;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragment_layout = inflater.inflate(R.layout.fragment_home, container, false);
        init();

        return fragment_layout;
    }

    public void init() {
        rcvBookHighlights = (RecyclerView) fragment_layout.findViewById(R.id.rcv_book_home1);
        rcvBook4U = (RecyclerView) fragment_layout.findViewById((R.id.rcv_home_4_you));
        rcvBookNewest = (RecyclerView) fragment_layout.findViewById((R.id.rcv_home_newest));
        shimmerFrameLayout= (ShimmerFrameLayout) fragment_layout.findViewById(R.id.shimmer_books);
        shimmerFrameLayout2 = (ShimmerFrameLayout) fragment_layout.findViewById(R.id.shimmer_books_2);
        shimmerFrameLayout3 = (ShimmerFrameLayout) fragment_layout.findViewById(R.id.shimmer_books_3);

        rcvBookHighlights.setHasFixedSize(true);
        rcvBook4U.setHasFixedSize(true);
        rcvBookNewest.setHasFixedSize(true);

        listHighlights = new ArrayList<>();
        list4U = new ArrayList<>();
        listNewest = new ArrayList<>();

        adapterHighlights = new AdapterHomeBook1(getContext(), listHighlights,this);
        adapter4U = new AdapterHomeBook2(getContext(), list4U, this);
        adapterNewest = new AdapterHomeBook2(getContext(), listNewest, this);

        rcvBookHighlights.setAdapter(adapterHighlights);
        rcvBook4U.setAdapter(adapter4U);
        rcvBookNewest.setAdapter(adapterNewest);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager4U = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerNewest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rcvBookHighlights.setLayoutManager(mLayoutManager);
        rcvBook4U.setLayoutManager(layoutManager4U);
        rcvBookNewest.setLayoutManager(layoutManagerNewest);

        getAllBooks();

    }

    private void getAllBooks() {
        database = FirebaseDatabase.getInstance().getReference("books");
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (listHighlights != null) {
                    listHighlights.clear();
                    list4U.clear();
                    listNewest.clear();
                }
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Book book = dataSnapshot.getValue(Book.class);
                    book.setBookId(dataSnapshot.getKey());
                    listHighlights.add(book);
                    list4U.add(book);
                    listNewest.add(book);
                    if (i == 2) {
                        break;
                    }
                    i++;
                }
                Log.d("debug",list4U.toString());
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout2.stopShimmer();
                shimmerFrameLayout2.setVisibility(View.GONE);
                shimmerFrameLayout3.stopShimmer();
                shimmerFrameLayout3.setVisibility(View.GONE);
                adapterHighlights.notifyDataSetChanged();
                adapter4U.notifyDataSetChanged();
                adapterNewest.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    // call Book Intro Activity
    @Override
    public void onItemClick(Book book) {
        Intent intent = new Intent(getActivity(), intro_manga_before_read.class);
        intent.putExtra("book_id",book.getBookId());
        intent.putExtra("name",book.book_title);
        intent.putExtra("image",book.thumbnail);
        intent.putExtra("category",categories);
        String chapter = String.valueOf(book.getChapters().size());
        intent.putExtra("chapter",chapter);
        intent.putExtra("author",book.author_name);
        intent.putExtra("dsc",book.book_description);

        startActivity(intent);
    }
}