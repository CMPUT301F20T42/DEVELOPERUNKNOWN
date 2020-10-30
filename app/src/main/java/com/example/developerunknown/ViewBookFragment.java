package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ViewBookFragment extends Fragment {
    Context context;
    User currentUser;
    Book clickedBook;

    public Button editBookButton;
    public Button deleteBookButton;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private TextView bookISBN;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("Book");

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("ViewBook Message", "Successfully entered fragment");

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        View view = inflater.inflate(R.layout.fragment_view_book, container,false);
        context = container.getContext();

        // Assign buttons
        editBookButton = view.findViewById(R.id.editButton);
        deleteBookButton = view.findViewById(R.id.deleteButton);

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitle);
        bookAuthor = view.findViewById(R.id.viewAuthor);
        bookDescription = view.findViewById(R.id.viewDescription);
        bookISBN = view.findViewById(R.id.viewISBN);

        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());

        editBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create new fragment
            }
        });

        deleteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete book from user collection
                Log.d("delete book", "delete button clicked");
                userBookCollectionReference
                        .document(clickedBook.getISBN())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("delete book", "book has been deleted successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("delete book", "ISBN is not be deleted! " + e.toString());
                            }
                        });

                // Close fragment
                destroy_current_fragment();
            }
        });

        return view;
    }

    public void destroy_current_fragment() {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragmentManager.findFragmentByTag("View Book Fragment"));

        Bundle args = new Bundle();
        args.putSerializable("current user", currentUser);
        Fragment fragment = new BookListFragment();
        fragment.setArguments(args);
        fragmentTransaction.replace(R.id.fragment_container, fragment, "Booklist Fragment");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
