package com.example.developerunknown;

import java.io.Serializable;
/**
 * initializes information for the request activites and list
 */
public class Request implements Serializable {
    // private String requestID;
    private String borrowerID;
    private String borrowerUname;
    private String targetBookid;
    private String Rid;

    private String Status = "Requested";



    public Request(String borrowerID, String borrowerUname, String targetBook) {
        // this.requestID = requestID;
        this.borrowerID = borrowerID;
        this.borrowerUname = borrowerUname;
        this.targetBookid = targetBook;
    }

    /**
     * refer to the current objects whose method is being called on
     * @param borrowerID the borrower id
     * @param borrowerUname  the borrower user name
     * @param targetBook a specific book being requested
     * @param Rid the request id?
     */
    public Request(String borrowerID, String borrowerUname, String targetBook,String Rid) {
        // this.requestID = requestID;
        this.borrowerID = borrowerID;
        this.borrowerUname = borrowerUname;
        this.targetBookid = targetBook;
        this.Rid = Rid;
    }

    /**
     * get the borrower id
     * @return
     * Return borrowerID
     */
    public String getBorrowerID() {
        return this.borrowerID;
    }


    /**
     * get the borrower user name
     * @return
     * Return borrowerUname
     */
    public String getBorrowerUname() { return this.borrowerUname; }

    /**
     * retrieve a specific book
     * @return
     * Return targetBookid
     */
    public String getTargetBook() {
        return this.targetBookid;
    }


    /**
     * Return the book status
     * @return
     * Return Status
     */
    public String getStatus() {
        return this.Status;
    }


    /**
     * sets the ISBN of book
     * @param status gets the book status
     */
    public void setStatus(String status) {
        this.Status = status;
    }
}
