package com.example.developerunknown;

import android.app.Activity;
import android.content.Intent;
import android.database.Observable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseAuth.AuthStateListener authStateListener;


    public Button confirmButton;
    private EditText registerEmail;
    private EditText registerUserName;
    private EditText registerPassword;
    private EditText registerFirstName;
    private EditText registerLastName;
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
//    private FirebaseFirestore db = FirebaseFirestore.getInstance();
 //   private CollectionReference userCollectionReference = db.collection("Users");
    @Override



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        registerUserName = findViewById(R.id.registerUname);
        registerPassword = findViewById(R.id.registerPassword);
        registerEmail = findViewById(R.id.registerEmail);
        registerFirstName = findViewById(R.id.registerFirstName);
        registerLastName = findViewById(R.id.registerLastName);


        confirmButton = findViewById(R.id.confirm_signup);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName = registerUserName.getText().toString();
                password = registerPassword.getText().toString();
                email = registerEmail.getText().toString();
                firstName=registerFirstName.getText().toString();
                lastName =registerLastName.getText().toString();

                //need to check if user name exist
                //need to check if email already registered
                if (userName.equals(password)) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","success");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            }
        });


    }

}