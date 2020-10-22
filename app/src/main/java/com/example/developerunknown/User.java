package com.example.developerunknown;

import java.util.ArrayList;

public class User {
    private String name;
    private String username;
    private String contact;
    private ArrayList<Book> bookList;

    // TODO: images

    public User(String name, String username, String contact) {
        this.name = name;
        this.username = username;
        this.contact = contact;
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public ArrayList<Book> getBookList() {
        return this.bookList;
    }


}
