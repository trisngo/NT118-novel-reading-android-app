package com.example.appmanga;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.appmanga.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private String key;
    private int flag=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
        Intent intent = new Intent(MainActivity.this, introActivity.class);
        startActivity(intent);
//        finish();
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
}