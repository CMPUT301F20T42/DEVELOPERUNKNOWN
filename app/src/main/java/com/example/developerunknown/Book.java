package com.example.developerunknown;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;

public class Book implements Serializable {
    private String title;
    private String author;
    private String status;
    private String ISBN;
    private String description;
    //private Float rating;
    //private User currentBorrower;

    public Book(String title, String author, String status, String ISBN, String description) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.ISBN = ISBN;
        this.description = description;

    }


    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAvailability() {
        return status;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getDescription() {
        return description;
    }

}
