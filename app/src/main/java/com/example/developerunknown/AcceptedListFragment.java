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
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AcceptedListFragment extends Fragment{


    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    Context context;
    User currentUser;

    //########################## this part is needed for the below blocking part.

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference acceptedBookCollectionReference = db.collection("user").document(uid).collection("AcceptedBook");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = (User) this.getArguments().getSerializable("current user");

        View view = inflater.inflate(R.layout.fragment_acceptlist,container,false);
        context = container.getContext();

        bookList = view.findViewById(R.id.user_accept_list);

        bookDataList = new ArrayList<Book>();

        bookAdapter = new BookList(context, bookDataList);
        bookList.setAdapter(bookAdapter);

        return view;
    }

    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Log.d("BookList Message", "Successfully clicked book");

                Book clickedBook = bookDataList.get(position);

                Bundle args = new Bundle();
                args.putSerializable("current user", currentUser);
                args.putSerializable("clicked book", clickedBook);

                Fragment fragment = new BorrowerViewAcceptedFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "View Book Accepted by owner");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //################################### this part retrieves book from online data base and automatically update ################################

        acceptedBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String OwnerId = doc.getString("ownerId");
                    String OwnerUname = doc.getString("ownerUname");
                    String title = (String) doc.getData().get("title");
                    String author = (String) doc.getData().get("author");
                    String description = (String) doc.getData().get("description");
                    String ISBN = (String) doc.getData().get("ISBN");
                    String status = (String) doc.getData().get("status");

                    bookDataList.add(new Book(doc.getId(), title, author, status, ISBN, description,OwnerId,OwnerUname)); // Adding the books from FireStore
                }
                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetcheh
            }
        });


    }

}
