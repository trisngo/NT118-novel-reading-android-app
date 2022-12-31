package com.example.appmanga.Model;

public class Notify {
    private int id;
    private String title;
    private String content;
    private Long received_time;
    private int seen;

    public Notify(int id, String title, String content, Long received_time, int seen) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.received_time = received_time;
        this.seen = seen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id= id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String notify_content) {
        this.content = content;
    }

    public Long getReceivedTime() {
        return received_time;
    }

    public void setReceivedTime(Long received_time) {
        this.received_time = received_time;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }
}
