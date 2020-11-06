package com.example.developerunknown;

import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class Book implements Serializable {
    private String id;
    private String title;
    private String author;
    private String status;
    private String ISBN;
    private String description;
    private ArrayList<Request> requestList;
    private String ownerId;
    private String ownerUname;
    //private Float rating;
    //private User currentBorrower;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setOwnerUname(String ownerUname) {
        this.ownerUname = ownerUname;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerUname() {
        return ownerUname;
    }

    public String getID() { return this.id; }

    public String getTitle() {
        return this.title;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getStatus () {
        return this.status;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<Request> getRequestList() {
        return this.requestList;
    }

    //public String getBid() { return Bid; }

    //public String getOwner() {return ownerId;}

    //public void setBid(String bid) {Bid = bid;}

    public Book(String id,String title, String author, String status, String ISBN, String description){
        this.title = title;
        this.author = author;
        this.status = status;
        this.ISBN = ISBN;
        this.description = description;
        this.id = id;
        this.requestList = new ArrayList<>();
    }

    public Book(String id,String title, String author, String status, String ISBN, String description, String ownerId, String ownerUname) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.ISBN = ISBN;
        this.description = description;
        this.id = id;
        this.ownerId = ownerId;
        this.ownerUname=ownerUname;
    }


    public void requestsListPushBack(Request request){
        this.requestList.add(request);
    }


    /*public void requestUpdate(Request nowRequest, Request targetRequest){
    }*/
}
