package com.example.developerunknown;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class AccountUITest {
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
        //LoginActivity loginActivity = (LoginActivity) solo.getCurrentActivity();
        solo.enterText((EditText)solo.getView(R.id.editUsername),"XZPshaw");
        solo.enterText((EditText)solo.getView(R.id.editPassward),"123456");
        solo.clickOnButton("Login");
        //solo.wait(100);
        solo.waitForActivity(MainActivity.class,5000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void SignUpTest() {
        solo.clickOnButton("sign up");
        solo.waitForActivity(SignUpActivity.class,5000);
        solo.assertCurrentActivity("Wrong Activity", SignUpActivity.class);
    }

    @Test
    public void searchUser(){
        solo.enterText((EditText)solo.getView(R.id.editUsername),"XZPshaw");
        solo.enterText((EditText)solo.getView(R.id.editPassward),"123456");

        View signinButton = solo.getView(R.id.signIn);
        solo.clickOnView(signinButton);

        solo.waitForActivity(MainActivity.class,5000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_account));
        solo.enterText((EditText)solo.getView(R.id.searchUnameEdit),"XZPshaw");
        solo.clickOnButton("Search");
        solo.waitForDialogToOpen(1000);

    }

    @Test
    public void AccountFragmentTest(){
        solo.enterText((EditText)solo.getView(R.id.editUsername),"XZPshaw");
        solo.enterText((EditText)solo.getView(R.id.editPassward),"123456");
        solo.clickOnButton("Login");
        solo.waitForActivity(MainActivity.class,5000);
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnView(solo.getView(R.id.nav_account));

        solo.clickOnButton("Edit");
        solo.clearEditText((EditText)solo.getView(R.id.editContactPhone));
        solo.clearEditText((EditText)solo.getView(R.id.editContactEmail));

        //solo.clearEditText(R.id.editContactPhone);
        //solo.clearEditText(R.id.editContactEmail);
        solo.enterText((EditText)solo.getView(R.id.editContactPhone),"7800009999");
        solo.enterText((EditText)solo.getView(R.id.editContactEmail),"zepeng@ualberta.ca");
        solo.clickOnButton("Confirm");
        assertTrue(solo.waitForText("7800009999", 1, 20000, true));
        assertTrue(solo.waitForText("zepeng@ualberta.ca", 1, 20000, true));

    }




    @After

    public void tearDown() throws Exception {

        solo.finishOpenedActivities();
    }
}
