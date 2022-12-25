package com.example.appmanga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import  androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadingActivity extends AppCompatActivity {
    public static String nameClass;
    private static String baseUrl;
    private static WebView webView;
    private Toolbar toolbar;
    FloatingActionButton mFeatureFab, mBookmarkFab, mNextFab, mPreviousFab;
    TextView bookmarkText, nextText, previousText;

    Boolean isAllFabsVisible;

    private static int currentPageNumber = 1;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();
    DatabaseReference booksRef = root.child("books");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        webView = findViewById(R.id.webview_book);
        showPage(currentPageNumber);

        mFeatureFab = findViewById(R.id.fab_feature);
        mBookmarkFab = findViewById(R.id.fab_bookmark);
        mNextFab = findViewById(R.id.fab_next_chap);
        mPreviousFab = findViewById(R.id.fab_previous_chap);

//        bookmarkText = findViewById(R.id.tv_bookmark);
//        nextText = findViewById(R.id.tv_next_chap);
//        previousText = findViewById(R.id.tv_previous_chap);

        mBookmarkFab.setVisibility(View.GONE);
        mNextFab.setVisibility(View.GONE);
        mPreviousFab.setVisibility(View.GONE);
//        bookmarkText.setVisibility(View.GONE);
//        nextText.setVisibility(View.GONE);
//        previousText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mFeatureFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                mBookmarkFab.show();
                mNextFab.show();
                mPreviousFab.show();
//                bookmarkText.setVisibility(View.VISIBLE);
//                nextText.setVisibility(View.VISIBLE);
//                previousText.setVisibility(View.VISIBLE);

                isAllFabsVisible = true;
            } else {
                mBookmarkFab.hide();
                mNextFab.hide();
                mPreviousFab.hide();
//                bookmarkText.setVisibility(View.GONE);
//                nextText.setVisibility(View.GONE);
//                previousText.setVisibility(View.GONE);

                isAllFabsVisible = false;
            }
        });

        mBookmarkFab.setOnClickListener(
            view -> {
                Toast.makeText(ReadingActivity.this, "Bookmark Added", Toast.LENGTH_SHORT).show();

        });

        mNextFab.setOnClickListener(
            view -> {
                int size = getIntent().getIntExtra("chapter_size", 0);
                Log.d("debug", String.valueOf(size));
                if (currentPageNumber > size-1){
                    Toast.makeText(ReadingActivity.this, "Do not have any next chapter", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ReadingActivity.this, "Next chapter", Toast.LENGTH_SHORT).show();
                    showPage(currentPageNumber + 1);
                }
            });
        mPreviousFab.setOnClickListener(
            view -> {
                if (currentPageNumber == 1){
                    Toast.makeText(ReadingActivity.this, "Do not have any previous chapter", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ReadingActivity.this, "Previous chapter", Toast.LENGTH_SHORT).show();
                    showPage(currentPageNumber - 1);
                }
            });
    }

    private void showPage(int currentPN) {
        //            get data from db

//        booksRef.child("book1669912049").child("chapter").child("chapter1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                Log.d("json", String.valueOf(task.getResult().getValue()));
//            }
//        });
        Intent intent = getIntent();
//        Log.d("debug2",  intent.getStringExtra("book_id"));
        Query query = booksRef.child(intent.getStringExtra("book_id")).child("chapters");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
//                for (DataSnapshot chap : dataSnapshot.getChildren()) {
//                    Log.d("json", chap.getValue().toString());
//                    String data = chap.getValue().toString();
//                    data = data.replace("\n", "<br><br>");
//
//                    currentPageNumber = currentPN;
//                    webView.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", null);
//                }

                Log.d("json", dataSnapshot.child("chapter" + currentPN).getValue().toString());
                getSupportActionBar().setTitle(intent.getStringExtra("name"));
                getSupportActionBar().setSubtitle("Chapter " + currentPN);
                String data = "<p style=\"line-height:1.5;text-align:justify;\">";
                data += dataSnapshot.child("chapter" + currentPN).getValue().toString();
                data += "</p>";
                data = data.replace("\n", "<br><br>");

                currentPageNumber = currentPN;
                webView.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", null);

            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        });
       //        webView.scrollTo(0, 0);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        super.dispatchTouchEvent(ev);
        return webView.onTouchEvent(ev);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reader_menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle arrow click here
//        if (item.getItemId() == android.R.id.home) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish(); // close this activity and return to preview activity (if there is any)
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                currentPageNumber = 1;
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onBackPressed() {
//        //Works the opposite ;)
//        if (isFullScreen() ) toggleFullScreenMode();
//        else{
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
//    }
}
