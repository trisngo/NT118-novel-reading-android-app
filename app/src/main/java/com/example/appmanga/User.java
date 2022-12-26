package com.example.appmanga;

import java.util.ArrayList;

public class User {

    private String username;
    private String email;
    private int already_read;
    private ArrayList<String> liked_books;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getAlready_read() {
        return already_read;
    }

    public ArrayList<String> getLiked_books() {
        return liked_books;
    }
    public User() {}
    public User(String username, String email, int already_read, ArrayList<String> liked_books) {
        this.username = username;
        this.email = email;
        this.already_read = already_read;
        this.liked_books = liked_books;
    }
}
