package com.example.developerunknown;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class RequestUITest {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =

            new ActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void LoginTest() throws InterruptedException {
        solo.enterText((EditText)solo.getView(R.id.editUsername),"XZPshaw");
        solo.enterText((EditText)solo.getView(R.id.editPassward),"123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class,2000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void RequestTest() throws InterruptedException {
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

        solo.sleep(500);

        // Click on add button
        View addButton = solo.getView(R.id.add_book_button);
        solo.clickOnView(addButton);

        // Add New Book
        EditText bookTitle = (EditText) solo.getView(R.id.book_title_editText);
        EditText bookAuthor = (EditText) solo.getView(R.id.book_author_editText);
        EditText bookDescription = (EditText) solo.getView(R.id.book_description_editText);
        EditText bookISBN = (EditText) solo.getView(R.id.book_isbn_editText);

        solo.enterText(bookTitle, "Game of Thrones");
        solo.enterText(bookAuthor, "George RR Martin");
        solo.enterText(bookDescription, "fantasy");
        solo.enterText(bookISBN, "7226116353267");


        // Click enter
        View enterBook = solo.getView(R.id.add_book_button2);
        solo.clickOnView(enterBook);

        solo.sleep(1000);

        // Logout
        solo.clickOnView(solo.getView(R.id.nav_account));
        solo.clickOnView(solo.getView(R.id.logout));

        solo.sleep(1000);

        // Log in as different user
        solo.enterText((EditText) solo.getView(R.id.editUsername), "gkeates");
        solo.enterText((EditText) solo.getView(R.id.editPassward), "123123");
        solo.clickOnView(signinButton);

        // Sleep, allow app time to log user in
        solo.sleep(5000);

        // Click on search fragment
        View searchTab = solo.getView(R.id.nav_search);
        solo.clickOnView(searchTab);

        solo.sleep(1000);

        EditText searchTest = (EditText) solo.getView(R.id.editText_book);
        solo.enterText(searchTest, "Game of Thrones");

        Button searchButton = (Button) solo.getView(R.id.search);
        solo.clickOnView(searchButton);

        solo.sleep(1000);


        solo.clickInList(1);
        solo.assertCurrentActivity("Wrong Activity", resultActivity.class);

        solo.clickOnView(solo.getView(R.id.request_button));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.sleep(1000);

        solo.clickOnView(solo.getView(R.id.nav_booklist));
        solo.sleep(500);
        solo.clickOnText("Wish List");
        TestCase.assertTrue(solo.waitForText("Game of Thrones", 1, 2000));

        // Log out again
        solo.sleep(1000);

        // Logout
        solo.clickOnView(solo.getView(R.id.nav_account));
        solo.clickOnView(solo.getView(R.id.logout));

        solo.sleep(1000);

        // Log in as different user
        solo.enterText((EditText) solo.getView(R.id.editUsername), "easybusy");
        solo.enterText((EditText) solo.getView(R.id.editPassward), "123123");
        solo.clickOnView(signinButton);

        // Sleep, allow app time to log user in
        solo.sleep(5000);

        TestCase.assertTrue(solo.waitForText("Request", 1, 2000));
        TestCase.assertTrue(solo.waitForText("Game of Thrones", 1, 2000));

        solo.clickOnView(solo.getView(R.id.nav_booklist));
        solo.sleep(500);
        solo.clickOnText("Game of Thrones");

        View viewRequests = solo.getView(R.id.requests_button);
        solo.clickOnView(viewRequests);

        // User can see new request
        TestCase.assertTrue(solo.waitForText("gkeates"));

        solo.clickOnText("gkeates");
        // Decline the request
        solo.clickOnText("Decline");
        TestCase.assertFalse(solo.waitForText("gkeates"));
    }




        @After
        public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
