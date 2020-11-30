package com.example.developerunknown;


import java.io.Serializable;
import java.util.ArrayList;
/**
 *Defines user information
 */
public class User implements Serializable {
    private String name;
    private String username;
    private String contact;
    private String Uid;
    private ArrayList<Book> bookList;



    /**
     *initializes values for user signup
     * @param name users name
     * @param username the users online name
     * @param contact the users contact information
     */
    public User(String name, String username, String contact) {
        this.name = name;
        this.username = username;
        this.contact = contact;
        this.bookList = new ArrayList<Book>();
    }

    /**
     * Return the user id
     * @return
     * Return Uid
     */
    //public String getUsername(){return username;}
    public String getUid() {
        return Uid;
    }


    /**
     * Return the user name
     * @return
     * Return username
     */
    //public String getUsername(){return username;}
    public String getUsername() {
        return username;
    }


    /**
     * sets the user id
     * @param uid user id
     */
    public void setUid(String uid) {
        Uid = uid;
    }


    /**
     * adds book
     * @param book specific book
     */
    public void addBook(Book book) {
        bookList.add(book);
    }


    /**
     * Return booklist array
     * @return
     * Return bookList
     */
    public ArrayList<Book> getBookList() {
        return this.bookList;
    }


    /**
     * Return the filtered book list
     * @return
     * Return filteredList
     */
    public ArrayList<Book> getFilteredBookList(String filter) {
        ArrayList<Book> filteredList = new ArrayList<>();

        for (Book book: this.bookList) {
            if (filter.equals(book.getStatus())) {
                filteredList.add(book);
            }
        }
        return filteredList;
    }

    /**
     * Return the booklist length
     * @return
     * Return bookList.size
     */
    public int getBookListLength() { return this.bookList.size(); }


}
