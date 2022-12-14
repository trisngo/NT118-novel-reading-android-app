package com.example.appmanga;

public class User {
    public String username;
    public String email;
    public int already_read;

    public User(String username, String email, int already_read) {
        this.username = username;
        this.email = email;
        this.already_read = already_read;
    }
}