package com.example.appmanga;

import java.util.ArrayList;
import java.util.Map;

public class Book {
    public String book_title;
    public String book_description;
    public String thumbnail;
    public String created_time;
    public String updated_time;
    public String author_name;
    public ArrayList<String> categories;
    public Map<String,String> comments;
    public Map<String,String> chapters;
    public int likes;
    public int views;

    public Book(String book_title, String book_description, String thumbnail, String created_time, String updated_time, String author_name, ArrayList<String> categories, Map<String, String> comments, Map<String, String> chapters, int likes, int views) {
        this.book_title = book_title;
        this.book_description = book_description;
        this.thumbnail = thumbnail;
        this.created_time = created_time;
        this.updated_time = updated_time;
        this.author_name = author_name;
        this.categories = categories;
        this.comments = comments;
        this.chapters = chapters;
        this.likes = likes;
        this.views = views;
    }

}
