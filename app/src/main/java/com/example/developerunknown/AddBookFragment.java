package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AddBookFragment extends Fragment {

    public Button addBookButton;
    public Button cancelButton;

    private EditText bookTitle;
    private EditText bookAuthor;
    private EditText bookDescription;
    private EditText bookISBN;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference bookCollectionReference = db.collection("book");

    Context context;
    User currentUser;

    public interface OnFragmentInteractionListener {
        void onOkPressed (Book newBook);
    }


    @Override
    @Nullable
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentUser = (User) this.getArguments().getSerializable("current user");

        final View view = inflater.inflate(R.layout.fragment_add_book, container, false);

        //initialize add button
        addBookButton = view.findViewById(R.id.add_book_button2);
        cancelButton = view.findViewById(R.id.cancel_book_button);

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                bookTitle = view.findViewById(R.id.book_title_editText);
                bookAuthor = view.findViewById(R.id.book_author_editText);
                bookDescription = view.findViewById(R.id.book_description_editText);
                bookISBN = view.findViewById(R.id.book_isbn_editText);

                String title = bookTitle.getText().toString();
                String author = bookAuthor.getText().toString();
                String description = bookDescription.getText().toString();
                String ISBN = bookISBN.getText().toString();

                if(title.length() > 0 && author.length() > 0 && description.length() > 0 && ISBN.length() > 0)
                {
                    Book book = new Book(title, author,  "Available", ISBN, description);
                    currentUser.addBook(book);

                    // Add book to book collection
                    HashMap<String, String> data = new HashMap<>();
                    data.put("title", title);
                    data.put("author", author);
                    data.put("description", description);
                    data.put("ISBN", ISBN);

                    bookCollectionReference
                            .document(ISBN)
                            .set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("create book", "book has been added successfully!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("create book", "ISBN is not be added!" + e.toString());
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

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag("Add Book Fragment"));

        Bundle args = new Bundle();
        args.putSerializable("current user", currentUser);
        Fragment fragment = new BookListFragment();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, fragment, "Booklist Fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
