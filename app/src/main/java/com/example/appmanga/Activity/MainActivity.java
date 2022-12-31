package com.example.appmanga.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.example.appmanga.Model.Book;
import com.example.appmanga.Fragment.HomeFragment;
import com.example.appmanga.Fragment.ProfileFragment;
import com.example.appmanga.Model.Notify;
import com.example.appmanga.R;
import com.example.appmanga.Fragment.RankingFragment;
import com.example.appmanga.Model.User;
import com.example.appmanga.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public String Uid;
    private String key;
    private int flag=0;
    ArrayList<String> user_liked_books_list = new ArrayList<>();
    DatabaseReference books_database, users_database;
    Map<String,String> id_to_user_list = new HashMap<String,String>();
    Map<String,String> user_comments_list = new HashMap<String,String>();


    // Gọi hàm này để lấy user id từ email
    public void getUIDFromEmail() {
        DatabaseReference users_database;
        users_database = FirebaseDatabase.getInstance().getReference("users");
        users_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        Uid= dataSnapshot.getKey();
                        break;
                    }
                }}
            }
                @Override
                public void onCancelled (@NonNull DatabaseError error){
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getUIDFromEmail();
        FirebaseMessaging.getInstance().subscribeToTopic("MainTopic");
        replaceFragment(new HomeFragment(),key);
        key="home";
        //Sự kiện bấm thay đổi fragmanent
        binding .bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.Home:
                    if(key!="home"){
                        key="home";
                        replaceFragment(new HomeFragment(),key);
                        break;
                    }else{
                        break;
                    }
                case R.id.Ranking:
                    if (key=="home"){
                        flag=1;
                        key="Rank";
                        replaceFragment(new RankingFragment(),key);
                        break;
                    }else if (key=="Profile"){
                        flag=2;
                        key="Rank";
                        replaceFragment(new RankingFragment(),key);
                        break;
                    }else if(key == "Rank"){
                        break;
                    }
                case  R.id.Profile:
                    if(key=="Profile"){
                        break;
                    }else{
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            key="Profile";
                            replaceFragment(new ProfileFragment(),key);
                            break;
                        }else{
                            Intent intent = new Intent(MainActivity.this, IntroActivity.class);
                            startActivity(intent);
                        }
                    }
            }
            return true;
        });


    }

    public void library(View view){
        Intent intent = new Intent(MainActivity.this, LibraryActivity.class);

        intent.putExtra("Uid",Uid);
        startActivity(intent);
//        finish();
    }
    public void search(View view){
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
//        finish();
    }

    //Hàm thể hiện fragment
    private void replaceFragment(Fragment fragment, String key){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(key=="home"){
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        }else if (key=="Profile"){
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(key=="Rank"){
            if (flag==1){
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
            }else if (flag==2){
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        }
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }

    // Gọi hàm này để cập nhật số like
    public void update_likes() {
        Log.d("Update likes and views","Update value begin");
        books_database.child("book1669997004/likes").setValue(44);
        books_database.child("book1669997004/views").setValue(562);
        Log.d("Update likes and views","Update value complete");
    }



    // Gọi hàm này để cập nhật comment
    public void update_comments() {
        user_comments_list.put("user1671252751","Khi nào ra chap mới vậy?");
        for (Map.Entry<String,String> entry : user_comments_list.entrySet())  {
            Log.d("New comment",entry.getKey() + ": " + entry.getValue());
        }
        books_database.child("book1669911762/comments").setValue(user_comments_list);
        Log.d("Update comments","Update value complete");
    }


    // Gọi hàm này để lấy tất cả comment của 1 truyện
    public void get_book_comments() {
        Log.d("Msg","In get user comments");
        users_database = FirebaseDatabase.getInstance().getReference("users");
        books_database = FirebaseDatabase.getInstance().getReference("books");
        users_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    id_to_user_list.put(dataSnapshot.getKey(),user.getUsername());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        books_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals("book1669911762")) {
                        Book book = dataSnapshot.getValue(Book.class);
                        for (Map.Entry<String,String> entry : book.getComments().entrySet()) {
                            user_comments_list.put(entry.getKey(),entry.getValue());
                            Log.d("User:Comments",id_to_user_list.get(entry.getKey()) + ": " + entry.getValue());
                        }
                        //update_comments();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    // Gọi hàm này để thêm sách vào database
    void init_database() {
        /*String book_title = "Angel Of Death";
        String book_description = "Thiếu nữ 13 tuổi Rachel tỉnh dậy và nhận ra mình đang bị nhốt dưới tầng hầm của một tòa nhà bỏ hoang. Không chút ký ức, cũng như manh mối về nơi mình đang ở, lạc lối và choáng váng, cô lang thang khắp tòa nhà. Trong lúc tìm kiếm manh mối tại nơi đây, cô chạm mặt một người đàn ông được bao phủ bởi băng gạc. Hắn là một kẻ mang lưỡi hái, tự nhận mình tên Zack. Họ bắt đầu một mối quan hệ kỳ lạ, được thắt chặt bằng những lời hứa điên rồ";
        String thumbnail = "https://m.media-amazon.com/images/M/MV5BOWRjMzU2NTYtMGRlOS00YjQwLWFiYWQtODFlOGNiNmU4OTVlXkEyXkFqcGdeQXVyMzgxODM4NjM@._V1_FMjpg_UX1000_.jpg";
        String created_time = "01/12/2022";
        String updated_time = "01/12/2022";
        String author_name = "Chiren Kina";
        int likes = 43;
        int views = 561;
        String categories = "Actions";

        Map<String, String> comments = new HashMap<>();
        comments.put("BuiDucAnh","Cũng hay hé hé hé");
        Map<String,String> chapters = new HashMap<>();
        chapters.put("chapter2","");
        Book exampleSach = new Book(book_title,book_description,thumbnail,created_time,updated_time,author_name,categories,comments,chapters,likes,views);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference booksRef = db.getReference().child("books");
        Map<String, Object> push_book = new HashMap<>();
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        push_book.put("book" + ts, exampleSach);
        booksRef.updateChildren(push_book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Output","OK");
                } else {
                    Log.d("Output","Failed");
                }
            }
        });*/
    }
}