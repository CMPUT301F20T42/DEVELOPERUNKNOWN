package com.example.developerunknown;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BorrowerViewBorrowedFragment extends Fragment{

    Context context;
    User currentUser;
    Book clickedBook;

    public Button denoteReturnButton;
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
    final Context applicationContext = MainActivity.getContextOfApplication();

    public DocumentReference currentBookDocRef;
    public DocumentReference ownerSideCurrentBookRef;

    private String returnDenoted = null;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        View view = inflater.inflate(R.layout.fragment_borrower_view_borrowed, container,false);
        context = container.getContext();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        currentBookDocRef = db.collection("user").document(uid).collection("BorrowedBook").document(clickedBook.getID());
        ownerSideCurrentBookRef = db.collection("user").document(clickedBook.getOwnerId()).collection("Book").document(clickedBook.getID());

        // Assign buttons
        backButton = view.findViewById(R.id.back);
        denoteReturnButton = view.findViewById(R.id.borrower_denote_return);

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitleBorrowerBorrowed);
        bookAuthor = view.findViewById(R.id.viewAuthorBorrowerBorrowed);
        bookDescription = view.findViewById(R.id.viewDescriptionBorrowerBorrowed);
        bookISBN = view.findViewById(R.id.viewISBNBorrowerBorrowed);
        bookOwner = view.findViewById(R.id.borrowedOwner);



        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());
        bookOwner.setText("Owner:"+clickedBook.getOwnerUname());
        imageView = view.findViewById(R.id.imageView);

        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, imageView);


        denoteReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBookDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            returnDenoted = document.getString("returnDenoted");
                        }
                    }
                });
                if (returnDenoted!=null && returnDenoted.equals("true")) {
                    Toast.makeText(getActivity(), "You already denoted borrow before", Toast.LENGTH_SHORT).show();
                }
                //the book is either first time to be denoted or denoted=="false" currently
                else {

                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getActivity(), "You must grant the permission of camera to confirm return", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                    } else {
                        Intent intent = new Intent(getActivity(), Scanner.class);
                        startActivityForResult(intent, 325);
                    }
                }

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


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 325) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("RESULT_ISBN");
                if (result.equals(clickedBook.getISBN())){
                    currentBookDocRef.update("returnDenoted","true");
                    ownerSideCurrentBookRef.update("returnDenoted","true");
                    //owner denote borrow
                    Toast.makeText(getActivity(), "Your denote of return is made successfully", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(getActivity(), "The ISBN you scaned does not match the ISBN of the book", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}


