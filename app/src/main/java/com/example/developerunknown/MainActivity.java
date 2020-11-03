package com.example.developerunknown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.developerunknown.MESSAGE";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference = db.collection("user");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    public FirebaseUser user;

    User currentUser;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");

                user = FirebaseAuth.getInstance().getCurrentUser();

                String uID = user.getUid();
                final DocumentReference userDocRef = userCollectionReference.document(uID);
                userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String email = document.getString("email");
                                String firstName = document.getString("firstName");
                                String lastName = document.getString("lastName");
                                String username = document.getString("userName");
                                currentUser.setUid(document.getId());

                                Log.d("TAG", "name: " + firstName);

                                currentUser = new User(firstName + " " + lastName, username, email);

                            } else {
                                Log.d("check email", "user does not exist");
                            }
                        }
                    }
                });

/*
                // Create dummy user + book data
                // TODO: replace with calls to firestore
                loggedIn = new User("A Admin", "admin", "admin@gmail.com");

                Book b1 = new Book("To Kill A Mockingbird", "Harper Lee", "Available", "123", "The mockingbird dies");
                Book b2 = new Book("1984", "George Orwell", "Borrowed", "1234", "Set in 1983");

                loggedIn.addBook(b1);
                loggedIn.addBook(b2);
*/
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Not logged in
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivityForResult(intent,123);

        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_search:
                            selectedFragment = new SearchFragment(currentUser);
                            break;
                        case R.id.nav_booklist:
                            Bundle args = new Bundle();
                            args.putSerializable("current user", currentUser);
                            selectedFragment = new BookListFragment();
                            selectedFragment.setArguments(args);
                            break;
                        case R.id.nav_account:
                            selectedFragment = new AccountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                    return true;


                    }
                };
}