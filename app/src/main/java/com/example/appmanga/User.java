package com.example.appmanga;

import java.util.ArrayList;

public class User {
    private String username;
    private String email;
    private ArrayList<String> liked_books;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getLiked_books() {
        return liked_books;
    }

    public User() {}

    public User(String username, String email, ArrayList<String> liked_books) {
        this.username = username;
        this.email = email;
        this.liked_books = liked_books;
    }
}