package com.example.appmanga.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.appmanga.Activity.MainActivity;
import com.example.appmanga.R;
import com.example.appmanga.UpdateProfile;
import com.example.appmanga.Activity.favoriteBooksActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public ImageButton exit_btn,setting_btn,like_btn;
    public View fragment_layout;
    public TextView tv_username, tv_userEmail, tv_user_read_books;
    ArrayList<String> user_liked_books;


    public ProfileFragment() {
        // Required empty public constructor
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (FirebaseAuth.getInstance().getCurrentUser()==null) {
            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment_layout = inflater.inflate(R.layout.fragment_profile, container, false);
        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            initButton();
        }
        return fragment_layout;
    }

    public void initButton()
    {
        exit_btn = fragment_layout.findViewById(R.id.exit_button);
        setting_btn = fragment_layout.findViewById(R.id.setting_button);
        like_btn = fragment_layout.findViewById(R.id.liked_books_button);
        tv_username = fragment_layout.findViewById(R.id.tv_username);
        tv_userEmail = fragment_layout.findViewById(R.id.tv_email);
        tv_user_read_books = fragment_layout.findViewById(R.id.tv_read_books);
        getDataFromFirebase();
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UpdateProfile.class);
                intent.putExtra("user_email",tv_userEmail.getText());
                intent.putExtra("username",tv_username.getText());
                startActivity(intent);
            }
        });

        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), favoriteBooksActivity.class);
                intent.putExtra("user_liked_books",user_liked_books);
                startActivity(intent);
            }
        });
    }

    public void getDataFromFirebase() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tv_userEmail.setText(user.getEmail());
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference root = db.getReference();
        DatabaseReference usersRef = root.child("users");
        Query query = usersRef.orderByKey();
        query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    boolean isFound = false;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, Object> userData = (Map<String, Object>) snapshot.getValue();
                        if (userData.get("email").equals(user.getEmail())) {
                            tv_username.setText(String.valueOf(userData.get("username")));
                            user_liked_books = (ArrayList<String>) userData.get("liked_books");
                            int number_of_liked_books = user_liked_books.size();
                            if (user_liked_books.contains("default")) {
                                number_of_liked_books -= 1;
                            }
                            tv_user_read_books.setText(String.valueOf(number_of_liked_books) + " truyện yêu thích");
                            isFound = true;
                            break;
                        }
                    }
                    if (isFound==false) tv_username.setText("Can not get data");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}