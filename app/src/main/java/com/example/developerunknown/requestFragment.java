package com.example.developerunknown;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
 * A simple {@link Fragment} subclass.
 * Use the {@link requestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class requestFragment extends DialogFragment {
    private TextView Title;
    private TextView Author;
    private Book nowBook;
    private Request nowRequest;
    private User currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public requestFragment() {
        // Required empty public constructor
    }

    public requestFragment(Book nowBook, Request nowRequest, User currentUser) {
        this.nowBook = nowBook;
        this.nowRequest = nowRequest;
        this.currentUser = currentUser;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment requestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static requestFragment newInstance(String param1, String param2) {
        requestFragment fragment = new requestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_request, null);
        Title = view.findViewById(R.id.rf_title);

        String dialogTitle = "Request from " + nowRequest.getBorrowerUname();
        //currentUser = (User) this.getArguments().getSerializable("current user");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle(dialogTitle)
                .setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Todo: Delete request from firebase
                        DocumentReference currentBookRef = db.collection("user").document(nowBook.getOwnerId()).collection("Book").document(nowBook.getID());
                        // Change book status to available
                        nowBook.setStatus("Available");
                        currentBookRef.update("status", "Available");

                        CollectionReference requestCollectionRef = currentBookRef.collection("Request");

                        requestCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String requesterID = document.getString("Borrower");
                                        //String documentId = document.getId();

                                        if (requesterID.equals(nowRequest.getBorrowerID())) {        //if not the user we accept,deny and send notification
                                            //send deny notification
                                            DocumentReference userNotificationRef = db.collection("user").document(requesterID).collection("Notification").document();
                                            String notificationId = userNotificationRef.getId();
                                            Map notiData = new HashMap<>();
                                            notiData.put("sender", currentUser.getUsername());
                                            notiData.put("type", "Denied");
                                            notiData.put("book", nowBook.getTitle());
                                            notiData.put("id", notificationId);
                                            userNotificationRef.set(notiData);

                                            document.getReference().delete();
                                            DocumentReference borrowerRequestedBookRef = db.collection("user").document(requesterID).collection("RequestedBook")
                                                    .document(nowBook.getID());
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
                                        }
                                    }
                                }
                            }
                        });
                    }})
                .setPositiveButton("OK I accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Update book's status
//                        nowBook.setStatus("Accepted");
//                        DocumentReference currentBookRef = db.collection("user").document(nowBook.getOwnerId()).collection("Book").document(nowBook.getID());
//                        currentBookRef.update("status", "Accepted");

                        Intent intent = new Intent(getActivity(),requestActicity.class);
                        intent.putExtra("CurrentUser", currentUser);
                        intent.putExtra("Request", nowRequest);
                        intent.putExtra("Book", nowBook);
                        startActivity(intent);
                    }}).create();
    }
}
