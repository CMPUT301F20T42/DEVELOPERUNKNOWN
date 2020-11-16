package com.example.developerunknown;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;


/**
 * controls intents of the request function
 */
public class requestActicity extends AppCompatActivity {
    TextView Address;
    Request request;
    Book Book;
    User currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_acticity);

        Intent intent = getIntent();
        request = (Request) intent.getSerializableExtra("Request");
        Book = (Book) intent.getSerializableExtra("Book");
        currentUser = (User) intent.getSerializableExtra("CurrentUser");

        Address = findViewById(R.id.ac_address);




    }
    public void finishIt(View view){
        String goAddress = Address.getText().toString();


        DocumentReference currentBookRef = db.collection("user").
            document(Book.getOwnerId()).collection("Book").
            document(Book.getID());

        //update the borrower info in this clicked book

        currentBookRef.update("borrowerID",request.getBorrowerID());
        currentBookRef.update("borrowerUname",request.getBorrowerUname());
        currentBookRef.update("status","Accepted");

        //notify the user who are accepted,and update his "AcceptedBook" in database


        //update accepted book list for borrower
        DocumentReference borrowerAcceptedBookRef = db.collection("user").document(request.getBorrowerID()).collection("AcceptedBook").document(Book.getID());
        Map acceptedBookData = new HashMap<>();
        acceptedBookData.put("Bookid", Book.getID());
        acceptedBookData.put("book", Book.getTitle());
        acceptedBookData.put("ownerUname", Book.getOwnerUname());
        acceptedBookData.put("ownerId",Book.getOwnerId());
        acceptedBookData.put("title",Book.getTitle());
        acceptedBookData.put("description", Book.getDescription());
        acceptedBookData.put("ISBN",Book.getISBN());
        acceptedBookData.put("borrower",request.getBorrowerUname());
        acceptedBookData.put("borrowerId", request.getBorrowerID());
        //borrowerBookRef.set(requestedBookData);




        //send notification

        DocumentReference userNotificationRef = db.collection("user").document(Book.getOwnerId()).collection("Notification").document();
        String notificationId = userNotificationRef.getId();
        Map acceptNotiData = new HashMap<>();
        acceptNotiData.put("sender", currentUser.getUid());
        acceptNotiData.put("type", "Accepted");
        acceptNotiData.put("book", Book.getTitle());
        acceptNotiData.put("id", notificationId);


        //retrieve all request,accept one request and reject the rest
        CollectionReference requestCollectionRef = currentBookRef.collection("Request");

        requestCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String requesterID = document.getString("Borrower");
                        String documentId = document.getId();

                        if (!requesterID.equals(request.getBorrowerID())) {        //if not the user we accept,deny and send notification
                            //send deny notification
                            DocumentReference userNotificationRef = db.collection("user").document(requesterID).collection("Notification").document();
                            String notificationId = userNotificationRef.getId();
                            Map notiData = new HashMap<>();
                            notiData.put("sender", currentUser.getUid());
                            notiData.put("type", "Denied");
                            notiData.put("book", Book.getTitle());
                            notiData.put("id", notificationId);

                        }

                        //clear data from "RequestedBook" of users since some user is accepted
                        DocumentReference borrowerRequestedBookRef = db.collection("user").document(requesterID).collection("RequestedBook")
                                .document(Book.getID());
                        borrowerRequestedBookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        document.getReference().delete();
                                    } else {

                                    }
                                }
                            }
                        });
                        //clear the request,no matter accepted one or denied one
                        document.getReference().delete();
                    }
                }
            }
        });
    }
}