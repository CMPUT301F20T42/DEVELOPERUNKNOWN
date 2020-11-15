package com.example.developerunknown;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * controls intents of the request function
 */
public class requestActicity extends AppCompatActivity {
    TextView Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_acticity);

        Intent intent = getIntent();
        Request request = (Request) intent.getSerializableExtra("Request");

        Address = findViewById(R.id.ac_address);



    }
    public void finishIt(View view){
        String goAddress = Address.getText().toString();

        /*


            Documentreference currentBookRef = db.collection("user").
                document(currentUser.getUid()).collection("Book").
                document(clickedBook.getID());

//update the borrower info in this clicked book

            currentBookRef.update("borrowerID",request.getBorrowerID());
            currentBookRef.update("borrowerUname",request.getBorrowerUname());
            currentBookRef.update("status","Accepted");

//notify the user who are accepted,and update his "AcceptedBook" in database


            //update accepted book list for borrower
            DocumentReference borrowerAcceptedBookRef = db.collection("user").document(borrower.getUid()).collection("AcceptedBook").document(clickedBook.getID());
            Map acceptedBookData = new HashMap<>();
            acceptedBookData.put("Bookid", clickedBook.getID());
            acceptedBookData.put("book", clickedBook.getTitle());
            acceptedBookData.put("ownerUname", clickedBook.getOwnerUname());
            acceptedBookData.put("ownerId",clickedBook.getOwnerId());
            acceptedBookData.put("title",clickedBook.getTitle());
            acceptedBookData.put("description", clickedBook.getDescription());
            acceptedBookData.put("ISBN",clickedBook.getISBN());
            acceptedBookData.put("borrower",request.getBorrowerUname());
            borrowerBookRef.set(requestedBookData);




            //send notification

            DocumentReference userNotificationRef = db.collection("user").document(requesterID).collection("Notification").document();
            String notificationId = userNotificationRef.getId();
            Map acceptNotiData = new HashMap<>();
            acceptNotiData.put("sender", currentUser.getUid());
            acceptNotiData.put("type", "Accepted");
            acceptNotiData.put("book", clickedBook.getTitle());
            acceptNotiData.put("id", notificationId);


//retrieve all request,accept one request and reject the rest
            CollectionReference requestCollectionRef = currentBookRef.collection("Request");

            requestCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           dataList.clear();
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               String requesterID = document.getString("Borrower");
                               String documentId = document.getID();

                               if (!requesterID.equals(request.getBorrowerID())){        //if not the user we accept,deny and send notification
                                    //send deny notification
                                    DocumentReference userNotificationRef = db.collection("user").document(requesterID).collection("Notification").document();
                                    String notificationId = userNotificationRef.getId();
                                    Map notiData = new HashMap<>();
                                    notiData.put("sender", currentUser.getUid());
                                    notiData.put("type", "Denied");
                                    notiData.put("book", clickedBook.getTitle());
                                    notiData.put("id", notificationId);

                               }

                               //clear data from "RequestedBook" of users since some user is accepted
                               DocumentReference borrowerRequestedBookRef = db.collection("user").document(requesterID).collection("RequestedBook")
                                                                                    .document(clickedBook.getID());
                               borrowerRequestedBookRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                               document.getReference().delete()
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


         */
    }
}