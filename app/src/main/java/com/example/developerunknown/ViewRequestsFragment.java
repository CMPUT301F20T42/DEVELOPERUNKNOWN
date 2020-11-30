package com.example.developerunknown;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
/**
 * allows user to see a list of requests on one of his/her book and perform future actions
 */
public class ViewRequestsFragment extends Fragment {
    ListView requestList;
    ArrayAdapter<Request> requestAdapter;
    ArrayList<Request> requestDataList = new ArrayList<>();
    Context context;
    User currentUser;
    Book clickedBook;
    private FragmentActivity myContext;

    public Button backButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public String bookid;
    public CollectionReference bookRequestCollectionReference;

    @Override
    /**take a given activity and attach it
     * @param activity
     */
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    /**
     * This displays the view of the class
     * @param inflater creates view
     * @param container contains the layout view
     * @param savedInstanceState contains the recent data
     * @return
     * Return the view of a list of requests,it shows a list of books owned by current user with necessary information
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        bookid = clickedBook.getID();
        
        bookRequestCollectionReference = db.collection("user").
                document(uid).collection("Book").
                document(bookid).collection("Request");

        View view = inflater.inflate(R.layout.fragment_view_requests, container,false);
        final Context context= container.getContext();

        requestList = view.findViewById(R.id.request_list);

        // Grab requests

        //CollectionReference bookRef = db.collection("user").document(uid).collection("Book").document(bookid).collection("Request");
        bookRequestCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                requestDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String bookid = (String)doc.getData().get("Bookid");
                    String borrower = (String) doc.getData().get("Borrower");
                    String borrowerUname = (String) doc.getData().get("BorrowerUname");
                    //Timestamp time = (Timestamp) doc.getData().get("time"); //discard for now
                    String Rid = doc.getId();
                    requestDataList.add(new Request(borrower, borrowerUname, bookid, Rid));
                }
                requestAdapter.notifyDataSetChanged();
            }
        });


        // Display requests
        requestAdapter = new RequestList(context, requestDataList);
        requestList.setAdapter(requestAdapter);


        // Click oh item listener

        // Get Back Button
        backButton = view.findViewById(R.id.back);

        return view;
    }

    @Nullable
    @Override
    /**
     * This method define some actions after the view is created,attach onClickListener to both request list component
     * and make a back button to allow it lead user back to previous page
     * @param view created view
     * @param savedInstanceState contains the recent data
     */
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Click oh item listener

        requestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> list, View v, int pos, long id) {
                /*Intent intent = new Intent(getActivity(),requestActicity.class);
                Request thisRequest = requestAdapter.getItem(pos);
                intent.putExtra("Request", thisRequest);
                startActivity(intent);*/

                Request thisRequest = requestDataList.get(pos);

                new requestFragment(clickedBook, thisRequest, currentUser).show(getActivity().getSupportFragmentManager(), "Request From");

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

    }

}
