package com.example.developerunknown;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void testCompareTo() {
        Book book1 = new Book("1234", "1984", "George Orwell", "Available", "4312", "dystopia");
        Book book2 = new Book("123", "1984", "George Orwell", "Available", "4312", "dystopia");
        Book book3 = new Book("12345", "Pride and Prejudice", "Jane Austen", "Available", "3612", "novel");

        assertEquals(0, book1.compareTo(book2));
        assertNotEquals(0, book1.compareTo(book3));
    }
}
