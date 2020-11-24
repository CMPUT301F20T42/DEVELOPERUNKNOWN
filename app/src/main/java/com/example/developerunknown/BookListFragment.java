package com.example.developerunknown;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import com.example.developerunknown.AddBookFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
/**
 * booklistfragment displays the Owner books, and wishlist books
 */
public class BookListFragment extends Fragment implements AddBookFragment.OnFragmentInteractionListener  {
    ListView bookList;
    ArrayAdapter<Book> bookAdapter;
    ArrayList<Book> bookDataList;
    Context context;
    User currentUser;

    Spinner filterSelection;

    ArrayAdapter<String> filterAdapter;
    ArrayList<String> filterList;



    Spinner selectSelector;
    ArrayAdapter<String> selectAdapter;
    ArrayList<String> selectList;

    Fragment fragment; // this fragment will show up after the owner clicks a specific book


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




        filterSelection = (Spinner) view.findViewById(R.id.filter_spinner);

        filterList = new ArrayList<String>();

        filterList.add("All");
        filterList.add("Accepted");
        filterList.add("Available");
        filterList.add("Borrowed");
        filterList.add("Requested");

        filterAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, filterList);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSelection.setAdapter(filterAdapter);

        //////////////////////////////////////////////////

        selectSelector = (Spinner) view.findViewById(R.id.list_spinner);
        selectList = new ArrayList<String>();

        //selectList.add("All");
        selectList.add("My Owned Books");
        selectList.add("Books Wishlist");


        selectAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, selectList);
        selectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSelector.setAdapter(selectAdapter);

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

                if (clickedBook.getStatus().equals("Accepted")){
                    fragment = new OwnerViewAcceptedFragment();
                }
                else if (clickedBook.getStatus().equals("Borrowed")){
                    fragment = new OwnerViewBorrowedFragment();
                }
                else {
                    fragment = new ViewBookFragment();
                }
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "View Book Fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        // this part is used to filter status when viewing my book
        filterSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String filter;

            @Override
            /**
             * Always user to select a spinner item
             *  @param parent the original view
             *  @param view what is being viewed currently
             *  @param position item position within spinner list
             *  @param id user id
             */
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get filter
                filter = filterList.get(position);
                // Apply filter
                if (!filter.equals("All")) {
                    // Reset bookDataList
                    bookDataList = currentUser.getFilteredBookList(filter);

                    bookAdapter = new CustomList(context, bookDataList);
                    bookList.setAdapter(bookAdapter);

                } else {
                    bookDataList = currentUser.getBookList();
                }
                bookAdapter = new CustomList(context, bookDataList);
                bookList.setAdapter(bookAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // Do nothing?
            }
        });

        selectSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            String select;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get filter
                select = selectList.get(position);
                // Apply filter
                if (!select.equals("My Owned Books")) {
                    // Reset bookDataList
                    bookDataList = currentUser.getFilteredBookList(select);

                    bookAdapter = new CustomList(context, bookDataList);
                    bookList.setAdapter(bookAdapter);

                } else {
                    bookDataList = currentUser.getBookList();
                }

                bookAdapter = new CustomList(context, bookDataList);
                bookList.setAdapter(bookAdapter);
            }

            @Override
            /**
             * method is called if no item is selected for spinner view
             *  @param parent
             */
            public void onNothingSelected(AdapterView<?> parent) {

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
                    String OwnerId = doc.getString("ownerId");
                    String OwnerUname = doc.getString("ownerUname");
                    String title = (String) doc.getData().get("title");
                    String author = (String) doc.getData().get("author");
                    String description = (String) doc.getData().get("description");
                    String ISBN = (String) doc.getData().get("ISBN");
                    String status = (String) doc.getData().get("status");

                    Double lat = doc.getDouble("lat");
                    Double lon = doc.getDouble("lat");
                    String address = doc.getString("address");

                    // Adding the books from FireStore
                    Book thisBook = new Book(doc.getId(), title, author, status, ISBN, description, OwnerId, OwnerUname);
                    if (status.equals("Accepted") || status.equals("Borrowed")){
                        String borrowerID = (String) doc.getData().get("borrowerID");
                        String borrowerUname = (String) doc.getData().get("borrowerUname");
                        thisBook.setBorrowerID(borrowerID);
                        thisBook.setBorrowerUname(borrowerUname);

                        thisBook.setLat(lat);
                        thisBook.setLon(lon);
                        thisBook.setAddress(address);
                    }
                    bookDataList.add(thisBook); // Adding the books from FireStore
                }
                bookAdapter.notifyDataSetChanged(); // Notifying the adapter to render any new data fetcheh
            }
        });


    }

    @Override
    /**
     * Tells fragments when button is pressed
     *  @param newBook
     */
    public void onOkPressed (Book newBook) {

    }
}
