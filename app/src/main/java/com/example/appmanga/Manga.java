package com.example.appmanga;

public class Manga {
    private int img;
    private String name;
    private String ranking;

    public Manga(int img, String name, String ranking) {
        this.img = img;
        this.name = name;
        this.ranking = ranking;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }
}
