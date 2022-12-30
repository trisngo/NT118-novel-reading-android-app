package com.example.appmanga.Activity;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.appmanga.Adapter.LibraryAdapter;
import com.example.appmanga.R;
import com.google.android.material.tabs.TabLayout;

public class LibraryActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        AnhXa();
        LibraryAdapter adapter =new LibraryAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setAllCaps(tabLayout,false);


    }
    public void AnhXa(){
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpaper);
    }
    public static void setAllCaps(View view, boolean caps) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++)
                setAllCaps(((ViewGroup) view).getChildAt(i),caps);
        } else if (view instanceof TextView) ((TextView) view).setAllCaps(caps);
    }

}
