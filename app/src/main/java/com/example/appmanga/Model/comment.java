package com.example.appmanga.Model;

public class comment {
    public String user_comment;
    public String content_comment;

    public comment(String user_comment, String content_comment) {
        this.user_comment = user_comment;
        this.content_comment = content_comment;
    }

    public String getUser_comment() {
        return user_comment;
    }

    public void setUser_comment(String user_comment) {
        this.user_comment = user_comment;
    }

    public String getContent_comment() {
        return content_comment;
    }

    public void setContent_comment(String content_comment) {
        this.content_comment = content_comment;
    }
}
