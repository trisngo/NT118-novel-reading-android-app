package com.example.appmanga;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment_layout = inflater.inflate(R.layout.fragment_profile, container, false);
        initButton();
        return fragment_layout;
    }

    public void initButton()
    {
        exit_btn = (ImageButton) fragment_layout.findViewById(R.id.exit_button);
        setting_btn = (ImageButton) fragment_layout.findViewById(R.id.setting_button);
        like_btn = (ImageButton) fragment_layout.findViewById(R.id.liked_books_button);
        tv_username = (TextView) fragment_layout.findViewById(R.id.tv_username);
        tv_userEmail = (TextView) fragment_layout.findViewById(R.id.tv_email);
        tv_user_read_books = (TextView) fragment_layout.findViewById(R.id.tv_read_books);
        getDataFromFirebase();
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
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
                            tv_user_read_books.setText(String.valueOf(userData.get("already_read")) + " đã đọc");
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