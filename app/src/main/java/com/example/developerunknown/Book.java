package com.example.developerunknown;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Book {

    private String title;
    private String author;
    private String availability;
    private String ISBN;
    private String description;
    private Float rating;
    private User currentBorrower;

    public Book(String title, String author, String availability, String ISBN, String description) {
        this.title = title;
        this.author = author;
        this.availability = availability;
        this.ISBN = ISBN;
        this.description = description;
    }



}
