package com.example.developerunknown;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    public User mockUser() {
        return new User("Joe", "joe123", "joe@gmail.com");
    }

    @Test
    public void testAddBook() {
        User testUser = mockUser();
        Book book1 = new Book("1234", "1984", "George Orwell", "Available", "12344", "dystopia");
        testUser.addBook(book1);

        assertEquals(1, testUser.getBookList().size());

    }

    @Test
    public void testGetBookList() {
        User testUser = mockUser();
        Book book1 = new Book("1234", "1984", "George Orwell", "Available", "12344", "dystopia");
        testUser.addBook(book1);

        assertEquals(0, testUser.getBookList().get(0).compareTo(book1));
    }

    @Test
    public void testGetFilteredBookList() {
        User testUser = mockUser();
        Book book1 = new Book("1", "1984", "George Orwell", "Available", "12344", "dystopia");
        Book book2 = new Book("2", "Pride and Prejudice", "Jane Austen", "Available", "12344", "novel");
        Book book3 = new Book("3", "King Lear", "Shakespeare", "Requested", "12344", "play");
        Book book4 = new Book("4", "The Republic", "Plato", "Available", "12344", "philosophy");

        testUser.addBook(book1);
        testUser.addBook(book2);
        testUser.addBook(book3);
        testUser.addBook(book4);

        assertEquals(0, testUser.getFilteredBookList("Accepted").size());
        assertEquals(1, testUser.getFilteredBookList("Requested").size());
        assertEquals(3, testUser.getFilteredBookList("Available").size());
    }

    @Test
    public void testGetBookListLength() {
        User testUser = mockUser();
        assertEquals(0, testUser.getBookListLength());

        testUser.addBook(new Book("1234", "1984", "George Orwell", "Available", "12344", "dystopia"));
        assertEquals(1, testUser.getBookListLength());
    }
}
