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

import java.util.HashMap;
import java.util.Map;


public class OwnerViewBorrowedFragment extends Fragment {

    Context context;
    User currentUser;
    Book clickedBook;

    public Button confirmReturnButton;
    public Button backButton;
    public ImageView imageView;

    private TextView bookTitle;
    private TextView bookAuthor;
    private TextView bookDescription;
    private TextView bookISBN;
    private TextView bookBorrower;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public String uid = user.getUid();
    final Context applicationContext = MainActivity.getContextOfApplication();
    public DocumentReference currentBookDocRef;
    public DocumentReference borrowerSideBorrowedBookRef;//the data in borrower side

    private String returnDenoted = null;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        currentUser = (User) this.getArguments().getSerializable("current user");
        clickedBook = (Book) this.getArguments().getSerializable("clicked book");

        View view = inflater.inflate(R.layout.fragment_owner_view_borrowed, container,false);
        context = container.getContext();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        currentBookDocRef = db.collection("user").document(uid).collection("Book").document(clickedBook.getID());
        borrowerSideBorrowedBookRef = db.collection("user").document(clickedBook.getBorrowerID()).collection("BorrowedBook").document(clickedBook.getID());

        // Assign buttons
        backButton = view.findViewById(R.id.back);
        confirmReturnButton = view.findViewById(R.id.owner_confirm_return);

        // Display clicked book
        bookTitle = view.findViewById(R.id.viewTitleOwnerBorrowed);
        bookAuthor = view.findViewById(R.id.viewAuthorOwnerBorrowed);
        bookDescription = view.findViewById(R.id.viewDescriptionOwnerBorrowed);
        bookISBN = view.findViewById(R.id.viewISBNOwnerBorrowed);
        bookBorrower = view.findViewById(R.id.borrowedBorrower);


        bookTitle.setText(clickedBook.getTitle());
        bookAuthor.setText(clickedBook.getAuthor());
        bookDescription.setText(clickedBook.getDescription());
        bookISBN.setText(clickedBook.getISBN());
        //need to get borrower here
        bookBorrower.setText("Borrower:"+clickedBook.getOwnerUname());
        imageView = view.findViewById(R.id.imageView);

        Photographs.viewImage("B", clickedBook.getID(), storageReference, applicationContext, imageView);

        confirmReturnButton.setOnClickListener(new View.OnClickListener() {
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
                if (returnDenoted!=null &&returnDenoted.equals("true")) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(getActivity(), "You must grant the permission of camera to confirm borrow", Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                    } else {
                        Intent intent = new Intent(getActivity(), Scanner.class);
                        startActivityForResult(intent, 325);
                    }
                }
                //the book is either first time to be denoted or denoted=="false" currently
                else {
                    Toast.makeText(getActivity(), "The borrower have not denoted return yet", Toast.LENGTH_SHORT).show();

                }

            }
        });

/*try to solve the bug,to be tested
        confirmReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBookDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                returnDenoted = document.getString("returnDenoted");
                                if (returnDenoted!=null &&returnDenoted.equals("true")) {
                                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
                                        Toast.makeText(getActivity(), "You must grant the permission of camera to confirm borrow", Toast.LENGTH_SHORT).show();
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 445);
                                    } else {
                                        Intent intent = new Intent(getActivity(), Scanner.class);
                                        startActivityForResult(intent, 325);
                                    }
                                }
                                //the book is either first time to be denoted or denoted=="false" currently
                                else {
                                    Toast.makeText(getActivity(), "The borrower have not denoted return yet", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getActivity(), "some error occurs", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


            }
        });
*/
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
                    //owner confirm receive returned book
                    Toast.makeText(getActivity(), "You successfully received this book", Toast.LENGTH_SHORT).show();
                    currentBookDocRef.update("returnDenoted","false");
                    currentBookDocRef.update("status","Available");
                    borrowerSideBorrowedBookRef.update("returnDenoted","false");

                    //for borrower,remove book from BorrowedBook
                    borrowerSideBorrowedBookRef.delete();
                    
                }
                else {
                    Toast.makeText(getActivity(), "The ISBN you scaned does not match the ISBN of the book", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
}


