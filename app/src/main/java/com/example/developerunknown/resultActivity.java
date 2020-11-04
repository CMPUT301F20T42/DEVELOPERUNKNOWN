package com.example.developerunknown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class resultActivity extends AppCompatActivity {
    TextView Title;
    TextView Author;
    TextView ISBN;
    TextView Description;
    TextView Status;
    ImageView BookImage;
    Book currentBook;
    User borrower;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Result");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        currentBook = (Book) intent.getSerializableExtra("SelectedBook");
        borrower = (User) intent.getSerializableExtra("nowUser");

        Title = findViewById(R.id.rbook_title);
        Author = findViewById(R.id.rbook_author);
        ISBN = findViewById(R.id.rbook_ISBN);
        Description = findViewById(R.id.rbook_description);
        Status = findViewById(R.id.rbook_status);

        Title.setText(currentBook.getTitle());
        Author.setText(currentBook.getAuthor());
        ISBN.setText(currentBook.getISBN());
        Description.setText(currentBook.getDescription());
        Status.setText(currentBook.getStatus());


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*public void startRequest(View view){
        Intent intent = new Intent(this,resultActivity.class);
        intent.putExtra("SelectedBook", currentBook);
        startActivity(intent);
    }*/

    public void startRequest(View view){
        if (currentBook.getOwner() == borrower.getUid()){
                Toast.makeText(resultActivity.this, "You cant request for this book", Toast.LENGTH_SHORT).show();
        } else {
            if (currentBook.getStatus().equals("Available") || currentBook.getStatus().equals("Requsted")) {
                final Request nowRequest = new Request(borrower.getUid(), currentBook);
                //DocumentReference docRef = db.collection("User").document(currentBook.getOwner());
                Query query = db.collectionGroup("Book");
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId() == currentBook.getBid()) {
                                    DocumentReference docRef = db.collection("user").document(currentBook.getOwner()).collection("Book").document(currentBook.getBid());
                                    Map<String, Object> requstData = new HashMap<>();
                                    requstData.put("Borrower", nowRequest.getBorrower());
                                    requstData.put("Bookid", currentBook.getBid());
                                    requstData.put("Status", nowRequest.getStatus());
                                    docRef.collection("Request").add(requstData);
                                }
                            }
                        }
                    }
                });
                Toast.makeText(resultActivity.this, "Your request has sent", Toast.LENGTH_SHORT).show();
            }
        }
    }
}