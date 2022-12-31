package com.example.appmanga.Activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Adapter.NotifyAdapter;
import com.example.appmanga.Adapter.SearchAdapter;
import com.example.appmanga.Adapter.clickListener;
import com.example.appmanga.DatabaseHandler;
import com.example.appmanga.Model.Book;
import com.example.appmanga.Model.Notify;
import com.example.appmanga.R;

import com.example.appmanga.Adapter.notifyClickListener;
import com.example.appmanga.intro_manga_before_read;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class NotifyActivity extends AppCompatActivity implements notifyClickListener {

    private ArrayList<Notify> list;
    private NotifyAdapter adapter;
    private RecyclerView recyclerView;
    LinearLayout nothingLayout;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        recyclerView = findViewById(R.id.rv_notify);
        nothingLayout = findViewById(R.id.layout_nothing);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        db = new DatabaseHandler(this);
        list = db.getAllNotify();
        list = sortNotify(list);
        adapter = new NotifyAdapter(this, list, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(Notify notify) {
        if (notify.getSeen() == 0){
            notify.setSeen(1);
            db.updateNotify(notify);
        }
    }

    public ArrayList<Notify> sortNotify(ArrayList<Notify> list) {
        Collections.sort(list, new Comparator<Notify>() {
            @Override
            public int compare(Notify o1, Notify o2) {
                return (o1.getReceivedTime() < o2.getReceivedTime()) ? -1 : ((o1.getReceivedTime() == o2.getReceivedTime()) ? 0 : 1);
            }
        });

        Collections.reverse(list);
        return list;
    }
}
