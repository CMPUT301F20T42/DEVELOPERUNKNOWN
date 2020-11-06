package com.example.developerunknown;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
/**
 * Controls the result of the book searches
 */
public class resultActivity extends AppCompatActivity {
    TextView Title;
    TextView Author;
    TextView ISBN;
    TextView Description;
    TextView Status;
    TextView OwnerUname;
    ImageView BookImage;
    Book currentBook;
    User borrower;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    final Context applicationContext = MainActivity.getContextOfApplication();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Result");
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        currentBook = (Book) intent.getSerializableExtra("SelectedBook");
        borrower = (User) intent.getSerializableExtra("nowUser");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Title = findViewById(R.id.rbook_title);
        Author = findViewById(R.id.rbook_author);
        ISBN = findViewById(R.id.rbook_ISBN);
        Description = findViewById(R.id.rbook_description);
        Status = findViewById(R.id.rbook_status);
        BookImage = findViewById(R.id.BookImage);
        OwnerUname = findViewById(R.id.displayOwner);

        Title.setText(currentBook.getTitle());
        Author.setText(currentBook.getAuthor());
        ISBN.setText(currentBook.getISBN());
        Description.setText(currentBook.getDescription());
        Status.setText(currentBook.getStatus());
        OwnerUname.setText("Owner:"+currentBook.getOwnerUname());

        Photographs.viewImage("B", currentBook.getID(), storageReference, applicationContext, BookImage);

    }

    @Override
    /**
     * Allows menu items to be selected
     * @return
     *  Return true
     *  Return item
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void startRequest(View view) {
        if (currentBook.getOwnerId().equals(borrower.getUid())) {
            Toast.makeText(resultActivity.this, "You can't request your own book", Toast.LENGTH_SHORT).show();
        } else {
            if (currentBook.getStatus().equals("Available") || currentBook.getStatus().equals("Requested")) {
                final Request nowRequest = new Request(borrower.getUid(), borrower.getUsername(), currentBook.getID());
                //DocumentReference docRef = db.collection("User").document(currentBook.getOwner());
/*
                Query query = db.collectionGroup("Book");
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (document.getId() == currentBook.getID()) {
                                    DocumentReference docRef = db.collection("user").document(currentBook.getOwnerId()).collection("Book").document(currentBook.getID());
                                    Map<String, Object> requstData = new HashMap<>();
                                    requstData.put("Borrower", nowRequest.getBorrower());
                                    requstData.put("Bookid", currentBook.getID());
                                    requstData.put("status", nowRequest.getStatus());
                                    docRef.collection("Request").add(requstData);
                                    Map<String, Object> newbookData = new HashMap<>();
                                    newbookData.put("status", "Requested");
                                    docRef.update(newbookData);
                                }
                            }
                        }
                    }
                });

*/
                // Start request sequence
                DocumentReference requestedBookRef = db.collection("user"). document(currentBook.getOwnerId()).
                        collection("Book").document(currentBook.getID());
                requestedBookRef.update("status","Requested");

                DocumentReference requestRef = db.collection("user").
                        document(currentBook.getOwnerId()).collection("Book").
                        document(currentBook.getID()).collection("Request").document();
                String requestId = requestRef.getId();
                //Get requestdata for data upload
                //In firestore,each
                Map requestData = new HashMap<>();
                requestData.put("id", requestId);
                requestData.put("Borrower", borrower.getUid());
                requestData.put("Bookid", currentBook.getID());
                requestData.put("BorrowerUname", borrower.getUsername());


                requestRef.set(requestData); // push to firestore


                //send notification
                //if getOwner return userName

                DocumentReference userNotificationRef = db.collection("user").document(currentBook.getOwnerId()).collection("Notification").document();

                String notificationId = userNotificationRef.getId();

                Map notiData = new HashMap<>();
                notiData.put("sender", borrower.getUsername());
                notiData.put("type", "Request");
                notiData.put("book", currentBook.getTitle());
                notiData.put("id", notificationId);

                userNotificationRef.set(notiData);

                DocumentReference borrowerBookRef = db.collection("user").document(borrower.getUid()).collection("RequestedBook").document(currentBook.getID());


                Map requestedBookData = new HashMap<>();
                requestedBookData.put("Bookid", currentBook.getID());
                requestedBookData.put("book", currentBook.getTitle());
                requestedBookData.put("ownerUname", currentBook.getOwnerUname());
                requestedBookData.put("ownerId",currentBook.getOwnerId());
                requestedBookData.put("title",currentBook.getTitle());
                requestedBookData.put("description", currentBook.getDescription());
                requestedBookData.put("ISBN",currentBook.getISBN());
                requestedBookData.put("requester",borrower.getUsername());

                borrowerBookRef.set(requestedBookData);


                Toast.makeText(resultActivity.this, "Your request has sent", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }
}
