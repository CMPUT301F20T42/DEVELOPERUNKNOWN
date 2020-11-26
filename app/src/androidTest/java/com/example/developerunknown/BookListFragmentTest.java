package com.example.developerunknown;

import android.app.Activity;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(AndroidJUnit4.class)
public class BookListFragmentTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

     // Adds a book to the listview and check the book name using assertTrue
    @Test
    public void checkAddBook() {
        // Login
        EditText username = (EditText) solo.getView(R.id.editUsername);
        EditText password = (EditText) solo.getView(R.id.editPassward);
        View signinButton = solo.getView(R.id.signIn);
        // Enter login information
        solo.enterText(username, "easybusy");
        solo.enterText(password, "123123");
        solo.clickOnView(signinButton);
        // Sleep, allow app time to log user in
        solo.sleep(5000);
        // Check that the activity is correct
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // Click on booklist fragment
        View bookTab = solo.getView(R.id.nav_booklist);
        solo.clickOnView(bookTab);

        // Click on add button
        View addButton = solo.getView(R.id.add_book_button);
        solo.clickOnView(addButton);

        // Add New Book
        EditText bookTitle = (EditText) solo.getView(R.id.book_title_editText);
        EditText bookAuthor = (EditText) solo.getView(R.id.book_author_editText);
        EditText bookDescription = (EditText) solo.getView(R.id.book_description_editText);
        EditText bookISBN = (EditText) solo.getView(R.id.book_isbn_editText);

        solo.enterText(bookTitle, "Hamlet");
        solo.enterText(bookAuthor, "Shakespeare");
        solo.enterText(bookDescription, "ghosts");
        solo.enterText(bookISBN, "5421");

        // Click enter
        View enterBook = solo.getView(R.id.add_book_button2);
        solo.clickOnView(enterBook);

        // Check that the book appears
        assertTrue(solo.waitForText("Hamlet", 1, 2000));
    }

    @Test
    public void checkViewBook() {
        // Login
        EditText username = (EditText) solo.getView(R.id.editUsername);
        EditText password = (EditText) solo.getView(R.id.editPassward);
        View signinButton = solo.getView(R.id.signIn);
        // Enter login information
        solo.enterText(username, "easybusy");
        solo.enterText(password, "123123");
        solo.clickOnView(signinButton);
        // Sleep, allow app time to log user in
        solo.sleep(5000);
        // Check that the activity is correct
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // Click on booklist fragment
        View bookTab = solo.getView(R.id.nav_booklist);
        solo.clickOnView(bookTab);

        // Click on add button
        View addButton = solo.getView(R.id.add_book_button);
        solo.clickOnView(addButton);

        // Add New Book
        EditText bookTitle = (EditText) solo.getView(R.id.book_title_editText);
        EditText bookAuthor = (EditText) solo.getView(R.id.book_author_editText);
        EditText bookDescription = (EditText) solo.getView(R.id.book_description_editText);
        EditText bookISBN = (EditText) solo.getView(R.id.book_isbn_editText);

        solo.enterText(bookTitle, "Pride and Prejudice");
        solo.enterText(bookAuthor, "Jane Austen");
        solo.enterText(bookDescription, "novel");
        solo.enterText(bookISBN, "5421");

        // Click enter
        View enterBook = solo.getView(R.id.add_book_button2);
        solo.clickOnView(enterBook);

        // Click on book
        solo.clickOnText("Pride and Prejudice");

        // Wait a second for description to appear
        assertTrue(solo.waitForText("novel", 1, 1000));
    }

    @Test
    public void checkEditBook() {
        // Login
        EditText username = (EditText) solo.getView(R.id.editUsername);
        EditText password = (EditText) solo.getView(R.id.editPassward);
        View signinButton = solo.getView(R.id.signIn);
        // Enter login information
        solo.enterText(username, "easybusy");
        solo.enterText(password, "123123");
        solo.clickOnView(signinButton);
        // Sleep, allow app time to log user in
        solo.sleep(5000);
        // Check that the activity is correct
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Click on booklist fragment
        View bookTab = solo.getView(R.id.nav_booklist);
        solo.clickOnView(bookTab);

        // Click on add button
        View addButton = solo.getView(R.id.add_book_button);
        solo.clickOnView(addButton);

        // Add New Book
        EditText bookTitle = (EditText) solo.getView(R.id.book_title_editText);
        EditText bookAuthor = (EditText) solo.getView(R.id.book_author_editText);
        EditText bookDescription = (EditText) solo.getView(R.id.book_description_editText);
        EditText bookISBN = (EditText) solo.getView(R.id.book_isbn_editText);

        solo.enterText(bookTitle, "Harry Potter");
        solo.enterText(bookAuthor, "JK Roling");
        solo.enterText(bookDescription, "fantasy");
        solo.enterText(bookISBN, "5421");

        // Click enter
        View enterBook = solo.getView(R.id.add_book_button2);
        solo.clickOnView(enterBook);

        // Click on book
        solo.clickOnText("Harry Potter");

        View editButton = solo.getView(R.id.edit_button);
        solo.clickOnView(editButton);

        EditText enterAuthor = (EditText) solo.getView(R.id.book_author_editText);
        solo.clearEditText(enterAuthor);
        solo.enterText(enterAuthor, "JK Rowling");

        // Save Changes
        View saveButton = solo.getView(R.id.add_book_button2);
        solo.clickOnView(saveButton);

        // Wait one second for changes to take effect
        assertTrue(solo.waitForText("JK Rowling", 1, 1000));
    }

    @Test
    public void checkFilterBooks() {
        // Login
        EditText username = (EditText) solo.getView(R.id.editUsername);
        EditText password = (EditText) solo.getView(R.id.editPassward);
        View signinButton = solo.getView(R.id.signIn);
        // Enter login information
        solo.enterText(username, "easybusy");
        solo.enterText(password, "123123");
        solo.clickOnView(signinButton);
        // Sleep, allow app time to log user in
        solo.sleep(5000);
        // Check that the activity is correct
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // Click on booklist fragment
        View bookTab = solo.getView(R.id.nav_booklist);
        solo.clickOnView(bookTab);

        // Click on add button
        View addButton = solo.getView(R.id.add_book_button);
        solo.clickOnView(addButton);

        // Add New Book
        EditText bookTitle = (EditText) solo.getView(R.id.book_title_editText);
        EditText bookAuthor = (EditText) solo.getView(R.id.book_author_editText);
        EditText bookDescription = (EditText) solo.getView(R.id.book_description_editText);
        EditText bookISBN = (EditText) solo.getView(R.id.book_isbn_editText);

        solo.enterText(bookTitle, "Brave New World");
        solo.enterText(bookAuthor, "Alduous Huxley");
        solo.enterText(bookDescription, "spooky");
        solo.enterText(bookISBN, "5421");

        // Click enter
        View enterBook = solo.getView(R.id.add_book_button2);
        solo.clickOnView(enterBook);

        solo.pressSpinnerItem(0,1);

        assertFalse(solo.waitForText("Brave New World"));

        solo.pressSpinnerItem(0,1);

        assertTrue(solo.waitForText("Brave New World", 1, 1000));
    }

    @Test
    public void checkDeleteBook() {
        // Login
        EditText username = (EditText) solo.getView(R.id.editUsername);
        EditText password = (EditText) solo.getView(R.id.editPassward);
        View signinButton = solo.getView(R.id.signIn);
        // Enter login information
        solo.enterText(username, "easybusy");
        solo.enterText(password, "123123");
        solo.clickOnView(signinButton);
        // Sleep, allow app time to log user in
        solo.sleep(5000);
        // Check that the activity is correct
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // Click on booklist fragment
        View bookTab = solo.getView(R.id.nav_booklist);
        solo.clickOnView(bookTab);

        // Click on add button
        View addButton = solo.getView(R.id.add_book_button);
        solo.clickOnView(addButton);

        // Add New Book
        EditText bookTitle = (EditText) solo.getView(R.id.book_title_editText);
        EditText bookAuthor = (EditText) solo.getView(R.id.book_author_editText);
        EditText bookDescription = (EditText) solo.getView(R.id.book_description_editText);
        EditText bookISBN = (EditText) solo.getView(R.id.book_isbn_editText);

        solo.enterText(bookTitle, "Lord of the Rings");
        solo.enterText(bookAuthor, "JRR Tolkien");
        solo.enterText(bookDescription, "hobbits and elves");
        solo.enterText(bookISBN, "5421");

        // Click enter
        View enterBook = solo.getView(R.id.add_book_button2);
        solo.clickOnView(enterBook);

        // Click on book
        solo.clickOnText("Lord of the Rings");

        // Delete book
        View deleteButton = solo.getView(R.id.delete_button);
        solo.clickOnView(deleteButton);

        assertFalse(solo.waitForText("Lord of the Rings"));
    }

    @Test
    public void checkViewRequests() {
        // Login
        EditText username = (EditText) solo.getView(R.id.editUsername);
        EditText password = (EditText) solo.getView(R.id.editPassward);
        View signinButton = solo.getView(R.id.signIn);
        // Enter login information
        solo.enterText(username, "easybusy");
        solo.enterText(password, "123123");
        solo.clickOnView(signinButton);
        // Sleep, allow app time to log user in
        solo.sleep(5000);
        // Check that the activity is correct
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        // Click on booklist fragment
        View bookTab = solo.getView(R.id.nav_booklist);
        solo.clickOnView(bookTab);

        // Click on add button
        View addButton = solo.getView(R.id.add_book_button);
        solo.clickOnView(addButton);

        // Add New Book
        EditText bookTitle = (EditText) solo.getView(R.id.book_title_editText);
        EditText bookAuthor = (EditText) solo.getView(R.id.book_author_editText);
        EditText bookDescription = (EditText) solo.getView(R.id.book_description_editText);
        EditText bookISBN = (EditText) solo.getView(R.id.book_isbn_editText);

        solo.enterText(bookTitle, "Moby Dick");
        solo.enterText(bookAuthor, "Herman Melville");
        solo.enterText(bookDescription, "whale watching");
        solo.enterText(bookISBN, "5421");

        // Click enter
        View enterBook = solo.getView(R.id.add_book_button2);
        solo.clickOnView(enterBook);

        // Click on book
        solo.clickOnText("Moby Dick");

        // Click on view requests
        View viewRequests = solo.getView(R.id.requests_button);
        solo.clickOnView(viewRequests);

        assertTrue(solo.waitForText("Requests"));
    }


    // Run after all tests have been completed
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }



}
