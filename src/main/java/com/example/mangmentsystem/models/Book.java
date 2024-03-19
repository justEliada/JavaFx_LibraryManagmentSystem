package com.example.mangmentsystem.models;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private boolean available;

    public Book(int id, String title, String author, String genre, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.available = available;
    }

    public Book(String title, String author, String genre) {
        this(-1, title, author, genre, true);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (this.id == -1) {
            this.id = id;
        }
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}