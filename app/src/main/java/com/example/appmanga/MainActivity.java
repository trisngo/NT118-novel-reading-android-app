package com.example.appmanga;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmanga.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private String key;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //init_database();
        replaceFragment(new HomeFragment(),key);
        key="home";
        //Sự kiện bấm thay đổi fragmanent
        binding .bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.Home:
                    key="home";
                    replaceFragment(new HomeFragment(),key);
                    break;
                case R.id.Ranking:
                    if (key=="home"){
                        flag=1;
                    }else if (key=="Profile"){
                        flag=2;
                    }
                    key="Rank";
                    replaceFragment(new RankingFragment(),key);
                    break;
                case  R.id.Profile:
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        key="Profile";
                        replaceFragment(new ProfileFragment(),key);
                        break;
                    }else{
                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                    }

            }
            return true;
        });


    }

    public void login(View view){
        Intent intent = new Intent(MainActivity.this, introActivity.class);
        startActivity(intent);
//        finish();
    }
    public void search(View view){
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
//        finish();
    }

    public void callReadLayout(View view){
        Intent intent = new Intent(MainActivity.this, intro_manga_before_read.class);
        startActivity(intent);
    }

    //Hàm thể hiện fragment
    private void replaceFragment(Fragment fragment, String key){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(key=="home"){
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        }else if (key=="Profile"){
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
        }else {
            if (flag==1){
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left);
            }else if (flag==2){
                fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        }
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }


    // Gọi hàm này để thêm sách vào database
    void init_database() {
        String book_title = "Angel Of Death";
        String book_description = "Thiếu nữ 13 tuổi Rachel tỉnh dậy và nhận ra mình đang bị nhốt dưới tầng hầm của một tòa nhà bỏ hoang. Không chút ký ức, cũng như manh mối về nơi mình đang ở, lạc lối và choáng váng, cô lang thang khắp tòa nhà. Trong lúc tìm kiếm manh mối tại nơi đây, cô chạm mặt một người đàn ông được bao phủ bởi băng gạc. Hắn là một kẻ mang lưỡi hái, tự nhận mình tên Zack. Họ bắt đầu một mối quan hệ kỳ lạ, được thắt chặt bằng những lời hứa điên rồ";
        String thumbnail = "https://m.media-amazon.com/images/M/MV5BOWRjMzU2NTYtMGRlOS00YjQwLWFiYWQtODFlOGNiNmU4OTVlXkEyXkFqcGdeQXVyMzgxODM4NjM@._V1_FMjpg_UX1000_.jpg";
        String created_time = "01/12/2022";
        String updated_time = "01/12/2022";
        String author_name = "Chiren Kina";
        int likes = 43;
        int views = 561;
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Action");
        categories.add("Horror");
        categories.add("Tragedy");
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
        });
    }
}