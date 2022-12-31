package com.example.appmanga.Fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmanga.Activity.NotifyActivity;
import com.example.appmanga.Activity.ReadingActivity;
import com.example.appmanga.Adapter.HomeBookAdapter1;
import com.example.appmanga.Adapter.HomeBookAdapter2;
import com.example.appmanga.Adapter.clickListener;
import com.example.appmanga.DatabaseHandler;
import com.example.appmanga.Model.Book;
import com.example.appmanga.Model.Notify;
import com.example.appmanga.Model.User;
import com.example.appmanga.R;
import com.example.appmanga.intro_manga_before_read;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

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
    private ArrayList<Book> listHighlights, list4U, listNewest, listViews, listLikes;
    private ArrayList<Notify> listNotify;
    private HomeBookAdapter1 adapterHighlights;
    private HomeBookAdapter2 adapter4U, adapterNewest, adapterViews, adapterLikes;
    private RecyclerView rcvBookHighlights, rcvBook4U, rcvBookNewest, rcvBookViews, rcvBookLikes;

    DatabaseReference database;
    private EditText editText;

    private TextView tv_username;
    private ImageView iv_bell, iv_red_dot;
    private String author_name;
    ShimmerFrameLayout shimmerFrameLayout, shimmerFrameLayout2, shimmerFrameLayout3, shimmerFrameLayout4, shimmerFrameLayout5;

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

    BroadcastReceiver broadCastNewNotify = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            detectSeen();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().registerReceiver(this.broadCastNewNotify, new IntentFilter("bcNewNotify"));
        FirebaseMessaging.getInstance().subscribeToTopic("MainTopic");
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
        tv_username = (TextView) fragment_layout.findViewById(R.id.tv_home_1);
        iv_bell = (ImageView)  fragment_layout.findViewById(R.id.iv_bell);
        iv_red_dot = (ImageView) fragment_layout.findViewById(R.id.iv_red_circle);
        iv_bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NotifyActivity.class);
                startActivity(intent);
            }
        });

        rcvBookHighlights = (RecyclerView) fragment_layout.findViewById(R.id.rcv_book_home1);
        rcvBook4U = (RecyclerView) fragment_layout.findViewById((R.id.rcv_home_4_you));
        rcvBookNewest = (RecyclerView) fragment_layout.findViewById((R.id.rcv_home_newest));
        rcvBookViews = (RecyclerView) fragment_layout.findViewById((R.id.rcv_home_most_viewed));
        rcvBookLikes = (RecyclerView) fragment_layout.findViewById((R.id.rcv_home_lovest));

        shimmerFrameLayout= (ShimmerFrameLayout) fragment_layout.findViewById(R.id.shimmer_books);
        shimmerFrameLayout2 = (ShimmerFrameLayout) fragment_layout.findViewById(R.id.shimmer_books_2);
        shimmerFrameLayout3 = (ShimmerFrameLayout) fragment_layout.findViewById(R.id.shimmer_books_3);
        shimmerFrameLayout4 = (ShimmerFrameLayout) fragment_layout.findViewById(R.id.shimmer_books_4);
        shimmerFrameLayout5 = (ShimmerFrameLayout) fragment_layout.findViewById(R.id.shimmer_books_5);

        rcvBookHighlights.setHasFixedSize(true);
        rcvBook4U.setHasFixedSize(true);
        rcvBookNewest.setHasFixedSize(true);
        rcvBookViews.setHasFixedSize(true);
        rcvBookLikes.setHasFixedSize(true);

        listHighlights = new ArrayList<>();
        list4U = new ArrayList<>();
        listNewest = new ArrayList<>();
        listViews = new ArrayList<>();
        listLikes = new ArrayList<>();

        adapterHighlights = new HomeBookAdapter1(getContext(), listHighlights,this);
        adapter4U = new HomeBookAdapter2(getContext(), list4U, this);
        adapterNewest = new HomeBookAdapter2(getContext(), listNewest, this);
        adapterViews = new HomeBookAdapter2(getContext(), listViews, this);
        adapterLikes = new HomeBookAdapter2(getContext(), listLikes, this);

        rcvBookHighlights.setAdapter(adapterHighlights);
        rcvBook4U.setAdapter(adapter4U);
        rcvBookNewest.setAdapter(adapterNewest);
        rcvBookViews.setAdapter(adapterViews);
        rcvBookLikes.setAdapter(adapterLikes);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        RecyclerView.LayoutManager layoutManager4U = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerNewest = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerViews = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager layoutManagerLikes = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        rcvBookHighlights.setLayoutManager(mLayoutManager);
        rcvBook4U.setLayoutManager(layoutManager4U);
        rcvBookNewest.setLayoutManager(layoutManagerNewest);
        rcvBookViews.setLayoutManager(layoutManagerViews);
        rcvBookLikes.setLayoutManager(layoutManagerLikes);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            getUserFromFirebase();
        }else{
            tv_username.setText("Xin chào,");
        }
        getAllBooks();
        detectSeen();
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
                    listViews.clear();
                    listLikes.clear();
                }
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Book book = dataSnapshot.getValue(Book.class);
                    book.setBookId(dataSnapshot.getKey());
                    listHighlights.add(book);
                    list4U.add(book);
                    listNewest.add(book);
                    listViews.add(book);
                    listLikes.add(book);
                    i++;
                }
                Log.d("debug",list4U.toString());
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                shimmerFrameLayout2.stopShimmer();
                shimmerFrameLayout2.setVisibility(View.GONE);
                shimmerFrameLayout3.stopShimmer();
                shimmerFrameLayout3.setVisibility(View.GONE);
                shimmerFrameLayout4.stopShimmer();
                shimmerFrameLayout4.setVisibility(View.GONE);
                shimmerFrameLayout5.stopShimmer();
                shimmerFrameLayout5.setVisibility(View.GONE);

                listHighlights = randomList(listHighlights);
                listNewest = sortListNewest(listNewest);
                listViews = sortListViews(listViews);
                listLikes = sortListLikes(listLikes);

                adapterHighlights.notifyDataSetChanged();
                adapter4U.notifyDataSetChanged();
                adapterNewest.notifyDataSetChanged();
                adapterViews.notifyDataSetChanged();
                adapterLikes.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public ArrayList<Book> randomList(ArrayList<Book> list) {
        Collections.shuffle(list);
        return list;
    }

    public ArrayList<Book> sortListNewest(ArrayList<Book> list) {
        Collections.sort(list, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                Timestamp ts1 = new Timestamp(Long.parseLong(o1.getUpdated_time()));
                Timestamp ts2 = new Timestamp(Long.parseLong(o2.getUpdated_time()));
                return ts1.compareTo(ts2);
            }
        });
        Collections.reverse(list);
        return list;
    }

    public ArrayList<Book> sortListViews(ArrayList<Book> list) {
        Collections.sort(list, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getViews() - o2.getViews();
            }
        });
        Collections.reverse(list);
        return list;
    }

    public ArrayList<Book> sortListLikes(ArrayList<Book> list) {
        Collections.sort(list, new Comparator<Book>() {
            @Override
            public int compare(Book o1, Book o2) {
                return o1.getLikes() - o2.getLikes();
            }
        });
        Collections.reverse(list);
        return list;
    }

    // call Book Intro Activity
    @Override
    public void onItemClick(Book book) {

    }

    public void getUserFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference();
        DatabaseReference usersRef = root.child("users");
        Query query = usersRef.orderByKey();
        query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isFound = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                        if (userData.get("email").equals(user.getEmail())) {
                            tv_username.setText("Xin chào, " + String.valueOf(userData.get("username")));
                            isFound = true;
                            break;
                        }
                    }
                    if (isFound==false) tv_username.setText("Can not get data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void detectSeen() {
        listNotify = new ArrayList<>();
        DatabaseHandler db = new DatabaseHandler(getContext());
        listNotify = db.getAllNotify();
        int seen = countSeenNotify(listNotify);
        if (seen > 0) {
            iv_red_dot.setVisibility(View.VISIBLE);
        }
        else {
            iv_red_dot.setVisibility(View.GONE);
        }
    }
    public int countSeenNotify(ArrayList<Notify> list) {
        int count = 0;
        for(Notify item : list) {
            if (item.getSeen() == 0){
                count += 1;
            }
        }
        return count;
    }

    @Override
    public void onResume() {
        detectSeen();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadCastNewNotify);
    }

}