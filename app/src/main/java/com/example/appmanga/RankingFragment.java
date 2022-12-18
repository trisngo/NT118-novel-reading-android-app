package com.example.appmanga;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmanga.Adapter.AdapterBook;
import com.example.appmanga.Adapter.AdapterHomeBook1;
import com.example.appmanga.Adapter.clickListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RankingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RankingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, clickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Book> book;
    private AdapterBook adapterBook;
    private RecyclerView rcvlistmanga;
    private TextView search_by_follow;
    private TextView search_by_hot;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DatabaseReference database;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RankingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RankingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RankingFragment newInstance(String param1, String param2) {
        RankingFragment fragment = new RankingFragment();
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
        View v = inflater.inflate(R.layout.fragment_ranking, container, false);
        rcvlistmanga = v.findViewById(R.id.rcv_item_layout);
        swipeRefreshLayout=v.findViewById(R.id.refresh_rcv_item);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.tv_ranking));



        book = new ArrayList<>();
        adapterBook = new AdapterBook(getContext(), book);
        rcvlistmanga.setAdapter(adapterBook);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvlistmanga.setLayoutManager(layoutManager);
        getAllBooks();
        search_by_hot = v.findViewById(R.id.tv_seach_by_hot);
        search_by_follow= v.findViewById(R.id.seach_by_follow);


        /*search_by_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    search_by_follow.setBackgroundResource(R.drawable.bg_btn_search_after);
                    search_by_follow.setTextColor(Color.WHITE);
                    search_by_hot.setBackgroundResource(R.drawable.bg_btn_search);
                    search_by_hot.setTextColor(Color.BLACK);
                    Collections.sort(book, new Comparator<Book>() {
                        @Override
                        public int compare(Manga manga, Manga manga1) {
                            return manga.getName().compareTo(manga1.getName());
                        }
                    });
                    Collections.reverse(book);
                    MangaAdapter.notifyDataSetChanged();
            }
        });*/

        /*search_by_hot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_by_hot.setBackgroundResource(R.drawable.bg_btn_search_after);
                search_by_hot.setTextColor(Color.WHITE);
                search_by_follow.setBackgroundResource(R.drawable.bg_btn_search);
                search_by_follow.setTextColor(Color.BLACK);
                Collections.sort(listManga, new Comparator<Manga>() {
                    @Override
                    public int compare(Manga manga, Manga manga1) {
                        return manga1.getName().compareTo(manga.getName());
                    }
                });
                Collections.reverse(listManga);
                MangaAdapter.notifyDataSetChanged();
            }
        });*/
        return v;
    }

    private void getAllBooks() {
        database = FirebaseDatabase.getInstance().getReference("books");
        database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (book != null) {
                    book.clear();
                }
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Book book1 = dataSnapshot.getValue(Book.class);
                    book.add(book1);
                }
                adapterBook.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
    //@Override
    public void onRefresh(){}/* {
        MangaAdapter.setData(book);
        Handler handler = new Handler();
         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                swipeRefreshLayout.setRefreshing(false);
             }
         },3000);
    }*/

    @Override
    public void onItemClick(Book book) {

    }
}