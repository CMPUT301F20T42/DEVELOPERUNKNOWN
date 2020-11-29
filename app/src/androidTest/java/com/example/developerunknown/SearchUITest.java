package com.example.developerunknown;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

public class SearchUITest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =

            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void SearchTest() throws InterruptedException {
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

        solo.enterText(bookTitle, "King Lear");
        solo.enterText(bookAuthor, "Shakespeare");
        solo.enterText(bookDescription, "ghosts");
        solo.enterText(bookISBN, "5221");

        // Click enter
        View enterBook = solo.getView(R.id.add_book_button2);
        solo.clickOnView(enterBook);

        solo.sleep(1000);

        // Click on search fragment
        View searchTab = solo.getView(R.id.nav_search);
        solo.clickOnView(searchTab);

        solo.sleep(1000);

        EditText searchTest = (EditText) solo.getView(R.id.editText_book);
        solo.enterText(searchTest, "King Lear");

        Button searchButton = (Button) solo.getView(R.id.search);
        solo.clickOnView(searchButton);

        solo.sleep(1000);

        assertTrue(solo.waitForText("King Lear", 1, 2000));

    }

    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
