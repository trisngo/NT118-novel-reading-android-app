package com.example.appmanga.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appmanga.Activity.LoginActivity;
import com.example.appmanga.Activity.ReadingActivity;
import com.example.appmanga.Adapter.commentAdapter;
import com.example.appmanga.ExtraFeature.MyFuntion;
import com.example.appmanga.Model.Book;
import com.example.appmanga.Model.User;
import com.example.appmanga.Model.comment;
import com.example.appmanga.R;
import com.example.appmanga.intro_manga_before_read;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class CommentFragment extends Fragment {

    public View fragment_layout;
    private DatabaseReference books_database, books_database2, users_database;
    private Book book = new Book();
    private Book book1 = new Book();
    private String received_book_id, current_user_id;
    RecyclerView rcv_view_comment;
    commentAdapter comment_Adapter;
    public ArrayList<comment> list_comment = new ArrayList<>();
    ArrayList<String> user_comments,content_comments;
    Map<String,String> id_to_user_list = new HashMap<String,String>();
    Map<String,String> user_comments_list = new HashMap<String,String>();
    private ImageButton add_comment_button;

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment_layout = inflater.inflate(R.layout.fragment_comment, container, false);
        add_comment_button = fragment_layout.findViewById(R.id.addCommentBtn);
        Intent intent =getActivity().getIntent();
        received_book_id = intent.getStringExtra("book_id");
        users_database = FirebaseDatabase.getInstance().getReference("users");
        users_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    id_to_user_list.put(dataSnapshot.getKey().trim(),user.getUsername());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w( "Failed to read value.", error.toException());
            }
        });

        if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
            getUIDFromEmail();
        }
        
        books_database = FirebaseDatabase.getInstance().getReference("books");
        books_database.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int i = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    book1 = dataSnapshot.getValue(Book.class);
                    if (book1.getBookId().equals(received_book_id)) {
                        Set<String> keySet = book1.comments.keySet();
                        user_comments = new ArrayList<>(keySet);
                        Collection<String> values = book1.comments.values();
                        content_comments = new ArrayList<>(values);
                        break;
                    }
                }
                rcv_view_comment= fragment_layout.findViewById(R.id.rcvBook);
                list_comment= new ArrayList<>();
                comment_Adapter = new commentAdapter(getContext(),list_comment);
                rcv_view_comment.setAdapter(comment_Adapter);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
                rcv_view_comment.setLayoutManager(layoutManager);
                for (int j = 0; j < user_comments.size(); j++) {
                    list_comment.add(new comment(id_to_user_list.get(user_comments.get(j).trim()),content_comments.get(j)));
                    user_comments_list.put(user_comments.get(j).trim(),content_comments.get(j));
                }
                comment_Adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
            }
        });

        add_comment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser()!=null) {
                    getUIDFromEmail();
                    showInputDialog(getContext());
                } else {
                    Toast.makeText(getContext(), "Vui lòng đăng nhập để comment",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        return fragment_layout;
    }

    public void showInputDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("")
                .setMessage("Nhập comment của bạn")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String comment = String.valueOf(taskEditText.getText());
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            getUIDFromEmail();
                            if (!comment.trim().equals("")){
                                user_comments_list.put(current_user_id,comment);
                                books_database.child( received_book_id + "/comments").setValue(user_comments_list);
                                list_comment.add(new comment(id_to_user_list.get(current_user_id),comment));
                                comment_Adapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(getContext(), "Vui lòng nhập comment",Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Vui lòng đăng nhập để comment",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }

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
                        Log.d("User Email",user.getEmail());
                        if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            current_user_id = dataSnapshot.getKey();
                            Log.d("Match",current_user_id);
                            break;
                        }
                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}