package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
/**
 * allows user to view book information in detail
 */
public class ViewBookFragment extends Fragment {
    Context context;
    User currentUser;
    Book clickedBook;

    public Button editBookButton;
    public Button deleteBookButton;
    public Button requestButton;
    public Button backButton;
    public ImageView imageView;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private TextView bookISBN;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("Book");
    final Context applicationContext = MainActivity.getContextOfApplication();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        View view = inflater.inflate(R.layout.fragment_view_book, container,false);
        context = container.getContext();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Assign buttons
        editBookButton = view.findViewById(R.id.edit_button);
        deleteBookButton = view.findViewById(R.id.delete_button);
        requestButton = view.findViewById(R.id.requests_button);
        backButton = view.findViewById(R.id.back);

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitle);
        bookAuthor = view.findViewById(R.id.viewAuthor);
        bookDescription = view.findViewById(R.id.viewDescription);
        bookISBN = view.findViewById(R.id.viewISBN);

        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());
        imageView = view.findViewById(R.id.imageViewBorrowerBorrowed);

        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, imageView);

        editBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Can only edit a book that is available
                if (clickedBook.getStatus().equals("Available")) {
                    Log.d("View Book Fragment", "Clicked on Edit Button");
                    // Create new fragment
                    Bundle args = new Bundle();
                    args.putSerializable("current user", currentUser);
                    args.putSerializable("clicked book", clickedBook);

                    Fragment fragment = new EditBookFragment();
                    fragment.setArguments(args);

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment, "Edit Book Fragment");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
                // Error message
                else {
                    Toast.makeText(getActivity(), "Cannot edit a book that is " + clickedBook.getStatus(), Toast.LENGTH_SHORT).show();
                }
            }

        });

        deleteBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickedBook.getStatus().equals("Available")) {

                    Log.d("delete book", "delete button clicked");
                    userBookCollectionReference.document(clickedBook.getID()).delete();

                    Photographs.deleteImage("B", clickedBook.getID(), storageReference, storage);

                // Close fragment
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.popBackStack();
                }
                else{
                    Toast.makeText(getActivity(), "Cannot delete a book that is " + clickedBook.getStatus(), Toast.LENGTH_SHORT).show();
                }
        }}
        );

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("View Book Fragment", "Clicked on View Requests Button");
                // Create new fragment
                Bundle args = new Bundle();
                args.putSerializable("current user", currentUser);
                args.putSerializable("clicked book", clickedBook);

                Fragment fragment = new ViewRequestsFragment();
                fragment.setArguments(args);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment, "View Requests Fragment");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return view;
    }

}
