package com.example.appmanga;

import java.util.ArrayList;
import java.util.Map;

public class Book {
    public String bookId;
    public String book_title;
    public String book_description;
    public String thumbnail;
    public String created_time;
    public String updated_time;
    public String author_name;
    public String categories;
    public Map<String, String> comments;
    public Map<String, String> chapters;
    public int likes;
    public int views;

    public Book() {}
    public Book(String author_name, String bookId, String book_title, String book_description, String thumbnail, String created_time, String categories, Map<String, String> comments, Map<String, String> chapters, int likes, int views) {
        this.bookId = bookId;
        this.book_title = book_title;
        this.book_description = book_description;
        this.thumbnail = thumbnail;
        this.created_time = created_time;
        this.author_name = author_name;
        this.categories = categories;
        this.comments = comments;
        this.chapters = chapters;
        this.likes = likes;
        this.views = views;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBook_description() {
        return book_description;
    }

    public void setBook_description(String book_description) {
        this.book_description = book_description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getUpdated_time() {
        return updated_time;
    }

    public void setUpdated_time(String updated_time) {
        this.updated_time = updated_time;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }


    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Map<String, String> getComments() {
        return comments;
    }

    public void setComments(Map<String, String> comments) {
        this.comments = comments;
    }

    public Map<String, String> getChapters() {
        return chapters;
    }

    public void setChapters(Map<String, String> chapters) {
        this.chapters = chapters;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
}
