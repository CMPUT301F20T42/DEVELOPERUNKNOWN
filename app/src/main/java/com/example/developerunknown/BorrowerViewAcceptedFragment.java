package com.example.developerunknown;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BorrowerViewAcceptedFragment extends Fragment{

    Context context;
    User currentUser;
    Book clickedBook;

    public Button confirmBorrowButton;
    public Button backButton;
    public ImageView imageView;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private TextView bookISBN;
    private TextView bookOwner;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    public CollectionReference userBookCollectionReference = db.collection("user").document(uid).collection("AcceptedBook");
    final Context applicationContext = MainActivity.getContextOfApplication();

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        View view = inflater.inflate(R.layout.fragment_borrower_view_accepted, container,false);
        context = container.getContext();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Assign buttons
        backButton = view.findViewById(R.id.back);
        confirmBorrowButton = view.findViewById(R.id.borrower_confirm_borrow);

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitleBorrowerAccepted);
        bookAuthor = view.findViewById(R.id.viewAuthorBorrowerAccepted);
        bookDescription = view.findViewById(R.id.viewDescriptionBorrowerAccepted);
        bookISBN = view.findViewById(R.id.viewISBNBorrowerAccepted);
        bookOwner = view.findViewById(R.id.acceptedOwner);


        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());
        bookOwner.setText("Owner:"+clickedBook.getOwnerUname());
        imageView = view.findViewById(R.id.imageView);

        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, imageView);


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

