package com.example.developerunknown;

import android.widget.ArrayAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Book Class implements Serializability and its interface
 */
public class Book implements Serializable {
    private String id;
    private String title;
    private String author;
    private String status;
    private String ISBN;
    private String description;
    private String ownerId;
    private String ownerUname;
    //private Float rating;
    private String borrowerID;
    private String borrowerUname;


    /**
     * sets the title of book
     * @param title book title
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * sets the author of book
     * @param author book author
     */
    public void setAuthor(String author) {
        this.author = author;
    }


    /**
     * sets the status of book
     * @param status book status
     */
    public void setStatus (String status) {
        this.status = status;
    }


    /**
     * sets the ISBN of book
     * @param ISBN book ISBN
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }


    /**
     * sets the description of book
     * @param description book description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * sets the title of book
     * @param ownerId owner id
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }


    /**
     * sets the title of book
     * @param ownerUname owner user name
     */
    public void setOwnerUname(String ownerUname) {
        this.ownerUname = ownerUname;
    }


    /**
     * Return the id of the owner
     * @return
     * Return ownerId
     */
    public String getOwnerId() {
        return ownerId;
    }



    /**
     * Return the owners user name
     * @return
     * Return ownerUname
     */
    public String getOwnerUname() {
        return ownerUname;
    }



    /**
     * Return the id user
     * @return
     * Return id
     */
    public String getID() { return this.id; }



    /**
     * Return the title of the book
     * @return
     * Return title
     */
    public String getTitle() {
        return this.title;
    }


    /**
     * Return the id of the owner
     * @return
     * Return ownerId
     */
    public String getAuthor() {
        return this.author;
    }


    /**
     * Return the book status
     * @return
     * Return status
     */
    public String getStatus () {
        return this.status;
    }


    /**
     * Return the ISBN of the book
     * @return
     * Return ISBN
     */
    public String getISBN() {
        return ISBN;
    }


    /**
     * Return the book description
     * @return
     * Return description
     */
    public String getDescription() {
        return description;
    }


    public Book(String id, String title, String author, String status, String ISBN, String description){
        this.title = title;
        this.author = author;
        this.status = status;
        this.ISBN = ISBN;
        this.description = description;
        this.id = id;
    }

    /**
    *used to retrieve book related to current user when we need extra information
     *@param id user id
     * @param title book title
     * @ param author book author
     * @ param status book status
     * @param ISBN book ISBN
     * @param description book description
     * @param ownerId id of book owner
     * @param ownerUname book owner's username
     */
    public Book(String id, String title, String author, String status, String ISBN, String description, String ownerId, String ownerUname) {
        this.title = title;
        this.author = author;
        this.status = status;
        this.ISBN = ISBN;
        this.description = description;
        this.id = id;
        this.ownerId = ownerId;
        this.ownerUname = ownerUname;
    }


    public String getBorrowerID() {
        return borrowerID;
    }

    public void setBorrowerID(String borrowerID) {
        this.borrowerID = borrowerID;
    }

    public String getBorrowerUname() {
        return borrowerUname;
    }

    public void setBorrowerUname(String borrowerUname) {
        this.borrowerUname = borrowerUname;
    }

    /**
     * This is a class that compares book titles
     * @param book book
     *  @return
     *  Return the book title comparison
     */
    public int compareTo(Book book) {
        return this.title.compareTo(book.getTitle());
    }
}
