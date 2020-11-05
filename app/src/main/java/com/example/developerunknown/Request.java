package com.example.developerunknown;

public class Request {
    // private String requestID;
    private String borrowerID;
    private String borrowerUname;
    private Book targetBook;
    private String Status = "Requested";

    public Request(String borrowerID, String borrowerUname, Book targetBook) {
        // this.requestID = requestID;
        this.borrowerID = borrowerID;
        this.borrowerUname = borrowerUname;
        this.targetBook = targetBook;
    }

    public String getBorrowerID() {
        return this.borrowerID;
    }

    public String getBorrowerUname() { return this.borrowerUname; }

    public Book getTargetBook() {
        return this.targetBook;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }
}
