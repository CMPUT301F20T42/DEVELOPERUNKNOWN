package com.example.developerunknown;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**this class hold is in fact first fully operated class in this project,it will get the username and password entered,and compare with
firestore,a trick is applied here,since in user stories,we should focus on userName but the firebase auth only has email and password,and UID
so I stored a key value pair about userName and email/Uid,therefore user can log in using username
*/
public class LoginActivity extends AppCompatActivity {
    private Button logInButton;
    private Button signUpButton;

    private EditText editUserName;
    private EditText editPassword;
    private String userName;
    private String password;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference unameCollectionReference = db.collection("userName");
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private String email;


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
    /**
     * creates the view for the activity by setting it to a R.layout
     * @param savedInstanceState contains data from previous activity
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editUserName = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassward);
        logInButton = findViewById(R.id.signIn);
        logInButton.setOnClickListener(new View.OnClickListener() {                                   //check if current user exist,if yes,try to login
                                           @Override
                                           public void onClick(View v) {
                                               userName = editUserName.getText().toString();
                                               password = editPassword.getText().toString();
                                               DocumentReference uNameDocRef = unameCollectionReference.document(userName);
                                               uNameDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                       if (task.isSuccessful()) {
                                                           DocumentSnapshot document = task.getResult();
                                                           if (document.exists()) {
                                                               Log.d("TAG", "email: " + email);
                                                               email = document.getString("email");
                                                               login(email, password);

                                                           } else {
                                                               Log.d("check userName", "user not Exist");
                                                               Toast.makeText(LoginActivity.this, "user not Exist", Toast.LENGTH_SHORT).show();
                                                           }
                                                       }
                                                   }
                                               });
                                           }
                                       });

        signUpButton = findViewById(R.id.signUp);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivityForResult(intent, 000);
            }
        });
    }
    /**
    * use firebaseAuth to login user,then return to main
     * @param email user email
     * @param password password used for app signin
     */
    public void login(String email, String password){
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "login success", Toast.LENGTH_SHORT).show();
                    //shows toast message when user sucessfully logins
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    FirebaseUser currentUser = mAuth.getCurrentUser();

                    if(currentUser == null){
                        Toast.makeText(LoginActivity.this, "error connecting to server", Toast.LENGTH_SHORT).show();
                        //If there is a login issue in regards to server a message will be shown
                    }
                    else{
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", "success");
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "wrong password", Toast.LENGTH_SHORT).show();
                    //If the password is wrong, a message will show
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


}
