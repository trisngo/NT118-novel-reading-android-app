package com.example.appmanga.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.appmanga.Fragment.ReadinglistTabFragment;
import com.example.appmanga.Fragment.FavoriteTabFragment;

public class LibraryAdapter extends FragmentStatePagerAdapter {
    public LibraryAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ReadinglistTabFragment();

            case 1:

                return new FavoriteTabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title ="Đang Đọc" ;
                break;
            case 1:
                title = "Yêu Thích";
                break;
            default:
                return null;
        }
        return title;
    }
}
