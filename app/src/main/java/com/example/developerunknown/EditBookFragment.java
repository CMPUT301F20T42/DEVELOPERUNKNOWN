package com.example.developerunknown;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class EditBookFragment extends Fragment {

    public Button addBookButton;
    public Button cancelButton;

    private EditText bookTitle;
    private EditText bookAuthor;
    private Spinner bookStatus;
    private EditText bookDescription;
    private EditText bookISBN;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("Book");

    Context context;
    User currentUser;
    Book clickedBook;

    @Override
    @Nullable
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        final View view = inflater.inflate(R.layout.fragment_edit_book, container, false);

        // initialize add button
        addBookButton = view.findViewById(R.id.add_book_button2);
        cancelButton = view.findViewById(R.id.cancel_book_button);

        bookTitle = view.findViewById(R.id.book_title_editText);
        bookAuthor = view.findViewById(R.id.book_author_editText);
        bookStatus = view.findViewById(R.id.spinner);
        bookDescription = view.findViewById(R.id.book_description_editText);
        bookISBN = view.findViewById(R.id.book_isbn_editText);

        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookStatus.setSelection(((ArrayAdapter) bookStatus.getAdapter()).getPosition(clickedBook.getStatus()));
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {


                String title = bookTitle.getText().toString();
                String author = bookAuthor.getText().toString();
                String status = bookStatus.getSelectedItem().toString();
                String description = bookDescription.getText().toString();
                String ISBN = bookISBN.getText().toString();

                if(title.length() > 0 && author.length() > 0 && description.length() > 0 && ISBN.length() > 0)
                {
                    // Change book
                    clickedBook.setTitle(title);
                    clickedBook.setAuthor(author);
                    clickedBook.setStatus(status);
                    clickedBook.setDescription(description);
                    clickedBook.setISBN(ISBN);
                    // Update database

                    HashMap<String, String> data = new HashMap<>();
                    data.put("title", title);
                    data.put("author", author);
                    data.put("status", status);
                    data.put("description", description);
                    // What if user updates the ISBN?
                    data.put("ISBN", ISBN);

                    userBookCollectionReference
                            .document(clickedBook.getID())
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("edit book", "book has been edited successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("edit book", "ISBN is not be edited!" + e.toString());
                                }
                            });

                    destroy_current_fragment();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroy_current_fragment();
            }
        });

        return view;
    }

    @Nullable
    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void destroy_current_fragment() {

//        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.remove(fragmentManager.findFragmentByTag("Edit Book Fragment"));
//
//        Bundle args = new Bundle();
//        args.putSerializable("current user", currentUser);
//        args.putSerializable("clicked book", clickedBook);
//        Fragment fragment = new ViewBookFragment();
//        fragment.setArguments(args);
//        fragmentTransaction.replace(R.id.fragment_container, fragment, "View Book Fragment");
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

}
