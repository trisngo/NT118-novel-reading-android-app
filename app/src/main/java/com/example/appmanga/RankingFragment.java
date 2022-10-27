package com.example.appmanga;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
public class RankingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView rcvlistmanga;
    private TextView search_by_follow;
    private TextView search_by_hot;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MangaAdapter MangaAdapter;
    List<Manga> listManga;


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

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvlistmanga.setLayoutManager(layoutManager);
        listManga = new ArrayList<>();
        listManga.add(new Manga(R.drawable.dao_ma_nhi_de,"Dao ma nhi de", "Top"));
        listManga.add(new Manga(R.drawable.co_gai_trong_rung,"Co gai trong rung", "Top"));
        listManga.add(new Manga(R.drawable.doraemon_png,"Doraemon", "Top"));
        listManga.add(new Manga(R.drawable.phep_thuat_ma_am,"Phep thuat ma am", "Top"));
        listManga.add(new Manga(R.drawable.naruto_png,"Cau be Naruto", "Top"));
        listManga.add(new Manga(R.drawable.re_zero,"Re Zero", "Top"));
        listManga.add(new Manga(R.drawable.tu_tien_tai_sinh,"Tu tien trung sinh", "Top"));
        listManga.add(new Manga(R.drawable.tuyet_the_vo_than,"Tuyet the vo than", "Top"));
        listManga.add(new Manga(R.drawable.anime_bro,"Co gai phep thuat", "Top"));
        MangaAdapter = new MangaAdapter(listManga);
        rcvlistmanga.setAdapter(MangaAdapter);

        search_by_hot = v.findViewById(R.id.tv_seach_by_hot);
        search_by_follow= v.findViewById(R.id.seach_by_follow);


        search_by_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    search_by_follow.setBackgroundResource(R.drawable.bg_btn_search_after);
                    search_by_follow.setTextColor(Color.WHITE);
                    search_by_hot.setBackgroundResource(R.drawable.bg_btn_search);
                    search_by_hot.setTextColor(Color.BLACK);
                    Collections.sort(listManga, new Comparator<Manga>() {
                        @Override
                        public int compare(Manga manga, Manga manga1) {
                            return manga.getName().compareTo(manga1.getName());
                        }
                    });
                    Collections.reverse(listManga);
                    MangaAdapter.notifyDataSetChanged();
            }
        });

        search_by_hot.setOnClickListener(new View.OnClickListener() {
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
        });
        return v;
    }

    @Override
    public void onRefresh() {
        MangaAdapter.setData(listManga);
        Handler handler = new Handler();
         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                swipeRefreshLayout.setRefreshing(false);
             }
         },3000);
    }
}