package com.example.appmanga;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import  androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

        mBookmarkFab.setVisibility(View.GONE);
        mNextFab.setVisibility(View.GONE);
        mPreviousFab.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mFeatureFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                mBookmarkFab.show();
                mNextFab.show();
                mPreviousFab.show();

                isAllFabsVisible = true;
            } else {
                mBookmarkFab.hide();
                mNextFab.hide();
                mPreviousFab.hide();

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

        LinearLayout layout= (LinearLayout) findViewById(R.id.shimmer_item_reader);
        for(int i=0; i<18; i++){
            LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.shimmer_reader, null);
            layout.addView(view);
        }

    }

    private void showPage(int currentPN) {
        Intent intent = getIntent();
        Query query = booksRef.child(intent.getStringExtra("book_id")).child("chapters");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d("json", dataSnapshot.child("chapter" + currentPN).getValue().toString());
                getSupportActionBar().setTitle(intent.getStringExtra("name"));
                getSupportActionBar().setSubtitle("Chapter " + currentPN);
                String data = "<p style=\"font-size:1.275em;line-height:1.5;text-align:justify;white-space:pre-line;\">";
                data += dataSnapshot.child("chapter" + currentPN).getValue().toString();
                data += "</p>";
//                data = data.replace("\n", "<br><br>");

                currentPageNumber = currentPN;

                LinearLayout layout = (LinearLayout) findViewById(R.id.shimmer_item_reader);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    child.setVisibility(View.GONE);
                }

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
