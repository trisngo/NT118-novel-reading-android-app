package com.example.appmanga.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appmanga.Fragment.Chapter_BookFragment;
import com.example.appmanga.Fragment.CommentFragment;
import com.example.appmanga.Fragment.Description_BookFragment;


public class TabDetailAdapter extends FragmentStatePagerAdapter {
    public TabDetailAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 1:
                title = "Bình Luận";
                break;
            case 2:
                title = "Chương";
                break;
            default:
                title = "Tóm Lược";
        }
        return title;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new CommentFragment();
            case 2:
                return new Chapter_BookFragment();
            default:
                return new Description_BookFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
