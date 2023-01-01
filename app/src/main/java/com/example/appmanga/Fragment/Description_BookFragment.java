package com.example.appmanga.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.appmanga.R;


public class Description_BookFragment extends Fragment {

    TextView dsc;
    String dsc1;
    private String book_id;

    public Description_BookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_description__book, container, false);
        dsc = root.findViewById(R.id.dsc);

        Intent intent =getActivity().getIntent();
        dsc1 = intent.getStringExtra("dsc");
        dsc.setText(dsc1);


        return root;
    }
}