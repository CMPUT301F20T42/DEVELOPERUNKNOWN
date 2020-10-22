package com.example.developerunknown;

import android.app.Activity;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignUpActivity extends Activity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;


    public Button signUpButton;
    public EditText registerEmail;
    public EditText registerUserName;
    public EditText registerPassword;
    public EditText registerFirstName;
    public EditText registerLastName;
    public String userName;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userCollectionReference = db.collection("Users");
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        registerUserName = findViewById(R.id.editUsername);
        registerPassword = findViewById(R.id.editPassward);
        registerEmail = findViewById(R.id.registerEmail);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);
        signUpButton = findViewById(R.id.confirm_signup);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = registerUserName.getText().toString();
                password = registerPassword.getText().toString();
                email = registerEmail.getText().toString();
                firstName=registerFirstName.getText().toString();
                lastName =registerLastName.getText().toString();

                //need to check if user name exist
                //need to check if email already registered

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "success");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            }
        });


    }

}