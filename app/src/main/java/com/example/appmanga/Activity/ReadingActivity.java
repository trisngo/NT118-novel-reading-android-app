package com.example.appmanga.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import  androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appmanga.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class ReadingActivity extends AppCompatActivity {
    public static String nameClass;
    private static String baseUrl;
    private static WebView webView;
    private Toolbar toolbar;
    FloatingActionButton mFeatureFab, mBookmarkFab, mNextFab, mPreviousFab;

    Boolean isAllFabsVisible;

    private static int currentPageNumber = 1;

    private int charSize = 16;
    private long book_views;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference root = db.getReference();
    DatabaseReference booksRef = root.child("books");

    private static final DecimalFormat df = new DecimalFormat("0.000");

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

        ActionBar ab = getSupportActionBar();

        webView = findViewById(R.id.webview_book);
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_SCROLL) {
//                    if (ab.isShowing()) {
//                        ab.hide();
//                    } else {
//                        ab.show();
//                    }
//                    return true;
//                } else return false;
//            }
//        });
        Intent intent = getIntent();
        String chapter_id = intent.getStringExtra("chapter_id");
        Log.d("PN", chapter_id.split("chapter")[1]);
        currentPageNumber = Integer.valueOf(chapter_id.split("chapter")[1]);
        showPage(currentPageNumber);

        mFeatureFab = findViewById(R.id.fab_feature);
//        mBookmarkFab = findViewById(R.id.fab_bookmark);
        mNextFab = findViewById(R.id.fab_next_chap);
        mPreviousFab = findViewById(R.id.fab_previous_chap);

//        mBookmarkFab.setVisibility(View.GONE);
        mNextFab.setVisibility(View.GONE);
        mPreviousFab.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mFeatureFab.setOnClickListener(view -> {
            if (!isAllFabsVisible) {
//                mBookmarkFab.show();
                mNextFab.show();
                mPreviousFab.show();

                isAllFabsVisible = true;
            } else {
//                mBookmarkFab.hide();
                mNextFab.hide();
                mPreviousFab.hide();

                isAllFabsVisible = false;
            }
        });

//        mBookmarkFab.setOnClickListener(
//            view -> {
//                Toast.makeText(ReadingActivity.this, "Bookmark Added", Toast.LENGTH_SHORT).show();
//                mBookmarkFab.hide();
//                mNextFab.hide();
//                mPreviousFab.hide();
//                isAllFabsVisible = false;
//            });
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
//                mBookmarkFab.hide();
                mNextFab.hide();
                mPreviousFab.hide();
                isAllFabsVisible = false;
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
//                mBookmarkFab.hide();
                mNextFab.hide();
                mPreviousFab.hide();
                isAllFabsVisible = false;
            });

        LinearLayout layout= (LinearLayout) findViewById(R.id.shimmer_item_reader);
        for(int i=0; i<18; i++){
            LinearLayout view = (LinearLayout) getLayoutInflater().inflate(R.layout.shimmer_reader, null);
            layout.addView(view);
        }
    }

    private void showPage(int currentPN) {
        Intent intent = getIntent();
        Query views_query = booksRef.child(intent.getStringExtra("book_id")).child("views");
        views_query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                book_views = (long) dataSnapshot.getValue();
            }
            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        });
        Query query = booksRef.child(intent.getStringExtra("book_id")).child("chapters");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                booksRef.child(intent.getStringExtra("book_id") + "/views").setValue(book_views+1);
                Log.d("json", dataSnapshot.child("chapter" + currentPN).getValue().toString());
                getSupportActionBar().setTitle(intent.getStringExtra("name"));
                getSupportActionBar().setSubtitle("Chapter " + currentPN);
                double sizeEm = (double) charSize/12;
                String data = "<p style=\"font-size:" +df.format(sizeEm) + "em;line-height:1.5;text-align:justify;white-space:pre-line;\">";
                data += dataSnapshot.child("chapter" + currentPN).getValue().toString();
                data += "</p>";
                data = data.replace("\n", "<br><br>");

                Log.d("debug",data);
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
//        item_your_choice = menu.findItem(R.id.character_size);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                currentPageNumber = 1;
                this.finish();
                return true;
            case R.id.character_size:
                showPickerDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showPickerDialog() {
        final AlertDialog.Builder d = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.size_reader_dialog, null);
        d.setTitle("Kích thước chữ");
        d.setMessage("Lựa chọn kích thước:");
        d.setView(dialogView);
        final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.np_size);
        numberPicker.setMaxValue(20);
        numberPicker.setMinValue(6);
//        numberPicker.setDisplayedValues( new String[] { "2", "5", "10" } );
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                Log.d("dialog", "onValueChange: ");
            }
        });
        d.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("dialog", "onClick: " + numberPicker.getValue());
                charSize = numberPicker.getValue();
                showPage(currentPageNumber);
//                String value = numberPicker.getDisplayedValues()[numberPicker.getValue()];
//                Log.d("dialog", "onClick value: " + value);

//                int pick;
//                switch (numberPicker.getValue()) {
//                    case 1:
//                        pick = 2;
//                        break;
//                    case 2:
//                        pick = 5;
//                        break;
//                    case 3:
//                        pick = 10;
//                        break;
//                    default:
//                        //...
//                }
            }
        });
        d.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = d.create();
        alertDialog.show();
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
