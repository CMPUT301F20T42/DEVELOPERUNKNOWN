package com.example.developerunknown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    public Button logInButton;
    public Button signUpButton;
    public EditText editUserName;
    public EditText editPassword;
    public String userName;
    public String password;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 000) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Not signed up
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final CollectionReference userCollectionReference=db.collection("Users");


        editUserName = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassward);
        logInButton = findViewById(R.id.signIn);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = editUserName.getText().toString();
                password = editPassword.getText().toString();
                if (userName.equals(password)) {                        //a place holder function
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result","success");
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }

            }
        });
        signUpButton = findViewById(R.id.signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result","success");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
*/
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivityForResult(intent,000);

            }
        });

    }


    public boolean loginSuccessful(String userName, String password) {
        if (userName == "admin" && password == "admin") {                   //do some check,will be updated later
            return true;
        }
        return false;
    }
}
