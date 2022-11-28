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
import android.widget.TextView;
import android.widget.Toast;

import com.example.appmanga.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                    key="Profile";
                    replaceFragment(new ProfileFragment(),key);
                    break;
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
//        finish();*/
    }

    public void callReadLayout(View view){
        Intent intent = new Intent(MainActivity.this, ReadingActivity.class);
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
        String book_title = "Arya-san bàn bên thi thoảng lại thả thính tôi bằng tiếng Nga";
        String book_description = "Đây là một câu truyện romcom tuổi trẻ với một cô nàng JK người Nga siêu cao cấp, là đối tượng của sự ngưỡng mộ của mọi học sinh trong trường!";
        String thumbnail = "https://i.docln.net/lightnovel/illusts/u67551-23879fcc-7c33-4966-a87b-4be1f5be5c60.jpg";
        String created_time = "21/11/2022";
        String updated_time = "21/11/2022";
        String author_name = "Sun";
        ArrayList<String> categories = new ArrayList<>();
        categories.add("Comedy");
        categories.add("Romance");
        categories.add("School Life");
        Map<String, String> comments = new HashMap<>();
        comments.put("BuiDucAnh","Gái Nga xinh quá hehe");
        Map<String,String> chapters = new HashMap<>();
        chapters.put("chapter1","Nội dung chapter 1");
        chapters.put("chapter2","Nội dung chapter 2");
        chapters.put("chapter3","Nội dung chapter 3");
        chapters.put("chapter4","Nội dung chapter 4");
        Book exampleSach = new Book(book_title,book_description,thumbnail,created_time,updated_time,author_name,categories,comments,chapters);
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