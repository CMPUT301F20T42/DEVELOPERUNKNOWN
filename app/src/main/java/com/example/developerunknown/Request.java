package com.example.developerunknown;

import java.io.Serializable;

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
    public Request(String borrowerID, String borrowerUname, String targetBook,String Rid) {
        // this.requestID = requestID;
        this.borrowerID = borrowerID;
        this.borrowerUname = borrowerUname;
        this.targetBookid = targetBook;
        this.Rid = Rid;
    }


    public String getBorrowerID() {
        return this.borrowerID;
    }

    public String getBorrowerUname() { return this.borrowerUname; }

    public String getTargetBook() {
        return this.targetBookid;
    }

    public String getStatus() {
        return this.Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }
}
