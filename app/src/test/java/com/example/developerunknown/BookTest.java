package com.example.developerunknown;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BookTest {

    @Test
    void testCompareTo() {
        Book book1 = new Book("1234", "1984", "George Orwell", "Available", "4312", "dystopia");
        Book book2 = new Book("123", "1984", "George Orwell", "Available", "3212", "dystopia");
        Book book3 = new Book("12345", "Pride and Prejudice", "Jane Austen", "Available", "3612", "novel");
        Book book4 = new Book("123324", "1984", "Jane Austen", "Available", "322834", "fan fiction");

        assertEquals(0, book1.compareTo(book2));
        assertEquals(0, book4.compareTo(book1));
        assertNotEquals(0, book1.compareTo(book3));
    }
}
