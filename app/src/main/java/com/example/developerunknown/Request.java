package com.example.developerunknown;

public class Request {
    private String borrower;
    private Book tagetBook;
    private String Status = "Requested";

    public Request(String borrower, Book tagetBook) {
        this.borrower = borrower;
        this.tagetBook = tagetBook;
    }

    public String getBorrower() {
        return borrower;
    }

    public Book getTagetBook() {
        return tagetBook;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
