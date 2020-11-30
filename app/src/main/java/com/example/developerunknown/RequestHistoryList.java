package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * This class shows a list of books the current user has ever requested
 */
public class  RequestHistoryList extends Fragment {


    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    Context context;
    User currentUser;
    public boolean bookExist = true;
    //########################## this part is needed for the below blocking part.

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference requestedBookCollectionReference = db.collection("user").document(uid).collection("RequestedHistory");

    ArrayList<String> bookIdList;
    ArrayList<String> ownerIdList;

    @Nullable
    @Override
    /**
     * This displays the view of a list of book user ever requested
     * @param inflater creates view
     * @param container contains the layout view
     * @param savedInstanceState contains the recent data
     * @return constructed view
     *
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = (User) this.getArguments().getSerializable("current user");

        View view = inflater.inflate(R.layout.fragment_request_history, container, false);
        context = container.getContext();

        bookList = view.findViewById(R.id.user_request_history);

        bookDataList = new ArrayList<>();
        bookIdList = new ArrayList<>();
        ownerIdList = new ArrayList<>();

        bookAdapter = new CustomList(context, bookDataList);
        bookList.setAdapter(bookAdapter);


        return view;
    }


    @Nullable
    @Override
    /**
     * @param view
     * @param savedInstanceState
     * attach onItemClickListener and addSpanpsshotListener to firestore collection reference,keep real-time updating
     * get reference to original book from borrower so that the status of book is also real-time updated
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get add book button
        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Log.d("BookList Message", "Successfully clicked book");

                Book clickedBook = bookDataList.get(position);

                Bundle args = new Bundle();
                args.putSerializable("clicked book", clickedBook);

                Fragment fragment = new BorrowerViewGeneralFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "View Books From Request History");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        //################################### this part retrieves book from online data base and automatically update ################################

        requestedBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            /**
             *Handles the event of recalling the past book requests
             * @param queryDocumentSnapshots contains data read from a document in your Firestore
             *                               database as part of a query.
             * @param error a class exception thrown by firstore
             */
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                bookDataList.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {

                    String bookID = doc.getString("Bookid");
                    String OwnerId = doc.getString("ownerId");

                    DocumentReference requestedHistoryBook = db.collection("user").document(OwnerId).collection("Book").document(bookID);
                    requestedHistoryBook.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                        /**
                         *Called when a task is completed
                         * @param task is either completed or uncompleted
                         */
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            Log.d("TAG", "found book");
                                            String ownerId = document.getString("ownerId");
                                            String OwnerUname = document.getString("ownerUname");
                                            String title = (String) document.getData().get("title");
                                            String author = (String) document.getData().get("author");
                                            String description = (String) document.getData().get("description");
                                            String ISBN = (String) document.getData().get("ISBN");
                                            String status = (String) document.getData().get("status");
                                            bookDataList.add(new Book(document.getId(), title, author, status, ISBN, description, ownerId, OwnerUname)); // Adding the books from FireStore
                                            bookAdapter.notifyDataSetChanged();
                                        }
                                        else{
                                            bookExist = false;
                                        }
                                    }
                                }
                            });
                    // delete from history if book is deleted
                    if (bookExist == false){
                        doc.getReference().delete();
                        bookExist = true;
                    }
                }
            }
        });
    }

}