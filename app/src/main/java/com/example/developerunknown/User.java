package com.example.developerunknown;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String name;
    private String username;
    private String contact;
    private String Uid;
    private ArrayList<Book> bookList;

    // TODO: images

    public User(String name, String username, String contact) {
        this.name = name;
        this.username = username;
        this.contact = contact;
        this.bookList = new ArrayList<Book>();
    }

    public String getUsername(){return username;}
    public String getUid() {
        return Uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public void addBook(Book book) {
        bookList.add(book);
    }

    public ArrayList<Book> getBookList() {
        return this.bookList;
    }

    public ArrayList<Book> getFilteredBookList(String filter) {
        ArrayList<Book> filteredList = new ArrayList<>();

        for (Book book: this.bookList) {
            if (filter.equals(book.getStatus())) {
                filteredList.add(book);
            }
        }
        return filteredList;
    }

    public int getBookListLength() { return this.bookList.size(); }


}
