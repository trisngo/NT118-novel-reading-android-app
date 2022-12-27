package com.example.appmanga.ExtraFeature;


import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MyFuntion extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static void addToReading(Context context, String bookId, String Uid){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null){
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("bookId", bookId);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
            ref.child(Uid).child("Reading").child(bookId).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        }
    }

    }



