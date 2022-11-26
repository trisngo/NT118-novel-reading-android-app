package com.example.appmanga;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import  androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.IOException;

public class ReadingActivity extends AppCompatActivity {
    private static String baseUrl;
    private static WebView webView;
    private Toolbar toolbar;
    FloatingActionButton mFeatureFab, mBookmarkFab, mNextFab;
    TextView bookmarkText, nextText;

    Boolean isAllFabsVisible;

    private static int currentPageNumber = 1;

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

        bookmarkText = findViewById(R.id.tv_bookmark);
        nextText = findViewById(R.id.tv_next_chap);

        mBookmarkFab.setVisibility(View.GONE);
        mNextFab.setVisibility(View.GONE);
        bookmarkText.setVisibility(View.GONE);
        nextText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mFeatureFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
                mBookmarkFab.show();
                mNextFab.show();
                bookmarkText.setVisibility(View.VISIBLE);
                nextText.setVisibility(View.VISIBLE);

                isAllFabsVisible = true;
            } else {
                mBookmarkFab.hide();
                mNextFab.hide();
                bookmarkText.setVisibility(View.GONE);
                nextText.setVisibility(View.GONE);

                isAllFabsVisible = false;
            }
        });

        mBookmarkFab.setOnClickListener(
                view -> Toast.makeText(ReadingActivity.this, "Bookmark Added", Toast.LENGTH_SHORT
                ).show());

        mNextFab.setOnClickListener(
                view -> Toast.makeText(ReadingActivity.this, "Next chapter", Toast.LENGTH_SHORT
                ).show());
    }

    private static void showPage(int currentPN) {
        //            get data from db
        String data = new String("123123");

        currentPageNumber = currentPN;
        webView.loadDataWithBaseURL(baseUrl, data, "text/html", "UTF-8", null);
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
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
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
