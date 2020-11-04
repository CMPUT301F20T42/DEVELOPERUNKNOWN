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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class BookListFragment extends Fragment implements AddBookFragment.OnFragmentInteractionListener  {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    Context context;
    User currentUser;


    Spinner selectStatus;
    Spinner selectList;
    RequestedListFragment requestedListFragment;


    //########################## this part is needed for the below blocking part.

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("Book");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = (User) this.getArguments().getSerializable("current user");

        View view = inflater.inflate(R.layout.fragment_booklist,container,false);
        context = container.getContext();

        bookList = view.findViewById(R.id.user_book_list);

        bookDataList = currentUser.getBookList();

        bookAdapter = new CustomList(context, bookDataList);
        bookList.setAdapter(bookAdapter);


        //Used so the spinner items can be selected for the different status, causing a fragment change
        selectStatus = view.findViewById(R.id.select_status_spinner);
        selectStatus.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
               Fragment fragment = null;
               switch (position){
/*
                   case 0:
                       //Show all books
                      break;

                   case 1:
                       //calls the available fragment
                       fragment = new AvailableBookFragment();
                       break;
*/
                   default:
                       break;
               }

               if (fragment != null) {
                   FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                   FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                   fragmentTransaction.replace(R.id.fragment_container, fragment);
                   fragmentTransaction.addToBackStack(null);
                   fragmentTransaction.commit();
                   Toast.makeText(context, selectStatus.getSelectedItem().toString()+"is working", Toast.LENGTH_LONG).show();


               } else {
                   // error in creating fragment
                   Log.e("Book list fragment", "Error in creating fragment");
               }
               }
            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }

        }));



        //this is the spinner for the list selection
        selectList = view.findViewById(R.id.list_spinner);
        selectList.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected (AdapterView<?> parent, View view, int position, long id){
                Fragment fragment = null;
                switch (position){
                   case 0:
                       //Owned book list
                       //automatically starts on this page
                       break;

                   case 1:
                       //Borrowed list
                       //add a request to users borrowed list fragment
                       fragment = new RequestedListFragment(); // for the time being im using the accpeted fragmnet
                       break;

                    default:
                        break;
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    //Toast.makeText(context, selectStatus.getSelectedItem().toString()+"is working", Toast.LENGTH_LONG).show();


                } else {
                    // error in creating fragment
                    Log.e("Book list fragment", "Error in creating fragment");
                }
            }
            @Override
            public void onNothingSelected (AdapterView<?> parent) {

            }

        }));








        return view;
    }


    @Nullable
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get add book button
        FloatingActionButton addBookButton = view.findViewById(R.id.add_book_button);
        addBookButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick (View v) {
                Bundle args = new Bundle();
                args.putSerializable("current user", currentUser);

                Fragment fragment = new AddBookFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "Add Book Fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

        });


        bookList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Log.d("BookList Message", "Successfully clicked book");

                Book clickedBook = bookDataList.get(position);

                Bundle args = new Bundle();
                args.putSerializable("current user", currentUser);
                args.putSerializable("clicked book", clickedBook);

                Fragment fragment = new ViewBookFragment();
                fragment.setArguments(args);



                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "View Book Fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });









//################################### this part retrieves book from online data base and automatically update ################################

        userBookCollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {

                bookDataList.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String title = (String) doc.getData().get("title");
                    String author = (String) doc.getData().get("author");
                    String description = (String) doc.getData().get("description");
                    String ISBN = (String) doc.getData().get("ISBN");
                    String status = (String) doc.getData().get("status");
                    User currentBorrower = (User) doc.getData().get("borrower");
                    bookDataList.add(new Book(title, author, status, ISBN, description)); // Adding the cities and provinces from FireStore
                }
                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetcheh
            }
        });


    }

    @Override
    public void onOkPressed (Book newBook) {

    }
}
